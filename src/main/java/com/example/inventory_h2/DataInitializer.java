package com.example.inventory_h2;

import com.example.inventory_h2.model.User;
import com.example.inventory_h2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;
    private final PasswordEncoder passwordEncoder;

    public DataInitializer(UserRepository userRepo, PasswordEncoder passwordEncoder) {
        this.userRepo = userRepo;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) throws Exception {
        if (userRepo.count() == 0) {
            User u1 = new User();
            u1.setUsername("Ania");
            u1.setEmail("ania@example.com");
            u1.setPassword(passwordEncoder.encode("password1"));  // encode password
            u1.setRoles(Set.of("ROLE_USER"));  // set roles

            User u2 = new User();
            u2.setUsername("Adam");
            u2.setEmail("adam@example.com");
            u2.setPassword(passwordEncoder.encode("password2"));
            u2.setRoles(Set.of("ROLE_USER"));

            User u3 = new User();
            u3.setUsername("Krzysztof");
            u3.setEmail("krzysztof@example.com");
            u3.setPassword(passwordEncoder.encode("password3"));
            u3.setRoles(Set.of("ROLE_USER"));

            userRepo.save(u1);
            userRepo.save(u2);
            userRepo.save(u3);

            System.out.println("Added example users with encoded passwords.");
        }
    }
}


