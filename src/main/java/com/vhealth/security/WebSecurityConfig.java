package com.vhealth.security;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.vhealth.security.jwt.AuthEntryPointJwt;
import com.vhealth.security.jwt.AuthTokenFilter;
import com.vhealth.security.services.UserDetailsServiceImpl;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(
		// securedEnabled = true,
		// jsr250Enabled = true,
		prePostEnabled = true)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
	@Autowired
	UserDetailsServiceImpl userDetailsService;

	@Autowired
	private AuthEntryPointJwt unauthorizedHandler;

	// @Autowired(required = true)

	// private AuthTokenFilter jwtRequestFilter;

	@Bean
	public AuthTokenFilter authenticationJwtTokenFilter() {
		return new AuthTokenFilter();
	}

	@Override
	public void configure(AuthenticationManagerBuilder authenticationManagerBuilder) throws Exception {
		authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
	}

	@Bean
	@Override
	public AuthenticationManager authenticationManagerBean() throws Exception {
		return super.authenticationManagerBean();
	}

	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http.cors().and().csrf().disable().exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and()
				.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS).and().authorizeRequests()
				.antMatchers("/api/auth/**").permitAll().antMatchers("/api/test/**").permitAll().anyRequest()
				.authenticated();

		http.addFilterBefore(authenticationJwtTokenFilter(), UsernamePasswordAuthenticationFilter.class);
	}

	/*
	 * @Override protected void configure(HttpSecurity httpSecurity) throws
	 * Exception { // We don't need CSRF for this example
	 * httpSecurity.addFilterBefore(authenticationJwtTokenFilter(),
	 * UsernamePasswordAuthenticationFilter.class); httpSecurity.cors();
	 * httpSecurity.csrf().disable() // dont authenticate this particular request
	 * .authorizeRequests().antMatchers("/signin").permitAll(). // all other
	 * requests need to be authenticated anyRequest().authenticated().and(). // make
	 * sure we use stateless session; session won't be used to // store user's
	 * state.
	 * exceptionHandling().authenticationEntryPoint(unauthorizedHandler).and().
	 * sessionManagement() .sessionCreationPolicy(SessionCreationPolicy.STATELESS);
	 * 
	 * // Add a filter to validate the tokens with every request
	 * 
	 * httpSecurity.requiresChannel().anyRequest().requiresSecure();
	 * 
	 * }
	 */
	/*
	 * @Bean public CorsConfigurationSource corsConfigurationSource() {
	 * CorsConfiguration configuration = new CorsConfiguration();
	 * 
	 * List methodList = new ArrayList<>(Arrays.asList("GET", "POST", "PUT",
	 * "PATCH", "DELETE", "OPTIONS"));
	 * 
	 * List<String> list = new ArrayList<>(); list.add("*");
	 * configuration.setAllowedOrigins(list); //
	 * configuration.setAllowedMethods(list); configuration.setAllowedHeaders(list);
	 * 
	 * configuration.setAllowedMethods(methodList); //
	 * configuration.setAllowedHeaders(ImmutableList.of("Content-Type", //
	 * "content-type", "authorization", "x-requested-with", //
	 * "Access-Control-Allow-Origin", "Access-Control-Allow-Headers", //
	 * "x-auth-token", "x-app-id", "Origin", "Accept", "X-Requested-With", //
	 * "Access-Control-Request-Method", "Access-Control-Request-Headers"));
	 * 
	 * 
	 * list = new ArrayList<>(); list.add("Authorization");
	 * list.add("x-auth-token"); configuration.setExposedHeaders(list);
	 * configuration.setMaxAge(1800l); configuration.setAllowCredentials(true);
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * configuration); source.registerCorsConfiguration("/api/**", configuration);
	 * source.registerCorsConfiguration("/authenticate", configuration);
	 * 
	 * return source;
	 * 
	 * 
	 * configuration.setMaxAge(1800l); // configuration.setAllowCredentials(true);
	 * UrlBasedCorsConfigurationSource source = new
	 * UrlBasedCorsConfigurationSource(); source.registerCorsConfiguration("/**",
	 * configuration); source.registerCorsConfiguration("/api/**", configuration);
	 * source.registerCorsConfiguration("/signin", configuration);
	 * 
	 * return source; }
	 */

}
