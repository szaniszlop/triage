package com.psz.restdemo.domain.metadata.model;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.psz.restdemo.domain.metadata.BusinessUnit;
import com.psz.restdemo.domain.metadata.MetadataEventPublisher;
import com.psz.restdemo.domain.metadata.MetadataService;
import com.psz.restdemo.domain.metadata.Tenant;
import com.psz.restdemo.domain.metadata.repository.TenantRepository;

@Service
public class MetadataServiceImpl implements MetadataService{

    @Autowired
    TenantRepository repository;

    @Autowired
    MetadataEventPublisher eventPublisher;
    
    @Override
    public Tenant mergeTenant(Tenant tenant) {
        TenantEntity entity = TenantEntity.copy(tenant);
        entity = repository.save(entity);
        eventPublisher.tenantCreated(entity);
        return entity;
    }

    @Override
    public Optional<? extends Tenant> getTenant(String id) {
        return repository.findById(id);
    }

    @Override
    public List<? extends Tenant> getAllTenants() {
        /*
        List<Tenant> tenants = new ArrayList<>();
        TenantEntityBuilder tenantBuilder = new TenantEntityBuilder();

        tenants.add(tenantBuilder
            .withId("id1")
            .withName("name1")
            .withBusinesUnit(new BusinessUnitEntity("bu1", "bu1")).build() );

        tenants.add(tenantBuilder
            .withId("id2")
            .withName("name2")
            .withBusinesUnit(new BusinessUnitEntity("bu2", "bu2"))
            .withBusinesUnit(new BusinessUnitEntity("bu3", "bu3"))
            .build() );
        return tenants;
        */
        return repository.findAll();
    }

    @Override
    public Optional<? extends Tenant> deleteTenant(String id) {
        Optional<TenantEntity> tenant = repository.findById(id);
        if(tenant.isPresent()){
            repository.delete(tenant.get());
        }
        return tenant;
    }

    @Override
    public Tenant addBusinessUnit(Tenant tenant, BusinessUnit bu) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'addBusinessUnit'");
    }

    @Override
    public Tenant removeBusinessUnit(Tenant tenant, BusinessUnit bu) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'removeBusinessUnit'");
    }
    
    
}
