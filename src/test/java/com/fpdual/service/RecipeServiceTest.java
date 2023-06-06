package com.fpdual.service;

import com.fpdual.api.dto.IngredientRecipeDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.utils.MappingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RecipeServiceTest {
    @InjectMocks
    private RecipeService recipeService;
    @Mock
    private MySQLConnector mySQLConnector;
    @Mock
    private RecipeManager recipeManager;
    @Mock
    private IngredientManager ingredientManager;
    private RecipeDto exampleRecipeDto;
    private RecipeDao exampleRecipeDao;
    private RecipeDto exampleRecipeDtoAccepted;
    private RecipeDao exampleRecipeDaoAccepted;
    private RecipeDto exampleRecipeDtoDeclined;
    private RecipeDao exampleRecipeDaoDeclined;

    private List<IngredientRecipeDto> exampleIngredientListDto;
    private List<IngredientRecipeDao> exampleIngredientListDao;
    private IngredientRecipeDto exampleIngredientRecipeDto;
    private IngredientRecipeDao exampleIngredientRecipeDao;




    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        MockitoAnnotations.openMocks(this);
        when(mySQLConnector.getMySQLConnection()).thenReturn(mock(Connection.class));

        recipeService = new RecipeService(mySQLConnector, recipeManager, ingredientManager);

        exampleRecipeDto = new RecipeDto();
        exampleRecipeDto.setId(3);
        exampleRecipeDto.setName("Ensalada");
        exampleRecipeDto.setStatus(RecipeStatus.valueOf(RecipeStatus.PENDING.getStatus()));

        exampleRecipeDao = new RecipeDao();
        exampleRecipeDao.setId(3);
        exampleRecipeDao.setName("Ensalada");
        exampleRecipeDao.setStatus(RecipeStatus.valueOf(RecipeStatus.PENDING.getStatus()));

        exampleRecipeDaoAccepted = new RecipeDao();
        exampleRecipeDaoAccepted.setId(3);
        exampleRecipeDaoAccepted.setName("Ensalada");
        exampleRecipeDaoAccepted.setStatus(RecipeStatus.valueOf(RecipeStatus.ACCEPTED.getStatus()));

        exampleRecipeDtoAccepted = new RecipeDto();
        exampleRecipeDtoAccepted.setId(3);
        exampleRecipeDtoAccepted.setName("Ensalada");
        exampleRecipeDtoAccepted.setStatus(RecipeStatus.valueOf(RecipeStatus.ACCEPTED.getStatus()));

        exampleRecipeDaoDeclined = new RecipeDao();
        exampleRecipeDaoDeclined.setId(3);
        exampleRecipeDaoDeclined.setName("Ensalada");
        exampleRecipeDaoDeclined.setStatus(RecipeStatus.valueOf(RecipeStatus.DECLINED.getStatus()));

        exampleRecipeDtoDeclined = new RecipeDto();
        exampleRecipeDtoDeclined.setId(3);
        exampleRecipeDtoDeclined.setName("Ensalada");
        exampleRecipeDtoDeclined.setStatus(RecipeStatus.valueOf(RecipeStatus.DECLINED.getStatus()));

        exampleIngredientRecipeDto = new IngredientRecipeDto();
        exampleIngredientRecipeDto.setId(5);
        exampleIngredientRecipeDto.setIdRecipe(3);
        exampleIngredientRecipeDto.setIdIngredient(13);

        exampleIngredientRecipeDao = new IngredientRecipeDao();
        exampleIngredientRecipeDao.setId(5);
        exampleIngredientRecipeDao.setIdRecipe(3);
        exampleIngredientRecipeDao.setIdIngredient(13);

        exampleIngredientListDto = new ArrayList<>();
        exampleIngredientListDto.add(exampleIngredientRecipeDto);

        exampleIngredientListDao = new ArrayList<>();
        exampleIngredientListDao.add(exampleIngredientRecipeDao);
    }

    @Test
    void testFindAll() {
        // Mock de datos
        List<RecipeDao> recipeDaos = new ArrayList<>();
        recipeDaos.add(new RecipeDao());
        recipeDaos.add(new RecipeDao());

        List<RecipeDto> expectedRecipeDtos = MappingUtils.mapRecipeListToDto(recipeDaos);

        // Mock del comportamiento del objeto RecipeManager
        when(recipeManager.findAll(any())).thenReturn(recipeDaos);

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
        when(recipeManager.findBy(any(), anyList(), anyInt(), anyBoolean(), anyInt(),anyBoolean())).thenReturn(recipeDaos);

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
        when(ingredientManager.getIngredientIdByName(any(), anyString())).thenReturn(1);

        // Mock del comportamiento del objeto RecipeManager
        when(recipeManager.findRecipesByIngredients(any(), anyList())).thenReturn(recipeDaos);

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
        when(ingredientManager.getIngredientIdByName(any(), anyString())).thenReturn(1);

        // Mock del comportamiento del objeto RecipeManager
        when(recipeManager.findRecipeSuggestions(any(), anyList())).thenReturn(recipeDaos);

        // Llamada al método que queremos probar
        List<RecipeDto> actualRecipeDtos = recipeService.findRecipeSuggestions(recipeIngredients);

        // Verificar el resultado
        assertEquals(expectedRecipeDtos, actualRecipeDtos);
    }

    @Test
    public void testFindByStatusPending_recipeDtoNotNull() throws SQLException, ClassNotFoundException {

        //Prepare method dependencies
        when(recipeManager.findByStatusPending(any())).thenReturn(new ArrayList<>(Arrays.asList(exampleRecipeDao)));
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        List<RecipeDto> recipeDtoListRs = recipeService.findByStatusPending();

        //Asserts
        assertNotNull(recipeDtoListRs);

    }

    @Test
    public void testFindByStatusPending_recipeException() throws SQLException, ClassNotFoundException {

        //Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(ClassNotFoundException.class);

        //Asserts
        assertThrows(ClassNotFoundException.class, () -> recipeService.findByStatusPending());
    }

    @Test
    public void testUpdateRecipeStatus_validIdStatus_recipeDtoNotNullAccepted() throws SQLException, ClassNotFoundException {

        //Prepare method dependencies
        when(recipeManager.updateRecipeStatus(any(), anyInt(),anyString())).thenReturn(exampleRecipeDaoAccepted);
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        RecipeDto recipeDtoRs = recipeService.updateRecipeStatus(exampleRecipeDtoAccepted.getId(), String.valueOf(exampleRecipeDtoAccepted.getStatus()));

        //Asserts
        assertNotNull(recipeDtoRs);

    }

    @Test
    public void testUpdateRecipeStatus_validIdStatus_recipeDtoNotNullDeclined() throws SQLException, ClassNotFoundException {

        //Prepare method dependencies
        when(recipeManager.updateRecipeStatus(any(), anyInt(),anyString())).thenReturn(exampleRecipeDaoDeclined);
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        RecipeDto recipeDtoRs = recipeService.updateRecipeStatus(exampleRecipeDtoDeclined.getId(), String.valueOf(exampleRecipeDtoDeclined.getStatus()));

        //Asserts
        assertNotNull(recipeDtoRs);

    }

    @Test
    public void testUpdateRecipeStatus_validIdStatus_recipeException() throws SQLException, ClassNotFoundException {

        //Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(ClassNotFoundException.class);

        //Asserts
        assertThrows(ClassNotFoundException.class,
                () -> recipeService.updateRecipeStatus(exampleRecipeDto.getId(), String.valueOf(exampleRecipeDto.getStatus())));
    }

}