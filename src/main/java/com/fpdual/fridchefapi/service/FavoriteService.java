package com.fpdual.fridchefapi.service;
import com.fpdual.fridchefapi.api.dto.RecipeDto;
import com.fpdual.fridchefapi.persistence.aplication.connector.MySQLConnector;
import com.fpdual.fridchefapi.persistence.aplication.dao.RecipeDao;
import com.fpdual.fridchefapi.persistence.aplication.manager.UserManager;
import com.fpdual.fridchefapi.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FavoriteService {
    private final MySQLConnector connector;
    private final UserManager userManager;

    public FavoriteService(MySQLConnector connector, UserManager userManager) {
        this.connector = connector;
        this.userManager = userManager;
    }

    public List<RecipeDto> findRecipeFavorite(int idUser){
        List<RecipeDto> recipeFavoriteListDto = new ArrayList<>();

        try(Connection con = connector.getMySQLConnection()){
            List<RecipeDao> recipeFavoriteListDao = userManager.findFavorite(con, idUser);

            for(RecipeDao recipeDao: recipeFavoriteListDao){
                RecipeDto recipeDto = MappingUtils.mapRecipeToDto(recipeDao);
                recipeFavoriteListDto.add(recipeDto);
            }

        }catch(SQLException | ClassNotFoundException e){
            e.printStackTrace();
        }
        return recipeFavoriteListDto;
    }
}
