package com.psz.restdemo.domain.metadata.rest;

import com.psz.restdemo.domain.metadata.BusinessUnit;

import lombok.Data;

@Data
public class BusinessUnitDto implements BusinessUnit {
    private final String id;
    private final String name;
}
