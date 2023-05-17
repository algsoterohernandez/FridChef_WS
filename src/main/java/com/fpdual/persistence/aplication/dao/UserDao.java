package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


@Data
@NoArgsConstructor
public class UserDao {
    private int id;
    private String name, surname1, surname2, email, password;
    private boolean isAdmin;
    private Date createDate;

    public UserDao(ResultSet result) throws SQLException {

        try {

            id = result.getInt("id");
            name = result.getString("name");
            surname1 = result.getString("surname1");
            surname2 = result.getString("surname2");
            email = result.getString("email");
            password = result.getString("password");
            createDate = result.getDate("create_date");
            isAdmin = result.getBoolean("is_admin");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            throw e;

        }
    }
}
