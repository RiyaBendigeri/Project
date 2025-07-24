//
//package org.example.controller;
//
//import org.example.model.Category;
//import org.example.model.Product;
//import org.example.repository.CategoryRepository;
//import org.example.repository.ProductRepository;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.http.MediaType;
//import org.springframework.test.web.servlet.MockMvc;
//
//import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//@SpringBootTest
//@AutoConfigureMockMvc//mockmvc is needed so that no need to start an actual HTTP server
//public class ProductControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private ProductRepository productRepository;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    private Category testCategory;
//
//    @BeforeEach
//    void setUp() {
//        // Clear both repositories before each test
//        productRepository.deleteAll();
//        categoryRepository.deleteAll();
//
//        // Create a test category for use in tests
//        testCategory = new Category();
//        testCategory.setName("Electronics");
//        testCategory = categoryRepository.save(testCategory);
//    }
//
//    //test for"/get" when products is empty
//    @Test
//    void getProducts_WhenNoProducts_ReturnsNoContent() throws Exception {
//        mockMvc.perform(get("/api/products"))
//                .andExpect(status().isNoContent())
//                .andExpect(content().string("No products available to display"));
//    }
//    //test for"/get" when products exists
//    @Test
//    void getProducts_WhenProductsExist_ReturnsProducts() throws Exception {
//        // Create test products
//        Product product1 = new Product();
//        product1.setName("Laptop");
//        product1.setPrice(1000);
//        product1.setcategoryID(testCategory.getID());
//        productRepository.save(product1);
//
//        Product product2 = new Product();
//        product2.setName("Phone");
//        product2.setPrice(500);
//        product2.setcategoryID(testCategory.getID());
//        productRepository.save(product2);
//
//        mockMvc.perform(get("/api/products"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Laptop"))
//                .andExpect(jsonPath("$[0].price").value(1000))
//                .andExpect(jsonPath("$[1].name").value("Phone"))
//                .andExpect(jsonPath("$[1].price").value(500));
//    }
//
//    //test for"/get:id" when product with that id exists
//    @Test
//    void getProduct_WhenExists_ReturnsProduct() throws Exception {
//        Product product = new Product();
//        product.setName("Laptop");
//        product.setPrice(1000);
//        product.setcategoryID(testCategory.getID());
//        Product savedProduct = productRepository.save(product);
//
//        mockMvc.perform(get("/api/products/" + savedProduct.getID()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Laptop"))
//                .andExpect(jsonPath("$.price").value(1000));
//    }
//    //test for"/get:id" when product with that id does not exist
//    @Test
//    void getProduct_WhenNotExists_ReturnsNotFound() throws Exception {
//        mockMvc.perform(get("/api/products/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Product with ID 999 not found"));
//    }
//
//    //test for"/post" when valid data is created
//    @Test
//    void addProduct_WithValidData_ReturnsCreated() throws Exception {
//        mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Laptop\",\"price\":1000,\"catid\":" + testCategory.getID() + "}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("Laptop"))
//                .andExpect(jsonPath("$.price").value(1000));
//    }
//    //test for"/post" when adding a product with duplicate name
//    @Test
//    void addProduct_WithDuplicateName_ReturnsConflict() throws Exception {
//        // First create a product
//        Product product = new Product();
//        product.setName("Laptop");
//        product.setPrice(1000);
//        product.setcategoryID(testCategory.getID());
//        productRepository.save(product);
//
//        // Try to create duplicate
//        mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Laptop\",\"price\":1000,\"catid\":" + testCategory.getID() + "}"))
//                .andExpect(status().isConflict())
//                .andExpect(content().string("Product with name 'Laptop' already exists"));
//    }
//    //test for"/post" when product with that id does not exist
//    @Test
//    void addProduct_WithInvalidCategoryId_ReturnsNotFound() throws Exception {
//        mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Laptop\",\"price\":1000,\"catid\":999}"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Category with ID 999 does not exist"));
//    }
//    //test for"/post" when product is added but with price invalid
//    @Test
//    void addProduct_WithInvalidPrice_ReturnsBadRequest() throws Exception {
//        mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Laptop\",\"price\":-100,\"catid\":" + testCategory.getID() + "}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Price must be greater than 0"));
//    }
//    //test for"/post" when the contents are missing
//    @Test
//    void addProduct_WithMissingFields_ReturnsBadRequest() throws Exception {
//        mockMvc.perform(post("/api/products")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Laptop\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Product price is required"));
//    }
//
//    //test for"/patch:id" when we have updated the price and name
//    @Test
//    void updateProduct_WithValidData_ReturnsOk() throws Exception {
//        // First create a product
//        Product product = new Product();
//        product.setName("Laptop");
//        product.setPrice(1000);
//        product.setcategoryID(testCategory.getID());
//        Product savedProduct = productRepository.save(product);
//
//        mockMvc.perform(patch("/api/products/" + savedProduct.getID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Updated Laptop\",\"price\":1500}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Laptop"))
//                .andExpect(jsonPath("$.price").value(1500));
//    }
//    //test for"/patch:id" when we have updated  name with a name that has duplicate value
//    @Test
//    void updateProduct_WithDuplicateName_ReturnsConflict() throws Exception {
//        // Create two products
//        Product product1 = new Product();
//        product1.setName("Laptop");
//        product1.setPrice(1000);
//        product1.setcategoryID(testCategory.getID());
//        productRepository.save(product1);
//
//        Product product2 = new Product();
//        product2.setName("Phone");
//        product2.setPrice(500);
//        product2.setcategoryID(testCategory.getID());
//        Product savedProduct2 = productRepository.save(product2);
//
//        mockMvc.perform(patch("/api/products/" + savedProduct2.getID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Laptop\"}"))
//                .andExpect(status().isConflict())
//                .andExpect(content().string("Product with name 'Laptop' already exists"));
//    }
////patch:id but when price is invalid
//    @Test
//    void updateProduct_WithInvalidPrice_ReturnsBadRequest() throws Exception {
//        Product product = new Product();
//        product.setName("Laptop");
//        product.setPrice(1000);
//        product.setcategoryID(testCategory.getID());
//        Product savedProduct = productRepository.save(product);
//
//        mockMvc.perform(patch("/api/products/" + savedProduct.getID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"price\":-100}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Price must be greater than 0"));
//    }
////patch when invalid catid which does not exist taht is given
//    @Test
//    void updateProduct_WithInvalidCategoryId_ReturnsNotFound() throws Exception {
//        Product product = new Product();
//        product.setName("Laptop");
//        product.setPrice(1000);
//        product.setcategoryID(testCategory.getID());
//        Product savedProduct = productRepository.save(product);
//
//        mockMvc.perform(patch("/api/products/" + savedProduct.getID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"catid\":999}"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Category with ID 999 does not exist"));
//    }
//
//    // DELETE PRODUCT TESTS
//    @Test
//    void deleteProduct_WhenExists_ReturnsOk() throws Exception {
//        Product product = new Product();
//        product.setName("Laptop");
//        product.setPrice(1000);
//        product.setcategoryID(testCategory.getID());
//        Product savedProduct = productRepository.save(product);
//
//        mockMvc.perform(delete("/api/products/" + savedProduct.getID()))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Product deleted successfully"));
//    }
//
//    @Test
//    void deleteProduct_WhenNotExists_ReturnsNotFound() throws Exception {
//        mockMvc.perform(delete("/api/products/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Product with ID 999 not found, cannot delete"));
//    }
//}
package org.example.controller;

import org.example.model.Product;
import org.example.services.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(ProductController.class)
class ProductControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService; // This is a mock!

    // Test for "/get" when products is empty
    /**
     * Test GET /api/products when no products exist.
     * Mocks the service to return an empty list.
     * Expects HTTP 200 OK and a JSON message indicating no products are available.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void getProducts_WhenNoProducts_ReturnsNoContent() throws Exception {
        when(productService.getAllProducts()).thenReturn(List.of());

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No products available to display"));
    }

    // Test for "/get" when products exist
    /**
     * Test GET /api/products when products exist.
     * Mocks the service to return a list of products.
     * Expects HTTP 200 OK and JSON array containing product details.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void getProducts_WhenProductsExist_ReturnsProducts() throws Exception {
        Product product1 = new Product();
        product1.setName("Laptop");
        product1.setPrice(1000);

        Product product2 = new Product();
        product2.setName("Phone");
        product2.setPrice(500);

        when(productService.getAllProducts()).thenReturn(List.of(product1, product2));

        mockMvc.perform(get("/api/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Laptop"))
                .andExpect(jsonPath("$[0].price").value(1000))
                .andExpect(jsonPath("$[1].name").value("Phone"))
                .andExpect(jsonPath("$[1].price").value(500));
    }

    // Test for "/get/:id" when product exists
    /**
     * Test GET /api/products/{id} when the product exists.
     * Mocks the service to return a product for the given ID.
     * Expects HTTP 200 OK and JSON with product details.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void getProduct_WhenExists_ReturnsProduct() throws Exception {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1000);

        when(productService.getProductById(5)).thenReturn(product);

        mockMvc.perform(get("/api/products/5"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000));
    }

    // Test for "/get/:id" when product does NOT exist
//    @Test
//    void getProduct_WhenNotExists_ReturnsNotFound() throws Exception {
//        when(productService.getProductById(999)).thenThrow(
//                new org.example.exception.CustomException.ResourceNotFoundException("Product with ID 999 not found")
//        );
//
//        mockMvc.perform(get("/api/products/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Product with ID 999 not found"));
//    }

    // Test for "/post" when valid
    /**
     * Test POST /api/products with valid product data.
     * Mocks the service to create and return the product.
     * Expects HTTP 201 Created and JSON with created product details.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void addProduct_WithValidData_ReturnsCreated() throws Exception {
        Product product = new Product();
        product.setName("Laptop");
        product.setPrice(1000);

        Map<String, Object> requestBody = Map.of(
                "name", "Laptop",
                "price", 1000,
                "categoryID", 1
        );

        when(productService.createProduct(anyMap())).thenReturn(product);

        mockMvc.perform(post("/api/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Laptop\",\"price\":1000,\"categoryID\":1}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Laptop"))
                .andExpect(jsonPath("$.price").value(1000));
    }

    // Test POST with duplicate name
//        @Test
//        void addProduct_WithDuplicateName_ReturnsConflict() throws Exception {
//            when(productService.createProduct(anyMap())).thenThrow(
//                    new org.example.exception.CustomException.DuplicateResourceException("Product with name 'Laptop' already exists")
//            );
//
//            mockMvc.perform(post("/api/products")
//                            .contentType(MediaType.APPLICATION_JSON)
//                            .content("{\"name\":\"Laptop\",\"price\":1000,\"categoryID\":1}"))
//                    .andExpect(status().isConflict())
//                    .andExpect(content().string("Product with name 'Laptop' already exists"));
//        }

    // Test PATCH with valid update
    /**
     * Test PATCH /api/products/{id} with valid update data.
     * Mocks the service to update and return the updated product.
     * Expects HTTP 200 OK and JSON with updated product details.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void updateProduct_WithValidData_ReturnsOk() throws Exception {
        Product updatedProduct = new Product();
        updatedProduct.setName("Updated Laptop");
        updatedProduct.setPrice(1500);

        when(productService.updateProduct(eq(5), anyMap())).thenReturn(updatedProduct);

        mockMvc.perform(patch("/api/products/5")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Laptop\",\"price\":1500}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Laptop"))
                .andExpect(jsonPath("$.price").value(1500));
    }

    // Test DELETE when exists
    /**
     * Test DELETE /api/products/{id} when the product exists.
     * Mocks the service to successfully delete the product.
     * Expects HTTP 200 OK and a success message.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void deleteProduct_WhenExists_ReturnsOk() throws Exception {
        doNothing().when(productService).deleteProduct(5);

        mockMvc.perform(delete("/api/products/5"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product deleted successfully"));
    }

    // Test DELETE when NOT exists
//    @Test
//    void deleteProduct_WhenNotExists_ReturnsNotFound() throws Exception {
//        doThrow(new org.example.exception.CustomException.ResourceNotFoundException("Product with ID 999 not found, cannot delete"))
//                .when(productService).deleteProduct(999);
//
//        mockMvc.perform(delete("/api/products/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Product with ID 999 not found, cannot delete"));
//    }

    // ...more tests for other controller scenarios as needed
}