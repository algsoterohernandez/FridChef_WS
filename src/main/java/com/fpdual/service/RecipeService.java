package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.RecipeManager;

import java.util.ArrayList;
import java.util.List;

public class RecipeService {

    private final RecipeManager recipeManager;
    public RecipeService() {
        recipeManager = new RecipeManager();
    }

    public List<RecipeDto> findAll() {
        List<RecipeDao> recipeDaos = recipeManager.findAll();

        List<RecipeDto> recipeDtos = null;

        if (recipeDaos != null) {
            recipeDtos = mapToDto(recipeDaos);
        }

        return recipeDtos;
    }

    private RecipeDto mapToDto(RecipeDao recipeDao) {
        RecipeDto recipeDto = new RecipeDto();

        recipeDto.setId(recipeDao.getId());
        recipeDto.setName(recipeDao.getName());
        recipeDto.setDescription(recipeDao.getDescription());
        recipeDto.setDifficulty(recipeDao.getDifficulty());
        recipeDto.setTime(recipeDao.getTime());
        recipeDto.setUnit_time(recipeDao.getUnit_time());
        recipeDto.setId_category(recipeDao.getId_category());
        recipeDto.setCreate_time(recipeDao.getCreate_time());


        return recipeDto;
    }

    private List<RecipeDto> mapToDto(List<RecipeDao> recipeDaos) {
        List<RecipeDto> recipesDtos = new ArrayList<>();

        for (RecipeDao recipeDao : recipeDaos) {
            RecipeDto recipeDto = mapToDto(recipeDao);
            recipesDtos.add(recipeDto);
        }

        return recipesDtos;
    }
}
