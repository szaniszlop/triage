package com.psz.restdemo.domain.metadata.rest;


import java.util.stream.StreamSupport;

import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.psz.restdemo.domain.metadata.Tenant;
import com.psz.restdemo.security.SecurityLabel;
import com.psz.restdemo.security.SecurityLabelHolder;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "tenants", itemRelation = "tenant")
public class TenantModel extends RepresentationModel<TenantModel> implements SecurityLabelHolder, Tenant{
    private String id;
    private String name;
    private CollectionModel<BusinessUnitModel> businessUnits;

    @Override
    public SecurityLabel getSecurityLabel() {
        return SecurityLabel.getLabel(SecurityLabel.LabelType.TENANT, this.id);
    }
}
