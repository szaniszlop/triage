package com.psz.restdemo.domain.metadata;

import java.util.List;
import java.util.Optional;

public interface MetadataService {

    public Tenant mergeTenant(Tenant tenant);
    
    public Optional<? extends Tenant> getTenant(String id);

    public List<? extends Tenant> getAllTenants();

    public Optional<? extends Tenant> deleteTenant(String id);

    public Tenant addBusinessUnit(Tenant tenant, BusinessUnit bu);

    public Tenant removeBusinessUnit( Tenant tenant, BusinessUnit bu);
    
}
