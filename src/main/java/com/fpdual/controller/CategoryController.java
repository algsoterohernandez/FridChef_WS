package com.fpdual.controller;


import com.fpdual.api.dto.CategoryDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.enums.HttpStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.service.CategoryService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.util.List;
import java.util.Optional;

/**
 * Controlador para la gestión de categorías.
 */
@Path("/category")
public class CategoryController {
    private final CategoryService categoryService;

    /**
     * Constructor de la clase.
     * Inicializa el CategoryService utilizando una conexión a la base de datos.
     */
    public CategoryController(){
        Connection con = null;
        try {
            con = new MySQLConnector().getMySQLConnection();
        } catch (Exception e){
        } finally {
            categoryService = new CategoryService(con);
        }
    }

    /**
     * Obtiene todas las categorías.
     *
     * @return Una respuesta HTTP con la lista de categorías en formato JSON.
     */
    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllCategories(){
        List<CategoryDto> categoryList = categoryService.findAllCategories();
        return Optional.ofNullable(categoryList)
                .map(list -> Response.status(HttpStatus.OK.getStatusCode()).entity(list).build())
                .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
    }

    /**
     * Obtiene una categoría por su ID.
     *
     * @param id El ID de la categoría.
     * @return Una respuesta HTTP con la categoría en formato JSON si existe, o un código de estado 404 si no se encuentra.
     */
    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCategoryById(@PathParam("id") int id){
        CategoryDto category = categoryService.findCategoryById(id);
        return Optional.ofNullable(category)
                .map(dto -> Response.status(HttpStatus.OK.getStatusCode()).entity(dto).build())
                .orElse(Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build());
    }

    /**
     * Obtiene las recetas de una categoría.
     *
     * @param idCategory El ID de la categoría.
     * @return Una respuesta HTTP con la lista de recetas en formato JSON si hay recetas, o un código de estado 204 si no hay recetas.
     */
    @GET
    @Path("/{idCategory}/recipes/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response findRecipesByCategory(@PathParam("idCategory") int idCategory) {
        try{

            CategoryDto category = new CategoryDto();
            category.setId(idCategory);
            List<RecipeDto> recipeList = categoryService.findRecipesByCategory(category);

            if(!recipeList.isEmpty()){
                return Response.ok(recipeList).build();
            } else{
                return Response.status(Response.Status.NO_CONTENT).build();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            return Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }

    }

    /**
     * Crea una nueva categoría.
     *
     * @param categoryDto El objeto CategoryDto que contiene los datos de la categoría.
     * @return Una respuesta HTTP con la categoría creada en formato JSON si se crea correctamente, o un código de estado 400 si los datos son inválidos.
     */
    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(CategoryDto categoryDto){
        Response rs;

        try{
            if(categoryDto == null){
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            }else {

                CategoryDto createdCategory = categoryService.createCategory(categoryDto);
                rs = Optional.ofNullable(createdCategory)
                        .map(dto -> Response.status(HttpStatus.CREATED.getStatusCode()).entity(dto).build())
                        .orElse(Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param id El ID de la categoría a actualizar.
     * @param categoryDto El objeto CategoryDto que contiene los nuevos datos de la categoría.
     * @return Una respuesta HTTP con la categoría actualizada en formato JSON si se actualiza correctamente, o un código de estado 404 si la categoría no se encuentra.
     */
    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(@PathParam("id") int id, CategoryDto categoryDto){
        Response rs;

        try{
            if(categoryDto == null){
                rs = Response.status(HttpStatus.BAD_REQUEST.getStatusCode()).build();
            }else {

                CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
                rs = Optional.ofNullable(updatedCategory)
                        .map(dto -> Response.status(HttpStatus.OK.getStatusCode()).entity(dto).build())
                        .orElse(Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }

    /**
     * Elimina una categoría existente.
     *
     * @param id El ID de la categoría a eliminar.
     * @return Una respuesta HTTP con un código de estado 200 si la categoría se elimina correctamente, o un código de estado 404 si la categoría no se encuentra.
     */
    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("id") int id){
        Response rs;

        try{
            boolean deleted = categoryService.deleteCategory(id);

            if(deleted){
                rs = Response.status(HttpStatus.OK.getStatusCode()).build();
            }else{
                rs = Response.status(HttpStatus.NOT_FOUND.getStatusCode()).build();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            rs = Response.status(HttpStatus.INTERNAL_SERVER_ERROR.getStatusCode()).build();
        }
        return rs;
    }
}