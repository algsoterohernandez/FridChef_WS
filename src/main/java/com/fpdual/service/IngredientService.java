package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.api.dto.UserDto;
import com.fpdual.exceptions.FridChefException;
import com.fpdual.exceptions.UserAlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class IngredientService {

    private final MySQLConnector connector;
    private final IngredientManager ingredientManager;

    public IngredientService(MySQLConnector connector, IngredientManager ingredientManager) {
        this.ingredientManager = ingredientManager;
        this.connector = connector;
    }

    public List<IngredientDto> findAll() {
        List<IngredientDto> ingredientDtos = null;

        try (Connection con = connector.getMySQLConnection()) {
            List<IngredientDao> ingredientDaos = ingredientManager.findAll(con);


            if (ingredientDaos != null) {
                ingredientDtos = MappingUtils.mapIngredientListToDto(ingredientDaos);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return ingredientDtos;
    }

    public boolean deleteIngredient(int id) throws SQLException, ClassNotFoundException {
        boolean deleted;

        try (Connection con = connector.getMySQLConnection()) {
            //Borramos el ingrediente
            deleted = this.ingredientManager.deleteIngredient(con, id);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return deleted;
    }

    public IngredientDto createIngredient(String name) throws SQLException, ClassNotFoundException, FridChefException {
        IngredientDto ingredientDto = null;

        List<IngredientDto> ingredients = findAll();

        //Si no hay u ingrediente con el mismo nombre, lo creamos
        if(!ingredients.stream().anyMatch(o -> o.getName().equals(name))){

            try (Connection con = connector.getMySQLConnection()) {

                IngredientDao ingredientDao = this.ingredientManager.insertIngredient(con, name);
                if (ingredientDao != null) {
                    ingredientDto = MappingUtils.mapIngredientToDto(ingredientDao);
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
                throw e;
            }
        }
        return ingredientDto;

    }


}
