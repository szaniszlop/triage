package com.psz.restdemo.security;

public interface TenantAuthorization {
    public static final String READ = "tenant:read";
    public static final String CREATE = "tenant:create";
    public static final String MODIFY = "tenant:modify";
    public static final String DELETE = "tenant:delete";    
    public static final String SECURITY_LABEL = "TENANT";    
}
