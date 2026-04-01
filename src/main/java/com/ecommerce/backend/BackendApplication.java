package com.ecommerce.backend;

import com.ecommerce.backend.entity.Admin;
import com.ecommerce.backend.entity.UserType;
import com.ecommerce.backend.repository.UserRepo;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.security.crypto.password.PasswordEncoder;

@SpringBootApplication
@Slf4j
@RequiredArgsConstructor
public class BackendApplication {

	public static void main(String[] args) {
		SpringApplication.run(BackendApplication.class, args);
	}

	@Bean
	CommandLineRunner seedAdmin(UserRepo userRepo, PasswordEncoder passwordEncoder) {
		return args -> {
			if (userRepo.findByEmail("admin@ecommerce.com").isEmpty()) {
				Admin admin = Admin.builder()
						.name("Admin")
						.email("admin@ecommerce.com")
						.password(passwordEncoder.encode("Admin@123#$%"))
						.userType(UserType.ADMIN)
						.department("Management")
						.build();
				userRepo.save(admin);
				log.info("✅ Admin seeded successfully");
			} else {
				log.info("ℹ️ Admin already exists, skipping seed");
			}
		};
	}
}
