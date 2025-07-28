package org.example.controller;

import jakarta.validation.Valid;
import org.example.dto.categoryRequestDTO;
import org.example.exception.customException;
import org.example.model.Category;
import org.example.services.categoryService;
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
public class categoryController {
    @Autowired
    private categoryService categoryService;
/**
 * Retrieves all categories.
 * If no categories are found, returns a message indicating this.
 */
    @GetMapping("/categories")
    //gets all categories
    public ResponseEntity<Object> getAllCategories() {
        List<Category> categories = categoryService.getAllCategories();
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
    public ResponseEntity<Category> getCategory(@PathVariable int id) {
        Category category = categoryService.getCategoryById(id);
        return ResponseEntity.ok(category);
    }
    /**
     * Creates a new category.
    */

    @PostMapping("/categories")
    public ResponseEntity<Category> postCategory(@Valid @RequestBody categoryRequestDTO dto) {

        Category saved = categoryService.createCategory(dto);
        return ResponseEntity.status(201).body(saved);
    }
/**
 * Updates the name of an existing category.
 */

@PatchMapping("/categories/{id}")
public ResponseEntity<Category> updateCategory(@PathVariable int id, @Valid @RequestBody categoryRequestDTO dto) {
    Category updated = categoryService.updateCategory(id, dto);
    return ResponseEntity.ok(updated);
}
/**
 * Deletes a category by its ID.
 */
    @DeleteMapping("/categories/{id}")
    public ResponseEntity<String> deleteCategory(@PathVariable int id) {
        categoryService.deleteCategory(id);
        return ResponseEntity.ok("Category successfully deleted");
    }
}