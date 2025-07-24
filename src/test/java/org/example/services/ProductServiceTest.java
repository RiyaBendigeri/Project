package org.example.services;

import org.example.exception.CustomException;
import org.example.model.Product;
import org.example.model.Category;
import org.example.repository.CategoryRepository;
import org.example.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ProductServiceTest {

    @Mock
    private ProductRepository productsRepo;

    @Mock
    private CategoryRepository categoryRepo;

    @InjectMocks
    private ProductService productService;

    private Category testCategory;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
        // Setup a sample category object
        testCategory = new Category();
        testCategory.setID(1);
        testCategory.setName("Electronics");
    }

    // GET all products
    /**
     * Test getAllProducts when products exist.
     * Mocks the repository to return a list of products.
     * Verifies that the service returns the correct number of products with expected names and prices.
     */
    @Test
    void getAllProducts_WhenProductsExist_ReturnsList() {
        Product prod1 = new Product();
        prod1.setName("Laptop");
        prod1.setPrice(1000);
        prod1.setcategoryID(1);

        Product prod2 = new Product();
        prod2.setName("Phone");
        prod2.setPrice(500);
        prod2.setcategoryID(1);

        when(productsRepo.findAll()).thenReturn(List.of(prod1, prod2));

        List<Product> result = productService.getAllProducts();
        assertEquals(2, result.size());
        assertEquals("Laptop", result.get(0).getName());
        assertEquals(1000, result.get(0).getPrice());
        assertEquals("Phone", result.get(1).getName());
        assertEquals(500, result.get(1).getPrice());
    }

    // GET by id: exists
    /**
     * Test getProductById when product exists.
     * Mocks the repository to return a product by ID.
     * Verifies the returned product has the expected name, price, and category ID.
     */
    @Test
    void getProductById_WhenExists_ReturnsProduct() {
        Product p = new Product();
        p.setID(5);
        p.setName("Laptop");
        p.setPrice(1000);
        p.setcategoryID(1);

        when(productsRepo.findById(5)).thenReturn(Optional.of(p));

        Product found = productService.getProductById(5);

        assertEquals("Laptop", found.getName());
        assertEquals(1000, found.getPrice());
    }

    // GET by id: not found
    /**
     * Test getProductById when product does not exist.
     * Mocks the repository to return empty.
     * Expects ResourceNotFoundException with correct message.
     */
    @Test
    void getProductById_NotExists_ThrowsException() {
        when(productsRepo.findById(99)).thenReturn(Optional.empty());

        Exception ex = assertThrows(CustomException.ResourceNotFoundException.class,
                () -> productService.getProductById(99));
        assertEquals("Product with ID 99 not found", ex.getMessage());
    }

    // CREATE product: valid
    /**
     * Test createProduct with valid input data.
     * Mocks the repository to check for name duplication and category existence.
     * Verifies that the product is created and returned correctly.
     */
    @Test
    void createProduct_Valid_ReturnsProduct() {
        Map<String, Object> req = new HashMap<>();
        req.put("name", "Laptop");
        req.put("price", 1000);
        req.put("categoryID", 1);

        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(false);
        when(categoryRepo.existsById(1)).thenReturn(true);

        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(1000);
        p.setcategoryID(1);
        when(productsRepo.save(any(Product.class))).thenReturn(p);

        Product result = productService.createProduct(req);

        assertEquals("Laptop", result.getName());
        assertEquals(1000, result.getPrice());
        assertEquals(1, result.getcategoryID());
    }

    // CREATE product: duplicate name
    /**
     * Test createProduct with a duplicate name.
     * Mocks the repository to simulate a product with an existing name.
     * Expects DuplicateResourceException with a specific message indicating the name conflict.
     */
    @Test
    void createProduct_DuplicateName_Throws() {
        Map<String, Object> req = new HashMap<>();
        req.put("name", "Laptop");
        req.put("price", 500);
        req.put("categoryID", 1);

        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(true);

        Exception ex = assertThrows(CustomException.DuplicateResourceException.class,
                () -> productService.createProduct(req));
        assertEquals("Product with name 'Laptop' already exists", ex.getMessage());
    }

    // CREATE product: invalid category id
    /**
     * Test createProduct with an invalid category ID.
     * Mocks the repository to simulate an invalid category ID.
     * Expects ResourceNotFoundException with a specific message indicating the category does not exist.
     */
    @Test
    void createProduct_InvalidCatId_Throws() {
        Map<String, Object> req = new HashMap<>();
        req.put("name", "Laptop");
        req.put("price", 1000);
        req.put("categoryID", 999);

        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(false);
        when(categoryRepo.existsById(999)).thenReturn(false);

        Exception ex = assertThrows(CustomException.ResourceNotFoundException.class,
                () -> productService.createProduct(req));
        assertEquals("Category with ID 999 does not exist", ex.getMessage());
    }

    // CREATE product: price invalid
    /**
     * Test createProduct with an invalid price.
     * Mocks the repository to simulate an invalid price (less than or equal to 0).
     * Expects ValidationException with a specific message indicating the price issue.
     */
    @Test
    void createProduct_InvalidPrice_Throws() {
        Map<String, Object> req = new HashMap<>();
        req.put("name", "Laptop");
        req.put("price", -56);
        req.put("categoryID", 1);

        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(false);

        Exception ex = assertThrows(CustomException.ValidationException.class,
                () -> productService.createProduct(req));
        assertEquals("Price must be greater than 0", ex.getMessage());
    }

    // CREATE product: missing fields (name)
//    @Test
//    void createProduct_MissingName_Throws() {
//        Map<String, Object> req = new HashMap<>();
//        req.put("price", 1000);
//        req.put("categoryID", 1);
//
//        Exception ex = assertThrows(CustomException.ValidationException.class,
//                () -> productService.createProduct(req));
//        assertEquals("Product name is required", ex.getMessage());
//    }

    // UPDATE product: valid update (name & price)
    /**
     * Test updateProduct with valid data.
     * Mocks the repository to simulate a valid product update (both name and price).
     * Verifies that the product is updated and saved correctly.
     */
    @Test
    void updateProduct_Valid_UpdatesAndReturns() {
        Product existing = new Product();
        existing.setID(4);
        existing.setName("Laptop");
        existing.setPrice(1000);
        existing.setcategoryID(1);

        when(productsRepo.existsById(4)).thenReturn(true);
        when(productsRepo.findById(4)).thenReturn(Optional.of(existing));
        when(productsRepo.existsByNameIgnoreCase("Phone")).thenReturn(false);
        when(categoryRepo.existsById(1)).thenReturn(true);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Phone");
        updates.put("price", 1500);

        Product updated = new Product();
        updated.setID(4);
        updated.setName("Phone");
        updated.setPrice(1500);
        updated.setcategoryID(1);

        when(productsRepo.save(any(Product.class))).thenReturn(updated);

        Product result = productService.updateProduct(4, updates);
        assertEquals("Phone", result.getName());
        assertEquals(1500, result.getPrice());
    }

    // UPDATE product: name duplicate
    /**
     * Test updateProduct with a duplicate name.
     * Mocks the repository to simulate a product with an existing name.
     * Expects DuplicateResourceException with a message indicating that the product name already exists.
     */
    @Test
    void updateProduct_DuplicateName_Throws() {
        Product existing = new Product();
        existing.setID(6);
        existing.setName("Laptop");
        existing.setPrice(1000);
        existing.setcategoryID(1);

        when(productsRepo.existsById(6)).thenReturn(true);
        when(productsRepo.findById(6)).thenReturn(Optional.of(existing));
        when(productsRepo.existsByNameIgnoreCase("Phone")).thenReturn(true);

        Map<String, Object> updates = new HashMap<>();
        updates.put("name", "Phone");

        Exception ex = assertThrows(CustomException.DuplicateResourceException.class,
                () -> productService.updateProduct(6, updates));
        assertEquals("Product with name 'Phone' already exists", ex.getMessage());
    }

    // UPDATE product: invalid price
    /**
     * Test updateProduct with an invalid price.
     * Mocks the repository to simulate an invalid price (less than or equal to 0).
     * Expects ValidationException with a message indicating that the price must be greater than 0.
     */
    @Test
    void updateProduct_InvalidPrice_Throws() {
        Product existing = new Product();
        existing.setID(7);
        existing.setName("Laptop");
        existing.setPrice(1000);
        existing.setcategoryID(1);

        when(productsRepo.existsById(7)).thenReturn(true);
        when(productsRepo.findById(7)).thenReturn(Optional.of(existing));

        Map<String, Object> updates = new HashMap<>();
        updates.put("price", -100);

        Exception ex = assertThrows(CustomException.ValidationException.class,
                () -> productService.updateProduct(7, updates));
        assertEquals("Price must be greater than 0", ex.getMessage());
    }

    // UPDATE product: invalid category id
    /**
     * Test updateProduct with an invalid category ID.
     * Mocks the repository to simulate an invalid category ID.
     * Expects ResourceNotFoundException with a specific message indicating the category ID does not exist.
     */
    @Test
    void updateProduct_InvalidCatId_Throws() {
        Product existing = new Product();
        existing.setID(8);
        existing.setName("Laptop");
        existing.setPrice(1000);
        existing.setcategoryID(1);

        when(productsRepo.existsById(8)).thenReturn(true);
        when(productsRepo.findById(8)).thenReturn(Optional.of(existing));

        Map<String, Object> updates = new HashMap<>();
        updates.put("categoryID", 999);

        when(categoryRepo.existsById(999)).thenReturn(false);

        Exception ex = assertThrows(CustomException.ResourceNotFoundException.class,
                () -> productService.updateProduct(8, updates));
        assertEquals("Category with ID 999 does not exist", ex.getMessage());
    }

    // DELETE product: exists
    /**
     * Test deleteProduct when product exists.
     * Mocks the repository to confirm that the product exists and can be deleted.
     * Verifies that the deleteById method is called on the repository.
     */
    @Test
    void deleteProduct_WhenExists_DeletesSuccessfully() {
        when(productsRepo.existsById(11)).thenReturn(true);

        productService.deleteProduct(11);

        verify(productsRepo).deleteById(11);
    }

    // DELETE: not exists
    /**
     * Test deleteProduct when product does not exist.
     * Mocks the repository to indicate the product does not exist.
     * Expects ResourceNotFoundException with a message indicating the product cannot be deleted.
     */
    @Test
    void deleteProduct_WhenNotExists_Throws() {
        when(productsRepo.existsById(888)).thenReturn(false);

        Exception ex = assertThrows(CustomException.ResourceNotFoundException.class,
                () -> productService.deleteProduct(888));
        assertEquals("Product with ID 888 not found, cannot delete", ex.getMessage());
    }
}
