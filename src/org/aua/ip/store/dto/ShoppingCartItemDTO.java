package org.aua.ip.store.dto;

/**
 * Created by Anna on 05/12/2015.
 */
public class ShoppingCartItemDTO {
    private int productID;
    private int userID;
    private int quantityInCart;
    private String model;
    private String image;
    private double price;


    public ShoppingCartItemDTO() {
    }


    public ShoppingCartItemDTO(int productID, int quantityInCart, double price, String model, String image) {
        this.productID = productID;
        this.quantityInCart = quantityInCart;
        this.price = price;
        this.model = model;
        this.image = image;
    }

    public int getUserID() {
        return userID;
    }

    public void setUserID(int userID) {
        this.userID = userID;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public int getQuantityInCart() {
        return quantityInCart;
    }

    public void setQuantityInCart(int quantityInCart) {
        this.quantityInCart = quantityInCart;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
