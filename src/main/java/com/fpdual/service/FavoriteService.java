package com.fpdual.service;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.utils.MappingUtils;

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

    public List<RecipeDto> findRecipeFavorite(int idUser) throws SQLException, ClassNotFoundException {
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
