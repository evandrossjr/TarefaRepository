package com.evtechsolution.gerenciador_tarefas;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.core.env.Environment;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@SpringBootApplication
public class GerenciadorTarefasApplication {

	public static void main(String[] args) {
		ApplicationContext applicationContext = SpringApplication.run(GerenciadorTarefasApplication.class, args);
        Environment environment = applicationContext.getEnvironment();
        String[] activeProfiles = environment.getActiveProfiles();

        System.out.println("Active Profiles:");
        for (String profile : activeProfiles) {
            System.out.println(profile);
        }

        System.out.println("Database URL: " + environment.getProperty("spring.datasource.url"));
        System.out.println("Database Driver: " + environment.getProperty("spring.datasource.driver-class-name"));
        System.out.println("Database Username: " + environment.getProperty("spring.datasource.username"));
        
        System.out.println(new BCryptPasswordEncoder().encode("adminpassword"));
	} 
	

}

