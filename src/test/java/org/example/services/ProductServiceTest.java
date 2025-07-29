package org.example.services;

import org.example.dto.productPatchDTO;
import org.example.dto.productRequestDTO;
import org.example.exception.customException;
import org.example.model.Product;
import org.example.model.Category;
import org.example.repository.categoryRepository;
import org.example.repository.productRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for {@link productService} using JUnit 5 and Mockito.
 *
 * <p>Verifies product CRUD operations, input validation, and exception handling by mocking repository data.</p>
 */
class ProductServiceTest {

    @Mock
    private productRepository productsRepo;

    @Mock
    private categoryRepository categoryRepo;

    @InjectMocks
    private productService productService;

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
        prod1.setcategoryId(1);

        Product prod2 = new Product();
        prod2.setName("Phone");
        prod2.setPrice(500);
        prod2.setcategoryId(1);

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
        p.setcategoryId(1);

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

        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
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
        productRequestDTO dto = new productRequestDTO();
        dto.setName("Laptop");
        dto.setPrice(1000);
        dto.setCategoryId(1);

        // Mock categoryRepo to find the category!
        when(categoryRepo.existsById(1)).thenReturn(true);

        // Also, mock repo.save() if using mocks and not real db
        Product p = new Product();
        p.setName("Laptop");
        p.setPrice(1000);
        p.setcategoryId(1);
        when(productsRepo.save(any(Product.class))).thenReturn(p);

        Product result = productService.createProduct(dto);

        assertEquals("Laptop", result.getName());
        assertEquals(1000, result.getPrice());
        assertEquals(1, result.getcategoryId());
    }

    // CREATE product: duplicate name
    /**
     * Test createProduct with a duplicate name.
     * Mocks the repository to simulate a product with an existing name.
     * Expects DuplicateResourceException with a specific message indicating the name conflict.
     */
    @Test
    void createProduct_DuplicateName_Throws() {
//        Map<String, Object> req = new HashMap<>();
//        req.put("name", "Laptop");
//        req.put("price", 500);
//        req.put("categoryId", 1);
//
//        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(true);
//
//        Exception ex = assertThrows(customException.DuplicateResourceException.class,
//                () -> productService.createProduct(req));
        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(true);

        productRequestDTO dto = new productRequestDTO();
        dto.setName("Laptop");
        dto.setPrice(500);
        dto.setCategoryId(1);

        Exception ex = assertThrows(customException.DuplicateResourceException.class,
                () -> productService.createProduct(dto));
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
//        Map<String, Object> req = new HashMap<>();
//        req.put("name", "Laptop");
//        req.put("price", 1000);
//        req.put("categoryId", 999);
//
//        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(false);
//        when(categoryRepo.existsById(999)).thenReturn(false);
//
//        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
//                () -> productService.createProduct(req));
//        assertEquals("Category with ID 999 does not exist", ex.getMessage());
        when(productsRepo.existsByNameIgnoreCase("Laptop")).thenReturn(false);
        when(categoryRepo.existsById(999)).thenReturn(false);

        productRequestDTO dto = new productRequestDTO();
        dto.setName("Laptop");
        dto.setPrice(1000);
        dto.setCategoryId(999);

        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
                () -> productService.createProduct(dto));
        assertEquals("Category with ID 999 does not exist", ex.getMessage());
    }

    // CREATE product: price invalid
    /**
     * Test createProduct with an invalid price.
     * Mocks the repository to simulate an invalid price (less than or equal to 0).
     * Expects ValidationException with a specific message indicating the price issue.
     */


    // CREATE product: missing fields (name)
//    @Test
//    void createProduct_MissingName_Throws() {
//        Map<String, Object> req = new HashMap<>();
//        req.put("price", 1000);
//        req.put("categoryId", 1);
//
//        Exception ex = assertThrows(customException.ValidationException.class,
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
        existing.setcategoryId(1);

        when(productsRepo.existsById(4)).thenReturn(true);
        when(productsRepo.findById(4)).thenReturn(Optional.of(existing));
        when(productsRepo.existsByNameIgnoreCase("Phone")).thenReturn(false);
        when(categoryRepo.existsById(1)).thenReturn(true);

        productPatchDTO updates = new productPatchDTO();
        updates.setName("Phone");
        updates.setPrice(1500); // Only set what you want to change

        Product updated = new Product();
        updated.setID(4);
        updated.setName("Phone");
        updated.setPrice(1500);
        updated.setcategoryId(1);

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
        existing.setcategoryId(1);

        when(productsRepo.existsById(6)).thenReturn(true);
        when(productsRepo.findById(6)).thenReturn(Optional.of(existing));
        when(productsRepo.existsByNameIgnoreCase("Phone")).thenReturn(true);


        productPatchDTO updates = new productPatchDTO();
        updates.setName("Phone");


        Exception ex = assertThrows(customException.DuplicateResourceException.class,
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
        existing.setcategoryId(1);

        when(productsRepo.existsById(7)).thenReturn(true);
        when(productsRepo.findById(7)).thenReturn(Optional.of(existing));

        productPatchDTO updates = new productPatchDTO();
        updates.setPrice(-100);
// Add other required fields if needed (your service may expect name and categoryId as well)
// updates.setName("Laptop"); updates.setCategoryId(1);

        Exception ex = assertThrows(customException.ValidationException.class,
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
        existing.setcategoryId(1);

        when(productsRepo.existsById(8)).thenReturn(true);
        when(productsRepo.findById(8)).thenReturn(Optional.of(existing));

//        Map<String, Object> updates = new HashMap<>();
//        updates.put("categoryId", 999);
//
//        when(categoryRepo.existsById(999)).thenReturn(false);
//
//        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
//                () -> productService.updateProduct(8, updates));
//        assertEquals("Category with ID 999 does not exist", ex.getMessage());
        productPatchDTO updates = new productPatchDTO();
        updates.setCategoryId(999);
// You likely need to set name and price if your service expects them not-null/not-blank
// updates.setName("Laptop"); updates.setPrice(1000);

        when(categoryRepo.existsById(999)).thenReturn(false);

        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
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

        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
                () -> productService.deleteProduct(888));
        assertEquals("Product with ID 888 not found, cannot delete", ex.getMessage());
    }
}
