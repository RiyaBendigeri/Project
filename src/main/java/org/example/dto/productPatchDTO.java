package org.example.dto;




public class productPatchDTO {
    private String name;     // nullable for partial update
    private Integer price;   // nullable for partial update
    private Integer categoryId; // nullable for partial update

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public Integer getPrice() { return price; }
    public void setPrice(Integer price) { this.price = price; }

    public Integer getCategoryId() { return categoryId; }
    public void setCategoryId(Integer categoryId) { this.categoryId = categoryId; }
}