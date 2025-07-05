package org.example.repository;

import org.example.model.Categories;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Categories,Integer> {
    boolean existsByNameIgnoreCase(String name);
    boolean existsById(int id);
}
