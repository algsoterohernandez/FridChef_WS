package com.fpdual.controller;

import com.fpdual.api.dto.UserDto;
import com.fpdual.enums.HttpStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.FavoriteManager;
import com.fpdual.persistence.aplication.manager.RolManager;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.service.FavoriteService;
import com.fpdual.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

/**
 * Controlador para las operaciones relacionadas con los usuarios.
 * <p>
 * Esta clase gestiona las operaciones CRUD para los usuarios.
 */
@Path("/user")
public class UserController {
    private final UserService userService;
    private final FavoriteService favoriteService;

    /**
     * Constructor de la clase UserController.
     * <p>
     * Inicializa el servicio UserService con las dependencias necesarias.
     */
    public UserController() {
        userService = new UserService(new MySQLConnector(), new UserManager(), new RolManager(), new FavoriteManager());
        favoriteService = new FavoriteService(new MySQLConnector(), new FavoriteManager());
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param userDto Objeto UserDto que contiene los datos del usuario a crear.
     * @return Response con el resultado de la operación.
     */
    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDto userDto) {
        Response rs = null;

        try {
            if (userDto == null) {
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            } else {
                UserDto userRs = userService.createUser(userDto);

                if (userRs != null && userRs.isAlreadyExists()) {
                    rs = Response.status(HttpStatus.NOT_MODIFIED.getStatusCode()).build();

                } else if (userRs != null && !userRs.isAlreadyExists()) {
                    rs = Response.status(HttpStatus.OK.getStatusCode()).entity(userRs).build();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }

        return rs;
    }


    /**
     * Elimina un usuario.
     *
     * @param email Email del usuario a eliminar.
     * @return Response con el resultado de la operación.
     */
    @DELETE
    @Path("/delete/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("email") String email) {
        Response rs;
        try {
            boolean deleted = userService.deleteUser(email);

            rs = Response.status(HttpStatus.OK.getStatusCode()).entity(deleted).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }

    /**
     * Busca un usuario.
     *
     * @param userDto Objeto UserDto que contiene los datos del usuario a buscar.
     * @return Response con el resultado de la operación.
     */
    @POST
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUser(UserDto userDto) {
        Response rs;
        try {
            if (userDto.getEmail() == null || userDto.getPassword() == null) {
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            } else {
                UserDto userRs = userService.findUser(userDto.getEmail(), userDto.getPassword());

                if (userRs == null) {
                    rs = Response.status(HttpStatus.NO_CONTENT.getStatusCode()).build();
                } else {
                    rs = Response.status(HttpStatus.OK.getStatusCode()).entity(userRs).build();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }

        return rs;
    }

    @POST
    @Path("/{idUser}/favorite/{idRecipe}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createFavorite(@PathParam("idUser") int idUser, @PathParam("idRecipe") int idRecipe) {
        Response rs = null;
        try {
            boolean favoriteCreated = favoriteService.favoriteAdd(idRecipe, idUser);
            rs = Response.status(HttpStatus.OK.getStatusCode()).entity(favoriteCreated).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }

    @DELETE
    @Path("/{idUser}/favorite/{idRecipe}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response removeFavorite(@PathParam("idUser") int idUser, @PathParam("idRecipe") int idRecipe) {
        Response rs = null;
        try {
            boolean favoriteDeleted = favoriteService.favoriteRemove(idRecipe, idUser);
            rs = Response.status(HttpStatus.OK.getStatusCode()).entity(favoriteDeleted).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }
}