package com.example.inventory_h2.specifications;

import com.example.inventory_h2.model.Product;
import org.springframework.data.jpa.domain.Specification;

import jakarta.persistence.criteria.*;


public class ProductSpecifications {

    public static Specification<Product> filterBy(
            String keyword,
            String category,
            String supplier,
            Integer minQuantity,
            Integer maxQuantity,
            Double minPrice,
            Double maxPrice,
            Long assignedUserId
    ) {
        return (Root<Product> root, CriteriaQuery<?> query, CriteriaBuilder cb) -> {
            Predicate predicate = cb.conjunction();

            if (keyword != null && !keyword.isEmpty()) {
                Predicate namePredicate = cb.like(cb.lower(root.get("name")), "%" + keyword.toLowerCase() + "%");
                Predicate categoryPredicate = cb.like(cb.lower(root.get("category")), "%" + keyword.toLowerCase() + "%");
                predicate = cb.and(predicate, cb.or(namePredicate, categoryPredicate));
            }
            if (category != null && !category.isEmpty()) {
                predicate = cb.and(predicate, cb.equal(root.get("category"), category));
            }
            if (supplier != null && !supplier.isEmpty()) {
                predicate = cb.and(predicate, cb.like(cb.lower(root.get("supplier")), "%" + supplier.toLowerCase() + "%"));
            }
            if (minQuantity != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("quantity"), minQuantity));
            }
            if (maxQuantity != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("quantity"), maxQuantity));
            }
            if (minPrice != null) {
                predicate = cb.and(predicate, cb.greaterThanOrEqualTo(root.get("price"), minPrice));
            }
            if (maxPrice != null) {
                predicate = cb.and(predicate, cb.lessThanOrEqualTo(root.get("price"), maxPrice));
            }
            if (assignedUserId != null) {
                predicate = cb.and(predicate, cb.equal(root.get("assignedUser").get("id"), assignedUserId));
            }

            return predicate;
        };
    }
}

