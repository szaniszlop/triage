package com.psz.restdemo.domain.metadata.rest;

import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.linkTo;
import static org.springframework.hateoas.server.mvc.WebMvcLinkBuilder.methodOn;


import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.mediatype.Affordances;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.http.HttpMethod;
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

        // public self link
        model.add( linkTo(
            methodOn(MetadataController.class)
                .getTenant(entity.getId(), null))
            .withSelfRel());
        
        // Admin post and get all link    
        model.add( Affordances.of(linkTo(
                                    methodOn(MetadataController.class)
                                        .getTenants())
                                    .withRel("admin"))
            .afford(HttpMethod.POST)
                .withInput(TenantDto.class)
                .withOutput(TenantModel.class)
                .withName("createTenant")    
            .andAfford(HttpMethod.GET)
                .withName("getTenants")
            .toLink());    

        // Admin put and delete link
        model.add( Affordances.of(linkTo(
                                    methodOn(MetadataController.class)
                                        .changeTenant(entity.getId(), null))
                                    .withRel("admin"))
            .afford(HttpMethod.DELETE)
                .withOutput(TenantModel.class)
                .withName("deleteTenant")
            .andAfford(HttpMethod.PUT)        
                .withInput(TenantDto.class)
                .withOutput(TenantModel.class)
                .withName("changeTenant")                    
            .toLink()
            );     

        model.setBusinessUnits(businessUnitModelAssembler
            .toCollectionModel(entity.getBusinessUnits()));
        return model;
    }

       
}
