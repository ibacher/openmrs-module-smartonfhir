/*
 * This Source Code Form is subject to the terms of the Mozilla Public License,
 * v. 2.0. If a copy of the MPL was not distributed with this file, You can
 * obtain one at http://mozilla.org/MPL/2.0/. OpenMRS is also distributed under
 * the terms of the Healthcare Disclaimer located at http://openmrs.org/license.
 *
 * Copyright (C) OpenMRS Inc. OpenMRS is a registered trademark and the OpenMRS
 * graphic logo is a trademark of OpenMRS Inc.
 */
package org.openmrs.module.smartonfhir.web;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class SmartConformance {
	
	@JsonProperty(value = "authorization_endpoint", required = true)
	private String authorizationEndpoint;
	
	@JsonProperty(value = "token_endpoint", required = true)
	private String tokenEndpoint;
	
	@JsonProperty("token_endpoint_auth_methods_supported")
	private String[] tokenEndpointAuthMethodsSupported;
	
	@JsonProperty("registration_endpoint")
	private String registrationEndpoint;
	
	@JsonProperty("scopes_supported")
	private String[] scopesSupported;
	
	@JsonProperty("response_types_supported")
	private String[] responseTypesSupported;
	
	@JsonProperty("management_endpoint")
	private String managementEndpoint;
	
	@JsonProperty("introspection_endpoint")
	private String introspectionEndpoint;
	
	@JsonProperty("revocation_endpoint")
	private String revocationEndpoint;
	
	@JsonProperty(value = "capabilities", required = true)
	private String[] capabilities;
}
