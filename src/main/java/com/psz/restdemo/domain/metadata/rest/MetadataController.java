package com.psz.restdemo.domain.metadata.rest;

import java.util.List;
import java.util.Optional;

import org.apache.tomcat.util.http.parser.Authorization;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
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
import com.psz.restdemo.security.SecurityLabelAuthorization;
import com.psz.restdemo.security.TenantAuthorization;

import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping(path = "/api/v1/tenants", produces = MediaType.APPLICATION_JSON_VALUE )
// For simplicity of this sample, allow all origins. Real applications should configure CORS for their use case.
@CrossOrigin(origins = "*")
@Slf4j
public class MetadataController {
    
    @Autowired
    TenantModelAssembler tenantModelAssembler;

    @Autowired
    private MetadataService metadataService;

    @GetMapping(path = "admin")
    @PreAuthorize("hasAuthority('tenant:read')")
    @SecurityLabelAuthorization
    public HttpEntity<TenantModel[]> getTenants(){
        List<? extends Tenant> tenants = metadataService.getAllTenants();
        List<TenantModel> tenantsModel = tenants.stream().map( t -> tenantModelAssembler.toModel(t)).toList();
        return new ResponseEntity<>(tenantsModel.toArray(new TenantModel[]{}) , HttpStatus.OK) ;
    }

    @GetMapping(path = "public/{id}")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.READ + "')")
    @SecurityLabelAuthorization
    public HttpEntity<TenantModel> getTenant(  @PathVariable String id, Authentication auth){
        return new ResponseEntity<>(
            tenantModelAssembler.toModel(metadataService.getTenant(id).orElseThrow()), HttpStatus.OK);
    }

    @PostMapping(path = "admin")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.CREATE + "')")
    public HttpEntity<TenantModel> createTenant(  @RequestBody TenantDto tenantModel){
        log.info("Create Tenant {}", tenantModel.toString());
        Tenant savedTenant = metadataService.mergeTenant(tenantModel);
        return new ResponseEntity<>(
            tenantModelAssembler.toModel(savedTenant), HttpStatus.OK);
    }

    @PutMapping(path = "admin/{id}")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.MODIFY + "')")
    public HttpEntity<Tenant> changeTenant(  @PathVariable String id, @RequestBody TenantDto tenant){
        log.info("Update tenant {}", tenant.toString());
        Tenant savedTenant = metadataService.mergeTenant(tenant);
        return new ResponseEntity<>(
            tenantModelAssembler.toModel(savedTenant), HttpStatus.OK);
    }    

    @DeleteMapping(path = "admin/{id}")
    @PreAuthorize("hasAuthority('" + TenantAuthorization.DELETE + "')")
    public HttpEntity<Tenant> deleteTenant( @PathVariable String id){
        log.info("Delete tenant {}", id);
        Optional<? extends Tenant> savedTenant = metadataService.getTenant(id);
        return savedTenant.isPresent() ? new ResponseEntity<>(
                tenantModelAssembler.toModel(savedTenant.get()), HttpStatus.OK)    
            : new ResponseEntity<>(HttpStatus.NOT_FOUND);
    }    
}
