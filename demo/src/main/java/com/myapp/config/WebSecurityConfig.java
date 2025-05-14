package com.myapp.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class WebSecurityConfig {

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
            .authorizeHttpRequests((requests) -> requests
                .requestMatchers(HttpMethod.PUT, "/accounts/{accountId}/freeze").hasRole("ADMIN")
                .requestMatchers(HttpMethod.PUT, "/accounts/{accountId}/unfreeze").hasRole("ADMIN")
                .requestMatchers(HttpMethod.DELETE, "/accounts/{accountId}").hasRole("ADMIN")
                .requestMatchers("/admin/**").hasRole("ADMIN") // /admin altındaki tüm endpoint'ler ADMIN rolüne sahip kullanıcılara açık
                .anyRequest().permitAll()
            )
            .httpBasic(Customizer.withDefaults()) // withDefaults() kaldırıldı
            .csrf(csrf -> csrf.disable()); // Basitlik için CSRF korumasını devre dışı bırakıyoruz (üretim ortamında dikkatli olun)
        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        UserDetails user = User.builder()
                .username("user")
                .password(passwordEncoder().encode("userpass"))
                .roles("USER")
                .build();

        UserDetails admin = User.builder()
                .username("admin")
                .password(passwordEncoder().encode("adminpass"))
                .roles("ADMIN")
                .build();

        return new InMemoryUserDetailsManager(user, admin);
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}