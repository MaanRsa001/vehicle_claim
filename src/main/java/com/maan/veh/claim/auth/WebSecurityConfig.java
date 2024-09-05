package com.maan.veh.claim.auth;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
//import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.access.channel.ChannelProcessingFilter;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

@Configuration
@EnableWebSecurity
//@EnableGlobalMethodSecurity(prePostEnabled = true)
@EnableMethodSecurity
@Order(1000)
public class WebSecurityConfig  {

	@Lazy
	@Autowired
    private UserDetailsService userDetailsService;
    
	@Lazy
    @Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Autowired
    private JwtAuthenticationEntryPoint unauthorizedHandler;

    
    @Bean
    public AuthenticationManager authenticationManagerBean(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }
    
    @Autowired
    public void globalUserDetails(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
              .passwordEncoder(bCryptPasswordEncoder);
    }

   	@Bean
    public JwtAuthenticationFilter authenticationTokenFilterBean() throws Exception {
        return new JwtAuthenticationFilter();
    }
   	
   
           
    
 
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration corsConfiguration = new CorsConfiguration();
        //Make the below setting as * to allow connection from any hos
        corsConfiguration.setAllowedOrigins(List.of("*"));
        corsConfiguration.setAllowedMethods(List.of("*"));
       // corsConfiguration.setAllowCredentials(true);
        corsConfiguration.setAllowedHeaders(List.of("*"));        
        corsConfiguration.setMaxAge(3600L);
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", corsConfiguration);
        return source;
    }
    
    private static final String HEALTHCHECK_URL = "/healthCheck/**";
    private static final String[] ALLOW_LIST = {
                HEALTHCHECK_URL, "/v3/api-docs/**", "/swagger-resources/**",
                "/swagger-ui.html/**", "/webjars/**", "/csrf/**", "/css/**"
        };
    
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http)throws Exception {
        http.csrf(AbstractHttpConfigurer::disable)
        		.cors(httpSecurityCorsConfigurer -> httpSecurityCorsConfigurer.configurationSource(corsConfigurationSource()))
        		.authorizeHttpRequests(auth -> 
        								auth.requestMatchers(HttpMethod.POST,"/login/**")
        								.permitAll()
        								.anyRequest().authenticated()
        				) 
                .formLogin(
                			formLogin-> formLogin.loginPage("/admin/region/list")
                			.loginPage("/authentication/login").permitAll()
                		)                
                .exceptionHandling(exceptionHandling -> 
                					exceptionHandling.authenticationEntryPoint(unauthorizedHandler)
                					)                
                .sessionManagement(sessionManagement ->
                			sessionManagement.sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                			);
        http
                .addFilterBefore(authenticationTokenFilterBean(), UsernamePasswordAuthenticationFilter.class)
                .addFilterBefore(new CORSFilter(), ChannelProcessingFilter.class);
        
        return http.build();
        
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
    	BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
        return encoder;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring().requestMatchers(ALLOW_LIST
                );
    }
    
}