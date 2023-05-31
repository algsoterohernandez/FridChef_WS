package com.fpdual.fridchefapi.controller;

import com.fpdual.fridchefapi.enums.HttpStatus;
import com.fpdual.fridchefapi.enums.RecipeStatus;
import com.fpdual.fridchefapi.api.dto.RecipeDto;
import com.fpdual.fridchefapi.api.dto.RecipeFilterDto;
import com.fpdual.fridchefapi.api.dto.ValorationDto;
import com.fpdual.fridchefapi.persistence.aplication.connector.MySQLConnector;
import com.fpdual.fridchefapi.persistence.aplication.manager.IngredientManager;
import com.fpdual.fridchefapi.persistence.aplication.manager.RecipeManager;
import com.fpdual.fridchefapi.persistence.aplication.manager.ValorationManager;
import com.fpdual.fridchefapi.service.RecipeService;
import com.fpdual.fridchefapi.service.ValorationService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.util.List;
import java.util.Optional;

@Path("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private final ValorationService valorationService;

    public RecipeController() {
        recipeService = new RecipeService(new MySQLConnector(), new RecipeManager(), new IngredientManager());
        valorationService = new ValorationService(new MySQLConnector(), new ValorationManager());
    }


    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<RecipeDto> recipeList = recipeService.findAll();
        return Optional.ofNullable(recipeList)
                .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recipeDetails(@PathParam("id") int id) {


        RecipeDto recipe = recipeService.findRecipebyId(id);

        return Optional.ofNullable(recipe)
                .map(dto -> Response.status(HttpStatus.OK.getStatusCode()).entity(dto).build())
                .orElse(Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build());
    }


    @POST
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response create(RecipeDto recipeDto) {
        Response rs;

        try {
            if (recipeDto == null) {
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            } else {
                RecipeDto recipeRs = recipeService.createRecipe(recipeDto);
                if (recipeRs != null) {
                    rs = Response.status(HttpStatus.OK.getStatusCode()).entity(recipeRs).build();
                } else {
                    rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
                }
            }
        } catch (Exception e) {
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }



    @POST
    @Path("/{id}/rating")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createValoration(ValorationDto valorationDto) {
        Response rs;

        try{
            if (valorationDto == null) {
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            } else {
                ValorationDto valorationRs = valorationService.createValoration(valorationDto);
                if (valorationRs != null) {
                    rs = Response.status(HttpStatus.OK.getStatusCode()).entity(valorationRs).build();
                } else {
                    rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
                }
            }
        } catch (Exception e) {
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
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
            List<RecipeDto> recipeList = recipeService.findRecipesByIngredients(recipeFilterDto.getIngredients());
            return Optional.ofNullable(recipeList)
                    .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                    .orElse(Response.status(HttpStatus.NO_CONTENT.getStatusCode()).build());
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
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
                List<RecipeDto> recipeRs = recipeService.findRecipeSuggestions(recipeFilterDto.getIngredients());
                if (recipeRs == null) {
                    rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
                } else {
                    rs = Response.status(HttpStatus.OK.getStatusCode()).entity(recipeRs).build();
                }
            }
        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }

    @GET
    @Path("/find-pending")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByStatusPending() {
        Response rs;
        try {

            List<RecipeDto> recipeRs = recipeService.findByStatusPending();

            if (recipeRs == null || recipeRs.isEmpty()) {
                rs = Response.status(HttpStatus.NO_CONTENT.getStatusCode()).build();
            } else {
                rs = Response.status(HttpStatus.OK.getStatusCode()).entity(recipeRs).build();
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build();
        }

        return rs;
    }

    @GET
    @Path("/update-status/{id}/{status}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateRecipeStatus(@PathParam("id") int id, @PathParam("status") String status) {
        Response rs = null;

        try {

            if (status == null) {

                rs = Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build();

            } else {
                RecipeDto recipeRs = recipeService.updateRecipeStatus(id, status);

                if (recipeRs != null && recipeRs.getStatus().equals(RecipeStatus.PENDING)) {

                    rs = Response.status(HttpStatus.NOT_MODIFIED.getStatusCode()).build();

                } else if (recipeRs != null && (recipeRs.getStatus().equals(RecipeStatus.ACCEPTED))
                        || recipeRs.getStatus().equals(RecipeStatus.DECLINED)) {

                    rs = Response.status(HttpStatus.OK.getStatusCode()).entity(recipeRs).build();
                }
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.ordinal()).build();

        }

        return rs;
    }
}