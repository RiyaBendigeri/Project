
package org.example.controller;

import org.example.dto.productPatchDTO;
import org.example.dto.productRequestDTO;
import org.example.exception.customException;
import org.example.exception.errorResponse;
import org.example.model.Product;
import org.example.services.productService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
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
    public ResponseEntity<Object> getProducts() {
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
    public ResponseEntity<Product> getProduct(@PathVariable int id) {
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
    public ResponseEntity<Product> addProduct(@Valid @RequestBody productRequestDTO dto) {
        Product savedProduct = productService.createProduct(dto);
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
    public ResponseEntity<Object> updateProduct(@PathVariable Integer id, @RequestBody productPatchDTO dto) {
        if (dto.getName() == null && dto.getPrice() == null && dto.getCategoryId() == null) {
            return ResponseEntity.badRequest()
                    .body(new errorResponse("At least one field must be provided for update", 400));
        }
        Product updatedProduct = productService.updateProduct(id, dto);
        return ResponseEntity.ok(updatedProduct);
    }
    /**
     * Deletes the product identified by its ID.
     *
     * @param id the ID of the product to delete.
     * @return ResponseEntity with a success message.
     */
    @DeleteMapping("/products/{id}")
    public ResponseEntity<String> removeProduct(@PathVariable int id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product deleted successfully");
    }
}