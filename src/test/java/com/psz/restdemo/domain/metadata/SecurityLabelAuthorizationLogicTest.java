package com.psz.restdemo.domain.metadata;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.expression.method.MethodSecurityExpressionOperations;

import com.psz.restdemo.domain.metadata.model.TenantEntityBuilder;
import com.psz.restdemo.domain.metadata.rest.BusinessUntitModelAssembler;
import com.psz.restdemo.domain.metadata.rest.TenantModelAssembler;
import com.psz.restdemo.security.SecurityLabelAuthorizationLogic;

@SpringBootTest(classes = {SecurityLabelAuthorizationLogic.class, 
        TenantModelAssembler.class, BusinessUntitModelAssembler.class})
public class SecurityLabelAuthorizationLogicTest {
    
    @Autowired
    SecurityLabelAuthorizationLogic slAutzLogic;

    @Autowired
    TenantModelAssembler tenantModelAssembler;    

    @Test
    public void whenHasAdminAuthority_callDecide_thenTrue(){
        MethodSecurityExpressionOperations operations = Mockito.mock(MethodSecurityExpressionOperations.class);
        Mockito.when(operations.hasAuthority("SCOPE_admin")).thenReturn(true);
        assertTrue(slAutzLogic.decide(operations));
    }


    @Test
    public void whenCommonUserWithTenantAuthority_callDecide_thenTrue(){
        MethodSecurityExpressionOperations operations = Mockito.mock(MethodSecurityExpressionOperations.class);
        Tenant tenant = new TenantEntityBuilder().withId("t1").withName("name").build();

        Mockito.when(operations.hasAuthority("SCOPE_admin")).thenReturn(false);
        Mockito.when(operations.hasAuthority("tenant:t1")).thenReturn(true);
        Mockito.when(operations.getReturnObject()).thenReturn(
                new ResponseEntity<>(tenantModelAssembler.toModel(tenant), HttpStatus.OK));
        assertTrue(slAutzLogic.decide(operations));
    }    

    @Test
    public void whenCommonUserWithoutTenantAuthority_callDecide_thenFalse(){
        MethodSecurityExpressionOperations operations = Mockito.mock(MethodSecurityExpressionOperations.class);
        Tenant tenant = new TenantEntityBuilder().withId("t1").withName("name").build();

        Mockito.when(operations.hasAuthority("SCOPE_admin")).thenReturn(false);
        Mockito.when(operations.hasAuthority("tenant:t1")).thenReturn(false);
        Mockito.when(operations.getReturnObject()).thenReturn(
                new ResponseEntity<>(tenantModelAssembler.toModel(tenant), HttpStatus.OK));
        assertFalse(slAutzLogic.decide(operations));
    }        
}
