package com.fpdual.controller;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.api.dto.RecipeFilterDto;
import com.fpdual.api.dto.ValorationDto;
import com.fpdual.enums.HttpStatus;
import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.persistence.aplication.manager.ValorationManager;
import com.fpdual.service.RecipeService;
import com.fpdual.service.ValorationService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para las operaciones relacionadas con las recetas.
 */
@Path("/recipes")
public class RecipeController {

    private final RecipeService recipeService;

    private final ValorationService valorationService;

    /**
     * Constructor de la clase RecipeController.
     * Inicializa los servicios necesarios para manejar las recetas y valoraciones.
     */
    public RecipeController() {
        IngredientManager ingredientManager = new IngredientManager();

        recipeService = new RecipeService(new MySQLConnector(), new RecipeManager(ingredientManager), ingredientManager);
        valorationService = new ValorationService(new MySQLConnector(), new ValorationManager());
    }

    /**
     * Obtiene todas las recetas.
     *
     * @return Respuesta HTTP con la lista de recetas si se encuentra, o un error si ocurre un problema.
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAll() {
        List<RecipeDto> recipeList = recipeService.findAll();
        return Optional.ofNullable(recipeList)
                .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

    /**
     * Obtiene los detalles de una receta específica.
     *
     * @param id El ID de la receta.
     * @return Respuesta HTTP con los detalles de la receta si se encuentra, o un error si no se encuentra.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response recipeDetails(@PathParam("id") int id,
                                  @DefaultValue("1") @QueryParam("only_accepted") String onlyAccepted) {

        List<String> ids = new ArrayList<>();
        ids.add(String.valueOf(id));
        List<RecipeDto> recipe = recipeService.findBy(ids, 0, false, 1, onlyAccepted.equals("1"));
        if (recipe.isEmpty()) {
            return Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build();
        } else {
            return Optional.ofNullable(recipe.get(0))
                    .map(dto -> Response.status(HttpStatus.OK.getStatusCode()).entity(dto).build())
                    .orElse(Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build());
        }
    }

    /**
     * Crea una nueva receta.
     *
     * @param recipeDto Objeto RecipeDto que contiene los datos de la receta a crear.
     * @return Respuesta HTTP con la receta creada si se realiza correctamente, o un error si ocurre algún problema.
     */
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

    /**
     * Crea una nueva valoración para una receta.
     *
     * @param valorationDto Objeto ValorationDto que contiene los datos de la valoración.
     * @return Respuesta HTTP con la valoración creada si se realiza correctamente, o un error si ocurre algún problema.
     */
    @POST
    @Path("/{id}/rating")
    @Produces(MediaType.APPLICATION_JSON)
    public Response createValoration(ValorationDto valorationDto) {
        Response rs;

        try {
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

    @GET
    @Path("/{id}/rating")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findValorations(
            @PathParam("id") int id,
            @DefaultValue("10") @QueryParam("limit") int limit) {
        Response response;

        try {
            List<ValorationDto> valorationList = valorationService.findValorations(id, limit);
            if (valorationList == null) {
                valorationList = new ArrayList<>();
            }
            response = Response.status(HttpStatus.OK.getStatusCode()).entity(valorationList).build();
        } catch (Exception e) {
            response = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return response;
    }

    /**
     * Busca recetas por ingredientes.
     *
     * @param recipeFilterDto Objeto RecipeFilterDto que contiene los ingredientes de búsqueda.
     * @return Respuesta HTTP con la lista de recetas encontradas si hay resultados, o un error si ocurre algún problema.
     */
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

    /**
     * Obtiene sugerencias de recetas basadas en los ingredientes proporcionados.
     *
     * @param recipeFilterDto Objeto RecipeFilterDto que contiene los ingredientes para sugerir recetas.
     * @return Respuesta HTTP con la lista de recetas sugeridas si hay resultados, o un error si ocurre algún problema.
     */
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

    /**
     * Obtiene las recetas con estado pendiente.
     *
     * @return Respuesta HTTP con la lista de recetas pendientes si hay resultados, o un error si ocurre algún problema.
     */
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
    @Path("/most-rated")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findByMostPopular(
            @DefaultValue("10") @QueryParam("limit") Integer limit) {
        Response rs;
        try {

            List<RecipeDto> recipeList = recipeService.findBy(new ArrayList<>(), 0, true, limit, true);
            return Optional.ofNullable(recipeList)
                    .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                    .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());

        } catch (Exception e) {
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build();
        }

        return rs;
    }

    @GET
    @Path("/favorites")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findFavorites(@QueryParam("ids") String ids) {
        Response rs;

        try {
            List<String> stringId = Arrays.asList(ids.split(","));

            List<RecipeDto> recipeList = recipeService.findBy(stringId, 0, false, 0, true);

            if (recipeList == null) {
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();

            } else {
                rs = Optional.ofNullable(recipeList)
                        .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                        .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
            }
        } catch (Exception e) {
            rs = Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build();
        }

        return rs;
    }

    /**
     * Actualiza el estado de una receta.
     *
     * @param id     El ID de la receta a actualizar.
     * @param status El nuevo estado de la receta.
     * @return Respuesta HTTP con la receta actualizada si se realiza correctamente, o un error si ocurre algún problema.
     */
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