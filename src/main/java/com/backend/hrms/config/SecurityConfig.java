package com.backend.hrms.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
public class SecurityConfig {

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    SecurityFilterChain apiSecurity(HttpSecurity http) throws Exception {

        // Matches any URI that has "/public/" in it (segment‑safe, case‑sensitive)
        RegexRequestMatcher publicEndpoints = new RegexRequestMatcher(".*/public(/.*)?$", null);

        return http
                /* --- basics -------------------------------------------------- */
                .csrf(csrf -> csrf.disable()) // stateless API → CSRF not needed
                // .sessionManagement(sm -> sm
                // .sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                /* --- authorization rules ------------------------------------ */
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(publicEndpoints).permitAll() // open
                        .anyRequest().authenticated()) // everything else protected

                /*
                 * --- (optional) extra filters --------------------------------
                 * .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                 * ------------------------------------------------------------
                 */

                .build();
    }
}