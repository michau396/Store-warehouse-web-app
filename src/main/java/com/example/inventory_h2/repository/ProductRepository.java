package com.example.inventory_h2.repository;

import com.example.inventory_h2.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import java.util.List;
import org.springframework.data.jpa.repository.Query;


public interface ProductRepository extends JpaRepository<Product, Long>, JpaSpecificationExecutor<Product> {
    List<Product> findByNameContainingIgnoreCaseOrCategoryContainingIgnoreCase(String name, String category);
    @Query("SELECT COUNT(p) FROM Product p")
    long countAllProducts();

    @Query("SELECT SUM(p.quantity) FROM Product p")
    Long sumAllQuantities();

    @Query("SELECT AVG(p.price) FROM Product p")
    Double averagePrice();

    @Query("SELECT p FROM Product p ORDER BY p.price DESC LIMIT 1")
    Product findMostExpensiveProduct();

    @Query("SELECT p.assignedUser.username, COUNT(p) FROM Product p GROUP BY p.assignedUser.username")
    List<Object[]> countProductsByUser();

}

