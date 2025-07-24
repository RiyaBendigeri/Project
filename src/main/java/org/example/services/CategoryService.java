package org.example.services;

import org.example.exception.CustomException;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public  class CategoryService {
    @Autowired
    private CategoryRepository repo;

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
     */

    public void deleteCategory(int id) {
        if (!repo.existsById(id)) {
            throw new CustomException.ResourceNotFoundException("Category with ID " + id + " not found");
        }

        repo.deleteById(id);
    }
}