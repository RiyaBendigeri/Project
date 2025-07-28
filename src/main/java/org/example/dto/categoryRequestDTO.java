package org.example.dto;


import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;


@JsonIgnoreProperties(ignoreUnknown = false)
public class categoryRequestDTO  {
    @NotBlank(message = "Category name cannot be empty")
    @Size(min = 1, max = 255, message = "Category name must be between 1 and 255 characters")
    private String name;

    // getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
}