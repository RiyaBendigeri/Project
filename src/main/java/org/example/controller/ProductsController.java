
    package org.example.controller;

    import org.example.repository.CategoryRepository;
    import org.example.model.Products;
    import org.example.repository.ProductsRepository;
    import org.springframework.beans.factory.annotation.Autowired;
    import org.springframework.http.ResponseEntity;
    import org.springframework.web.bind.annotation.*;

    import java.util.List;
    import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProductsController {
    @Autowired
    private ProductsRepository repo;

    @Autowired
    private CategoryRepository categoryRepo;

    @GetMapping("/products")
    public ResponseEntity<?> getProducts() {
        try {
            List<Products> products = repo.findAll();
            if (products.isEmpty()) {
                return ResponseEntity.status(200)  // or just ResponseEntity.ok()
                        .body(Map.of("message", "No products available to display"));
            }
            return ResponseEntity.ok(products);
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error fetching products: " + e.getMessage());
        }
    }

    @GetMapping("/products/{id}")
    public ResponseEntity<?> getProduct(@PathVariable int id) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.status(404)
                        .body("Product with ID " + id + " not found");
            }
            return ResponseEntity.ok(repo.findById(id).get());
        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error fetching product: " + e.getMessage());
        }
    }

    @PostMapping("/products")
    public ResponseEntity<?> addProduct(@RequestBody Map<String, Object> requestBody) {
        try {
            // Check if ID is provided
            if (requestBody.containsKey("id")) {
                return ResponseEntity.status(400)
                        .body("ID should not be provided as it's auto-generated");
            }

            // Validate required fields
            if (!requestBody.containsKey("name")) {
                return ResponseEntity.status(400)
                        .body("Product name is required");
            }
            if (!requestBody.containsKey("price")) {
                return ResponseEntity.status(400)
                        .body("Product price is required");
            }
            if (!requestBody.containsKey("catid")) {
                return ResponseEntity.status(400)
                        .body("Category ID is required");
            }

            // Validate field values
            String name = (String) requestBody.get("name");
            if (name == null || name.trim().isEmpty()) {
                return ResponseEntity.status(400)
                        .body("Product name cannot be empty");
            }

            // Check for duplicate product name
            if (repo.existsByName(name.trim())) {
                return ResponseEntity.status(409)
                        .body("Product with name '" + name + "' already exists");
            }

            // Validate price
            int price;
            try {
                price = Integer.parseInt(requestBody.get("price").toString());
                if (price <= 0) {
                    return ResponseEntity.status(400)
                            .body("Price must be greater than 0");
                }
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400)
                        .body("Invalid price format");
            }

            // Validate category ID
            int catId;
            try {
                catId = Integer.parseInt(requestBody.get("catid").toString());
            } catch (NumberFormatException e) {
                return ResponseEntity.status(400)
                        .body("Invalid category ID format");
            }

            if (!categoryRepo.existsById(catId)) {
                return ResponseEntity.status(404)
                        .body("Category with ID " + catId + " does not exist");
            }

            // Create and save product
            Products product = new Products();
            product.setName(name.trim());
            product.setPrice(price);
            product.setcatid(catId);

            Products savedProduct = repo.save(product);
            return ResponseEntity.status(201).body(savedProduct);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error creating product: " + e.getMessage());
        }
    }

    @DeleteMapping("/products/{id}")
    public ResponseEntity<?> removeProduct(@PathVariable int id) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.status(404)
                        .body("Product with ID " + id + " not found, cannot delete");
            }

            repo.deleteById(id);
            return ResponseEntity.status(200)
                    .body("Product deleted successfully");

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error deleting product: " + e.getMessage());
        }
    }

    @PatchMapping("/products/{id}")
    public ResponseEntity<?> updateProduct(@PathVariable int id, @RequestBody Map<String, Object> updates) {
        try {
            if (!repo.existsById(id)) {
                return ResponseEntity.status(404)
                        .body("Product with ID " + id + " not found");
            }

            if (updates.isEmpty()) {
                return ResponseEntity.status(400)
                        .body("No updates provided");
            }

            Products product = repo.findById(id).get();

            // Update name if provided
            if (updates.containsKey("name")) {
                String newName = (String) updates.get("name");
                if (newName == null || newName.trim().isEmpty()) {
                    return ResponseEntity.status(400)
                            .body("Product name cannot be empty");
                }
                if (repo.existsByName(newName) && !product.getName().equals(newName)) {
                    return ResponseEntity.status(409)
                            .body("Product with name '" + newName + "' already exists");
                }
                product.setName(newName.trim());
            }

            // Update price if provided
            if (updates.containsKey("price")) {
                try {
                    int newPrice = Integer.parseInt(updates.get("price").toString());
                    if (newPrice <= 0) {
                        return ResponseEntity.status(400)
                                .body("Price must be greater than 0");
                    }
                    product.setPrice(newPrice);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(400)
                            .body("Invalid price format");
                }
            }

            // Update category ID if provided
            if (updates.containsKey("catid")) {
                try {
                    int newCatId = Integer.parseInt(updates.get("catid").toString());
                    if (!categoryRepo.existsById(newCatId)) {
                        return ResponseEntity.status(404)
                                .body("Category with ID " + newCatId + " does not exist");
                    }
                    product.setcatid(newCatId);
                } catch (NumberFormatException e) {
                    return ResponseEntity.status(400)
                            .body("Invalid category ID format");
                }
            }

            Products updatedProduct = repo.save(product);
            return ResponseEntity.ok(updatedProduct);

        } catch (Exception e) {
            return ResponseEntity.status(500)
                    .body("Error updating product: " + e.getMessage());
        }
    }
}