package com.cisco.epx.course.app.config;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import com.cisco.epx.course.app.model.CustomOAuth2User;
import com.cisco.epx.course.app.services.CustomOAuth2UserService;
import com.cisco.epx.course.app.services.UserService;
import com.cisco.epx.model.Provider;
import com.cisco.epx.model.User;

@EnableWebSecurity
public class SecurityConfig extends WebSecurityConfigurerAdapter {

	@Autowired
	private CustomOAuth2UserService customOAuth2UserService;

	@Autowired
	private UserService userservice;
	
	@Autowired
	private OAuth2AuthorizedClientService clientService;
	
	@Override
	public void configure(HttpSecurity httpSecurity) throws Exception {

		httpSecurity.csrf().disable().antMatcher("/**").authorizeRequests().antMatchers("/", "/courses/list")
				.permitAll().antMatchers("/courses/view", "/courses/add").authenticated().anyRequest().authenticated()
				.and().oauth2Login()
				// .loginPage("/login")
				.userInfoEndpoint().userService(customOAuth2UserService).and()
				.successHandler(new AuthenticationSuccessHandler() {

					@Override
					public void onAuthenticationSuccess(HttpServletRequest request, HttpServletResponse response,
							Authentication authentication) throws IOException, ServletException {
						OAuth2AuthenticationToken oauthToken =
							    (OAuth2AuthenticationToken) authentication;
												
						OAuth2AuthorizedClient client =
							    clientService.loadAuthorizedClient(
							            oauthToken.getAuthorizedClientRegistrationId(),
							            oauthToken.getName());

						String accessToken = client.getAccessToken().getTokenValue();
							
						CustomOAuth2User oauthUser = (CustomOAuth2User) authentication.getPrincipal();

						User user = new User();
						user.setEmail(oauthUser.getEmail());
						user.setUsername(oauthUser.getName());
						user.setProvider(Provider.GOOGLE);
							
						if (!userservice.findByEmail(user.getEmail()).isPresent()) {
							userservice.register(user);
						}
						User userResponse = userservice.findByEmail(user.getEmail())
								.orElseThrow(() -> new IllegalArgumentException("User Registration failed"));

						// Storing user info in session
						request.getSession().setAttribute(AppConstant.USER_ID, userResponse.getId());
						request.getSession().setAttribute("email", user.getEmail());
						response.sendRedirect("/");
						
					}
				}).and().logout().logoutSuccessUrl("/");

	}
}