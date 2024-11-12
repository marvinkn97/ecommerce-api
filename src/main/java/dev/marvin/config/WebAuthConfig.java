package dev.marvin.config;

import dev.marvin.filter.CustomAccessDeniedHandler;
import dev.marvin.filter.CustomAuthenticationEntryPoint;
import dev.marvin.filter.JwtValidationFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

@Configuration
@RequiredArgsConstructor
@EnableWebSecurity
@EnableMethodSecurity
public class WebAuthConfig {
    private final PasswordEncoder passwordEncoder;
    private final UserDetailsService userDetailsService;
    private final CorsConfigurationSource corsConfigurationSource;
    private final JwtValidationFilter jwtValidationFilter;
    private final CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
    private final CustomAccessDeniedHandler customAccessDeniedHandler;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
        return httpSecurity
                .cors(c -> c.configurationSource(corsConfigurationSource))
                .csrf(AbstractHttpConfigurer::disable)
                .sessionManagement(c -> c.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(c -> {
                    c.requestMatchers("api/v1/auth/**",
                            "/v3/api-docs/**",       // Swagger 3.0 API docs
                            "/swagger-ui/**",         // Swagger UI
                            "/swagger-resources/**",  // Swagger resources
                            "/webjars/**",            // Webjars (Swagger's static content)
                            "/v2/api-docs/**").permitAll();
                    c.anyRequest().authenticated();
                })
                .addFilterBefore(jwtValidationFilter, UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling(c -> {
                    c.authenticationEntryPoint(customAuthenticationEntryPoint);
                    c.accessDeniedHandler(customAccessDeniedHandler);
                })

                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider daoAuthenticationProvider = new DaoAuthenticationProvider();
        daoAuthenticationProvider.setPasswordEncoder(passwordEncoder);
        daoAuthenticationProvider.setUserDetailsService(userDetailsService);
        return daoAuthenticationProvider;
    }


}
