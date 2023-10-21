package com.psz.restdemo.domain.metadata.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.psz.restdemo.domain.metadata.Tenant;

@Component
public class TenantModelAssembler extends RepresentationModelAssemblerSupport<Tenant, TenantModel> {

    @Autowired
    BusinessUntitModelAssembler businessUnitModelAssembler;

    public TenantModelAssembler(){
        super(MetadataController.class, TenantModel.class);
    }

    @Override
    public TenantModel toModel(Tenant entity) {
        TenantModel model = instantiateModel(entity);
        BeanUtils.copyProperties(entity, model);
        model.add(linkTo(
            methodOn(MetadataController.class)
                .getTenant(entity.getId(), null))
        .withSelfRel());
        model.setBusinessUnits(businessUnitModelAssembler.toCollectionModel(entity.getBusinessUnits()));
        return model;
    }
    
}
