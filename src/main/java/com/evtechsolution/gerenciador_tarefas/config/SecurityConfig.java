package com.evtechsolution.gerenciador_tarefas.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import static org.springframework.security.config.Customizer.withDefaults;


import com.evtechsolution.gerenciador_tarefas.services.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	
	@Autowired
	private CustomUserDetailsService userDetailsService;
	
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		return http
                .csrf(csrf -> csrf.disable())  
                .authorizeHttpRequests(auth -> auth
                    .requestMatchers("/public/**").permitAll()  // Libera endpoints públicos
                    .requestMatchers("/admin/**").hasRole("ADMIN")  // Apenas ADMIN acessa
                    .requestMatchers("/user/**").hasAnyRole("USER", "ADMIN")  // USER e ADMIN acessam
                    .anyRequest().authenticated()
                )
                .userDetailsService(userDetailsService)
                .httpBasic(withDefaults())
                .build();
    }



    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}


