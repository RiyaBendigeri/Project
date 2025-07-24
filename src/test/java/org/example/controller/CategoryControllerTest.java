////13 tc for categorycontroller
//package org.example.controller;
//
//import org.example.model.Category;
//import org.example.repository.CategoryRepository;
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
//@AutoConfigureMockMvc
//public class CategoryControllerTest {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private CategoryRepository categoryRepository;
//
//    @BeforeEach
//    void setUp() {
//        // Clear the database before each test
//        categoryRepository.deleteAll();
//    }
//
//    // GET ALL CATEGORIES TESTS
//    //get all categories when they dont exist
//    @Test
//    //check if 404
//    void getAllCategories_WhenNoCategories_ReturnsNotFound() throws Exception {
//        mockMvc.perform(get("/api/categories"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("No categories found"));
//    }
////get all categories when they exist
//    @Test
//    void getAllCategories_WhenCategoriesExist_ReturnsCategories() throws Exception {
//        // Create test data
//        Category category1 = new Category();
//        category1.setName("Electronics");
//        categoryRepository.save(category1);
//
//        Category category2 = new Category();
//        category2.setName("Books");
//        categoryRepository.save(category2);
//
//        mockMvc.perform(get("/api/categories"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$[0].name").value("Electronics"))
//                .andExpect(jsonPath("$[1].name").value("Books"));
//    }
//
//    // GET CATEGORY BY ID TESTS
//    //get category by id when it exists
//    @Test
//    void getCategory_WhenExists_ReturnsCategory() throws Exception {
//        Category category = new Category();
//        category.setName("Electronics");
//        Category savedCategory = categoryRepository.save(category);
//
//        mockMvc.perform(get("/api/categories/" + savedCategory.getID()))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Electronics"));
//    }
//    //get category by id when it doesnt exists
//    @Test
//    void getCategory_WhenNotExists_ReturnsNotFound() throws Exception {
//        mockMvc.perform(get("/api/categories/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Category with ID 999 not found"));
//    }
//
//    // POST CATEGORY TESTS
//    //post test when its created
//    @Test
//    void postCategory_WithValidData_ReturnsCreated() throws Exception {
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Electronics\"}"))
//                .andExpect(status().isCreated())
//                .andExpect(jsonPath("$.name").value("Electronics"));
//    }
////post test with some extra fields
//    @Test
//    void postCategory_WithExtraFields_ReturnsBadRequest() throws Exception {
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Electronics\",\"price\":100}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Invalid request. Only 'name' field is allowed. Extra fields are not permitted."));
//    }
////post with empty name
//    @Test
//    void postCategory_WithEmptyName_ReturnsBadRequest() throws Exception {
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Category name cannot be empty"));
//    }
////post with name which already exists
//    @Test
//    void postCategory_WithDuplicateName_ReturnsConflict() throws Exception {
//        // First create a category
//        Category category = new Category();
//        category.setName("Electronics");
//        categoryRepository.save(category);
//
//        // Try to create duplicate
//        mockMvc.perform(post("/api/categories")
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Electronics\"}"))
//                .andExpect(status().isConflict())
//                .andExpect(content().string("Category with name 'Electronics' already exists"));
//    }
//
//    // PATCH CATEGORY TESTS
//    //patch when update will return a valid data
//    @Test
//    void patchCategory_WithValidData_ReturnsOk() throws Exception {
//        // First create a category
//        Category category = new Category();
//        category.setName("Electronics");
//        Category savedCategory = categoryRepository.save(category);
//
//        mockMvc.perform(patch("/api/categories/" + savedCategory.getID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Updated Electronics\"}"))
//                .andExpect(status().isOk())
//                .andExpect(jsonPath("$.name").value("Updated Electronics"));
//    }
////patch when update will duplicate the name
//    @Test
//    void patchCategory_WithDuplicateName_ReturnsConflict() throws Exception {
//        // Create two categories
//        Category category1 = new Category();
//        category1.setName("Electronics");
//        categoryRepository.save(category1);
//
//        Category category2 = new Category();
//        category2.setName("Books");
//        Category savedCategory2 = categoryRepository.save(category2);
//
//        // Try to update second category with first category's name
//        mockMvc.perform(patch("/api/categories/" + savedCategory2.getID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"Electronics\"}"))
//                .andExpect(status().isConflict())
//                .andExpect(content().string("Category with name 'Electronics' already exists"));
//    }
//    //patch when update will give rise to empty name
//    @Test
//    void patchCategory_WithEmptyName_ReturnsBadRequest() throws Exception {
//        Category category = new Category();
//        category.setName("Electronics");
//        Category savedCategory = categoryRepository.save(category);
//
//        mockMvc.perform(patch("/api/categories/" + savedCategory.getID())
//                        .contentType(MediaType.APPLICATION_JSON)
//                        .content("{\"name\":\"\"}"))
//                .andExpect(status().isBadRequest())
//                .andExpect(content().string("Category name cannot be empty"));
//    }
//
//    // DELETE CATEGORY TESTS
//    @Test
//    //delete when it exists
//    void deleteCategory_WhenExists_ReturnsOk() throws Exception {
//        Category category = new Category();
//        category.setName("Electronics");
//        Category savedCategory = categoryRepository.save(category);
//
//        mockMvc.perform(delete("/api/categories/" + savedCategory.getID()))
//                .andExpect(status().isOk())
//                .andExpect(content().string("Category successfully deleted"));
//    }
////delete when that id does not exist
//    @Test
//    void deleteCategory_WhenNotExists_ReturnsNotFound() throws Exception {
//        mockMvc.perform(delete("/api/categories/999"))
//                .andExpect(status().isNotFound())
//                .andExpect(content().string("Category with ID 999 not found"));
//    }
//}
package org.example.controller;

import org.example.model.Category;
import org.example.services.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest; //Spring Boot test annotation used for testing only the controllers.
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import java.util.Map;

import static org.hamcrest.Matchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
/**
 * Unit test for {@link CategoryController} using Spring's {@link WebMvcTest}.
 *
 * This test class loads only the web layer components related to CategoryController,
 * without starting the full Spring context.
 *
 * Uses {@link MockMvc} to simulate HTTP requests and verify responses.
 * The {@link CategoryService} dependency is mocked with Mockito using {@link MockBean},
 * allowing isolation of the controller layer from the service and database.
 */
@WebMvcTest(CategoryController.class)
class CategoryControllerTest {

    @Autowired// Injects a MockMvc instance for simulating HTTP requests to the controller
    private MockMvc mockMvc;

    @MockBean// Creates a Mockito mock of CategoryService and puts it into the Spring context
    private CategoryService categoryService;

    // GET ALL CATEGORIES TESTS
    /**
     * Test case for GET /api/categories when there are no categories.
     *
     * Mocks the CategoryService to return an empty list.

     * Expects HTTP 200 OK status and a JSON response with a message indicating no categories exist.
     *
     * @throws Exception if request processing fails
     */
    @Test
    void getAllCategories_whenNoCategories_returnsMessage() throws Exception {
        when(categoryService.getAllCategories()).thenReturn(List.of());
        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("No categories available to display"));
    }
    /**
     * Test GET /api/categories endpoint when categories exist.
     * Mocks the service layer to return two categories.
     * Expects HTTP 200 OK status and JSON array with category names.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void getAllCategories_whenCategoriesExist_returnsCategories() throws Exception {
        Category cat1 = new Category();
        cat1.setName("Electronics");
        Category cat2 = new Category();
        cat2.setName("Books");
        when(categoryService.getAllCategories()).thenReturn(List.of(cat1, cat2));

        mockMvc.perform(get("/api/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Electronics"))
                .andExpect(jsonPath("$[1].name").value("Books"));
    }

    // GET CATEGORY BY ID TESTS
    /**
     * Test GET /api/categories/{id} when the category exists.
     * Mocks the service to return a category with the given ID.
     * Expects HTTP 200 OK and JSON response with the category name.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void getCategory_whenExists_returnsCategory() throws Exception {
        Category cat = new Category();
        cat.setName("Electronics");
        when(categoryService.getCategoryById(1)).thenReturn(cat);

        mockMvc.perform(get("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }
    /**
     * Test GET /api/categories/{id} when the category does not exist.
     * Mocks the service to throw ResourceNotFoundException for the ID.
     * Expects HTTP 404 Not Found status.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void getCategory_whenNotExists_returnsNotFound() throws Exception {
        when(categoryService.getCategoryById(999)).thenThrow(new org.example.exception.CustomException.ResourceNotFoundException("Category with ID 999 not found"));

        mockMvc.perform(get("/api/categories/999"))
                .andExpect(status().isNotFound());
    }

    // POST CATEGORY TESTS
    /**
     * Test POST /api/categories with valid data.
     * Mocks the service to create and return the category.
     * Expects HTTP 201 Created status and JSON response with category name.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void postCategory_withValidData_returnsCreated() throws Exception {
        Category cat = new Category();
        cat.setName("Electronics");
        when(categoryService.createCategory("Electronics")).thenReturn(cat);

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Electronics"));
    }
    /**
     * Test POST /api/categories with extra unexpected fields.
     * Expects HTTP 400 Bad Request status due to validation failure.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void postCategory_withExtraFields_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\",\"price\":100}"))
                .andExpect(status().isBadRequest());
    }
    /**
     * Test POST /api/categories with missing required "name" field.
     * Expects HTTP 400 Bad Request status due to validation failure.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void postCategory_withMissingName_returnsBadRequest() throws Exception {
        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }
    /**
     * Test PATCH /api/categories/{id} with valid update data.
     * Mocks the service to update and return the updated category.
     * Expects HTTP 200 OK status and JSON response with updated name.
     *
     * @throws Exception if request execution fails
     */
    // PATCH CATEGORY TESTS
    @Test
    void patchCategory_withValidData_returnsOk() throws Exception {
        Category cat = new Category();
        cat.setName("Updated Electronics");
        when(categoryService.updateCategory(1, "Updated Electronics")).thenReturn(cat);

        mockMvc.perform(patch("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Electronics\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Electronics"));
    }
    /**
     * Test PATCH /api/categories/{id} with missing "name" field.
     * Expects HTTP 400 Bad Request status due to validation failure.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void patchCategory_withMissingName_returnsBadRequest() throws Exception {
        mockMvc.perform(patch("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isBadRequest());
    }

    // DELETE CATEGORY TESTS
    /**
     * Test DELETE /api/categories/{id} when category exists.
     * Mocks the service to successfully delete the category.
     * Expects HTTP 200 OK status and confirmation message in response.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void deleteCategory_whenExists_returnsOk() throws Exception {
        doNothing().when(categoryService).deleteCategory(1);

        mockMvc.perform(delete("/api/categories/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Category successfully deleted"));
    }
    /**
     * Test DELETE /api/categories/{id} when category does not exist.
     * Mocks the service to throw ResourceNotFoundException.
     * Expects HTTP 404 Not Found status.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void deleteCategory_whenNotExists_returnsNotFound() throws Exception {
        doThrow(new org.example.exception.CustomException.ResourceNotFoundException("Category with ID 999 not found"))
                .when(categoryService).deleteCategory(999);

        mockMvc.perform(delete("/api/categories/999"))
                .andExpect(status().isNotFound());
    }

    // Additional: Category update duplicate and empty name error
    /**
     * Test PATCH /api/categories/{id} when updating to a duplicate name.
     * Mocks the service to throw DuplicateResourceException.
     * Expects HTTP 409 Conflict status.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void patchCategory_withDuplicateName_returnsConflict() throws Exception {
        doThrow(new org.example.exception.CustomException.DuplicateResourceException("Category with name 'Books' already exists"))
                .when(categoryService).updateCategory(1, "Books");

        mockMvc.perform(patch("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Books\"}"))
                .andExpect(status().isConflict());
    }
    /**
     * Test PATCH /api/categories/{id} with empty "name" field.
     * Mocks the service to throw ValidationException.
     * Expects HTTP 400 Bad Request status.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void patchCategory_withEmptyName_returnsBadRequest() throws Exception {
        doThrow(new org.example.exception.CustomException.ValidationException("Category name cannot be empty"))
                .when(categoryService).updateCategory(1, "");

        mockMvc.perform(patch("/api/categories/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest());
    }
    /**
     * Test POST /api/categories with a duplicate category name.
     * Mocks the service to throw DuplicateResourceException.
     * Expects HTTP 409 Conflict status.
     *
     * @throws Exception if request execution fails
     */
    @Test
    void postCategory_withDuplicateName_returnsConflict() throws Exception {
        doThrow(new org.example.exception.CustomException.DuplicateResourceException("Category with name 'Electronics' already exists"))
                .when(categoryService).createCategory("Electronics");

        mockMvc.perform(post("/api/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Electronics\"}"))
                .andExpect(status().isConflict());
    }
}