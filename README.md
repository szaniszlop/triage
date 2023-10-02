#Idea is to simulate a rest API for handling rules and rule sets

**Entities:**
- QualityAssurance
    - defines QualityAssurance schedule for a tenant and rule set
    - only active rule set can be selected
    - only rule set belonging to the tenant of the QualityAssurance can be selected
    - there can be multiple QualityAssurances for the same tenant with eventually different schedules and rule sets
    - once created the tenant can not be modified
    - creation, modification and deletion by administrator only

- verified_entity
    - is the entity that is used for QualityAssurance process
    - it is assigned to one specific business unit - this drives the rule set to be used by the QualityAssurance process
    - it hase a state that represents if it is relevant for the QualityAssurance process or not
    - entities are created based on data from the source data store and is not managed or stored in this application


- verified_entity_action
    - is and action that has been taken on an entity at a specific run of the QualityAssurance process
    - it links the entity, the selector that selected the entity and the action that has should be taken 
    - it undergoes different status changes: identified, triggered, executed, failed


- tenant
    - is the entity managing access to the system
    - workflow administrators have access on level of the tenant
    - tenants can be created, activated and deactivated and deleted  by administrator
    - tenant can be only deleted if there are no business unit and rule sets assigned to it
    - tenant can be viewed by anyone with read privilege on that tenant


- business_unit
    - belongs to one tenant
    - represents the linkage between rule sets and entity root the rule set is supposed to be applied to
    - business units can be created, activated and deactivated and deleted by administrator
    - business unit can be only deleted if there is no selector assigned to it
    - business unit can be viewed by anyone with read privilege on that tenant


- rule_template
    - Represents a template for a rule
    - A rule template can be created, deleted and modified by rule administrator only
    - It can be viewed by anyone having read role or any tenant
    - Template has an ID, name, description and an expression representing the rule logic


- rule_instance
    - is an instantination of the rule template in context of an rule set
    - It is created by an workflow administrator based on a rule template
    - It can be created, deleted and modified by anyone having workflow administrator rule for the tenant of the rule set this instance belongs to


- rule_set
    - identifies the root entities and rules to be applied to them
    - it has a list of rule instances
    - is associated with a tenant for access controll
    - it has a list of selectors to identify entity roots to be apply to
    - it also have an action to be applied when the rules in the rule set are satisfied for an entity root
    - it has a default action to be applied for all entity roots not selected by any rule
    - A new rule set is in status DRAFT until it is sent out for approval - IN_REVIEW status
    - Any change in a rule set reset status to DRAFT
    - A rule set in IN_REVIEW status has to be approved by a different user then the user who initiated the approval
    - only approved rule sets - status APPROVED - are considered by a QualityAssurance run


- selector
    - is a combination of business unit and status
    - used to identify root elements to be processed by a rule set
    - a selector is created in a context of an rule set by workflow administrator and is limited to business units associated with the tenant the rule set belongs to


- action
    - represents an action to be performed on the entity root
    - usually an state change into a target state


```mermaid
classDiagram
      Tenant *.. BusienssUnit : belongs to
      Tenant "1" o.. "*" QualityAssuranceSchedule : belongs to
      QualityAssuranceSchedule "1" <.. "*" QualityAssuranceRun : run for
      QualityAssuranceSchedule "1" <.. "1" RuleSet : evaluated for
      RuleTemplate "1" ..|> "*" RuleInstance : instantinates
      RuleSet "*" ..* "1" Tenant
      RuleSet "1" *-- "*" RuleInstance
      RuleSet "*" o-- "*" Selector
      RuleSet "*" o-- "1" Action : Default Action
      RuleSet "*" o-- "1" Action : Triggered Action
      RuleSet "*" --> "1" RuleSetStatus
      Selector <|-- BusienssUnitSelector
      Selector <|-- EntityStatusSelector
      Selector <|-- CompositeSelector
      CompositeSelector "*" o-- "*" Selector
      EntityStatusSelector "*" ..> "1" EntityStatus : source status
      BusienssUnitSelector "*" ..> "1" BusienssUnit : source BU
      QualityAssuranceRun "1" *-- "*" VerifiedEntity : processed in
      VerifiedEntity "1" *-- "1" VerifiedEntityAction : resulted in
      VerifiedEntity "*" --> "1" Selector : selected by
      VerifiedEntityAction "*" ..> "1" Action : perform action
      Action <|-- StateTransitionAction
      StateTransitionAction "*" ..> "1" EntityStatus : target Status
        namespace RuleMetadata{
            class RuleTemplate{
                -int id
                -String name
                -String description
                -String expression
            }  
        }
      namespace Metadata{
        class Tenant{
            -String tenantId
            -String tenantName
        }
        class BusienssUnit{
            -String businessUnitId
            -String businessUnitName
        }  
        class EntityStatus{
            -int id
            -String statusCode
            -String statusDeescription
        }                
      }
      namespace QualityAssuranceMetadata{
        class QualityAssuranceSchedule{
            -int id
            -String tenantId
            -RuleSet ruleSet
            -Schedule schedule
            +run()
        }
      }
      namespace ActionMetadata{
        class Action{
            -int id
            -String name
            -String description
            +apply(VerifiedEntity)
        }    
        class StateTransitionAction{
            -EntityStatus targetStatus
        }
      }
    namespace SelectorMetadata{
        class Selector{
        -int id
        -String name
        +getCondition()
        }
        class BusienssUnitSelector{
        -String businessUnitId
        }
        class EntityStatusSelector{
        -EntityStatus status
        }
        class CompositeSelector{
        -List<Selector> selectors
        }                        
    }  
    namespace RuleSets{
        class RuleInstance{
            -int samplePercentage
            -int  minCount
            -String name
            -String expression
            +evaluate()
        }
        class RuleSet{
            -int id
            -String name
            -String description
            -RuleSetStatus status
            -User modifiedBy
            -User approvedBy
            +create()
            +modify()
            +approve()
            +evaluateRules()
        }
        class RuleSetStatus{
            <<enumeration>>
            DRAFT
            IN_REVIEW
            APPROVED
            DISABLED
        }      
    }
    namespace QualityAssuranceResult{
        class VerifiedEntity{
            -int id
            -EntityId entity
            -Selector selectedBy
        }        
        class VerifiedEntityAction{
            -int VerifiedEntityId
            -Action action
            -ActionStatus status   
            +apply()
        }
        class QualityAssuranceRun{
            -int id
            -Date executedOn
        }
        class ActionStatus{
            <<enumeration>>
            IDENTIFIED
            TRIGGERED
            EXECUTED
            FAILED
        }
    }

```    