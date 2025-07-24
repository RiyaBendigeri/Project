//
//    package org.example.controller;
//
//    import org.example.repository.CategoryRepository;
//    import org.example.model.Product;
//    import org.example.repository.ProductRepository;
//    import org.springframework.beans.factory.annotation.Autowired;
//    import org.springframework.http.ResponseEntity;
//    import org.springframework.web.bind.annotation.*;
//
//    import java.util.List;
//    import java.util.Map;
//
//@RestController
//@RequestMapping("/api")
//public class ProductController {
//    @Autowired
//    private ProductRepository repo;
//
//    @Autowired
//    private CategoryRepository categoryRepo;
//
//    @GetMapping("/products")
//    public ResponseEntity<?> getProducts() {
//        try {
//            List<Product> products = repo.findAll();
//            if (products.isEmpty()) {
//                return ResponseEntity.status(200)  // or just ResponseEntity.ok()
//                        .body(Map.of("message", "No products available to display"));
//            }
//            return ResponseEntity.ok(products);
//        } catch (Exception e) {
//            return ResponseEntity.status(500)
//                    .body("Error fetching products: " + e.getMessage());
//        }
//    }
//
//    @GetMapping("/products/{id}")
//    public ResponseEntity<?> getProduct(@PathVariable int id) {
//        try {
//            if (!repo.existsById(id)) {
//                return ResponseEntity.status(404)
//                        .body("Product with ID " + id + " not found");
//            }
//            return ResponseEntity.ok(repo.findById(id).get());
//        } catch (Exception e) {
//            return ResponseEntity.status(500)
//                    .body("Error fetching product: " + e.getMessage());
//        }
//    }
//
//    @PostMapping("/products")
//    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> requestBody) {
//        try {
//            // Check if ID is provided
//            if (requestBody.containsKey("id")) {
//                return ResponseEntity.status(400)
//                        .body("ID should not be provided as it's auto-generated");
//            }
//
//            // Validate required fields
//            if (!requestBody.containsKey("name")) {
//                return ResponseEntity.status(400)
//                        .body("Product name is required");
//            }
//            if (!requestBody.containsKey("price")) {
//                return ResponseEntity.status(400)
//                        .body("Product price is required");
//            }
//            if (!requestBody.containsKey("catid")) {
//                return ResponseEntity.status(400)
//                        .body("Category ID is required");
//            }
//
//            // Validate field values
//            String name = (String) requestBody.get("name");
//            if (name == null || name.trim().isEmpty()) {
//                return ResponseEntity.status(400)
//                        .body("Product name cannot be empty");
//            }
//
//            // Check for duplicate product name
//            if (repo.existsByNameIgnoreCase(name.trim())) {
//                return ResponseEntity.status(409)
//                        .body("Product with name '" + name + "' already exists");
//            }
//
//
//            // Validate price
//            int price;
//            try {
//                price = Integer.parseInt(requestBody.get("price").toString());
//                if (price <= 0) {
//                    return ResponseEntity.status(400)
//                            .body("Price must be greater than 0");
//                }
//            } catch (NumberFormatException e) {
//                return ResponseEntity.status(400)
//                        .body("Invalid price format");
//            }
//
//            // Validate category ID
//            int catId;
//            try {
//                catId = Integer.parseInt(requestBody.get("catid").toString());
//            } catch (NumberFormatException e) {
//                return ResponseEntity.status(400)
//                        .body("Invalid category ID format");
//            }
//
//            if (!categoryRepo.existsById(catId)) {
//                return ResponseEntity.status(404)
//                        .body("Category with ID " + catId + " does not exist");
//            }
//
//            // Create and save product
//            Product product = new Product();
//            product.setName(name.trim());
//            product.setPrice(price);
//            product.setcategoryID(catId);
//
//            Product savedProduct = repo.save(product);
//            return ResponseEntity.status(201).body(savedProduct);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500)
//                    .body("Error creating product: " + e.getMessage());
//        }
//    }
//
//    @DeleteMapping("/products/{id}")
//    public ResponseEntity<?> removeProduct(@PathVariable int id) {
//        try {
//            if (!repo.existsById(id)) {
//                return ResponseEntity.status(404)
//                        .body("Product with ID " + id + " not found, cannot delete");
//            }
//
//            repo.deleteById(id);
//            return ResponseEntity.status(200)
//                    .body("Product deleted successfully");
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500)
//                    .body("Error deleting product: " + e.getMessage());
//        }
//    }
//
//    @PatchMapping("/products/{id}")
//    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Map<String, Object> updates) {
//        try {
//            if (!repo.existsById(id)) {
//                return ResponseEntity.status(404)
//                        .body("Product with ID " + id + " not found");
//            }
//
//            if (updates.isEmpty()) {
//                return ResponseEntity.status(400)
//                        .body("No updates provided");
//            }
//
//            Product product = repo.findById(id).get();
//
//            // Update name if provided
//            if (updates.containsKey("name")) {
//                String newName = (String) updates.get("name");
//                if (newName == null || newName.trim().isEmpty()) {
//                    return ResponseEntity.status(400)
//                            .body("Product name cannot be empty");
//                }
////                if (repo.existsByName(newName) && !product.getName().equals(newName)) {
////                    return ResponseEntity.status(409)
////                            .body("Product with name '" + newName + "' already exists");
////                }
//                if (repo.existsByNameIgnoreCase(newName.trim()) &&
//                        !product.getName().equalsIgnoreCase(newName.trim())) {
//                    return ResponseEntity.status(409)
//                            .body("Product with name '" + newName + "' already exists"); // Status: 409 Conflict
//                }
//                product.setName(newName.trim());
//            }
//
//            // Update price if provided
//            if (updates.containsKey("price")) {
//                try {
//                    int newPrice = Integer.parseInt(updates.get("price").toString());
//                    if (newPrice <= 0) {
//                        return ResponseEntity.status(400)
//                                .body("Price must be greater than 0");
//                    }
//                    product.setPrice(newPrice);
//                } catch (NumberFormatException e) {
//                    return ResponseEntity.status(400)
//                            .body("Invalid price format");
//                }
//            }
//
//            // Update category ID if provided
//            if (updates.containsKey("catid")) {
//                try {
//                    int newCatId = Integer.parseInt(updates.get("catid").toString());
//                    if (!categoryRepo.existsById(newCatId)) {
//                        return ResponseEntity.status(404)
//                                .body("Category with ID " + newCatId + " does not exist");
//                    }
//                    product.setcategoryID(newCatId);
//                } catch (NumberFormatException e) {
//                    return ResponseEntity.status(400)
//                            .body("Invalid category ID format");
//                }
//            }
//
//            Product updatedProduct = repo.save(product);
//            return ResponseEntity.ok(updatedProduct);
//
//        } catch (Exception e) {
//            return ResponseEntity.status(500)
//                    .body("Error updating product: " + e.getMessage());
//        }
//    }
//}
package org.example.controller;

import org.example.exception.CustomException;
import org.example.exception.ErrorResponse;
import org.example.model.Product;
import org.example.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.Set;

@RestController
@RequestMapping("/api")
public class ProductController {
    @Autowired
    private ProductService productService;
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
     * The request body must include "name", "price", and "categoryID".
     * The "id" field is not allowed as it is auto-generated.
     *
     * @param requestBody a map containing product details: "name" (String), "price" (Number), "categoryID" (Number).
     * @return ResponseEntity containing the created product.
     * @throws CustomException.ValidationException if required fields are missing, empty, or "id" is provided.
     *         Also if extra fields beyond "name", "price", and "categoryID" are included.
     */
    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> requestBody) {
        if (requestBody.containsKey("id")) { throw new CustomException.ValidationException("ID should not be provided as it's auto-generated"); }
        if (!requestBody.containsKey("name")) { throw new CustomException.ValidationException("Product name is required"); }
        if (!requestBody.containsKey("price")) { throw new CustomException.ValidationException("Product price is required"); }
        if (!requestBody.containsKey("categoryID")) { throw new CustomException.ValidationException("Category ID is required"); }
        Set<String> allowedFields = Set.of("name", "price", "categoryID");
        Set<String> requestKeys = requestBody.keySet();

        boolean hasExtra = requestKeys.stream()
                .anyMatch(key -> !allowedFields.contains(key));

        if (hasExtra) {
            return ResponseEntity.status(400)
                    .body("Extra fields are not permitted. Allowed fields: " + allowedFields);
        }
        String name = (String) requestBody.get("name"); if (name == null || name.trim().isEmpty()) { throw new CustomException.ValidationException("Product name cannot be empty"); }

        Product savedProduct = productService.createProduct(requestBody);
        return ResponseEntity.status(201).body(savedProduct);
    }
    /**
     * Updates an existing product identified by its ID with the given updates.
     *
     * @param id the ID of the product to update.
     * @param updates a map of fields to update; cannot be empty.
     * @return ResponseEntity containing the updated product.
     * @throws CustomException.ValidationException if no updates are provided.
     */
    @PatchMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable Integer id, @RequestBody Map<String, Object> updates) {
        if (id == null) {
            return ResponseEntity.badRequest().body(new ErrorResponse("Product ID must be provided", 400));
        }
        if (updates.isEmpty()) {
            throw new CustomException.ValidationException("No updates provided");
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