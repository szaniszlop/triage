package com.psz.restdemo.domain.metadata.model;

import org.springframework.stereotype.Component;

import com.psz.restdemo.domain.metadata.MetadataEventPublisher;
import com.psz.restdemo.domain.metadata.Tenant;

@Component
public class MetadataEventPublisherImpl implements MetadataEventPublisher{

    @Override
    public void tenantCreated(Tenant tenant) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tenantCreated'");
    }

    @Override
    public void tenantModified(Tenant tenant) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tenantModified'");
    }

    @Override
    public void tenantDeleted(Tenant tenant) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'tenantDeleted'");
    }
    
}
