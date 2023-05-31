package com.fpdual.fridchefapi.controller;

import com.fpdual.fridchefapi.api.dto.RecipeDto;
import com.fpdual.fridchefapi.enums.HttpStatus;
import com.fpdual.fridchefapi.persistence.aplication.connector.MySQLConnector;
import com.fpdual.fridchefapi.persistence.aplication.manager.UserManager;
import com.fpdual.fridchefapi.service.FavoriteService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
@Path("/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    public FavoriteController(){
            favoriteService = new FavoriteService(new MySQLConnector(), new UserManager());
    }
    @GET
    @Path("/{idUser}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recipe(@PathParam("idUser") int idUser) throws SQLException, ClassNotFoundException {
        List<RecipeDto> recipesFavorite = favoriteService.findRecipeFavorite(idUser);
        return Optional.ofNullable(recipesFavorite)
                .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

}
