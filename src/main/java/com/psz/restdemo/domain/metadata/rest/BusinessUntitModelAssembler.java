package com.psz.restdemo.domain.metadata.rest;

import org.springframework.beans.BeanUtils;
import org.springframework.hateoas.server.mvc.RepresentationModelAssemblerSupport;
import org.springframework.stereotype.Component;

import com.psz.restdemo.domain.metadata.BusinessUnit;

@Component
public class BusinessUntitModelAssembler extends RepresentationModelAssemblerSupport<BusinessUnit, BusinessUnitModel> {

    public BusinessUntitModelAssembler(){
        super(MetadataController.class, BusinessUnitModel.class);
    }

    @Override
    public BusinessUnitModel toModel(BusinessUnit entity) {
        BusinessUnitModel model = instantiateModel(entity);
        BeanUtils.copyProperties(entity, model);
        return model;
    }
    
}
