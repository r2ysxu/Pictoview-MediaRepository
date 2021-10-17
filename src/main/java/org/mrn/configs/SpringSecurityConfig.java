package org.mrn.configs;

import org.mrn.security.CustomUserDetailService;
import org.mrn.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

@Configuration
@EnableWebSecurity
public class SpringSecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	UserService userService;
	
	@Value("${app.login.success.url}")
	private String loginSuccessUrl;
	
	@Value("${app.login.failure.url}")
	private String loginFailureUrl;

	@Autowired
	public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(new CustomUserDetailService(userService));
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		//@formatter:off
		http.authorizeRequests()
			.antMatchers("/").permitAll()
			.antMatchers("/user/*").hasRole("USER")
			.antMatchers("/graphql").authenticated()
			.and()
				.headers().frameOptions().sameOrigin()
			.and()
				.formLogin().loginProcessingUrl("/login")
				.usernameParameter("username").passwordParameter("password")
				.failureHandler((req, resp, auth) -> {
					resp.sendError(201);
				})
				.successHandler((req, resp, auth) -> {
					resp.setStatus(200);
				})
			.and()
				.logout().logoutUrl("/logout").logoutSuccessUrl("/")
			.and().cors().and().csrf().disable();
		//@formatter:on
	}
}