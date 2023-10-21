package com.psz.restdemo.domain.metadata.repository;

import org.springframework.data.mongodb.repository.MongoRepository;
import com.psz.restdemo.domain.metadata.model.TenantEntity;

public interface TenantRepository extends MongoRepository<TenantEntity, String> {

    
}
