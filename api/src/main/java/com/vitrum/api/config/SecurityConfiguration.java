package com.vitrum.api.config;

import com.vitrum.api.entity.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfiguration {

    private final JwtAuthenticationFilter jwtAuthFilter;
    private final AuthenticationProvider authenticationProvider;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .cors(httpSecurityCorsConfigurer ->
                        httpSecurityCorsConfigurer.configurationSource(request ->
                                new CorsConfiguration().applyPermitDefaultValues()))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/api/v1/auth/**")
                            .permitAll()
                        .requestMatchers(HttpMethod.GET, "/api/v1/module/course/**")
                            .hasAnyAuthority(Role.TEACHER.name(), Role.ADMIN.name(), Role.STUDENT.name())
                        .requestMatchers("/api/v1/module/course/**")
                            .hasAnyAuthority(Role.ADMIN.name(), Role.TEACHER.name())

                        .requestMatchers(HttpMethod.GET, "/api/v1/module/topic/**")
                            .hasAnyAuthority(Role.TEACHER.name(), Role.ADMIN.name(), Role.STUDENT.name())
                        .requestMatchers("/api/v1/module/topic/**")
                            .hasAnyAuthority(Role.ADMIN.name(), Role.TEACHER.name())

                        .requestMatchers(HttpMethod.GET ,"/api/v1/module/task/**")
                            .hasAnyAuthority(Role.TEACHER.name(), Role.ADMIN.name(), Role.STUDENT.name())
                        .requestMatchers("/api/v1/module/task/**")
                            .hasAnyAuthority(Role.TEACHER.name(), Role.ADMIN.name())

                        .requestMatchers(HttpMethod.POST ,"/api/v1/module/result/**")
                            .hasAnyAuthority(Role.TEACHER.name(), Role.ADMIN.name(), Role.STUDENT.name())
                        .requestMatchers(HttpMethod.GET ,"/api/v1/module/result/**")
                            .hasAnyAuthority(Role.TEACHER.name(), Role.ADMIN.name(), Role.STUDENT.name())

                        .requestMatchers("/api/v1/module/result/**")
                            .hasAnyAuthority(Role.TEACHER.name(), Role.ADMIN.name())

                )
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authenticationProvider(authenticationProvider)
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

}
