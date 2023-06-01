package com.fpdual.controller;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.enums.HttpStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.service.FavoriteService;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;
/**
 * Controlador para las operaciones relacionadas con los favoritos.
 */
@Path("/favorite")
public class FavoriteController {
    private final FavoriteService favoriteService;

    /**
     * Constructor de la clase.
     * Inicializa el servicio de favoritos utilizando un conector MySQL y un gestor de usuarios.
     */
    public FavoriteController(){
        favoriteService = new FavoriteService(new MySQLConnector(), new UserManager());
    }

    /**
     * Obtiene la lista de recetas favoritas de un usuario.
     *
     * @param idUser ID del usuario.
     * @return Objeto Response con la lista de recetas favoritas en formato JSON.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     * @throws ClassNotFoundException Si no se encuentra la clase del conector MySQL.
     */
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

