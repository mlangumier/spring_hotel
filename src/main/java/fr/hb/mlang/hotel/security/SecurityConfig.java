package fr.hb.mlang.hotel.security;

import fr.hb.mlang.hotel.security.filter.JwtFilter;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
@RequiredArgsConstructor
public class SecurityConfig {

  private final AuthenticationConfiguration authenticationConfiguration;
  private final LogoutHandler logoutHandler;
  private final JwtFilter jwtFilter;

  @Bean
  SecurityFilterChain accessControl(HttpSecurity http) throws Exception {
    http
        // Prevents injections trying to steal cookies (no sessions)
        .csrf(AbstractHttpConfigurer::disable)
        // Manages routes authorizations
        .authorizeHttpRequests(authorizeRequests -> authorizeRequests
            // Public routes:
            .requestMatchers("/api/v1/auth/**").permitAll()
            // Public routes:
            .anyRequest().authenticated())
        // Prevents Spring Security from managing sessions (we're only using JWT)
        .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
        // Set our jwtFilter to be the method that
        .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
        .logout(logout -> logout
            .logoutUrl("/api/v1/auth/logout")
            .addLogoutHandler(logoutHandler)
            .logoutSuccessHandler((request, response, authentication) -> SecurityContextHolder.clearContext()))
    ;

    return http.build();
  }

  @Bean
  PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  AuthenticationManager getAuthenticationManager() throws Exception {
    return authenticationConfiguration.getAuthenticationManager();
  }
}
