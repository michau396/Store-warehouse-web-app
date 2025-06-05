package com.example.inventory_h2.model;

import jakarta.persistence.*;
import java.util.Set;

@Entity
@Table(name = "users")  // <- tutaj zmieniamy nazwę tabeli z "user" na "users"
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;

    @OneToMany(mappedBy = "assignedUser")
    private Set<Product> products;

    // Gettery i settery

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Set<Product> getProducts() { return products; }
    public void setProducts(Set<Product> products) { this.products = products; }
}


