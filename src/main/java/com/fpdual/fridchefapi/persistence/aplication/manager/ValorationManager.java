package com.fpdual.fridchefapi.persistence.aplication.manager;

import com.fpdual.fridchefapi.persistence.aplication.dao.ValorationDao;

import java.sql.*;

public class ValorationManager {
    public ValorationDao createValoration(Connection con, ValorationDao valoration){

        try (PreparedStatement stm = con.prepareStatement("INSERT INTO valoration (id_recipe, id_user, comment, valoration) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, valoration.getIdRecipe());
            stm.setInt(2, valoration.getIdUser());
            stm.setString(3, valoration.getComment());
            stm.setDouble(4, valoration.getValoration());
            stm.executeUpdate();

            ResultSet result = stm.getGeneratedKeys();
            if (result.next()) {
                int pk = result.getInt(1);
                valoration.setId(pk);
            }
            stm.close();

            return valoration;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}
