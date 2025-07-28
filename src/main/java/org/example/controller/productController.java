
package org.example.controller;

import org.example.exception.customException;
import org.example.exception.errorResponse;
import org.example.model.Product;
import org.example.services.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;
/**
 * REST controller for managing products.
 * Provides endpoints to create, read, update, and delete products.
 */
@RestController

@RequestMapping("/api")

public class productController {
    @Autowired
    private productService productService;
    /**
     * Retrieves all products.
     *
     * @return ResponseEntity containing a list of all products,
     *         or a message if no products are available.
     */
    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        List<Product> products = productService.getAllProducts();
        if (products.isEmpty()) {
            return ResponseEntity.ok(Map.of("message", "No products available to display"));
        }
        return ResponseEntity.ok(products);
    }
    /**
     * Retrieves a product by its ID.
     *
     * @param id the ID of the product to retrieve.
     * @return ResponseEntity containing the requested product.
     */
    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {
        Product product = productService.getProductById(id);
        return ResponseEntity.ok(product);
    }
    /**
     * Adds a new product with the details provided in the request body.
     * The request body must include "name", "price", and "categoryId".
     * The "id" field is not allowed as it is auto-generated.
     *
     * @param requestBody a map containing product details: "name" (String), "price" (Number), "categoryId" (Number).
     * @return ResponseEntity containing the created product.
     * @throws customException.ValidationException if required fields are missing, empty, or "id" is provided.
     *         Also if extra fields beyond "name", "price", and "categoryId" are included.
     */
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> requestBody) {
        if (requestBody.containsKey("id")) { throw new customException.ValidationException("ID should not be provided as it's auto-generated"); }
        if (!requestBody.containsKey("name")) { throw new customException.ValidationException("Product name is required"); }
        if (!requestBody.containsKey("price")) { throw new customException.ValidationException("Product price is required"); }
        if (!requestBody.containsKey("categoryId")) { throw new customException.ValidationException("Category Id is required"); }
        Set<String> allowedFields = Set.of("name", "price", "categoryId");
        Set<String> requestKeys = requestBody.keySet();

        boolean hasExtra = requestKeys.stream()
                .anyMatch(key -> !allowedFields.contains(key));

        if (hasExtra) {
            return ResponseEntity.status(400)
                    .body("Extra fields are not permitted. Allowed fields: " + allowedFields);
        }
        String name = (String) requestBody.get("name"); if (name == null || name.trim().isEmpty()) { throw new customException.ValidationException("Product name cannot be empty"); }

        Product savedProduct = productService.createProduct(requestBody);
        return ResponseEntity.status(201).body(savedProduct);
    }
    /**
     * Updates an existing product identified by its ID with the given updates.
     *
     * @param id the ID of the product to update.
     * @param updates a map of fields to update; cannot be empty.
     * @return ResponseEntity containing the updated product.
     * @throws customException.ValidationException if no updates are provided.
     */
    @PatchMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new errorResponse("Product ID must be provided", 400));
        }
        if (updates.isEmpty()) {
            throw new customException.ValidationException("No updates provided");
        }
        Product updatedProduct = productService.updateProduct(id, updates);
        return ResponseEntity.ok(updatedProduct);
    }
    /**
     * Deletes the product identified by its ID.
     *
     * @param id the ID of the product to delete.
     * @return ResponseEntity with a success message.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}