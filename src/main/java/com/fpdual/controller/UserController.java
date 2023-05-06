package com.fpdual.controller;

import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.service.UserService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import com.fpdual.api.dto.UserDto;

@Path("/user")
public class UserController {
    private UserService userService;

    public UserController() {
        userService = new UserService(new MySQLConnector(), new UserManager());
    }

    @GET
    @Path("/ping")
    public Response ping() {
        return Response.ok().entity("Service online").build();
    }


    @POST
    @Path("/create")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createUser(UserDto userDto) {
        Response rs;

        try {
            UserDto userRs = userService.createUser(userDto);

            if (userRs == null) {
                rs = Response.notModified().build();
            } else {
                rs = Response.ok().entity(userRs).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }

        return rs;
    }

    @DELETE
    @Path("/delete/{email}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteUser(@PathParam("email") String email) {

        try {
            boolean deleted = userService.deleteUser(email);

            return Response.ok().entity(deleted).build();

        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.serverError().build();
        }
    }

    @POST
    @Path("/find")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findUser(@QueryParam("email") String email, @QueryParam("password") String password) {
        Response rs;
        try {
            UserDto userRs = userService.findUser(email, password);

            if (userRs == null) {
                rs = Response.noContent().build();
            } else {

                rs = Response.ok().entity(userRs).build();
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }

        return rs;
    }

}