
package org.example.services;

import org.example.exception.CustomException;
import org.example.model.Product;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class ProductService {

    @Autowired
    private ProductRepository repo;

    @Autowired
    private CategoryRepository categoryRepo;

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
     * @throws CustomException.ResourceNotFoundException if no product with the given ID is found.
     */
    public Product getProductById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException(
                        "Product with ID " + id + " not found"));
    }
    /**
     * Creates a new product from the provided request data.
     *
     * @param requestBody a map containing the product details:
     *                    - "name": String, required
     *                    - "price": numeric (positive integer), required
     *                    - "categoryID": numeric, must correspond to an existing category, required
     * @return the newly created Product object.
     * @throws CustomException.ValidationException if required fields are missing, empty, or invalid.
     * @throws CustomException.DuplicateResourceException if a product with the same name (case-insensitive) already exists.
     * @throws CustomException.ResourceNotFoundException if the categoryID does not exist.
     */

    public Product createProduct(Map<String, Object> requestBody) {
//        if (requestBody.containsKey("id")) {
//            throw new CustomException.ValidationException("ID should not be provided as it's auto-generated");
//        }
//        if (!requestBody.containsKey("name")) {
//            throw new CustomException.ValidationException("Product name is required");
//        }
//        if (!requestBody.containsKey("price")) {
//            throw new CustomException.ValidationException("Product price is required");
//        }
//        if (!requestBody.containsKey("categoryID")) {
//            throw new CustomException.ValidationException("Category ID is required");
//        }
//
//        String name = (String) requestBody.get("name");
//        if (name == null || name.trim().isEmpty()) {
//            throw new CustomException.ValidationException("Product name cannot be empty");
//        }
        String name = (String) requestBody.get("name");
        if (repo.existsByNameIgnoreCase(name.trim())) {
            throw new CustomException.DuplicateResourceException("Product with name '" + name + "' already exists");
        }

        int price;
        try {
            price = Integer.parseInt(requestBody.get("price").toString());
            if (price <= 0) {
                throw new CustomException.ValidationException("Price must be greater than 0");
            }
        } catch (NumberFormatException e) {
            throw new CustomException.ValidationException("Invalid price format");
        }

        int categoryID;
        try {
            categoryID = Integer.parseInt(requestBody.get("categoryID").toString());
        } catch (NumberFormatException e) {
            throw new CustomException.ValidationException("Invalid category ID format");
        }

        if (!categoryRepo.existsById(categoryID)) {
            throw new CustomException.ResourceNotFoundException(
                    "Category with ID " + categoryID + " does not exist");
        }

        Product product = new Product();
        product.setName(name.trim());
        product.setPrice(price);
        product.setcategoryID(categoryID);

        return repo.save(product);
    }

    /**
     * Updates an existing product identified by its ID with the given field updates.
     *
     * @param id the ID of the product to update.
     * @param updates a map containing fields to update. Allowed keys:
     *                - "name": String, must not be empty or duplicate another product's name.
     *                - "price": numeric, must be > 0.
     *                - "categoryID": numeric, must correspond to an existing category.
     * @return the updated Product object.
     * @throws CustomException.ResourceNotFoundException if the product or category does not exist.
     * @throws CustomException.ValidationException if provided values are invalid (empty name, price ≤ 0, wrong formats).
     * @throws CustomException.DuplicateResourceException if the new name conflicts with another product's name.
     */
    public Product updateProduct(int id, Map<String, Object> updates) {
        Product product = getProductById(id);

//        if (updates.isEmpty()) {
//            throw new CustomException.ValidationException("No updates provided");
//        }

        // Update name if present
        if (updates.containsKey("name")) {
            String newName = (String) updates.get("name");
            if (newName == null || newName.trim().isEmpty()) {
                throw new CustomException.ValidationException("Product name cannot be empty");
            }
            if (repo.existsByNameIgnoreCase(newName.trim()) &&
                    !product.getName().equalsIgnoreCase(newName.trim())) {
                throw new CustomException.DuplicateResourceException(
                        "Product with name '" + newName + "' already exists");
            }
            product.setName(newName.trim());
        }

        // Update price if present
        if (updates.containsKey("price")) {
            try {
                int newPrice = Integer.parseInt(updates.get("price").toString());
                if (newPrice <= 0) {
                    throw new CustomException.ValidationException("Price must be greater than 0");
                }
                product.setPrice(newPrice);
            } catch (NumberFormatException e) {
                throw new CustomException.ValidationException("Invalid price format");
            }
        }

        // Update category id if present
        if (updates.containsKey("categoryID")) {
            try {
                int newCatId = Integer.parseInt(updates.get("categoryID").toString());
                if (!categoryRepo.existsById(newCatId)) {
                    throw new CustomException.ResourceNotFoundException(
                            "Category with ID " + newCatId + " does not exist");
                }
                //product.setcategoryID(newCatId);
            } catch (NumberFormatException e) {
                throw new CustomException.ValidationException("Invalid category ID format");
            }
        }

        return repo.save(product);
    }
    /**
     * Deletes a product by its ID.
     *
     * @param id the ID of the product to delete.
     * @throws CustomException.ResourceNotFoundException if no product with the given ID exists.
     */

    public void deleteProduct(int id) {
        if (!repo.existsById(id)) {
            throw new CustomException.ResourceNotFoundException(
                    "Product with ID " + id + " not found, cannot delete");
        }
        repo.deleteById(id);
    }
}