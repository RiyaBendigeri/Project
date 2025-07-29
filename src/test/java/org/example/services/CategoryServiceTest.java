//12 cases for this categoryservice
package org.example.services;
import org.example.exception.customException;
import org.example.model.Category;
import org.example.repository.categoryRepository;
import org.example.repository.productRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import java.util.*;
import org.example.dto.categoryRequestDTO;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
/**
 * Unit tests for {@link categoryService} using Mockito and JUnit 5.
 *
 * <p>Verifies CRUD operations, input validation, and exception handling for categories.</p>
 *
 * <p>Mocks the repository to control data and test all major business logic cases.</p>
 */
class CategoryServiceTest {

    @Mock
    private categoryRepository repo;

    @Mock
    private productRepository productRepo;

    @InjectMocks
    private categoryService service;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);// to enable Mockito's annotations in  tests.
    }

    // GET ALL CATEGORIES
    /**
     * Test getAllCategories when categories exist.
     * Mocks repository to return a list of categories.
     * Verifies that the service returns the correct list with expected names.
     */
    @Test
    void getAllCategories_WhenCategoriesExist_ReturnsList() {
        Category cat1 = new Category();
        cat1.setName("Electronics");
        Category cat2 = new Category();
        cat2.setName("Books");

        when(repo.findAll()).thenReturn(List.of(cat1, cat2));

        List<Category> result = service.getAllCategories();

        assertEquals(2, result.size());
        assertEquals("Electronics", result.get(0).getName());
        assertEquals("Books", result.get(1).getName());
    }

    // GET CATEGORY BY ID - exists
    /**
     * Test getCategoryById when category exists.
     * Mocks repository to return a category by ID.
     * Verifies the returned category has the expected name.
     */
    @Test
    void getCategoryById_WhenFound_ReturnsCategory() {
        Category cat = new Category();
        cat.setName("Books");
        when(repo.findById(1)).thenReturn(Optional.of(cat));

        Category found = service.getCategoryById(1);

        assertEquals("Books", found.getName());
    }
    /**
     * Test getCategoryById when category does not exist.
     * Mocks repository to return empty .
     * Expects ResourceNotFoundException with correct message.
     */
    // GET CATEGORY BY ID - not exists
    @Test
    void getCategoryById_NotExists_ThrowsResourceNotFoundException() {
        when(repo.findById(23)).thenReturn(Optional.empty());

        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
                () -> service.getCategoryById(23));
        assertEquals("Category with ID 23 not found", ex.getMessage());
    }

    // CREATE CATEGORY - empty name
    /**
     * Test createCategory with empty name.
     * Expects ValidationException due to invalid input.
     */
//    @Test
//    void createCategory_EmptyName_ThrowsValidation() {
//        Exception ex = assertThrows(customException.ValidationException.class,
//                () -> service.createCategory(""));
//
//        assertEquals("Category name cannot be empty", ex.getMessage());
//    }
    @Test
    void createCategory_EmptyName_ThrowsValidation() {
        categoryRequestDTO dto = new categoryRequestDTO();
        dto.setName("");

        Exception ex = assertThrows(customException.ValidationException.class,
                () -> service.createCategory(dto));
        assertEquals("Category name cannot be empty", ex.getMessage());
    }

    // CREATE CATEGORY - duplicate
    /**
     * Test createCategory with duplicate name.
     * Mocks repository to indicate name already exists.
     * Expects DuplicateResourceException with correct message.
     */
    @Test
    void createCategory_Duplicate_ThrowsDuplicateResource() {
        categoryRequestDTO dto = new categoryRequestDTO();
        dto.setName("Electronics");
        when(repo.existsByNameIgnoreCase("Electronics")).thenReturn(true);
        Exception ex = assertThrows(customException.DuplicateResourceException.class,
                () -> service.createCategory(dto));
        assertEquals("Category with name 'Electronics' already exists", ex.getMessage());
    }

    // CREATE CATEGORY - valid
    /**
     * Test createCategory with valid name.
     * Mocks repository to save and return the category.
     * Verifies that the category is saved and returned correctly.
     */
//    @Test
//    void createCategory_Valid_SavesAndReturns() {
//
//        when(repo.existsByNameIgnoreCase("Books")).thenReturn(false);
//
//        Category cat = new Category();
//        cat.setName("Books");
//        when(repo.save(any(Category.class))).thenReturn(cat);
//
//        Category result = service.createCategory("Books");
//        assertEquals("Books", result.getName());
//        verify(repo, times(1)).save(any(Category.class));
//    }
    @Test
    void createCategory_Valid_SavesAndReturns() {
        when(repo.existsByNameIgnoreCase("Books")).thenReturn(false);

        Category cat = new Category();
        cat.setName("Books");
        when(repo.save(any(Category.class))).thenReturn(cat);

        categoryRequestDTO dto = new categoryRequestDTO();
        dto.setName("Books");

        Category result = service.createCategory(dto);
        assertEquals("Books", result.getName());
        verify(repo, times(1)).save(any(Category.class));
    }

    // UPDATE CATEGORY - not exists
    /**
     * Test updateCategory when category does not exist.
     * Mocks repository to return empty .
     * Expects ResourceNotFoundException with correct message.
     */
//    @Test
//    void updateCategory_CategoryNotExists_ThrowsResourceNotFound() {
//        when(repo.findById(6)).thenReturn(Optional.empty());
//
//        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
//                () -> service.updateCategory(6, "NewName"));
//        assertEquals("Category with ID 6 not found", ex.getMessage());
//    }
    @Test
    void updateCategory_CategoryNotExists_ThrowsResourceNotFound() {
        when(repo.findById(6)).thenReturn(Optional.empty());

        categoryRequestDTO dto = new categoryRequestDTO();
        dto.setName("NewName");

        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
                () -> service.updateCategory(6, dto));
        assertEquals("Category with ID 6 not found", ex.getMessage());
    }
    // UPDATE CATEGORY - empty name
    /**
     * Test updateCategory with empty new name.
     * Expects ValidationException due to invalid name.
     */
//    @Test
//    void updateCategory_EmptyNewName_ThrowsValidationException() {
//        Category cat = new Category();
//        cat.setName("Electronics");
//        when(repo.findById(5)).thenReturn(Optional.of(cat));
//
//        Exception ex = assertThrows(customException.ValidationException.class,
//                () -> service.updateCategory(5, ""));
//        assertEquals("Category name cannot be empty", ex.getMessage());
//    }
    @Test
    void updateCategory_EmptyNewName_ThrowsValidationException() {
        Category cat = new Category();
        cat.setName("Electronics");
        when(repo.findById(5)).thenReturn(Optional.of(cat));

        categoryRequestDTO dto = new categoryRequestDTO();
        dto.setName(""); // or even "   " for blank test

        Exception ex = assertThrows(customException.ValidationException.class,
                () -> service.updateCategory(5, dto));
        assertEquals("Category name cannot be empty", ex.getMessage());
    }

    // UPDATE CATEGORY - duplicate
    /**
     * Test updateCategory with duplicate new name.
     * Mocks repository to indicate name already exists.
     * Expects DuplicateResourceException with correct message.
     */

//    @Test
//    void updateCategory_DuplicateName_ThrowsDuplicateResourceException() {
//        Category cat = new Category();
//        cat.setName("Electronics");
//        when(repo.findById(4)).thenReturn(Optional.of(cat));
//        when(repo.existsByNameIgnoreCase("Books")).thenReturn(true);
//
//        Exception ex = assertThrows(customException.DuplicateResourceException.class,
//                () -> service.updateCategory(4, "Books"));
//        assertEquals("Category with name 'Books' already exists", ex.getMessage());
//    }
    @Test
    void updateCategory_DuplicateName_ThrowsDuplicateResourceException() {
        Category cat = new Category();
        cat.setName("Electronics");
        when(repo.findById(4)).thenReturn(Optional.of(cat));
        when(repo.existsByNameIgnoreCase("Books")).thenReturn(true);

        categoryRequestDTO dto = new categoryRequestDTO();
        dto.setName("Books");

        Exception ex = assertThrows(customException.DuplicateResourceException.class,
                () -> service.updateCategory(4, dto));
        assertEquals("Category with name 'Books' already exists", ex.getMessage());
    }

    // UPDATE CATEGORY - valid
    /**
     * Test updateCategory with valid data.
     * Mocks repository to find, update and save category.
     * Verifies the update and save operations occur and result is correct.
     */
//    @Test
//    void updateCategory_Valid_UpdateAndSave() {
//        Category cat = new Category();
//        cat.setName("Electronics");
//        when(repo.findById(3)).thenReturn(Optional.of(cat));
//        when(repo.existsByNameIgnoreCase("Books")).thenReturn(false);
//
//        Category updatedCat = new Category();
//        updatedCat.setName("Books");
//        when(repo.save(any(Category.class))).thenReturn(updatedCat);
//
//        Category result = service.updateCategory(3, "Books");
//        assertEquals("Books", result.getName());
//        verify(repo, times(1)).save(cat);
//    }
    @Test
    void updateCategory_Valid_UpdateAndSave() {
        Category cat = new Category();
        cat.setName("Electronics");
        when(repo.findById(3)).thenReturn(Optional.of(cat));
        when(repo.existsByNameIgnoreCase("Books")).thenReturn(false);

        Category updatedCat = new Category();
        updatedCat.setName("Books");
        when(repo.save(any(Category.class))).thenReturn(updatedCat);

        categoryRequestDTO dto = new categoryRequestDTO();
        dto.setName("Books");

        Category result = service.updateCategory(3, dto);
        assertEquals("Books", result.getName());
        verify(repo, times(1)).save(cat);
    }

    // DELETE CATEGORY - not exists
    /**
     * Test deleteCategory when category does not exist.
     * Mocks repository to indicate ID does not exist.
     * Expects ResourceNotFoundException with correct message.
     */
    @Test
    void deleteCategory_NotExists_ThrowsResourceNotFound() {
        when(repo.existsById(99)).thenReturn(false);

        Exception ex = assertThrows(customException.ResourceNotFoundException.class,
                () -> service.deleteCategory(99));
        assertEquals("Category with ID 99 not found", ex.getMessage());
    }

    // DELETE CATEGORY - exists
    /**
     * Test deleteCategory when category exists.
     * Mocks repository to confirm existence.
     * Verifies the deleteById method is called once.
     */
    @Test
    void deleteCategory_Exists_Deletes() {
        when(repo.existsById(70)).thenReturn(true);
        when(productRepo.existsBycategoryId(70)).thenReturn(false);
        service.deleteCategory(70);

        verify(repo, times(1)).deleteById(70);
    }
}
