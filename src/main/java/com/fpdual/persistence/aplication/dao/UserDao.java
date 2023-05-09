package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;


@Data
@NoArgsConstructor

public class UserDao {
    private int id;
    private String name, surname1, surname2, email, password;
    private Date createTime;

    public UserDao(ResultSet result) {

        try {

            id = result.getInt("id");
            name = result.getString("name");
            surname1 = result.getString("surname1");
            surname2 = result.getString("surname2");
            email = result.getString("email");
            password = result.getString("password");
            createTime = result.getDate("create_time");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}