package org.example.repository;

import org.example.model.Products;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository//spring repo component
public interface ProductsRepository extends JpaRepository<Products,Integer> {
    //custom query methods
    boolean existsByName(String name);
    boolean existsById(int id);

    boolean existsByNameIgnoreCase(String trim);
}
//jpa will create queries based on the method name