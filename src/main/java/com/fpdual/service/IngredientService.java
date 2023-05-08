package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;

import java.util.ArrayList;
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
            ingredientDtos = mapToDto(ingredientDaos);
        }

        return ingredientDtos;
    }

    private IngredientDto mapToDto(IngredientDao ingredientDao) {
        IngredientDto ingredientDto = new IngredientDto();

        ingredientDto.setId(ingredientDao.getId());
        ingredientDto.setName(ingredientDao.getName());


        return ingredientDto;
    }

    private List<IngredientDto> mapToDto(List<IngredientDao> ingredientDaos) {
        List<IngredientDto> ingredientDtos = new ArrayList<>();

        for (IngredientDao ingredientDao : ingredientDaos) {
            IngredientDto ingredientDto = mapToDto(ingredientDao);
            ingredientDtos.add(ingredientDto);
        }

        return ingredientDtos;
    }

}
