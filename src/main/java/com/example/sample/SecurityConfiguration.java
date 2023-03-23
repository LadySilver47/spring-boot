package com.example.sample;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.oauth2.client.oidc.web.logout.OidcClientInitiatedLogoutSuccessHandler;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableGlobalMethodSecurity(securedEnabled = true)
@EnableWebSecurity
public class SecurityConfiguration {

    private final ClientRegistrationRepository clientRegistrationRepository;

    public SecurityConfiguration(ClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    OidcClientInitiatedLogoutSuccessHandler oidcLogoutSuccessHandler() {
        OidcClientInitiatedLogoutSuccessHandler successHandler =
            new OidcClientInitiatedLogoutSuccessHandler(clientRegistrationRepository);
        successHandler.setPostLogoutRedirectUri("http://localhost:8080/");
        return successHandler;
    }

    @Bean
    public DefaultSecurityFilterChain configure(HttpSecurity http) throws Exception {
       return http.authorizeHttpRequests(auth -> {
           try {
               auth
                       .requestMatchers("/").permitAll()
                       .requestMatchers("/api/**").authenticated()

                       // allow anonymous access to the root page
                       //.requestMatchers("/**").permitAll()
                       // all other requests
                       .and().cors().and().csrf().disable()
                       .oauth2ResourceServer().jwt();
           } catch (Exception e){
               throw new RuntimeException(e);
           }
               }).build();
    }
}
