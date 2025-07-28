
package org.example.services;

import org.example.dto.productPatchDTO;
import org.example.dto.productRequestDTO;
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



    public Product createProduct(productRequestDTO dto) {
        if (repo.existsByNameIgnoreCase(dto.getName().trim())) {
            throw new customException.DuplicateResourceException("Product with name '" + dto.getName() + "' already exists");
        }
        if (!categoryRepo.existsById(dto.getCategoryId())) {
            throw new customException.ResourceNotFoundException("Category with ID " + dto.getCategoryId() + " does not exist");
        }
        Product product = new Product();
        product.setName(dto.getName().trim());
        product.setPrice(dto.getPrice());
        product.setcategoryId(dto.getCategoryId());
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

    public Product updateProduct(int id, productPatchDTO dto) {

        Product product = getProductById(id);

        // Track update so we can reject PATCH with empty body
        boolean hasUpdate = false;

        if (dto.getName() != null) {
            String newName = dto.getName().trim();
            if (newName.isEmpty()) {
                throw new customException.ValidationException("Product name cannot be empty");
            }
            if (repo.existsByNameIgnoreCase(newName) && !product.getName().equalsIgnoreCase(newName)) {
                throw new customException.DuplicateResourceException("Product with name '" + newName + "' already exists");
            }
            product.setName(newName);
            hasUpdate = true;
        }
        if (dto.getPrice() != null) {
            if (dto.getPrice() <= 0) {
                throw new customException.ValidationException("Price must be greater than 0");
            }
            product.setPrice(dto.getPrice());
            hasUpdate = true;
        }
        if (dto.getCategoryId() != null) {
            if (!categoryRepo.existsById(dto.getCategoryId())) {
                throw new customException.ResourceNotFoundException("Category with ID " + dto.getCategoryId() + " does not exist");
            }
            product.setcategoryId(dto.getCategoryId());
            hasUpdate = true;
        }

        if (!hasUpdate) {
            throw new customException.ValidationException("At least one field must be provided for update");
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