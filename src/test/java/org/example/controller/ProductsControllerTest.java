
package org.example.controller;

import org.example.model.Categories;
import org.example.model.Products;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductsRepository;
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
public class ProductsControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductsRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Categories testCategory;

    @BeforeEach
    void setUp() {
        // Clear both repositories before each test
        productRepository.deleteAll();
        categoryRepository.deleteAll();

        // Create a test category for use in tests
        testCategory = new Categories();
        testCategory.setName("Electronics");
        testCategory = categoryRepository.save(testCategory);
    }

    // GET ALL PRODUCTS TESTS
    @Test
    void getProducts_WhenNoProducts_ReturnsNoContent() throws Exception {
        mockMvc.perform(get("/api/products"))
                .andExpect(status().isNoContent())
                .andExpect(content().string("No products available to display"));
    }

    @Test
    void getProducts_WhenProductsExist_ReturnsProducts() throws Exception {
        // Create test products
        Products product1 = new Products();
        product1.setName("Laptop");
        product1.setPrice(1000);
        product1.setcatid(testCategory.getID());
        productRepository.save(product1);

        Products product2 = new Products();
        product2.setName("Phone");
        product2.setPrice(500);
        product2.setcatid(testCategory.getID());
        productRepository.save(product2);

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[1].name").value("Phone"))
                .andExpect(jsonPath("$[1].price").value(500));
    }

    // GET PRODUCT BY ID TESTS
    @Test
    void getProduct_WhenExists_ReturnsProduct() throws Exception {
        Products product = new Products();
        product.setName("Laptop");
        product.setPrice(1000);
        product.setcatid(testCategory.getID());
        Products savedProduct = productRepository.save(product);

        mockMvc.perform(get("/api/products/" + savedProduct.getID()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000));
    }

    @Test
    void getProduct_WhenNotExists_ReturnsNotFound() throws Exception {
        mockMvc.perform(get("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product with ID 999 not found"));
    }

    // POST PRODUCT TESTS
    @Test
    void addProduct_WithValidData_ReturnsCreated() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":1000,\"catid\":" + testCategory.getID() + "}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000));
    }

    @Test
    void addProduct_WithDuplicateName_ReturnsConflict() throws Exception {
        // First create a product
        Products product = new Products();
        product.setName("Laptop");
        product.setPrice(1000);
        product.setcatid(testCategory.getID());
        productRepository.save(product);

        // Try to create duplicate
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":1000,\"catid\":" + testCategory.getID() + "}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Product with name 'Laptop' already exists"));
    }

    @Test
    void addProduct_WithInvalidCategoryId_ReturnsNotFound() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":1000,\"catid\":999}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category with ID 999 does not exist"));
    }

    @Test
    void addProduct_WithInvalidPrice_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":-100,\"catid\":" + testCategory.getID() + "}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Price must be greater than 0"));
    }

    @Test
    void addProduct_WithMissingFields_ReturnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Product price is required"));
    }

    // PATCH PRODUCT TESTS
    @Test
    void updateProduct_WithValidData_ReturnsOk() throws Exception {
        // First create a product
        Products product = new Products();
        product.setName("Laptop");
        product.setPrice(1000);
        product.setcatid(testCategory.getID());
        Products savedProduct = productRepository.save(product);

        mockMvc.perform(patch("/api/products/" + savedProduct.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Laptop\",\"price\":1500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1500));
    }

    @Test
    void updateProduct_WithDuplicateName_ReturnsConflict() throws Exception {
        // Create two products
        Products product1 = new Products();
        product1.setName("Laptop");
        product1.setPrice(1000);
        product1.setcatid(testCategory.getID());
        productRepository.save(product1);

        Products product2 = new Products();
        product2.setName("Phone");
        product2.setPrice(500);
        product2.setcatid(testCategory.getID());
        Products savedProduct2 = productRepository.save(product2);

        mockMvc.perform(patch("/api/products/" + savedProduct2.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\"}"))
                .andExpect(status().isConflict())
                .andExpect(content().string("Product with name 'Laptop' already exists"));
    }

    @Test
    void updateProduct_WithInvalidPrice_ReturnsBadRequest() throws Exception {
        Products product = new Products();
        product.setName("Laptop");
        product.setPrice(1000);
        product.setcatid(testCategory.getID());
        Products savedProduct = productRepository.save(product);

        mockMvc.perform(patch("/api/products/" + savedProduct.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":-100}"))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Price must be greater than 0"));
    }

    @Test
    void updateProduct_WithInvalidCategoryId_ReturnsNotFound() throws Exception {
        Products product = new Products();
        product.setName("Laptop");
        product.setPrice(1000);
        product.setcatid(testCategory.getID());
        Products savedProduct = productRepository.save(product);

        mockMvc.perform(patch("/api/products/" + savedProduct.getID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"catid\":999}"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Category with ID 999 does not exist"));
    }

    // DELETE PRODUCT TESTS
    @Test
    void deleteProduct_WhenExists_ReturnsOk() throws Exception {
        Products product = new Products();
        product.setName("Laptop");
        product.setPrice(1000);
        product.setcatid(testCategory.getID());
        Products savedProduct = productRepository.save(product);

        mockMvc.perform(delete("/api/products/" + savedProduct.getID()))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

    @Test
    void deleteProduct_WhenNotExists_ReturnsNotFound() throws Exception {
        mockMvc.perform(delete("/api/products/999"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product with ID 999 not found, cannot delete"));
    }
}