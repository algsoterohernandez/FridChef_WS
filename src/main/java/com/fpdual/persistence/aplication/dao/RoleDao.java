package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

@Data
@Getter
@Setter
@NoArgsConstructor

public class RoleDao {
    private int id;
    private String description;

    public RoleDao(ResultSet result) {

        try {
            this.id = result.getInt("id");
            this.description = result.getString("description");
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
