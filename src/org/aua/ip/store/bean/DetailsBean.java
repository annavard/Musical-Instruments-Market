package org.aua.ip.store.bean;

import org.aua.ip.store.db.ProductDAO;
import org.aua.ip.store.db.ShoppingCartDAO;
import org.aua.ip.store.dto.ProductDTO;
import org.aua.ip.store.dto.ShoppingCartItemDTO;
import org.aua.ip.store.dto.UserDTO;

import javax.faces.context.FacesContext;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by Anna on 08/12/2015.
 */
public class DetailsBean {
    private ProductDTO product;
    private ProductDAO productDAO;
    private int totalQuantity;
    ArrayList<ShoppingCartItemDTO> items;
    private ShoppingCartDAO shoppingCartDAO;
    private ShoppingCartItemDTO cartItemDTO;
    private int productID;


    public DetailsBean() {
        productDAO = new ProductDAO();
        shoppingCartDAO = new ShoppingCartDAO();
        items  =  new ArrayList<ShoppingCartItemDTO>();
    }

    public ProductDTO getProduct() {
        return product;
    }

    public void setProduct(ProductDTO product) {
        this.product = product;
    }

    public int getTotalQuantity() {
        return totalQuantity;
    }

    public void setTotalQuantity(int totalQuantity) {
        this.totalQuantity = totalQuantity;
    }

    public int getProductID() {
        return productID;
    }

    public void setProductID(int productID) {
        this.productID = productID;
    }




    public void displayDetails(){
        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        String prodId = request.getParameter("productID");
        productID = Integer.parseInt(prodId);
        try{
            product = productDAO.gainDetailsFromDB(productID);
        }
        catch(Exception e){
            e.printStackTrace();
        }
        gainItemsQuantity();
    }





    public void gainItemsQuantity(){
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UserDTO user = (UserDTO)session.getAttribute("user");
       try {
           items = shoppingCartDAO.gainCartItemsFromDB(user, session);
       }
       catch (Exception e) {
            e.printStackTrace();
       }
       totalQuantity = items.size();
    }




    public void addItemToCart(){
        cartItemDTO = new ShoppingCartItemDTO();
        cartItemDTO.setProductID(productID);
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UserDTO user = (UserDTO)session.getAttribute("user");
        if (user == null) {
            String sessionId = session.getId();
            try {
                shoppingCartDAO.enterCartItemToDBAnonym(cartItemDTO, sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            cartItemDTO.setUserID(user.getUserID());
            try {
                shoppingCartDAO.enterCartItemToDB(cartItemDTO);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

}
