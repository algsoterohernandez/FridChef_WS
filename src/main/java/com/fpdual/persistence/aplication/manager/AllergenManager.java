package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.dao.IngredientDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class AllergenManager {

    public List<AllergenDao> findAllAllergens() {
        try (Connection con = new MySQLConnector().getMySQLConnection(); Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from allergen");

            result.beforeFirst();

            List<AllergenDao> allergenDaoList = new ArrayList<>();

            while (result.next()) {
                allergenDaoList.add(new AllergenDao(result));
            }

            return allergenDaoList;

        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
            return null;
        }
    }
}
