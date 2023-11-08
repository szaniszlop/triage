package com.psz.restdemo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.core.convert.converter.Converter;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.core.DelegatingOAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtDecoders;
import org.springframework.security.oauth2.jwt.JwtValidators;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationConverter;
import org.springframework.security.oauth2.server.resource.authentication.JwtGrantedAuthoritiesConverter;
import org.springframework.security.web.SecurityFilterChain;

import com.psz.restdemo.security.AudienceValidator;
import com.psz.restdemo.security.JwtScopeAndPermissionGrantedAuthoritiesConverter;
import com.psz.restdemo.security.Scopes;

import static org.springframework.security.authorization.AuthorityAuthorizationManager.*;
import static org.springframework.security.authorization.AuthorizationManagers.*;

import java.util.Collection;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@PropertySource("classpath:application.yml")
@EnableHypermediaSupport(type = HypermediaType.HAL_FORMS)
public class SecurityConfig {
    
    @Value("${auth0.audience}")
    private String audience;

    @Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
    private String issuer;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception{
        http.authorizeHttpRequests((customizer) ->
            customizer.requestMatchers("/api/*/*/private/**").access(anyOf( hasAuthority(Scopes.ADMIN), hasAuthority(Scopes.USER)))
                        .requestMatchers("/api/*/*/admin/**").hasAuthority(Scopes.ADMIN)
                        .anyRequest().authenticated())
            .cors(cors -> cors.configure(http))
            .oauth2ResourceServer( (oauth2) -> oauth2.jwt(Customizer.withDefaults()));
        return http.build();    
    }

    @Bean
    JwtDecoder jwtDecoder() {
        /*
        By default, Spring Security does not validate the "aud" claim of the token, to ensure that this token is
        indeed intended for our app. Adding our own validator is easy to do:
        */

        NimbusJwtDecoder jwtDecoder = (NimbusJwtDecoder)
                JwtDecoders.fromOidcIssuerLocation(issuer);

        OAuth2TokenValidator<Jwt> audienceValidator = new AudienceValidator(audience);
        OAuth2TokenValidator<Jwt> withIssuer = JwtValidators.createDefaultWithIssuer(issuer);
        OAuth2TokenValidator<Jwt> withAudience = new DelegatingOAuth2TokenValidator<>(withIssuer, audienceValidator);

        jwtDecoder.setJwtValidator(withAudience);

        return jwtDecoder;
    }   

    @Bean
    public Converter<Jwt, Collection<GrantedAuthority>> jwtGrantedAuthoritiesConverter() {
        return new JwtScopeAndPermissionGrantedAuthoritiesConverter();
    }
    
    @Bean
    public JwtAuthenticationConverter customJwtAuthenticationConverter() {
        JwtAuthenticationConverter converter = new JwtAuthenticationConverter();
        converter.setJwtGrantedAuthoritiesConverter(jwtGrantedAuthoritiesConverter());
        return converter;
    }

}
