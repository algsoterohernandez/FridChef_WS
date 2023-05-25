package com.fpdual.controller;

import com.fpdual.api.dto.*;
import com.fpdual.enums.HttpStatus;
import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.service.RecipeService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.sql.Blob;
import java.util.*;

@Path("/recipes")
public class RecipeController {

    private final RecipeService recipesService;

    public RecipeController() {
        recipesService = new RecipeService(new MySQLConnector(), new RecipeManager(), new IngredientManager());
    }


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<RecipeDto> recipeList = recipesService.findAll();
        return Optional.ofNullable(recipeList)
                .map(list -> Response.ok().entity(list).build())
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recipeDetails(@PathParam("id") int id) {


        RecipeDto recipe = recipesService.findRecipebyId(id);

        return Optional.ofNullable(recipe)
                .map(dto -> Response.ok().entity(dto).build())
                .orElse(Response.status(404).build());
    }


    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(RecipeDto recipeDto) {
        Response rs = null;

        try {
            if (recipeDto == null) {
                rs = Response.status(400).build();
            } else {
                RecipeDto recipeRs = recipesService.createRecipe(recipeDto);
                if (recipeRs != null) {
                    rs = Response.status(200).entity(recipeRs).build();
                } else {
                    rs = Response.status(500).build();
                }
            }
        } catch (Exception e) {
            rs = Response.serverError().build();
        }
        return rs;
    }

    // Probar que llega la imagen correctamente al backend, generando el fihcero.
//        File outputFile = new File("C:\\Users\\a.carmona.garrido\\Desktop\\test.png");
//
//        try (FileOutputStream outputStream = new FileOutputStream(outputFile)) {
//            outputStream.write(imagenContent);
//        } catch (FileNotFoundException e) {
//            throw new RuntimeException(e);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    @POST
    @Path("/findbyingredients")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByIngredients(RecipeFilterDto recipeFilterDto) {
        try {
            if (recipeFilterDto == null) {
                return Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            }
            List<RecipeDto> recipeList = recipesService.findRecipesByIngredients(recipeFilterDto.getIngredients());
            return Optional.ofNullable(recipeList)
                    .map(list -> Response.ok().entity(list).build())
                    .orElse(Response.status(HttpStatus.NO_CONTENT.getStatusCode()).build()); // No content
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.serverError().build();
        }
    }


    @POST
    @Path("/findSuggestions")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRecipeSuggestions(RecipeFilterDto recipeFilterDto) {
        Response rs;

        try {
            if (recipeFilterDto == null) {
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            } else {
                List<RecipeDto> recipeRs = recipesService.findRecipeSuggestions(recipeFilterDto.getIngredients());
                if (recipeRs == null) {
                    rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();//status No content
                } else {
                    rs = Response.ok().entity(recipeRs).build();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }

}