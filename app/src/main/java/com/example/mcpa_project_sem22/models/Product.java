package com.example.mcpa_project_sem22.models;

public class Product {
    private int productId;
    private String name;
    private String description;
    private double price;
    private double discountedPrice;
    private int categoryId;
    private String categoryName;
    private String brand;
    private String unit;
    private int stockQuantity;
    private int minOrderQuantity;
    private int maxOrderQuantity;
    private String imageUrl;
    private double rating;
    private int reviewCount;
    private boolean isActive;
    private String createdAt;

    // Default constructor
    public Product() {
        this.isActive = true;
        this.minOrderQuantity = 1;
        this.maxOrderQuantity = 10;
        this.stockQuantity = 0;
        this.rating = 0.0;
        this.reviewCount = 0;
    }

    // Constructor with essential fields
    public Product(String name, String description, double price, int categoryId) {
        this();
        this.name = name;
        this.description = description;
        this.price = price;
        this.categoryId = categoryId;
    }

    // Legacy constructor for backward compatibility
    public Product(int id, String name, String description, double price, int categoryId) {
        this(name, description, price, categoryId);
        this.productId = id;
    }

    // Getters and Setters
    public int getProductId() {
        return productId;
    }

    public void setProductId(int productId) {
        this.productId = productId;
    }

    // Legacy getter for backward compatibility
    public int getId() {
        return productId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public double getDiscountedPrice() {
        return discountedPrice;
    }

    public void setDiscountedPrice(double discountedPrice) {
        this.discountedPrice = discountedPrice;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public int getStockQuantity() {
        return stockQuantity;
    }

    public void setStockQuantity(int stockQuantity) {
        this.stockQuantity = stockQuantity;
    }

    public int getMinOrderQuantity() {
        return minOrderQuantity;
    }

    public void setMinOrderQuantity(int minOrderQuantity) {
        this.minOrderQuantity = minOrderQuantity;
    }

    public int getMaxOrderQuantity() {
        return maxOrderQuantity;
    }

    public void setMaxOrderQuantity(int maxOrderQuantity) {
        this.maxOrderQuantity = maxOrderQuantity;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }

    public int getReviewCount() {
        return reviewCount;
    }

    public void setReviewCount(int reviewCount) {
        this.reviewCount = reviewCount;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    // Utility methods
    public boolean hasDiscount() {
        return discountedPrice > 0 && discountedPrice < price;
    }

    public double getEffectivePrice() {
        return hasDiscount() ? discountedPrice : price;
    }

    public double getDiscountPercentage() {
        if (hasDiscount()) {
            return ((price - discountedPrice) / price) * 100;
        }
        return 0;
    }

    public boolean isInStock() {
        return stockQuantity > 0;
    }

    public String getFormattedPrice() {
        return String.format("R%.2f", getEffectivePrice());
    }

    public String getFormattedOriginalPrice() {
        return String.format("R%.2f", price);
    }

    @Override
    public String toString() {
        return "Product{" +
                "productId=" + productId +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", discountedPrice=" + discountedPrice +
                ", categoryId=" + categoryId +
                ", brand='" + brand + '\'' +
                ", stockQuantity=" + stockQuantity +
                ", isActive=" + isActive +
                '}';
    }
}