package com.psz.restdemo.domain.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

import com.psz.restdemo.domain.metadata.model.BusinessUnitEntity;
import com.psz.restdemo.domain.metadata.model.TenantEntity;
import com.psz.restdemo.domain.metadata.model.TenantEntityBuilder;

public class TenantBuilderTest {
    
    @Test
    void tenantMustHaveId(){
        TenantEntityBuilder builder = new TenantEntityBuilder();
        assertThrows(AssertionError.class, () -> {
            builder.build();;
        });
    }
    
    @Test
    void tenantIdMustBeNottEmpty(){
        TenantEntityBuilder builder = new TenantEntityBuilder();
        assertThrows(AssertionError.class, () -> {
            builder.withId("").build();
        });
    }

    @Test
    void tenantMustHaveName(){
        TenantEntityBuilder builder = new TenantEntityBuilder();
        assertThrows(AssertionError.class, () -> {
            builder.withId("someId").build();;
        });
    }

    @Test
    void tenantNameMustBeNotEmpty(){
        TenantEntityBuilder builder = new TenantEntityBuilder();
        assertThrows(AssertionError.class, () -> {
            builder.withName("").build();
        });
    }

    @Test
    void tenantHasNoBusinessUnitsByDefault(){
        TenantEntityBuilder builder = new TenantEntityBuilder();
        TenantEntity tenant = builder.withId("ID").withName("Name").build();
        assertTrue(tenant.getBusinessUnits().size() == 0);
    }

    @Test
    void tenantWithMultipleUniqueBusinesUnits(){
        BusinessUnitEntity bu1 = new BusinessUnitEntity("id1", "name1");
        BusinessUnitEntity bu2 = new BusinessUnitEntity("id2", "name2");
        TenantEntityBuilder builder = new TenantEntityBuilder();
        TenantEntity tenant = builder.withId("ID")
            .withName("Name")
            .withBusinesUnit(bu1)
            .withBusinesUnit(bu2)
            .withBusinesUnit(bu1)
            .build();
        assertEquals("ID", tenant.getId());
        assertEquals("Name", tenant.getName());    
        assertTrue(tenant.getBusinessUnits().size() == 2);
        assertTrue(tenant.getBusinessUnits().contains(bu1));
        assertTrue(tenant.getBusinessUnits().contains(bu2));
    }
}
