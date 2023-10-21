package com.psz.restdemo.domain.metadata;

import java.util.Set;

public interface Tenant {
    public String getId();
    public String getName();
    public  Set<BusinessUnit> getBusinessUnits();    
}
