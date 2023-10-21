package com.psz.restdemo.domain.metadata.model;

import org.springframework.data.mongodb.core.mapping.Document;

import com.psz.restdemo.domain.metadata.BusinessUnit;

import lombok.Data;

@Data
@Document
public class BusinessUnitEntity implements Comparable<BusinessUnitEntity>, BusinessUnit{
    private final String id;
    private final String name;

    @Override
    public int compareTo(BusinessUnitEntity o) {
        return this.id.compareTo(o.getId());
    }
}
