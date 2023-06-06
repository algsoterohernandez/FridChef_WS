package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.exceptions.AlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.utils.MappingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class IngredientServiceTest {
    @InjectMocks
    private IngredientService ingredientService;
    @Mock
    private MySQLConnector mySQLConnector;
    @Mock
    private IngredientManager ingredientManager;
    @Mock
    private Connection mockConnection;
    private IngredientDto exampleIngredientDto;
    private IngredientDao exampleIngredientDao;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        MockitoAnnotations.openMocks(this);
        when(mySQLConnector.getMySQLConnection()).thenReturn(mockConnection);

        ingredientService = new IngredientService(mySQLConnector, ingredientManager);

        exampleIngredientDto = new IngredientDto();
        exampleIngredientDto.setId(3);
        exampleIngredientDto.setName("Tomate");

        exampleIngredientDao = new IngredientDao();
        exampleIngredientDao.setId(3);
        exampleIngredientDao.setName("Tomate");

    }

    @Test
    void testFindAll() throws SQLException, ClassNotFoundException {
        // Mock de datos
        List<IngredientDao> ingredientDaos = Arrays.asList(
                IngredientDao.builder().id(1).name("Tomate").build(),
                IngredientDao.builder().id(2).name("Lechuga").build()
        );

        List<IngredientDto> expectedIngredientDtos = MappingUtils.mapIngredientListToDto(ingredientDaos);

        // Mock del comportamiento del objeto IngredientManager
        when(ingredientManager.findAll(any())).thenReturn(ingredientDaos);

        // Llamada al método que queremos probar
        List<IngredientDto> actualIngredientDtos = ingredientService.findAll();

        // Verificar el resultado
        assertEquals(expectedIngredientDtos, actualIngredientDtos);
    }

    @Test
    public void testDeleteIngredient_validId_true() throws Exception {

        //Prepare method dependencies
        when(ingredientManager.deleteIngredient(any(), anyInt())).thenReturn(true);

        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        boolean deleted = ingredientService.deleteIngredient(exampleIngredientDto.getId());

        //Asserts
        assertTrue(deleted);
    }

    @Test
    public void testDeleteIngredient_validId_false() throws Exception {

        //Prepare method dependencies
        when(ingredientManager.deleteIngredient(any(),anyInt())).thenReturn(false);
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        boolean deleted = ingredientService.deleteIngredient(exampleIngredientDto.getId());

        //Asserts
        assertFalse(deleted);
    }

    @Test
    public void testDeleteIngredient_validId_userException() throws Exception {

        //Prepare method dependencies
        when(ingredientManager.deleteIngredient(any(),anyInt())).thenThrow(SQLException.class);

        //Asserts
        assertThrows(SQLException.class, () -> ingredientService.deleteIngredient(exampleIngredientDto.getId()));
    }

    @Test
    public void testCreateIngredient_validName_ingredientDtoNotNull() throws Exception {

        //Prepare method dependencies
        when(ingredientManager.insertIngredient(any(),anyString())).thenReturn(exampleIngredientDao);
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        IngredientDto ingredientDtoRs = ingredientService.createIngredient(exampleIngredientDto.getName());

        //Asserts
        assertNotNull(ingredientDtoRs);

    }

    @Test
    public void testCreateIngredient_validName_ingredientDtoNullPointerException() throws SQLException, ClassNotFoundException {

        //Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(NullPointerException.class);

        //Asserts
        assertThrows(NullPointerException.class, () -> ingredientService.createIngredient(exampleIngredientDto.getName()));
    }

}