package com.psz.restdemo.domain.metadata;

public interface Tenant {
    public String getId();
    public String getName();
    public  Iterable<? extends BusinessUnit> getBusinessUnits();    
}
