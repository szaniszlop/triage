package com.psz.restdemo.domain.metadata.rest;

import org.springframework.hateoas.RepresentationModel;

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
public class BusinessUnitModel extends RepresentationModel<BusinessUnitModel> {
    private String id;
    private String name;
}
