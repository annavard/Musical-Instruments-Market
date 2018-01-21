package org.aua.ip.store.db;

import org.aua.ip.store.dto.ShoppingCartItemDTO;
import org.aua.ip.store.dto.UserDTO;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

/**
 * Created by Anna on 05/12/2015.
 */
public class ShoppingCartDAO {

    public void enterCartItemToDB(ShoppingCartItemDTO item) throws SQLException {

        PreparedStatement stat;
        int userID = item.getUserID();
        int productID = item.getProductID();
        stat = DBConnection.getInstance().getCon().prepareStatement("insert into shopping_cart(user_id, product_id, quantity_in_cart) values (?, ?, 1)");
        stat.setInt(1, userID);
        stat.setInt(2, productID);
        stat.executeUpdate();
    }



    public void enterCartItemToDBAnonym(ShoppingCartItemDTO item, String sessionId) throws SQLException{

        PreparedStatement stat;
        int productID = item.getProductID();
        stat = DBConnection.getInstance().getCon().prepareStatement("insert into shopping_cart(session_id, product_id, quantity_in_cart) values (?, ?, 1)");
        stat.setString(1, sessionId);
        stat.setInt(2, productID);
        stat.executeUpdate();
    }




    public ArrayList<ShoppingCartItemDTO> gainCartItemsFromDB(UserDTO user, HttpSession session)throws SQLException {

        ArrayList<ShoppingCartItemDTO> cartItems = new ArrayList<ShoppingCartItemDTO>();
        Connection con = DBConnection.getInstance().getCon();
        PreparedStatement stat, statement;
        int prodID;
        double price;
        String model, image;
        if (user == null) {
            String sessionID = session.getId();
            stat = con.prepareStatement("select product_id, quantity_in_cart from shopping_cart where session_id = ?");
            stat.setString(1, sessionID);
            ResultSet rs = stat.executeQuery();
            while (rs.next()) {
                prodID = rs.getInt("product_id");
                statement = con.prepareStatement("select price, model, image from products where product_id = ?");
                statement.setInt(1, prodID);
                ResultSet resultSet = statement.executeQuery();
                while (resultSet.next()) {
                    price = resultSet.getDouble("price");
                    model = resultSet.getString("model");
                    image = resultSet.getString("image");
                    cartItems.add(new ShoppingCartItemDTO(prodID, 1, price, model, image));
                }
            }
        }
        else{

            stat = con.prepareStatement("select product_id, quantity_in_cart from shopping_cart where user_id = ?");
            stat.setInt(1, user.getUserID());
            ResultSet rs = stat.executeQuery();
            while(rs.next()){
                prodID = rs.getInt("product_id");
                statement = con.prepareStatement("select price, model, image from products where product_id = ?");
                statement.setInt(1, prodID);
                ResultSet resultSet = statement.executeQuery();
                while(resultSet.next()){
                    price = resultSet.getDouble("price");
                    model = resultSet.getString("model");
                    image = resultSet.getString("image");
                    cartItems.add(new ShoppingCartItemDTO(prodID, 1, price, model, image));
                }
            }
        }
        return cartItems;
    }





    public void removeItemFromDB(int productID) throws SQLException {
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UserDTO user = (UserDTO)session.getAttribute("user");
        PreparedStatement stat;
        Connection con = DBConnection.getInstance().getCon();
        if (user == null) {
            String sessionID = session.getId();
            stat = con.prepareStatement("delete from shopping_cart where session_id = ? and product_id = ? limit 1");
            stat.setString(1, sessionID);
            stat.setInt(2, productID);
            stat.executeUpdate();
        }
        else{
            stat = con.prepareStatement("delete from shopping_cart where user_id = ? and product_id = ? limit 1");
            stat.setInt(1, user.getUserID());
            stat.setInt(2, productID);
            stat.executeUpdate();
        }
    }



    public void deleteItemsFromBD(int userID) throws SQLException{

        PreparedStatement stat;
        stat = DBConnection.getInstance().getCon().prepareStatement("delete from shopping_cart where user_id = ?");
        stat.setInt(1, userID);
        stat.executeUpdate();
    }

    public UserDTO assignUserToProducts(UserDTO user, String sessionID)throws SQLException{

        PreparedStatement userStat = DBConnection.getInstance().getCon().prepareStatement("select user_id,user_name from user where email = ? and pass_word = ?");
        PreparedStatement prodStat = DBConnection.getInstance().getCon().prepareStatement("update shopping_cart set user_id = ? where session_id = ?");
        userStat.setString(1, user.getEmail());
        userStat.setString(2, user.getPassword());
        ResultSet resultSet = userStat.executeQuery();
        int userID;
        String userName;
        while(resultSet.next()){
            userID = resultSet.getInt("user_id");
            userName = resultSet.getString("user_name");
            prodStat.setInt(1, userID);
            prodStat.setString(2, sessionID);
            prodStat.executeUpdate();
            user.setUserID(userID);
            user.setName(userName);
        }
        return user;
    }
}
