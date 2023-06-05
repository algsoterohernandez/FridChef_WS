package com.fpdual.service;

import com.fpdual.api.dto.RecipeDto;
import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.utils.MappingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class RecipeServiceTest {
    @Mock
    private MySQLConnector mockConnector;
    @Mock
    private RecipeManager mockRecipeManager;
    @Mock
    private IngredientManager mockIngredientManager;

    private RecipeService recipeService;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        MockitoAnnotations.openMocks(this);
        when(mockConnector.getMySQLConnection()).thenReturn(mock(Connection.class));
        recipeService = new RecipeService(mockConnector, mockRecipeManager, mockIngredientManager);
    }

    @Test
    void testFindAll() {
        // Mock de datos
        List<RecipeDao> recipeDaos = new ArrayList<>();
        recipeDaos.add(new RecipeDao());
        recipeDaos.add(new RecipeDao());

        List<RecipeDto> expectedRecipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);

        // Mock del comportamiento del objeto RecipeManager
        when(mockRecipeManager.findAll(any())).thenReturn(recipeDaos);

        // Llamada al método que queremos probar
        List<RecipeDto> actualRecipeDtos = recipeService.findAll();

        // Verificar el resultado
        assertEquals(expectedRecipeDtos, actualRecipeDtos);
    }

    @Test
    void testFindBy() {
        // Mock de datos
        List<RecipeDao> recipeDaos = new ArrayList<>();
        recipeDaos.add(new RecipeDao());
        recipeDaos.add(new RecipeDao());

        List<RecipeDto> expectedRecipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);

        // Mock del comportamiento del objeto RecipeManager
        when(mockRecipeManager.findBy(any(), anyList(), anyInt(), anyBoolean(), anyInt(), true)).thenReturn(recipeDaos);

        // Llamada al método que queremos probar
        List<RecipeDto> actualRecipeDtos = recipeService.findBy(new ArrayList<>(), 1, false, 10, true);

        // Verificar el resultado
        assertEquals(expectedRecipeDtos, actualRecipeDtos);
    }

    @Test
    void testFindRecipesByIngredients() {
        // Mock de datos
        List<String> recipeIngredients = new ArrayList<>();
        recipeIngredients.add("Ingredient 1");
        recipeIngredients.add("Ingredient 2");

        List<RecipeDao> recipeDaos = new ArrayList<>();
        recipeDaos.add(new RecipeDao());
        recipeDaos.add(new RecipeDao());

        List<RecipeDto> expectedRecipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);

        // Mock del comportamiento del objeto IngredientManager
        when(mockIngredientManager.getIngredientIdByName(any(), anyString())).thenReturn(1);

        // Mock del comportamiento del objeto RecipeManager
        when(mockRecipeManager.findRecipesByIngredients(any(), anyList())).thenReturn(recipeDaos);

        // Llamada al método que queremos probar
        List<RecipeDto> actualRecipeDtos = recipeService.findRecipesByIngredients(recipeIngredients);

        // Verificar el resultado
        assertEquals(expectedRecipeDtos, actualRecipeDtos);
    }

    @Test
    void testFindRecipeSuggestions() {
        // Mock de datos
        List<String> recipeIngredients = new ArrayList<>();
        recipeIngredients.add("Ingredient 1");
        recipeIngredients.add("Ingredient 2");

        List<RecipeDao> recipeDaos = new ArrayList<>();
        recipeDaos.add(new RecipeDao());
        recipeDaos.add(new RecipeDao());

        List<RecipeDto> expectedRecipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);

        // Mock del comportamiento del objeto IngredientManager
        when(mockIngredientManager.getIngredientIdByName(any(), anyString())).thenReturn(1);

        // Mock del comportamiento del objeto RecipeManager
        when(mockRecipeManager.findRecipeSuggestions(any(), anyList())).thenReturn(recipeDaos);

        // Llamada al método que queremos probar
        List<RecipeDto> actualRecipeDtos = recipeService.findRecipeSuggestions(recipeIngredients);

        // Verificar el resultado
        assertEquals(expectedRecipeDtos, actualRecipeDtos);
    }


}
