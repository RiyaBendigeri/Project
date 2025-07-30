
package org.example.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.Positive;
@JsonIgnoreProperties(ignoreUnknown = false)
public class productRequestDTO {
    @NotBlank(message = "Product name cannot be empty")
    @Size(min = 1, max = 255, message = "Product name must be between 1 and 255 characters")
    private String name;

    @Positive(message = "Price must be greater than 0")
    private int price;

    @Positive(message = "Category ID must be greater than 0")
    private int categoryId;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }

    public int getCategoryId() { return categoryId; }
    public void setCategoryId(int categoryId) { this.categoryId = categoryId; }
}