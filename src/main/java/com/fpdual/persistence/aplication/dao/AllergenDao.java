package com.fpdual.persistence.aplication.dao;

import lombok.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class AllergenDao {
    private int id;
    private String name;


    public AllergenDao(ResultSet result) {
        try {
            this.id = result.getInt("id");
            this.name = result.getString("name");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
