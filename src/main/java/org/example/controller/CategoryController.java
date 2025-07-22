//custom exception
//documentation
//service model->data layer validations,business logic

//unit test->data validate,exceptions,

package org.example.controller;

import org.example.model.Categories;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

//this is the restcontroller class
@RestController
//all routes start with "/api"
@RequestMapping("/api")
public class CategoryController {
    //automatically injects the  category repository dependency
    @Autowired
    private CategoryRepository repo;
    //get all categories at "/api/categories"
@GetMapping("/categories")
public ResponseEntity<?> getAllCategories() {
    try {
        List<Categories> categories = repo.findAll();
        if (categories.isEmpty()) {
            return ResponseEntity.status(200)
                    .body(Map.of("message", "No categories available to display"));
        }
        return ResponseEntity.ok(categories); // Status: 200 OK
    } catch(Exception e) {
        return ResponseEntity.status(500)
                .body("Internal server error"); // Status: 500 Internal Server Error
    }
}
//get a category by an id
@GetMapping("/categories/{id}")
public ResponseEntity<?> getCategory(@PathVariable int id) {
    try {
        if (!repo.existsById(id)) {
            return ResponseEntity.status(404)
                    .body("Category with ID " + id + " not found");
        }

        Categories category = repo.findById(id)
                .orElseThrow(() -> new RuntimeException("Category not found"));

        return ResponseEntity.ok(category);

    } catch (Exception e) {
        return ResponseEntity.status(500)
                .body("Server error occurred");
    }
}
//create a new category
@PostMapping("/categories")
public ResponseEntity<?> postCategory(@RequestBody Map<String, Object> requestBody) {
    // Define allowed fields
    Set<String> allowedFields = Set.of("name");

    // Check for extra fields
    if (requestBody.keySet().stream()
            .anyMatch(key -> !allowedFields.contains(key))) {
        return ResponseEntity.status(400)
                .body("Invalid request. Only 'name' field is allowed. Extra fields are not permitted.");
    }

    // Check if name is present
    if (!requestBody.containsKey("name")) {
        return ResponseEntity.status(400)
                .body("Name is required");
    }
    //if name is empty throw error
    String name = (String) requestBody.get("name");
    if (name == null || name.trim().isEmpty()) {
        return ResponseEntity.status(400)
                .body("Category name cannot be empty");
    }

    // Check for duplicate names
    if (repo.existsByNameIgnoreCase(name.trim())) {
        return ResponseEntity.status(409)
                .body("Category with name '" + name + "' already exists");
    }

    Categories category = new Categories();
    category.setName(name.trim());
    Categories savedCategory = repo.save(category);
    return ResponseEntity.status(201).body(savedCategory);
}
//delete category by id
@DeleteMapping("/categories/{id}")
public ResponseEntity<?> deleteCategory(@PathVariable int id) {
    if (!repo.existsById(id)) {
        return ResponseEntity.status(404)
                .body("Category with ID " + id + " not found"); // Status: 404 Not Found
    }
    repo.deleteById(id);
    return ResponseEntity.status(200)
            .body("Category successfully deleted"); // Status: 200 OK
}
//update category by id
@PatchMapping("/categories/{id}")
public ResponseEntity<?> updateCategory(@PathVariable int id, @RequestBody Map<String, Object> updates) {

    if (!repo.existsById(id)) {
        return ResponseEntity.status(404)
                .body("Category with ID " + id + " not found"); // Status: 404 Not Found
    }

    if (!updates.containsKey("name")) {
        return ResponseEntity.status(400)
                .body("Name is required for update"); // Status: 400 Bad Request
    }

    String newName = (String) updates.get("name");
    if (newName == null || newName.trim().isEmpty()) {
        return ResponseEntity.status(400)
                .body("Category name cannot be empty"); // Status: 400 Bad Request
    }

    Categories existingCategory = repo.findById(id).get();
    if (repo.existsByNameIgnoreCase(newName.trim()) &&
            !existingCategory.getName().equalsIgnoreCase(newName.trim())) {
        return ResponseEntity.status(409)
                .body("Category with name '" + newName + "' already exists"); // Status: 409 Conflict
    }

    existingCategory.setName(newName.trim());
    Categories updatedCategory = repo.save(existingCategory);
    return ResponseEntity.ok(updatedCategory); // Status: 200 OK
}
}