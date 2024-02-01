package com.example.chat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserModel {
    public boolean usersave(UserDto dto) throws SQLException {
        Connection connection = DbConnection.getInstance().getConnection();
        String sql = "INSERT INTO credentials(username,email,password) VALUES(?, ?, ?)";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setObject(1, dto.getUsername());
        pstm.setString(2, dto.getEmail());
        pstm.setString(3, dto.getPassword());
        boolean isSaved= pstm.executeUpdate()>0;
        System.out.println("doo");
        return isSaved;


    }

    public boolean userLogin(UserDto dto) throws SQLException {
        Connection connection=DbConnection.getInstance().getConnection();
        String sql = "SELECT  * FROM credentials WHERE username=? AND password=?";
        PreparedStatement pstm = connection.prepareStatement(sql);
        pstm.setString(1, dto.getUsername());
        pstm.setString(2, dto.getPassword()); // Use getPassword() instead of getUsername() for the password

        ResultSet resultSet = pstm.executeQuery();
        boolean isLogin = resultSet.next();
        return isLogin;
    }
}
