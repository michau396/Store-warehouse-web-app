package com.example.inventory_h2.controller;

import com.example.inventory_h2.model.Product;
import com.example.inventory_h2.model.User;
import com.example.inventory_h2.repository.ProductRepository;
import com.example.inventory_h2.repository.UserRepository;
import com.example.inventory_h2.specifications.ProductSpecifications;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;

@Controller
public class ProductController {

    @Autowired
    private ProductRepository productRepo;

    @Autowired
    private UserRepository userRepo;

    // Inject ObjectMapper to convert objects to JSON strings
    @Autowired
    private ObjectMapper objectMapper;

    // ✅ Predefined categories for dropdown
    @ModelAttribute("categories")
    public List<String> categories() {
        return List.of("Sweets & Candies", "Chips & Savory Snacks", "Beverages", "Instant meals"); // You can expand this list
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("products", productRepo.findAll());
        return "index";
    }

    @GetMapping("/add")
    public String addForm(Model model) {
        model.addAttribute("product", new Product());
        model.addAttribute("users", userRepo.findAll());
        return "add_product";
    }

    @PostMapping("/add")
    public String addProduct(@ModelAttribute Product product, @RequestParam Long assignedUserId) {
        User user = userRepo.findById(assignedUserId).orElse(null);
        product.setAssignedUser(user);
        productRepo.save(product);
        return "redirect:/";
    }

    @GetMapping("/delete/{id}")
    public String deleteProduct(@PathVariable Long id) {
        productRepo.deleteById(id);
        return "redirect:/";
    }

    @GetMapping("/search")
    public String redirectSearch(@RequestParam("keyword") String keyword) {
        return "redirect:/products?name=" + keyword;
    }

    @GetMapping("/edit/{id}")
    public String editForm(@PathVariable Long id, Model model) {
        Product product = productRepo.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid product Id:" + id));
        model.addAttribute("product", product);
        model.addAttribute("users", userRepo.findAll());
        return "edit_product";
    }

    @PostMapping("/edit/{id}")
    public String updateProduct(@PathVariable Long id, @ModelAttribute Product product, @RequestParam Long assignedUserId) {
        User user = userRepo.findById(assignedUserId).orElse(null);
        product.setAssignedUser(user);
        product.setId(id);
        productRepo.save(product);
        return "redirect:/";
    }

    @GetMapping("/products")
    public String listProducts(
            @RequestParam(required = false) String name,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String supplier,
            @RequestParam(required = false) Integer minQuantity,
            @RequestParam(required = false) Integer maxQuantity,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) Long assignedUserId,
            @RequestParam(defaultValue = "id") String sortBy,
            @RequestParam(defaultValue = "asc") String sortDir,
            Model model) {

        Sort sort = sortDir.equalsIgnoreCase("asc") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(0, 50, sort);

        Specification<Product> spec = ProductSpecifications.filterBy(
                name,
                category,
                supplier,
                minQuantity,
                maxQuantity,
                minPrice,
                maxPrice,
                assignedUserId);

        Page<Product> page = productRepo.findAll(spec, pageable);

        model.addAttribute("products", page.getContent());
        model.addAttribute("sortBy", sortBy);
        model.addAttribute("sortDir", sortDir);
        model.addAttribute("reverseSortDir", sortDir.equals("asc") ? "desc" : "asc");
        model.addAttribute("name", name);
        model.addAttribute("category", category);
        model.addAttribute("supplier", supplier);
        model.addAttribute("minQuantity", minQuantity);
        model.addAttribute("maxQuantity", maxQuantity);
        model.addAttribute("minPrice", minPrice);
        model.addAttribute("maxPrice", maxPrice);
        model.addAttribute("assignedUserId", assignedUserId);

        return "index";
    }

    @GetMapping("/stats")
    public String showStatistics(Model model) throws JsonProcessingException {
        model.addAttribute("totalProducts", productRepo.countAllProducts());
        model.addAttribute("totalQuantity", productRepo.sumAllQuantities());
        model.addAttribute("averagePrice", productRepo.averagePrice());
        model.addAttribute("mostExpensive", productRepo.findMostExpensiveProduct());
        model.addAttribute("leastExpensive", productRepo.findLeastExpensiveProduct());
        model.addAttribute("largestQuantity", productRepo.findProductWithLargestQuantity());

        List<Object[]> userStats = productRepo.countProductsByUser();
        Map<String, Long> productsByUser = new LinkedHashMap<>();
        for (Object[] obj : userStats) {
            String username = (String) obj[0];
            Long count = (Long) obj[1];
            productsByUser.put(username != null ? username : "Unassigned", count);
        }

        // Convert keys and values to JSON strings for Thymeleaf safe JS parsing
        String userLabelsJson = objectMapper.writeValueAsString(productsByUser.keySet());
        String userDataJson = objectMapper.writeValueAsString(productsByUser.values());

        model.addAttribute("userLabels", userLabelsJson);
        model.addAttribute("userData", userDataJson);

        return "statistics";
    }
}



