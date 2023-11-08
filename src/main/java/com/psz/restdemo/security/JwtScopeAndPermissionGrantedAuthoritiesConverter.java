package com.psz.restdemo.security;

import java.util.ArrayList;
import java.util.Collection;

import org.springframework.core.convert.converter.Converter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;


public class JwtScopeAndPermissionGrantedAuthoritiesConverter implements Converter<Jwt, Collection<GrantedAuthority>>{

    private static final String PERMISSIONS_CLAIM = "permissions";
    private JwtGrantedAuthoritiesConverter scopeConverter = new JwtGrantedAuthoritiesConverter();

    @Override
    public Collection<GrantedAuthority> convert(Jwt jwt) {
        Collection<GrantedAuthority> authorities = scopeConverter.convert(jwt);
        authorities.addAll(extractPermissionsAsGrantedAuthorities(jwt));
        return authorities;
    }

    private Collection<GrantedAuthority> extractPermissionsAsGrantedAuthorities(Jwt jwt){
        Collection<GrantedAuthority> permissions = new ArrayList<>();
        if(jwt.hasClaim(PERMISSIONS_CLAIM)){
            permissions.addAll(
                jwt.getClaimAsStringList(PERMISSIONS_CLAIM)
                .stream()
                .map( permission -> new SimpleGrantedAuthority(permission))
                .toList());
        }
        return permissions;
    }
    
}
