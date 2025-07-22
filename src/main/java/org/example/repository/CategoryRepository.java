package org.example.repository;
//spring data jpa repository
//is required so that no need to write the boilerplate code for db operations
import org.example.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//spring repo component
public interface CategoryRepository extends JpaRepository<Categories,Integer> {//integer is the type of the primary key//category is entity that this repo manages
    // Custom query methods
    boolean existsByNameIgnoreCase(String name);

    boolean existsById(int id);
}
//jpa creates queries based on method names