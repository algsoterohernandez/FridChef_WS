package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.dao.AllergenDao;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona los alérgenos.
 */
public class AllergenManager {

    /**
     * Busca y devuelve todos los alérgenos en la base de datos.
     *
     * @param con Conexión a la base de datos.
     * @return Lista de objetos AllergenDao que representan los alérgenos encontrados.
     */
    public List<AllergenDao> findAllAllergens(Connection con) {
        try (Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select * from allergen");

            List<AllergenDao> allergenDaoList = new ArrayList<>();

            while (result.next()) {
                allergenDaoList.add(new AllergenDao(result));
            }

            return allergenDaoList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * Busca y devuelve los alérgenos de un ingrediente específico en la base de datos.
     *
     * @param con         Conexión a la base de datos.
     * @param ingredientId ID del ingrediente.
     * @return Lista de objetos AllergenDao que representan los alérgenos encontrados para el ingrediente.
     */
    public List<AllergenDao> findIngredientAllergens(Connection con, int ingredientId) {
        try (Statement stm = con.createStatement()) {
            ResultSet result = stm.executeQuery("select a.* from allergen a inner join ingredient_allergen ia on ia.id_allergen = a.id where ia.id_ingredient = " + ingredientId);

            List<AllergenDao> allergenDaoList = new ArrayList<>();

            while (result.next()) {
                allergenDaoList.add(new AllergenDao(result));
            }

            return allergenDaoList;

        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
