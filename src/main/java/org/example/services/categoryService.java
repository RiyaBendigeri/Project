package org.example.services;

import org.example.exception.customException;
import org.example.model.Category;
import org.example.repository.categoryRepository;
import org.example.repository.productRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Service class that handles business logic and data validation for {@link Category} entities.
 * <p>
 * This service layer acts as an intermediate between the controller and data repository,
 * providing methods to create, retrieve, update, and delete categories. It also handles validation
 * and throws custom exceptions when business rules are violated.
 * </p>
 *
 * <p>
 * Main responsibilities:
 * <ul>
 *   <li>Retrieves all categories from the repository</li>
 *   <li>Finds a category by its unique ID, throwing an exception if not found</li>
 *   <li>Validates and creates new categories, preventing duplicates and invalid names</li>
 *   <li>Updates category names, ensuring validity and uniqueness</li>
 *   <li>Deletes a category by ID, verifying its existence</li>
 * </ul>
 * </p>
 *
 * <p>
 * All major operations throw {@link customException} subclasses to clearly indicate error conditions.
 * </p>
 *
 * @see org.example.model.Category
 * @see categoryRepository
 * @see customException
 */
@Service
public  class categoryService {
    @Autowired
    private categoryRepository repo;
    @Autowired
    private productRepository productRepo;
    /**
     * Retrieves all categories from the repository.
     *
     * @return a list of all Category objects; if none exist, returns an empty list.
     */
    public List<Category> getAllCategories() {
        return repo.findAll();
    }

    /**
     * Retrieves a category by its ID.
     *
     * @param id the unique identifier of the category to retrieve.
     * @return the Category object with the specified ID.
     * @throws customException.ResourceNotFoundException if no category with the given ID is found.
     */
    public Category getCategoryById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new customException.ResourceNotFoundException("Category with ID " + id + " not found"));
    }

    /**
     * Creates a new category with the given name.
     *
     * @param name the name of the category to create.
     * @return the newly created Category object.
     * @throws customException.ValidationException if the category name is null, empty, or only whitespace.
     * @throws customException.DuplicateResourceException if a category with the same name (case-insensitive) already exists.
     */
    public Category createCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new customException.ValidationException("Category name cannot be empty");
        }
        if (repo.existsByNameIgnoreCase(name.trim())) {
            throw new customException.DuplicateResourceException("Category with name '" + name + "' already exists");
        }
        Category category = new Category();
        category.setName(name.trim());
        return repo.save(category);
    }
    /**
     * Updates the name of an existing category identified by its ID.
     *
     * @param id the ID of the category to update.
     * @param newName the new name to set for the category.
     * @return the updated Category object.
     * @throws customException.ResourceNotFoundException if no category with the given ID exists.
     * @throws customException.ValidationException if the new category name is null, empty, or only whitespace.
     * @throws customException.DuplicateResourceException if a category with the new name (case-insensitive) already exists
     *                                                    and it's not the same category being updated.
     */

    public Category updateCategory(int id, String newName) {

        Category existing = getCategoryById(id);

        if (newName == null || newName.trim().isEmpty()) {
            throw new customException.ValidationException("Category name cannot be empty");
        }
        if (repo.existsByNameIgnoreCase(newName.trim()) &&
                !existing.getName().equalsIgnoreCase(newName.trim())) {
            throw new customException.DuplicateResourceException("Category with name '" + newName + "' already exists");
        }
        existing.setName(newName.trim());
        return repo.save(existing);
    }
    /**
     * Deletes the category with the specified ID.
     *
     * @param id the ID of the category to delete.
     * @throws customException.ResourceNotFoundException if no category with the given ID exists.
     * @throws customException.ValidationException if product with the given categoryId exists.
     */

    public void deleteCategory(int id) {
        if (!repo.existsById(id)) {
            throw new customException.ResourceNotFoundException("Category with ID " + id + " not found");
        }
// Check if products exist for this categoryId
        boolean hasProducts = productRepo.existsBycategoryId(id);
        if (hasProducts) {
            throw new customException.ValidationException("Cannot delete category: Products exist in this category!");
        }
        repo.deleteById(id);
    }
}