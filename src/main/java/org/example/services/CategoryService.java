package org.example.services;

import org.example.exception.CustomException;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
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
 * All major operations throw {@link CustomException} subclasses to clearly indicate error conditions.
 * </p>
 *
 * @see org.example.model.Category
 * @see org.example.repository.CategoryRepository
 * @see org.example.exception.CustomException
 */
@Service
public  class CategoryService {
    @Autowired
    private CategoryRepository repo;
    @Autowired
    private ProductRepository productRepo;
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
     * @throws CustomException.ResourceNotFoundException if no category with the given ID is found.
     */
    public Category getCategoryById(int id) {
        return repo.findById(id)
                .orElseThrow(() -> new CustomException.ResourceNotFoundException("Category with ID " + id + " not found"));
    }

    /**
     * Creates a new category with the given name.
     *
     * @param name the name of the category to create.
     * @return the newly created Category object.
     * @throws CustomException.ValidationException if the category name is null, empty, or only whitespace.
     * @throws CustomException.DuplicateResourceException if a category with the same name (case-insensitive) already exists.
     */
    public Category createCategory(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new CustomException.ValidationException("Category name cannot be empty");
        }
        if (repo.existsByNameIgnoreCase(name.trim())) {
            throw new CustomException.DuplicateResourceException("Category with name '" + name + "' already exists");
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
     * @throws CustomException.ResourceNotFoundException if no category with the given ID exists.
     * @throws CustomException.ValidationException if the new category name is null, empty, or only whitespace.
     * @throws CustomException.DuplicateResourceException if a category with the new name (case-insensitive) already exists
     *                                                    and it's not the same category being updated.
     */

    public Category updateCategory(int id, String newName) {

        Category existing = getCategoryById(id);

        if (newName == null || newName.trim().isEmpty()) {
            throw new CustomException.ValidationException("Category name cannot be empty");
        }
        if (repo.existsByNameIgnoreCase(newName.trim()) &&
                !existing.getName().equalsIgnoreCase(newName.trim())) {
            throw new CustomException.DuplicateResourceException("Category with name '" + newName + "' already exists");
        }
        existing.setName(newName.trim());
        return repo.save(existing);
    }
    /**
     * Deletes the category with the specified ID.
     *
     * @param id the ID of the category to delete.
     * @throws CustomException.ResourceNotFoundException if no category with the given ID exists.
     * @throws CustomException.ValidationException if product with the given categoryID exists.
     */

    public void deleteCategory(int id) {
        if (!repo.existsById(id)) {
            throw new CustomException.ResourceNotFoundException("Category with ID " + id + " not found");
        }
// Check if products exist for this categoryID
        boolean hasProducts = productRepo.existsByCategoryID(id);
        if (hasProducts) {
            throw new CustomException.ValidationException("Cannot delete category: Products exist in this category!");
        }
        repo.deleteById(id);
    }
}