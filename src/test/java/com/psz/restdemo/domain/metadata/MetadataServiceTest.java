package com.psz.restdemo.domain.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.psz.restdemo.domain.metadata.model.MetadataServiceImpl;
import com.psz.restdemo.domain.metadata.model.TenantEntity;
import com.psz.restdemo.domain.metadata.model.TenantEntityBuilder;
import com.psz.restdemo.domain.metadata.repository.TenantRepository;

@ExtendWith(MockitoExtension.class)
public class MetadataServiceTest {
    
    @Mock
    TenantRepository repository;

    @Mock
    MetadataEventPublisher eventPublisher;

    @InjectMocks
    MetadataServiceImpl service;

    @Captor
    ArgumentCaptor<TenantEntity> tenantEntityCaptor;

    @Test
    public void givenValidInput_whenCreateTenant_thenRepoDocumentSaveAndCreateEventEventEmited(){
        Tenant tenant = new TenantEntityBuilder().withId("id1").withName("id1_name").build();
        Mockito.clearInvocations(repository);
        Mockito.clearInvocations(eventPublisher);
        
        service.mergeTenant(tenant);

        Mockito.verify(repository).save(tenantEntityCaptor.capture());
        Tenant savedTenant = tenantEntityCaptor.getValue();
        assertEquals(tenant, savedTenant);

        Mockito.verify(eventPublisher).tenantCreated(tenantEntityCaptor.capture());
        assertEquals(tenant, savedTenant);
    }

    @Test
    public void givenValidInput_whenGetTenant_thenTenantEntityReturned(){
        TenantEntity tenant = new TenantEntityBuilder().withId("id1").withName("id1_name").build();
        Mockito.when(repository.findById("id1")).thenReturn(Optional.of(tenant));  

        Optional<? extends Tenant> myTenant = service.getTenant("id1");

        assertTrue(myTenant.isPresent());
        assertEquals(tenant, myTenant.get());
    }    

    @Test
    public void givenInvalidInput_whenGetTenant_thenReturnEmptyOption(){
        Mockito.when(repository.findById("id1")).thenReturn(Optional.empty());  

        Optional<? extends Tenant> myTenant = service.getTenant("id1");

        assertFalse(myTenant.isPresent());
    }      
}
