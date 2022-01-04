/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.smartonfhir.web.servlet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;
import static org.powermock.api.mockito.PowerMockito.doReturn;
import static org.powermock.api.mockito.PowerMockito.mockStatic;
import static org.powermock.api.mockito.PowerMockito.when;
import static org.powermock.api.mockito.PowerMockito.whenNew;

import javax.crypto.spec.SecretKeySpec;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.openmrs.User;
import org.openmrs.api.context.Context;
import org.openmrs.module.smartonfhir.model.SmartSession;
import org.openmrs.module.smartonfhir.util.SmartSecretKeyHolder;
import org.openmrs.module.smartonfhir.util.SmartSessionCache;
import org.powermock.core.classloader.annotations.PowerMockIgnore;
import org.powermock.core.classloader.annotations.PrepareForTest;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;

@RunWith(PowerMockRunner.class)
@PowerMockIgnore("javax.crypto.*")
@PrepareForTest({ Context.class, SmartSessionCache.class, SmartAccessConfirmation.class, SecretKeySpec.class,
        SmartSecretKeyHolder.class })
public class SmartAccessConfirmationTest {
	
	private static final String TOKEN = "http://localhost:8180/auth/realms/openmrs/login-actions/action-token?key=abcd&app-token=%7BAPP_TOKEN%7D";
	
	private static final byte[] SMART_SECRET_KEY_HOLDER = "SecretKey".getBytes(StandardCharsets.UTF_8);
	
	private static final String BASE_URL = "http://localhost:8180/auth/realms/openmrs/login-actions/action-token";
	
	private static final String APP_TOKEN_VALUE = "eyJhbGciOiJIUzI1NiJ9.eyJwYXRpZW50IjoiNDU2IiwidmlzaXQiOiI3ODkifQ.XujZXboXbmJ5ZOgmWg6ihX8kN1Vf2XaZO0RQMBlOygA";
	
	private static final String LAUNCH_ID = "12345";
	
	private static final String USER_UUID = "123";
	
	private static final String PATIENT_UUID = "456";
	
	private static final String VISIT_UUID = "789";
	
	private MockHttpServletRequest request;
	
	private MockHttpServletResponse response;
	
	@Mock
	private SmartSessionCache smartSessionCache;
	
	private SmartSession smartSession;
	
	private User user;
	
	private SmartAccessConfirmation smartAccessConfirmation;
	
	@Before
	public void setup() throws Exception {
		request = new MockHttpServletRequest();
		response = new MockHttpServletResponse();
		user = new User();
		smartSession = new SmartSession();
		smartAccessConfirmation = new SmartAccessConfirmation();
		
		smartSession.setPatientUuid(PATIENT_UUID);
		smartSession.setVisitUuid(VISIT_UUID);
		
		request.setParameter("token", TOKEN);
		request.setParameter("launch", LAUNCH_ID);
		
		user.setUuid(USER_UUID);
		
		mockStatic(Context.class);
		mockStatic(SmartSecretKeyHolder.class);
		
		doReturn(user).when(Context.class, "getAuthenticatedUser");
		doReturn(SMART_SECRET_KEY_HOLDER).when(SmartSecretKeyHolder.class, "getSecretKey");
		
		whenNew(SmartSessionCache.class).withNoArguments().thenReturn(smartSessionCache);
		when(smartSessionCache.get(LAUNCH_ID)).thenReturn(smartSession);
	}
	
	@Test
	public void shouldReturnCorrectBaseURL() throws IOException {
		smartAccessConfirmation.doGet(request, response);
		
		assertThat(response, notNullValue());
		assertThat(response.getRedirectedUrl(), notNullValue());
		assertThat(response.getRedirectedUrl().contains(BASE_URL), equalTo(true));
	}
	
	@Test
	public void shouldContainsEveryQuery() throws IOException {
		smartAccessConfirmation.doGet(request, response);
		
		assertThat(response, notNullValue());
		assertThat(response.getRedirectedUrl(), notNullValue());
		assertThat(response.getRedirectedUrl().contains("key="), equalTo(true));
		assertThat(response.getRedirectedUrl().contains("app-token="), equalTo(true));
	}
	
	@Test
	public void shouldContainAppToken() throws IOException {
		smartAccessConfirmation.doGet(request, response);
		
		assertThat(response, notNullValue());
		assertThat(response.getRedirectedUrl(), notNullValue());
		assertThat(response.getRedirectedUrl().contains(APP_TOKEN_VALUE), equalTo(true));
	}
}
