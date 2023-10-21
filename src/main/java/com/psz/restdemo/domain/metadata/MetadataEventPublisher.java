package com.psz.restdemo.domain.metadata;

public interface MetadataEventPublisher {
    public void tenantCreated(Tenant tenant);
    public void tenantModified(Tenant tenant);
    public void tenantDeleted(Tenant tenant);
    
}
