/*******************************************************************************
 * Copyright 2018 T Mobile, Inc. or its affiliates. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License.  You may obtain a copy
 * of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.  See the
 * License for the specific language governing permissions and limitations under
 * the License.
 ******************************************************************************/
package com.tmobile.pacman.api.statistics.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;

import feign.RequestInterceptor;
import feign.RequestTemplate;

/**
 * Sample SpringSecurty Config.
 * Http security is overriden to permit AL.
 *
 * @author anil
 *
 */
@Configuration("WebSecurityConfig")
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {

	/**
	 * Constructor disables the default security settings
	 **/
	public SpringSecurityConfig() {
		super(true);
	}

	@Bean
	public RequestInterceptor requestTokenBearerInterceptor() {
	    return new RequestInterceptor() {
	        @Override
	        public void apply(RequestTemplate requestTemplate) {
	            OAuth2AuthenticationDetails details = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
	            requestTemplate.header("Authorization", "bearer " + details.getTokenValue());
	        }
	    };
	}

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/public/**", "/swagger-ui.html", "/api.html", "/css/styles.js", "/js/swagger.js", "/js/swagger-ui.js", "/js/swagger-oauth.js", "/images/pacman_logo.svg", "/images/favicon-32x32.png", "/images/favicon-16x16.png", "/images/favicon.ico", "/docs/v1/api.html", "/swagger-resources/**", "/v2/api-docs/**", "/v2/swagger.json");
		web.ignoring().antMatchers("/imgs/**");
		web.ignoring().antMatchers("/css/**");
		web.ignoring().antMatchers("/css/font/**");
		web.ignoring().antMatchers("/proxy*/**");
		web.ignoring().antMatchers("/hystrix/monitor/**");
		web.ignoring().antMatchers("/hystrix/**");
		web.ignoring().antMatchers(HttpMethod.OPTIONS, "/**");
    }

    /* (non-Javadoc)
     * @see org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity)
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.csrf().disable();
        http.anonymous().and().antMatcher("/user").authorizeRequests().antMatchers("/public/**").permitAll()
		.antMatchers("/secure/**").authenticated();
    }
}
