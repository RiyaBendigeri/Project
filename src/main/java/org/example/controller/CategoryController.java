package org.example.controller;

import org.example.exception.CustomException;
import org.example.model.Category;
import org.example.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;


/**
 * REST controller for managing categories.
 * Provides endpoints to create, read, update, and delete categories.
 */
@RestController
@RequestMapping("/api")
public class CategoryController {
    @Autowired
    private CategoryService CategoryService;
/**
 * Retrieves all categories.
 * If no categories are found, returns a message indicating this.
 */
    @GetMapping("/categories")
    //gets all categories
    public ResponseEntity<?> getAllCategories() {
        List<Category> categories = CategoryService.getAllCategories();
        if (categories.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No categories available to display"));
        }
        return ResponseEntity.ok(categories);
    }
    /**
     * Retrieves a category by its ID.
     *
     * @param id the ID of the category to retrieve.
     * @return ResponseEntity containing the category.
     */
    @GetMapping("/categories/{id}")
    public ResponseEntity<?> getCategory(@PathVariable int id) {
        Category category = CategoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    /**
     * Creates a new category.
    */
    @PostMapping("/categories")
    public ResponseEntity<?> postCategory(@RequestBody Map<String, String> requestBody) {

        if (!requestBody.containsKey("name")) {
            throw new CustomException.ValidationException("Name is required");
        }
        Set<String> allowedFields = Set.of("name");

    // Check for extra fields
    if (requestBody.keySet().stream()
            .anyMatch(key -> !allowedFields.contains(key))) {
        return ResponseEntity.status(400)
                .body("Invalid request. Only 'name' field is allowed. Extra fields are not permitted.");
    }
        String name = (String) requestBody.get("name");
        Category saved = CategoryService.createCategory(name);
        return ResponseEntity.status(201).body(saved);
    }
/**
 * Updates the name of an existing category.
 */
    @PatchMapping("/categories/{id}")
    public ResponseEntity<?> updateCategory(@PathVariable int id, @RequestBody Map<String, String> updates) {

        if (!updates.containsKey("name")) {
            throw new CustomException.ValidationException("Name is required for update");
        }
        String newName = (String) updates.get("name");
        Category updated = CategoryService.updateCategory(id, newName);
        return ResponseEntity.ok(updated);
    }
/**
 * Deletes a category by its ID.
 */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<?> deleteCategory(@PathVariable int id) {
        CategoryService.deleteCategory(id);
        return ResponseEntity.ok("Category successfully deleted");
    }
}