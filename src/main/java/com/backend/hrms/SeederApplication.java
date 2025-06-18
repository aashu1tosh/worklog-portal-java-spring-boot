package com.backend.hrms;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.backend.hrms.constants.enums.Role;
import com.backend.hrms.entity.AdminEntity;
import com.backend.hrms.entity.AuthEntity;
import com.backend.hrms.repository.AdminRepository;
import com.backend.hrms.repository.AuthRepository;

@SpringBootApplication
public class SeederApplication implements CommandLineRunner {

    private final AdminRepository adminRepository;
    private final AuthRepository authRepository;
    private final PasswordEncoder passwordEncoder;
    
    public SeederApplication(AdminRepository adminRepository, 
                           AuthRepository authRepository,
                           PasswordEncoder passwordEncoder) {
        this.adminRepository = adminRepository;
        this.authRepository = authRepository;
        this.passwordEncoder = passwordEncoder;
    }
    
    public static void main(String[] args) {
        SpringApplication.run(SeederApplication.class, args);
    }

    @Override
    public void run(String... args) throws Exception {
        seedSudoAdmin();
    }

    private void seedSudoAdmin() {
        // Check if SUDO ADMIN already exists
        String sudoAdminEmail = "sudo@admin.com";
        
        if (authRepository.findByEmail(sudoAdminEmail).isEmpty()) {
            System.out.println("🔄 Seeding SUDO ADMIN...");
            // Create Admin Entity first
            AdminEntity adminEntity = new AdminEntity();
            adminEntity.setFullName("Super Admin");
            adminEntity.setLastName("Administrator");
            
            // Create Auth Entity
            AuthEntity authEntity = new AuthEntity();
            authEntity.setEmail(sudoAdminEmail);
            authEntity.setPassword(passwordEncoder.encode("SuperSecurePassword123!"));
            authEntity.setRole(Role.SUDO_ADMIN);
            
            // Set the bidirectional relationship
            authEntity.setAdmin(adminEntity);
            adminEntity.setAuth(authEntity);
            
            // Save the auth entity (will cascade to admin due to CascadeType.ALL)
            authRepository.save(authEntity);
            
            System.out.println("✅ SUDO ADMIN seeded successfully!");
            System.out.println("📧 Email: " + sudoAdminEmail);
            System.out.println("🔐 Password: SuperSecurePassword123!");
        } else {
            System.out.println("ℹ️ SUDO ADMIN already exists, skipping seeding.");
        }
    }
}