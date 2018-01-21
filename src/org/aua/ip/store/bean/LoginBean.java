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
public class LoginBean {
    private String email;
    private String password;
    private UserDAO userDao;
    private ShoppingCartDAO shoppingCartDAO;
    private String message;
    UserDTO user;


    public LoginBean() {
        userDao = new UserDAO();
        shoppingCartDAO = new ShoppingCartDAO();
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public UserDTO getUser() {
        return user;
    }

    public void setUser(UserDTO user) {
        this.user = user;
    }


    public void tryLogin(){
        try {
            boolean emailResult = userDao.checkEmail(email);
            boolean passwordResult = userDao.checkPassword(password, email);
            if(emailResult && passwordResult){
                user = new UserDTO();
                user.setEmail(email);
                user.setPassword(password);
                FacesContext facesContext = FacesContext.getCurrentInstance();
                ExternalContext ec = facesContext.getExternalContext();
                HttpSession session = (HttpSession)ec.getSession(true);
                String sessionID = session.getId();
                try{
                    user  = shoppingCartDAO.assignUserToProducts(user, sessionID);
                }catch(Exception e){
                    e.printStackTrace();
                }
                ec.getSessionMap().put("user", user);
                String url = (String)session.getAttribute("requestURI");
                if(url == null) {
                    url = "/index.xhtml";
                }
                    try {
                        ec.redirect(url);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
            }
            else if((!emailResult) && (!passwordResult)){
                message = "Incorrect email and password";
            }
            else if(!emailResult){
                message = "Incorrect email";
            }
            else if(!passwordResult){
                message = "Incorrect password";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            message = "Error with DB connection!";
        }
    }




    public void makeLogOut(){
        user = null;
        FacesContext.getCurrentInstance().getExternalContext().invalidateSession();
    }

}
