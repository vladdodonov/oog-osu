package com.dodonov.oogosu.config.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import javax.servlet.http.HttpServletResponse;
@EnableGlobalMethodSecurity(prePostEnabled = true, proxyTargetClass = true)
@EnableWebSecurity 	// Enable security config. This annotation denotes config for spring security.
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private UserDetailsServiceImpl userDetailsService;

	@Autowired
	private JwtConfig jwtConfig;

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		    .csrf().disable()
		     // make sure we use stateless session; session won't be used to store user's state.
	            .sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS)
	        .and()
	         // handle an authorized attempts
	            .exceptionHandling().authenticationEntryPoint((req, rsp, e) -> rsp.sendError(HttpServletResponse.SC_UNAUTHORIZED))
	        .and()
		    // Add a filter to validate user credentials and add token in the response header

		    // What's the authenticationManager()?
		    // An object provided by WebSecurityConfigurerAdapter, used to authenticate the user passing user's credentials
		    // The filter needs this auth manager to authenticate the user.
		    .addFilter(new JwtUsernameAndPasswordAuthenticationFilter(authenticationManager(), jwtConfig))
				.addFilterBefore(new JwtTokenAuthenticationFilter(jwtConfig), UsernamePasswordAuthenticationFilter.class)
				.authorizeRequests()
				// allow all who are accessing "auth" service
				.antMatchers(jwtConfig.getUri()).permitAll()
				.antMatchers("/api/appeal/create/**").permitAll()
				// must be an admin if trying to access admin area (authentication is also required here)
				//.antMatchers("/department/**").hasRole("ADMIN")
				// Any other request must be authenticated
				.anyRequest().authenticated();
	}
	
	// Spring has UserDetailsService interface, which can be overriden to provide our implementation for fetching user from database (or any other source).
	// The UserDetailsService object is used by the auth manager to load the user from database.
	// In addition, we need to define the password encoder also. So, auth manager can compare and verify passwords.
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", new CorsConfiguration().applyPermitDefaultValues());
		return source;
	}
	
	@Bean
	public JwtConfig jwtConfig() {
        	return new JwtConfig();
	}
	
	@Bean
	public BCryptPasswordEncoder passwordEncoder() {
	    return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(WebSecurity web) throws Exception {
		web
				.ignoring()
				.antMatchers("/v2/api-docs", "/swagger-resources",
						"/swagger-resources/configuration/security", "/swagger-ui.html",
						"/webjars/**", "/swagger-resources/configuration/ui");
	}
}
