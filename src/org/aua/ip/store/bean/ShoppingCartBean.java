package org.aua.ip.store.bean;

import org.aua.ip.store.db.ShoppingCartDAO;
import org.aua.ip.store.dto.ShoppingCartItemDTO;
import org.aua.ip.store.dto.UserDTO;

import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by Anna on 05/12/2015.
 */
public class ShoppingCartBean {
    private int totalQuantity;
    private  double total;
    private ArrayList<ShoppingCartItemDTO> cartItems;
    private ShoppingCartDAO shoppingCartDAO;


    public ShoppingCartBean() {
      cartItems = new ArrayList<ShoppingCartItemDTO>();
      shoppingCartDAO = new ShoppingCartDAO();
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public ArrayList<ShoppingCartItemDTO> getCartItems() {
        return cartItems;
    }

    public void setCartItems(ArrayList<ShoppingCartItemDTO> cartItems) {
        this.cartItems = cartItems;
    }



    public void displayCartItems(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UserDTO user = (UserDTO)session.getAttribute("user");
        try {
            cartItems = shoppingCartDAO.gainCartItemsFromDB(user, session);
            }
        catch (Exception e) {
            e.printStackTrace();
        }
        totalQuantity = cartItems.size();
        total = 0;
        for (int i = 0; i < cartItems.size(); i++) {
            total += cartItems.get(i).getPrice();
        }
    }




    public void removeItem(ActionEvent event){
        int productID = (Integer)event.getComponent().getAttributes().get("productID");
        try{
            shoppingCartDAO.removeItemFromDB(productID);
        }catch(Exception e){
            e.printStackTrace();
        }
        displayCartItems();
    }



    public String redeemCart(){
        totalQuantity = 0;
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(true);
        HttpServletRequest request = (HttpServletRequest)context.getExternalContext().getRequest();
        String requestURI = request.getRequestURI();
        session.setAttribute("requestURI", requestURI);
        UserDTO user = (UserDTO)session.getAttribute("user");
        if(user == null){
            return "login?faces-redirect=true";
        }
        else{
            int userID = user.getUserID();
            try {
                shoppingCartDAO.deleteItemsFromBD(userID);
            }catch(Exception e){
                e.printStackTrace();
            }
           return "success?faces-redirect = true";
        }
    }

}
