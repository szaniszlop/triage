package com.psz.restdemo.domain.metadata.rest;

import org.springframework.hateoas.RepresentationModel;
import org.springframework.hateoas.server.core.Relation;

import com.psz.restdemo.domain.metadata.BusinessUnit;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@NoArgsConstructor
@AllArgsConstructor
@Setter
@Getter
@ToString
@EqualsAndHashCode(callSuper=false)
@Relation(collectionRelation = "businessUnits", itemRelation = "businessUnit")
public class BusinessUnitModel extends RepresentationModel<BusinessUnitModel> implements BusinessUnit{
    private String id;
    private String name;
}
