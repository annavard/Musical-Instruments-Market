package org.aua.ip.store.db;

import org.aua.ip.store.dto.CategoriesDTO;
import org.aua.ip.store.dto.ProductDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Anna on 02/12/2015.
 */
public class ProductDAO {

    public ArrayList<CategoriesDTO> gainCategoriesFromDB() throws SQLException {

        ArrayList<CategoriesDTO> categoriesDTO = new ArrayList<CategoriesDTO>();
        PreparedStatement stat;
        String categoryName;
        int categoryID;

        stat = DBConnection.getInstance().getCon().prepareStatement("select * from categories");
        ResultSet rs = stat.executeQuery();
        while(rs.next()) {
            categoryID = rs.getInt("category_id");
            categoryName = rs.getString("category_name");
            categoriesDTO.add(new CategoriesDTO(categoryID, categoryName));
        }

        return categoriesDTO;
    }

    public ArrayList<ProductDTO> gainProductsFromDB(int prodCategoryID) throws SQLException {

        ArrayList<ProductDTO> productsDTO = new ArrayList<ProductDTO>();
        PreparedStatement stat;
        int prodID;
        String model, description, image;
        Double price;
        stat = DBConnection.getInstance().getCon().prepareStatement("select product_id, model, price, description, image from products where category_id = ?");
        stat.setDouble(1, prodCategoryID);
        ResultSet rs = stat.executeQuery();
        while(rs.next()) {
            prodID = rs.getInt("product_id");
            model = rs.getString("model");
            price = rs.getDouble("price");
            description = rs.getString("description");
            image = rs.getString("image");
            productsDTO.add(new ProductDTO(prodID, model, price, description, image));
        }

        return productsDTO;
    }

    public ProductDTO gainDetailsFromDB(int prodID) throws SQLException {
        ProductDTO productDTO = new ProductDTO();
        PreparedStatement stat;
        String model, description, image;
        Double price;
        stat = DBConnection.getInstance().getCon().prepareStatement("select * from products where product_id = ?");
        stat.setInt(1, prodID);
        ResultSet rs  = stat.executeQuery();
        while(rs.next()) {
            model = rs.getString("model");
            price = rs.getDouble("price");
            description = rs.getString("description");
            image = rs.getString("image");
            productDTO.setProductID(prodID);
            productDTO.setDescription(description);
            productDTO.setImage(image);
            productDTO.setPrice(price);
            productDTO.setModel(model);
        }
        return productDTO;
    }
}
