package com.example.mcpa_project_sem22.models;

public class Cart {
    private int id;
    private int productId;
    private int quantity;

    public Cart(int id, int productId, int quantity) {
        this.id = id;
        this.productId = productId;
        this.quantity = quantity;
    }

    public int getId() {
        return id;
    }

    public int getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}