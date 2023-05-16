package com.fpdual.controller;

import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fpdual.api.dto.UserDto;

public class UserController {
    private final UserService userService;

    public UserController() {
        userService = new UserService(new MySQLConnector(), new UserManager());
    }

    @GET
    public Response ping() {
        return Response.ok().entity("Service online").build();
    }


    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDto userDto) {
        Response rs = null;

        try {
            if (userDto == null) {
                rs = Response.status(400).build(); //status Bad request
            } else {
                UserDto userRs = userService.createUser(userDto);

                if (userRs != null && userRs.isAlreadyExists()) {
                    rs = Response.status(304).build();//status Not modified

                } else if (userRs != null && !userRs.isAlreadyExists()) {
                    rs = Response.ok().entity(userRs).build();
                }
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }

        return rs;
    }

    @DELETE
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("email") String email) {
        Response rs;
        try {
            boolean deleted = userService.deleteUser(email);

            rs = Response.ok().entity(deleted).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }

    @POST
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUser(UserDto userDto) {
        Response rs;
        try {
            if (userDto.getEmail() == null || userDto.getPassword() == null) {
                rs = Response.status(400).build();
            } else {
                UserDto userRs = userService.findUser(userDto.getEmail(), userDto.getPassword());

                if (userRs == null) {
                    rs = Response.status(204).build();//status No content
                } else {
                    rs = Response.ok().entity(userRs).build();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }

        return rs;
    }

}