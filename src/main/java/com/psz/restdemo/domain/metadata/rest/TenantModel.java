package com.psz.restdemo.domain.metadata.rest;


import org.springframework.hateoas.CollectionModel;
import org.springframework.hateoas.RepresentationModel;

import com.psz.restdemo.security.SecurityLabel;
import com.psz.restdemo.security.SecurityLabelHolder;

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
public class TenantModel extends RepresentationModel<TenantModel> implements SecurityLabelHolder{
    private String id;
    private String name;
    private CollectionModel<BusinessUnitModel> businessUnits;


    @Override
    public SecurityLabel getSecurityLabel() {
        return SecurityLabel.getLabel(SecurityLabel.LabelType.TENANT, this.id);
    }
}
