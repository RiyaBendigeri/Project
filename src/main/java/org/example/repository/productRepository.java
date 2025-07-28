package org.example.repository;

import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
/**
 * Spring Data JPA repository for {@link Product} entities.
 *
 * <p>
 * This interface provides automatic implementations for standard CRUD operations as well as custom
 * query methods for interacting with the "products" table. By extending {@link JpaRepository},
 * it eliminates most boilerplate code needed for data access.
 * </p>
 *
 * <p>
 * Supported operations include:
 * <ul>
 *   <li>Saving, updating, deleting, and finding products by ID</li>
 *   <li>{@link #existsByName(String)}: checks if a product exists by exact (case-sensitive) name</li>
 *   <li>{@link #existsById(int)}: checks if a product exists by product ID</li>
 *   <li>{@link #existsByNameIgnoreCase(String)}: checks if a product exists by name, ignoring case</li>
 * </ul>
 * </p>
 *
 * <p>
 * Spring will automatically implement all these methods based on their names,
 * allowing easy and efficient data interaction without writing SQL queries.
 * </p>
 *
 * @see org.springframework.data.jpa.repository.JpaRepository
 * @see Product
 */
@Repository//spring repo component
public interface productRepository extends JpaRepository<Product,Integer> {
    //custom query methods
    /**
     * Checks if a category with the exact given name exists.
     *
     * @param name the category name to check (case-sensitive)
     * @return true if a category with the specified name exists, false otherwise
     */
    boolean existsByName(String name);
    /**
     * Checks if a category with the given ID exists.
     *
     * @param id the category ID to check
     * @return true if a category with the specified ID exists, false otherwise
     */
    boolean existsById(int id);
    /**
     * Checks if a category with the given name exists, ignoring case.
     *
     * @param trim the category name to check (case-insensitive)
     * @return true if a category with the specified name exists ignoring case, false otherwise
     */
    boolean existsByNameIgnoreCase(String trim);
    /**
     * Checks if a product with given category id exists, ignoring case.
     *
     * @param categoryId the id of the category
     * @return true if a product with the specified categoryId exists, false otherwise
     */
    boolean existsBycategoryId(int categoryId);

}
//jpa will create queries based on the method name