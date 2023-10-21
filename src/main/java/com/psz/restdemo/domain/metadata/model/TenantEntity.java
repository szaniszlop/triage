package com.psz.restdemo.domain.metadata.model;

import java.util.Set;

import org.springframework.data.mongodb.core.mapping.Document;

import com.psz.restdemo.domain.metadata.BusinessUnit;
import com.psz.restdemo.domain.metadata.Tenant;
import com.psz.restdemo.security.SecurityLabel;
import com.psz.restdemo.security.SecurityLabelHolder;

import lombok.Data;

@Data
@Document
public class TenantEntity implements Tenant, SecurityLabelHolder{
    private final String id;
    private final String name;
    private final Set<BusinessUnit> businessUnits;

    @Override
    public SecurityLabel getSecurityLabel() {
        return SecurityLabel.getLabel(SecurityLabel.LabelType.TENANT, this.id);
    }    

    public static final TenantEntity copy(Tenant tenant){
        assert tenant != null;
        TenantEntityBuilder builder = new TenantEntityBuilder()
            .withId(tenant.getId())
            .withName(tenant.getName());
        tenant.getBusinessUnits().stream().forEach( bu -> builder.withBusinesUnit( 
                            new BusinessUnitEntity(bu.getId(), bu.getName())));
        return builder.build();        
    }
}
