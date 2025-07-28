package org.example.repository;
//spring data jpa repository
//is required so that no need to write the boilerplate code for db operations
import org.example.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Spring Data JPA repository for {@link Category} entities.
 *
 * <p>
 * This interface enables standard CRUD operations and custom queries for the "categories" table
 * without needing explicit query or boilerplate code in the application.
 * </p>
 *
 * <p>
 * Provided methods include:
 * <ul>
 *   <li>Saving, updating, deleting, and finding categories by ID</li>
 *   <li>{@link #existsByNameIgnoreCase(String)}: returns true if a category with the given name exists, case-insensitive</li>
 *   <li>{@link #existsById(int)}: returns true if a category with the specified ID exists</li>
 * </ul>
 * </p>
 *
 * <p>
 * Spring automatically generates and implements all methods based on their signatures,
 * making data access fast and easy.
 * </p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see Category
 */
@Repository//spring repo component
public interface CategoryRepository extends JpaRepository<Category,Integer> {//integer is the type of the primary key//category is entity that this repo manages
    // Custom query methods
    /**
     * Checks if a category with the given name exists, ignoring case.
     *
     * @param name the category name to check
     * @return true if a category with the specified name exists (case-insensitive), false otherwise
     */
    boolean existsByNameIgnoreCase(String name);
    /**
     * Checks if a category with the given ID exists.
     *
     * @param id the category ID to check
     * @return true if a category with the specified ID exists, false otherwise
     */
    boolean existsById(int id);
}
//jpa creates queries based on method names