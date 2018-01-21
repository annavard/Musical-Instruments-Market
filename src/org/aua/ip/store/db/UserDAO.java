package org.aua.ip.store.db;

import org.aua.ip.store.dto.UserDTO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Created by Anna on 28/11/2015.
 */
public class UserDAO {


    public boolean checkEmail(String email) throws SQLException {
        PreparedStatement stat = DBConnection.getInstance().getCon().prepareStatement("select email from user where email = ?");
        stat.setString(1, email);
        ResultSet rs = stat.executeQuery();
            return (rs.first());
    }

    public boolean checkPassword(String password, String email) throws SQLException{
       PreparedStatement stat;
       if(checkEmail(email)){
           stat = DBConnection.getInstance().getCon().prepareStatement("select pass_word from user where pass_word = ? and email = ?");
           stat.setString(1, password);
           stat.setString(2, email);
       }
       else{
           stat = DBConnection.getInstance().getCon().prepareStatement("select pass_word from user where pass_word = ?");
           stat.setString(1, password);
       }
        ResultSet rs = stat.executeQuery();
           return(rs.first());
    }

    public void registerData(UserDTO user)throws SQLException{
        PreparedStatement stat = DBConnection.getInstance().getCon().prepareStatement("insert into user(user_name, user_lastname, email, pass_word) values( ?, ?, ?, ?) ");
        stat.setString(1, user.getName());
        stat.setString(2, user.getLastName());
        stat.setString(3, user.getEmail());
        stat.setString(4, user.getPassword());
        stat.execute();
    }
}
