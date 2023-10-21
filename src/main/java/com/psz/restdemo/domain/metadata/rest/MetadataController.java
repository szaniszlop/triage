package com.psz.restdemo.domain.metadata.rest;

import java.util.List;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.psz.restdemo.domain.metadata.MetadataService;
import com.psz.restdemo.domain.metadata.Tenant;
import com.psz.restdemo.domain.metadata.model.TenantEntity;
import com.psz.restdemo.domain.metadata.model.TenantEntityBuilder;
import com.psz.restdemo.security.SecurityLabelAuthorization;
import com.psz.restdemo.security.TenantAuthorization;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/v1/", produces = MediaType.APPLICATION_JSON_VALUE )
// For simplicity of this sample, allow all origins. Real applications should configure CORS for their use case.
@CrossOrigin(origins = "*")
@Slf4j
public class MetadataController {
    
    @Autowired
    TenantModelAssembler tenantModelAssembler;

    @Autowired
    private MetadataService metadataService;

    @GetMapping(path = "admin/tenants")
    @PreAuthorize("hasAuthority('tenant:read')")
    @SecurityLabelAuthorization
    public HttpEntity<TenantModel[]> getTenants(){
        List<? extends Tenant> tenants = metadataService.getAllTenants();
        List<TenantModel> tenantsModel = tenants.stream().map( t -> tenantModelAssembler.toModel(t)).toList();
        return new ResponseEntity<>(tenantsModel.toArray(new TenantModel[]{}) , HttpStatus.OK) ;
    }

    @GetMapping(path = "public/tenant/{id}")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.READ + "')")
    @SecurityLabelAuthorization
    public HttpEntity<TenantModel> getTenant(  @PathVariable String id, Authentication auth){
        return new ResponseEntity<>(
            tenantModelAssembler.toModel(metadataService.getTenant(id).orElseThrow()), HttpStatus.OK);
    }

    @PostMapping(path = "admin/tenant")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.CREATE + "')")
    public HttpEntity<TenantModel> createTenant(  @RequestBody TenantEntity tenant){
        Tenant savedTenant = metadataService.mergeTenant(tenant);
        return new ResponseEntity<>(
            tenantModelAssembler.toModel(savedTenant), HttpStatus.OK);
    }

    @PutMapping(path = "admin/tenant")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.MODIFY + "')")
    public Tenant changeTenant(  String id, String name){
        return new TenantEntityBuilder().withId(id).withName(name).build();
    }    

    @DeleteMapping(path = "admin/tenant/{id}")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.DELETE + "')")
    public Tenant changeTenant( @PathVariable String id){
        return new TenantEntityBuilder().withId(id).withName("name").build();
    }    
}
