package com.elphie.savings;

// =============================================================================
// File Name: java/SavingApplication.java
// File Description:
// This is the main file that should be running for the Service to be accessible.
// =============================================================================

// =============================================================================
// Imports
// =============================================================================
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

// =============================================================================
// Service Main Class (Initializer)
// =============================================================================
@SpringBootApplication
public class SavingsServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(SavingsServiceApplication.class, args);
	}

}
