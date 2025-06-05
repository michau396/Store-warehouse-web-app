package com.example.inventory_h2;

import com.example.inventory_h2.model.User;
import com.example.inventory_h2.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    private final UserRepository userRepo;

    public DataInitializer(UserRepository userRepo) {
        this.userRepo = userRepo;
    }

    @Override
    public void run(String... args) throws Exception {
        // Sprawdzamy, czy użytkownicy już istnieją, żeby nie dublować
        if (userRepo.count() == 0) {
            User u1 = new User();
            u1.setUsername("Ania");
            u1.setEmail("ania@example.com");

            User u2 = new User();
            u2.setUsername("Adam");
            u2.setEmail("adam@example.com");

            User u3 = new User();
            u3.setUsername("Krzysztof");
            u3.setEmail("krzysztof@example.com");

            userRepo.save(u1);
            userRepo.save(u2);
            userRepo.save(u3);

            System.out.println("Dodano przykładowych użytkowników.");
        }
    }
}

