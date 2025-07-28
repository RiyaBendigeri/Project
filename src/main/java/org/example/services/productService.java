
package org.example.services;

import org.example.exception.customException;
import org.example.model.Product;
import org.example.repository.categoryRepository;
import org.example.repository.productRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
/**
 * Service layer handling business logic, validation, and data operations for {@link Product} entities.
 *
 * <p>
 * This class interacts with the {@link productRepository} and {@link categoryRepository} to perform
 * product creation, retrieval, update, and deletion, enforcing business rules and data integrity
 * through validation logic and custom exceptions.
 * </p>
 *
 * <p>
 * Major responsibilities include:
 * <ul>
 *   <li>Retrieving all products or a product by its unique identifier</li>
 *   <li>Creating products while checking for duplicate names, valid category IDs, and input constraints</li>
 *   <li>Updating product fields with comprehensive validation</li>
 *   <li>Safely deleting products, ensuring they exist before removal</li>
 * </ul>
 * </p>
 *
 * <p>
 * All validation and error scenarios result in the throwing of specific
 * {@link customException} subclasses to aid in diagnostics and client feedback.
 * </p>
 *
 * @see org.example.model.Product
 * @see productRepository
 * @see categoryRepository
 * @see customException
 */
@Service
public class productService {

    @Autowired
    private productRepository repo;

    @Autowired
    private categoryRepository categoryRepo;

    /**
     * Retrieves all products from the repository.
     *
     * @return a list of all Product objects; if none exist, returns an empty list.
     */
    public List<Product> getAllProducts() {
        return repo.findAll();
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the unique identifier of the product to retrieve.
     * @return the Product object with the specified ID.
     * @throws customException.ResourceNotFoundException if no product with the given ID is found.
     */
    public Product getProductById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new customException.ResourceNotFoundException(
                        "Product with ID " + id + " not found"));
    }
    /**
     * Creates a new product from the provided request data.
     *
     * @param requestBody a map containing the product details:
     *                    - "name": String, required
     *                    - "price": numeric (positive integer), required
     *                    - "categoryId": numeric, must correspond to an existing category, required
     * @return the newly created Product object.
     * @throws customException.ValidationException if required fields are missing, empty, or invalid.
     * @throws customException.DuplicateResourceException if a product with the same name (case-insensitive) already exists.
     * @throws customException.ResourceNotFoundException if the categoryId does not exist.
     */

    public Product createProduct(Map<String, Object> requestBody) {
//        if (requestBody.containsKey("id")) {
//            throw new customException.ValidationException("ID should not be provided as it's auto-generated");
//        }
//        if (!requestBody.containsKey("name")) {
//            throw new customException.ValidationException("Product name is required");
//        }
//        if (!requestBody.containsKey("price")) {
//            throw new customException.ValidationException("Product price is required");
//        }
//        if (!requestBody.containsKey("categoryId")) {
//            throw new customException.ValidationException("Category ID is required");
//        }
//
//        String name = (String) requestBody.get("name");
//        if (name == null || name.trim().isEmpty()) {
//            throw new customException.ValidationException("Product name cannot be empty");
//        }
        String name = (String) requestBody.get("name");
        if (repo.existsByNameIgnoreCase(name.trim())) {
            throw new customException.DuplicateResourceException("Product with name '" + name + "' already exists");
        }

        int price;
        try {
            price = Integer.parseInt(requestBody.get("price").toString());
            if (price <= 0) {
                throw new customException.ValidationException("Price must be greater than 0");
            }
        } catch (NumberFormatException e) {
            throw new customException.ValidationException("Invalid price format");
        }

        int categoryId;
        try {
            categoryId = Integer.parseInt(requestBody.get("categoryId").toString());
        } catch (NumberFormatException e) {
            throw new customException.ValidationException("Invalid category ID format");
        }

        if (!categoryRepo.existsById(categoryId)) {
            throw new customException.ResourceNotFoundException(
                    "Category with ID " + categoryId + " does not exist");
        }

        Product product = new Product();
        product.setName(name.trim());
        product.setPrice(price);
        product.setcategoryId(categoryId);

        return repo.save(product);
    }

    /**
     * Updates an existing product identified by its ID with the given field updates.
     *
     * @param id the ID of the product to update.
     * @param updates a map containing fields to update. Allowed keys:
     *                - "name": String, must not be empty or duplicate another product's name.
     *                - "price": numeric, must be > 0.
     *                - "categoryId": numeric, must correspond to an existing category.
     * @return the updated Product object.
     * @throws customException.ResourceNotFoundException if the product or category does not exist.
     * @throws customException.ValidationException if provided values are invalid (empty name, price ≤ 0, wrong formats).
     * @throws customException.DuplicateResourceException if the new name conflicts with another product's name.
     */
    public Product updateProduct(int id, Map<String, Object> updates) {
        Product product = getProductById(id);

//        if (updates.isEmpty()) {
//            throw new customException.ValidationException("No updates provided");
//        }

        // Update name if present
        if (updates.containsKey("name")) {
            String newName = (String) updates.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                throw new customException.ValidationException("Product name cannot be empty");
            }
            if (repo.existsByNameIgnoreCase(newName.trim()) &&
                    !product.getName().equalsIgnoreCase(newName.trim())) {
                throw new customException.DuplicateResourceException(
                        "Product with name '" + newName + "' already exists");
            }
            product.setName(newName.trim());
        }

        // Update price if present
        if (updates.containsKey("price")) {
            try {
                int newPrice = Integer.parseInt(updates.get("price").toString());
                if (newPrice <= 0) {
                    throw new customException.ValidationException("Price must be greater than 0");
                }
                product.setPrice(newPrice);
            } catch (NumberFormatException e) {
                throw new customException.ValidationException("Invalid price format");
            }
        }

        // Update category id if present
        if (updates.containsKey("categoryId")) {
            try {
                int newCatId = Integer.parseInt(updates.get("categoryId").toString());
                if (!categoryRepo.existsById(newCatId)) {
                    throw new customException.ResourceNotFoundException(
                            "Category with ID " + newCatId + " does not exist");
                }
                product.setcategoryId(newCatId);
            } catch (NumberFormatException e) {
                throw new customException.ValidationException("Invalid category ID format");
            }
        }

        return repo.save(product);
    }
    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete.
     * @throws customException.ResourceNotFoundException if no product with the given ID exists.
     */

    public void deleteProduct(int id) {
        if (!repo.existsById(id)) {
            throw new customException.ResourceNotFoundException(
                    "Product with ID " + id + " not found, cannot delete");
        }
        repo.deleteById(id);
    }
}