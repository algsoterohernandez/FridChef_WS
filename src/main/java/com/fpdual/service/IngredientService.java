package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.UserManager;
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
                ingredientDtos = MappingUtils.mapIngredientDto(ingredientDaos);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        return ingredientDtos;
    }
}
