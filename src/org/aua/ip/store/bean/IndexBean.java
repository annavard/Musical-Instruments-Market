package org.aua.ip.store.bean;

import org.aua.ip.store.db.ProductDAO;
import org.aua.ip.store.db.ShoppingCartDAO;
import org.aua.ip.store.dto.CategoriesDTO;
import org.aua.ip.store.dto.ProductDTO;
import org.aua.ip.store.dto.ShoppingCartItemDTO;
import org.aua.ip.store.dto.UserDTO;

import javax.annotation.PostConstruct;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.util.ArrayList;

/**
 * Created by Anna on 02/12/2015.
 */
public class IndexBean {
    private ArrayList<CategoriesDTO> categories;
    private ArrayList<ProductDTO> products;
    private ProductDAO productDAO;
    private ShoppingCartDAO shoppingCartDAO;
    UserDTO currentUser;
    private int quantity;


    public IndexBean() {
        categories = new ArrayList<CategoriesDTO>();
        products = new ArrayList<ProductDTO>();
        productDAO = new ProductDAO();
        shoppingCartDAO = new ShoppingCartDAO();
    }

    public ArrayList<CategoriesDTO> getCategories() {
        return categories;
    }

    public void setCategories(ArrayList<CategoriesDTO> categories) {
        this.categories = categories;
    }

    public ArrayList<ProductDTO> getProducts() {
        return products;
    }

    public void setProducts(ArrayList<ProductDTO> products) {
        this.products = products;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public UserDTO getCurrentUser() {
        return currentUser;
    }

    public void setCurrentUser(UserDTO currentUser) {
        this.currentUser = currentUser;
    }

    @PostConstruct
    public void displayCategories() {
        try {
            categories = productDAO.gainCategoriesFromDB();
        } catch (Exception e) {
            e.printStackTrace();
        }
        displayQuantity();
    }


    public void categoryIsSelected() {

        HttpServletRequest request = (HttpServletRequest) FacesContext.getCurrentInstance().getExternalContext().getRequest();
        int prodCategoryID = Integer.parseInt(request.getParameter("id"));
        try {
            products = productDAO.gainProductsFromDB(prodCategoryID);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void displayQuantity() {

        ArrayList<ShoppingCartItemDTO> cart = new ArrayList<ShoppingCartItemDTO>();
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UserDTO user = (UserDTO)session.getAttribute("user");
            try {
                cart = shoppingCartDAO.gainCartItemsFromDB(user, session);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
        if(user!=null){
            currentUser = user;
        }
        quantity = cart.size();
    }





    public void addItemToCart(ActionEvent event) {

        ProductDTO product = (ProductDTO) event.getComponent().getAttributes().get("product");
        FacesContext context = FacesContext.getCurrentInstance();
        HttpSession session = (HttpSession) context.getExternalContext().getSession(false);
        UserDTO user = (UserDTO) session.getAttribute("user");
        ShoppingCartItemDTO item = new ShoppingCartItemDTO();
        item.setProductID(product.getProductID());
        item.setQuantityInCart(1);
        if (user == null) {
            String sessionId = session.getId();
            try {
                shoppingCartDAO.enterCartItemToDBAnonym(item, sessionId);
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else {
            item.setUserID(user.getUserID());
            try {
                shoppingCartDAO.enterCartItemToDB(item);
            } catch (Exception e) {
                e.printStackTrace();
                currentUser = user;
            }
        }
        displayQuantity();
    }

}
