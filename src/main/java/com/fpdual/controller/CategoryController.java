package com.fpdual.controller;


import com.fpdual.api.dto.CategoryDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.service.CategoryService;
import jakarta.ws.rs.*;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

@Path("/category")
public class CategoryController {
    private final CategoryService categoryService;

    public CategoryController(){
        Connection con = null;
        try {
            con = new MySQLConnector().getMySQLConnection();
        } catch (Exception e){
        } finally {
            categoryService = new CategoryService(con);
        }
    }

    @GET
    @Path("/")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findAllCategories(){
        List<CategoryDto> categoryList = categoryService.findAllCategories();
        return Optional.ofNullable(categoryList)
                .map(list -> Response.ok().entity(list).build())
                .orElse(Response.status(500).build());
    }

    @GET
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response findCategoryById(@PathParam("id") int id){
        CategoryDto category = categoryService.findCategoryById(id);
        return Optional.ofNullable(category)
                .map(dto -> Response.ok().entity(dto).build())
                .orElse(Response.status(404).build());
    }

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
            return Response.serverError().build();
        }

//        try {
//            List<RecipeDto> recipes = recipesService.findRecipesByIdCategory(idCategory);
//            if (recipes.isEmpty()) {
//            return Response.status(204).build(); // No Content
//            } else {
//            return Response.ok(recipes).build();
//            }
//            } catch (Exception e) {
//            System.out.println(e.getMessage());
//            return Response.serverError().build();
//        }

    }

    @POST
    @Path("/")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response createCategory(CategoryDto categoryDto){
        Response rs = null;

        try{
            if(categoryDto == null){
                rs = Response.status(400).build();
            }else {

                CategoryDto createdCategory = categoryService.createCategory(categoryDto);
                rs = Optional.ofNullable(createdCategory)
                        .map(dto -> Response.status(201).entity(dto).build())
                        .orElse(Response.status(500).build());
            }
        }catch(Exception e){
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }

    @PUT
    @Path("/{id}")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response updateCategory(@PathParam("id") int id, CategoryDto categoryDto){
        Response rs = null;

        try{
            if(categoryDto == null){
                rs = Response.status(400).build();
            }else {

                CategoryDto updatedCategory = categoryService.updateCategory(id, categoryDto);
                rs = Optional.ofNullable(updatedCategory)
                        .map(dto -> Response.ok().entity(dto).build())
                        .orElse(Response.status(404).build());
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }

    @DELETE
    @Path("/{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response deleteCategory(@PathParam("id") int id){
        Response rs;

        try{
            boolean deleted = categoryService.deleteCategory(id);

            if(deleted){
                rs = Response.ok().build();
            }else{
                rs = Response.status(404).build();
            }
        }catch (Exception e){
            System.out.println(e.getMessage());
            rs = Response.serverError().build();
        }
        return rs;
    }
}
