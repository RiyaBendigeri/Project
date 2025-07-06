
package org.example.controller;

import org.example.model.Categories;
import org.example.repository.CategoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CategoryRepository categoryRepository;

    @BeforeEach
    void setUp() {
        // Clear the database before each test
        categoryRepository.deleteAll();
    }

    // GET ALL CATEGORIES TESTS
    @Test
    void getAllCategories_WhenNoCategories_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("No categories found"));
    }

    @Test
    void getAllCategories_WhenCategoriesExist_ReturnsCategories() throws Exception {
        // Create test data
        Categories category1 = new Categories();
        category1.setName("Electronics");
        categoryRepository.save(category1);

        Categories category2 = new Categories();
        category2.setName("Books");
        categoryRepository.save(category2);

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }

    // GET CATEGORY BY ID TESTS
    @Test
    void getCategory_WhenExists_ReturnsCategory() throws Exception {
        Categories category = new Categories();
        category.setName("Electronics");
        Categories savedCategory = categoryRepository.save(category);

        mockMvc.perform(get("/api/categories/" + savedCategory.getID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void getCategory_WhenNotExists_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category with ID 999 not found"));
    }

    // POST CATEGORY TESTS
    @Test
    void postCategory_WithValidData_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }

    @Test
    void postCategory_WithExtraFields_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\",\"price\":100}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Invalid request. Only 'name' field is allowed. Extra fields are not permitted."));
    }

    @Test
    void postCategory_WithEmptyName_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Category name cannot be empty"));
    }

    @Test
    void postCategory_WithDuplicateName_ReturnsConflict() throws Exception {
        // First create a category
        Categories category = new Categories();
        category.setName("Electronics");
        categoryRepository.save(category);

        // Try to create duplicate
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Category with name 'Electronics' already exists"));
    }

    // PATCH CATEGORY TESTS
    @Test
    void patchCategory_WithValidData_ReturnsOk() throws Exception {
        // First create a category
        Categories category = new Categories();
        category.setName("Electronics");
        Categories savedCategory = categoryRepository.save(category);

        mockMvc.perform(patch("/api/categories/" + savedCategory.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Electronics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Electronics"));
    }

    @Test
    void patchCategory_WithDuplicateName_ReturnsConflict() throws Exception {
        // Create two categories
        Categories category1 = new Categories();
        category1.setName("Electronics");
        categoryRepository.save(category1);

        Categories category2 = new Categories();
        category2.setName("Books");
        Categories savedCategory2 = categoryRepository.save(category2);

        // Try to update second category with first category's name
        mockMvc.perform(patch("/api/categories/" + savedCategory2.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Category with name 'Electronics' already exists"));
    }

    @Test
    void patchCategory_WithEmptyName_ReturnsBadRequest() throws Exception {
        Categories category = new Categories();
        category.setName("Electronics");
        Categories savedCategory = categoryRepository.save(category);

        mockMvc.perform(patch("/api/categories/" + savedCategory.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Category name cannot be empty"));
    }

    // DELETE CATEGORY TESTS
    @Test
    void deleteCategory_WhenExists_ReturnsOk() throws Exception {
        Categories category = new Categories();
        category.setName("Electronics");
        Categories savedCategory = categoryRepository.save(category);

        mockMvc.perform(delete("/api/categories/" + savedCategory.getID()))
                .andExpect(status().isOk())
                .andExpect(content().string("Category successfully deleted"));
    }

    @Test
    void deleteCategory_WhenNotExists_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/categories/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category with ID 999 not found"));
    }
}