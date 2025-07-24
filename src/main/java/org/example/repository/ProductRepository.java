package org.example.repository;

import org.example.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//spring repo component
public interface ProductRepository extends JpaRepository<Product,Integer> {
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


}
//jpa will create queries based on the method name