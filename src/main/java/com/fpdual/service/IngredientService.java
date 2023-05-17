package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.utils.MappingUtils;

import java.util.List;

public class IngredientService {

    private final IngredientManager ingredientManager;

    public IngredientService() {
        ingredientManager = new IngredientManager();
    }

    public List<IngredientDto> findAll() {
        List<IngredientDao> ingredientDaos = ingredientManager.findAll();

        List<IngredientDto> ingredientDtos = null;

        if (ingredientDaos != null) {
            ingredientDtos = MappingUtils.mapIngredientDto(ingredientDaos);
        }

        return ingredientDtos;
    }
}
