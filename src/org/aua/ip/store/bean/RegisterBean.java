package org.aua.ip.store.bean;
import org.aua.ip.store.db.ShoppingCartDAO;
import org.aua.ip.store.db.UserDAO;
import org.aua.ip.store.dto.UserDTO;

import javax.faces.context.ExternalContext;
import javax.faces.context.FacesContext;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.sql.SQLException;

/**
 * Created by Anna on 28/11/2015.
 */
public class RegisterBean {
    private String name;
    private String lastName;
    private String email;
    private String password;
    private UserDAO userDao;
    private ShoppingCartDAO shoppingCartDAO;
    private String regMessage;
    private UserDTO user;

    public RegisterBean(){
        shoppingCartDAO = new ShoppingCartDAO();
        userDao = new UserDAO();
    }

    public String getRegMessage() {
        return regMessage;
    }

    public void setRegMessage(String regMessage) {
        this.regMessage = regMessage;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword() {
        return password;
    }

    public void makeRegister() {
        UserDTO user = new UserDTO();
        user.setName(name);
        user.setLastName(lastName);
        user.setEmail(email);
        user.setPassword(password);
        try {
            userDao.registerData(user);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        FacesContext facesContext = FacesContext.getCurrentInstance();
        ExternalContext ec = facesContext.getExternalContext();
        HttpSession session = (HttpSession)ec.getSession(true);
        String url = (String)session.getAttribute("requestURI");
        if(url == null) {
            url = "/index.xhtml";
        }
        try {
            ec.redirect(url);
        } catch (IOException e) {
            e.printStackTrace();
        }
        String sessionID = session.getId();
        try{
            user  = shoppingCartDAO.assignUserToProducts(user, sessionID);
        }catch(Exception e){
            e.printStackTrace();
        }
        ec.getSessionMap().put("user", user);
    }
}
