package org.example.controller;//package org.example.controller; //

// custom exception
//documentation
//service model->data layer validations,business logic
//
//unit test->data validate,exceptions,
//
//
//
//import org.example.model.Category;
//import org.example.repository.CategoryRepository;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.http.ResponseEntity;
//import org.springframework.web.bind.annotation.*;
//
//import java.util.List;
//import java.util.Map;
//import java.util.Set;
//
//this is the restcontroller class
//@RestController
//all routes start with "/api"
//@RequestMapping("/api")
//public class CategoryController {
//    //automatically injects the  category repository dependency
//    @Autowired
//    private CategoryRepository repo;
//    //get all categories at "/api/categories"
//@GetMapping("/categories")
//public ResponseEntity<?> getAllCategories() {
//    try {
//        List<Category> categories = repo.findAll();
//        if (categories.isEmpty()) {
//            return ResponseEntity.status(200)
//                    .body(Map.of("message", "No categories available to display"));
//        }
//        return ResponseEntity.ok(categories); // Status: 200 OK
//    } catch(Exception e) {
//        return ResponseEntity.status(500)
//                .body("Internal server error"); // Status: 500 Internal Server Error
//    }
//}
////get a category by an id
//@GetMapping("/categories/{id}")
//public ResponseEntity<?> getCategory(@PathVariable int id) {
//    try {
//        if (!repo.existsById(id)) {
//            return ResponseEntity.status(404)
//                    .body("Category with ID " + id + " not found");
//        }
//
//        Category category = repo.findById(id)
//                .orElseThrow(() -> new RuntimeException("Category not found"));
//
//        return ResponseEntity.ok(category);
//
//    } catch (Exception e) {
//        return ResponseEntity.status(500)
//                .body("Server error occurred");
//    }
//}
////create a new category
//@PostMapping("/categories")
//public ResponseEntity<?> postCategory(@RequestBody Map<String, Object> requestBody) {
//    // Define allowed fields
//    Set<String> allowedFields = Set.of("name");
//
//    // Check for extra fields
//    if (requestBody.keySet().stream()
//            .anyMatch(key -> !allowedFields.contains(key))) {
//        return ResponseEntity.status(400)
//                .body("Invalid request. Only 'name' field is allowed. Extra fields are not permitted.");
//    }
//
//    // Check if name is present
//    if (!requestBody.containsKey("name")) {
//        return ResponseEntity.status(400)
//                .body("Name is required");
//    }
//    //if name is empty throw error
//    String name = (String) requestBody.get("name");
//    if (name == null || name.trim().isEmpty()) {
//        return ResponseEntity.status(400)
//                .body("Category name cannot be empty");
//    }
//
//    // Check for duplicate names
//    if (repo.existsByNameIgnoreCase(name.trim())) {
//        return ResponseEntity.status(409)
//                .body("Category with name '" + name + "' already exists");
//    }
//
//    Category category = new Category();
//    category.setName(name.trim());
//    Category savedCategory = repo.save(category);
//    return ResponseEntity.status(201).body(savedCategory);
//}
////delete category by id
//@DeleteMapping("/categories/{id}")
//public ResponseEntity<?> deleteCategory(@PathVariable int id) {
//    if (!repo.existsById(id)) {
//        return ResponseEntity.status(404)
//                .body("Category with ID " + id + " not found"); // Status: 404 Not Found
//    }
//    repo.deleteById(id);
//    return ResponseEntity.status(200)
//            .body("Category successfully deleted"); // Status: 200 OK
//}
//

import org.example.exception.CustomException;
import org.example.model.Category;
import org.example.services.CategoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

////update category by id
//@PatchMapping("/categories/{id}")
//public ResponseEntity<?> updateCategory(@PathVariable int id, @RequestBody Map<String, Object> updates) {
//
//    if (!repo.existsById(id)) {
//        return ResponseEntity.status(404)
//                .body("Category with ID " + id + " not found"); // Status: 404 Not Found
//    }
//
//    if (!updates.containsKey("name")) {
//        return ResponseEntity.status(400)
//                .body("Name is required for update"); // Status: 400 Bad Request
//    }
//
//    String newName = (String) updates.get("name");
//    if (newName == null || newName.trim().isEmpty()) {
//        return ResponseEntity.status(400)
//                .body("Category name cannot be empty"); // Status: 400 Bad Request
//    }
//
//    Category existingCategory = repo.findById(id).get();
//    if (repo.existsByNameIgnoreCase(newName.trim()) &&
//            !existingCategory.getName().equalsIgnoreCase(newName.trim())) {
//        return ResponseEntity.status(409)
//                .body("Category with name '" + newName + "' already exists"); // Status: 409 Conflict
//    }
//
//    existingCategory.setName(newName.trim());
//    Category updatedCategory = repo.save(existingCategory);
//    return ResponseEntity.ok(updatedCategory); // Status: 200 OK
//}
//}
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