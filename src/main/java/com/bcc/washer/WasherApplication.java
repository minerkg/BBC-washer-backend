// src/main/java/com/bcc/washer/WasherApplication.java
package com.bcc.washer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Import; // Keep this import for @Import

import com.bcc.washer.config.SecurityConfig; // Keep this import

@SpringBootApplication
// --- ENSURE @Import is ACTIVE to load SecurityConfig ---
@Import(SecurityConfig.class) // This line must be active
// --- END @Import ---
public class WasherApplication {

	public static void main(String[] args) {
		SpringApplication.run(WasherApplication.class, args);
		System.out.println("A pornit motorul! Vrum!");
	}

	// --- ENSURE PasswordEncoder BEAN IS REMOVED FROM HERE (it's in SecurityConfig now) ---
	// @Bean
	// public PasswordEncoder passwordEncoder() {
	//     return new BCryptPasswordEncoder();
	// }
	// --- END REMOVAL ---

	// REMEMBER TO REMOVE CommandLineRunner if it's still here.
}