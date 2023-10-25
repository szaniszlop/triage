package com.psz.restdemo;

import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.oidc.StandardClaimNames;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.web.servlet.MockMvc;

import com.psz.restdemo.config.SecurityConfig;
import com.psz.restdemo.domain.metadata.Tenant;
import com.psz.restdemo.domain.metadata.model.MetadataServiceImpl;
import com.psz.restdemo.domain.metadata.model.TenantEntity;
import com.psz.restdemo.domain.metadata.model.TenantEntityBuilder;
import com.psz.restdemo.domain.metadata.rest.BusinessUntitModelAssembler;
import com.psz.restdemo.domain.metadata.rest.MetadataController;
import com.psz.restdemo.domain.metadata.rest.TenantDto;
import com.psz.restdemo.domain.metadata.rest.TenantModelAssembler;
import com.psz.restdemo.security.Scopes;
import com.psz.restdemo.security.SecurityLabelAuthorizationLogic;
import com.psz.restdemo.security.TenantAuthorization;

import lombok.extern.slf4j.Slf4j;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.util.List;
import java.util.Optional;


@WebMvcTest( controllers = {MetadataController.class})
@ContextConfiguration(classes = {SecurityConfig.class, MetadataController.class,
								TenantModelAssembler.class, BusinessUntitModelAssembler.class,
								MetadataServiceImpl.class, SecurityLabelAuthorizationLogic.class})
@Slf4j
class MetadataControlerTest {

	private static final AnonymousAuthenticationToken ANONYMOUS = 
		new AnonymousAuthenticationToken("anonymous", "anonymousUser", 
      	AuthorityUtils.createAuthorityList("ROLE_ANONYMOUS"));
	
	@Autowired
    MockMvc mockMvc;

	@MockBean
	MetadataServiceImpl service;

	@Captor
	ArgumentCaptor<TenantDto> tenantDtoArgumentCaptor;


	@Test
	@WithAnonymousUser
	void givenRequestIsAnonymous_whenGetTenants_thenUnauthorized() throws Exception {
		mockMvc.perform(get("/api/v1/tenants/public")
		.with(anonymous()))
		.andExpect(status().isUnauthorized()) ;
	}

	@Test
	void givenUserAuthenticated_whenGetTenants_thenOk() throws Exception{
		String response = mockMvc.perform(
			get("/api/v1/tenants/admin")
			.with(  oauth2Login().authorities(
					List.of(new SimpleGrantedAuthority(Scopes.ADMIN),
							new SimpleGrantedAuthority(TenantAuthorization.READ)))))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		log.info(response);
	}

	@Test
	void givenUserAuthenticatedButNotAdmin_whenGetTenants_thenOk() throws Exception{
		String response = mockMvc.perform(
			get("/api/v1/tenants/admin")
			.with(  oauth2Login().authorities(
					List.of(new SimpleGrantedAuthority(TenantAuthorization.READ)))))
			.andExpect(status().isForbidden())
			.andReturn().getResponse().getContentAsString();
		log.info(response);
	}

	@Test
	void givenUserAuthenticatedWithTenantAuthorization_whenGetTenant_thenOk() throws Exception{
		String myTenant = "mytenant";
		TenantEntity tenant = new TenantEntityBuilder().withId(myTenant).withName(myTenant).build();
		Mockito.when(service.getTenant("mytenant"))
			.thenAnswer(x -> Optional.of(tenant));

		String response = mockMvc.perform(
			get("/api/v1/tenants/public/" + myTenant)
			.with( jwt().jwt( jwt -> jwt.claim( StandardClaimNames.PREFERRED_USERNAME, "user1")
					.audience( List.of("https://testapi.psz.com/")))
					.authorities(List.of(new SimpleGrantedAuthority(TenantAuthorization.READ),
						 new SimpleGrantedAuthority(TenantAuthorization.SECURITY_LABEL + ":" + myTenant)))))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		log.info(response);
	}

	@Test
	void givenUserAuthenticatedMissingTenantAuthorization_whenGetTenant_thenForbidden() throws Exception{
		String myTenant = "mytenant";
		TenantEntity tenant = new TenantEntityBuilder().withId(myTenant).withName(myTenant).build();
		Mockito.when(service.getTenant("mytenant"))
			.thenAnswer(x -> Optional.of(tenant));

		String response = mockMvc.perform(
			get("/api/v1/tenants/public/mytenant")
			.with( jwt().jwt( jwt -> jwt.claim( StandardClaimNames.PREFERRED_USERNAME, "user1")
					.audience( List.of("https://testapi.psz.com/")))
					.authorities(List.of(new SimpleGrantedAuthority(TenantAuthorization.READ),
										 new SimpleGrantedAuthority(TenantAuthorization.SECURITY_LABEL + ":other")))))
			.andExpect(status().isForbidden())
			.andReturn().getResponse().getContentAsString();
		log.info(response);
	}	

	@Test
	void givenIncorrectAuthority_whenGetTenant_thenForbidden() throws Exception{
		mockMvc.perform(
			get("/api/v1/tenants/public/mytenant")
			.with( jwt().jwt( jwt -> jwt.claim( StandardClaimNames.PREFERRED_USERNAME, "user1")
					.audience( List.of("https://testapi.psz.com/")))
					.authorities(List.of(new SimpleGrantedAuthority("wrong")))))
			.andExpect(status().isForbidden());
	}

	@Test
	void givenIncorrectAuthority_whendeleteTenant_thenForbidden() throws Exception{
		mockMvc.perform(
			delete("/api/v1/tenants/admin/mytenant")
			.with( jwt().jwt( jwt -> jwt.claim( StandardClaimNames.PREFERRED_USERNAME, "user1")
					.audience( List.of("https://testapi.psz.com/")))
					.authorities(List.of(new SimpleGrantedAuthority(Scopes.ADMIN), 
								new SimpleGrantedAuthority("wrong")))))
			.andExpect(status().isForbidden());
	}	

	@Test
	void givenCorrectAuthorityAndAdminScope_whendeleteTenant_thenOk() throws Exception{
		String myTenant = "mytenant";
		TenantEntity tenant = new TenantEntityBuilder().withId(myTenant).withName(myTenant).build();
		Mockito.when(service.getTenant(myTenant))
			.thenAnswer(x -> Optional.of(tenant));

		mockMvc.perform(
			delete("/api/v1/tenants/admin/mytenant")
			.with( jwt().jwt( jwt -> jwt.claim( StandardClaimNames.PREFERRED_USERNAME, "user1")
					.audience( List.of("https://testapi.psz.com/")))
					.authorities(List.of(new SimpleGrantedAuthority(Scopes.ADMIN), 
								new SimpleGrantedAuthority(TenantAuthorization.DELETE)))))
			.andExpect(status().isOk());
	}	

	@Test
	void givenCorrectAuthorityWithoutAdminScope_whendeleteTenant_thenForbidden() throws Exception{
		mockMvc.perform(
			delete("/api/v1/tenants/admin/mytenant")
			.with( jwt().jwt( jwt -> jwt.claim( StandardClaimNames.PREFERRED_USERNAME, "user1")
					.audience( List.of("https://testapi.psz.com/")))
					.authorities(List.of(new SimpleGrantedAuthority(TenantAuthorization.DELETE)))))
			.andExpect(status().isForbidden());
	}	

	@Test
	void givenUserAuthenticatedAsAdmin_whenCreateTenant_thenMergeTenantServiceCalled() throws Exception{
		String myTenant = "tenant_1";
		TenantEntity tenant = new TenantEntityBuilder().withId(myTenant).withName(myTenant).build();
		Mockito.when(service.mergeTenant(Mockito.any()))
			.thenAnswer(x -> tenant);

		String response = mockMvc.perform(
			post("/api/v1/tenants/admin")
			.with(  oauth2Login().authorities(
					List.of(new SimpleGrantedAuthority(Scopes.ADMIN),
							new SimpleGrantedAuthority(TenantAuthorization.CREATE))))
			.content("{\r\n" + //
					"      \"id\": \"tenant_1\",\r\n" + //
					"      \"name\": \"tenant_1_name\", \r\n" + //	
					"      \"businessUnits\": [{\"id\": \"bu1\", \"name\": \"bu1_name\"}, \r\n" + //
					"                          {\"id\": \"bu2\", \"name\": \"bu2_name\"}]  \r\n" + //					
					"}\r\n")
			.contentType(MediaType.APPLICATION_JSON)   
			.with( csrf()))
			.andExpect(status().isOk())
			.andReturn().getResponse().getContentAsString();
		log.info(response);
		Mockito.verify(service).mergeTenant(tenantDtoArgumentCaptor.capture());
		Tenant tenantArgument = tenantDtoArgumentCaptor.getValue();
		assertEquals("tenant_1", tenantArgument.getId());
		assertEquals("tenant_1_name", tenantArgument.getName());
		final int[] count = {0};
		tenantArgument.getBusinessUnits().forEach( b -> count[0]++);
		assertEquals(2, count[0]);
	}

}
