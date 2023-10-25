package com.psz.restdemo.domain.metadata.rest;

import java.util.Set;

import com.psz.restdemo.domain.metadata.Tenant;

import lombok.Data;

@Data
public class TenantDto implements Tenant{
    private final String id;
    private final String name;
    private final Set<BusinessUnitDto> businessUnits;
}
