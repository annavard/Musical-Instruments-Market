package org.aua.ip.store.dto;

/**
 * Created by Anna on 02/12/2015.
 */
public class ProductDTO {
    private int productID;
    private String model;
    private double price;
    private String description;
    private String image;
    private CategoriesDTO prodCategory;


    public ProductDTO() {
    }

    public ProductDTO(int productID, String model, double price, String description, String image) {
        this.productID = productID;
        this.model = model;
        this.price = price;
        this.description = description;
        this.image = image;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }

    public CategoriesDTO getProdCategory() {
        return prodCategory;
    }

    public void setProdCategory(CategoriesDTO prodCategory) {
        this.prodCategory = prodCategory;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}
