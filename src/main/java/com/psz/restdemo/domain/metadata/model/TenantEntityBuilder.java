package com.psz.restdemo.domain.metadata.model;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class TenantEntityBuilder {
    private String id;
    private String name;
    private Set<BusinessUnitEntity> businessUnits;

    public TenantEntityBuilder(){
        this.businessUnits = new HashSet<>();
    }

    public TenantEntityBuilder withId(String id){
        this.id = id;
        return this;
    }

    public TenantEntityBuilder withName(String name){
        this.name = name;
        return this;
    }

    public TenantEntityBuilder withBusinesUnit(BusinessUnitEntity bu){
        this.businessUnits.add(bu);
        return this;
    }
    
    public TenantEntity build(){
        assert this.id != null;
        assert this.name != null;
        assert !this.id.equals("");
        assert !this.name.equals("");
        return new TenantEntity(this.id, this.name, Collections.unmodifiableSet(this.businessUnits));
    }
}
