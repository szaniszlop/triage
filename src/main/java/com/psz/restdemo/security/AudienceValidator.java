package com.psz.restdemo.security;

import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.core.OAuth2Error;

public class AudienceValidator implements OAuth2TokenValidator<Jwt> {

    private final String audience;

    public AudienceValidator(String audience){
        this.audience = audience;
    }

    @Override
    public OAuth2TokenValidatorResult validate(Jwt token) {
        OAuth2Error missingAudienceerror = new OAuth2Error("invalid_token", "The required audience is missing", null);

        if(token.getAudience().contains(audience)){
            return OAuth2TokenValidatorResult.success();
        } 
        return OAuth2TokenValidatorResult.failure(missingAudienceerror);
    }
    
}
