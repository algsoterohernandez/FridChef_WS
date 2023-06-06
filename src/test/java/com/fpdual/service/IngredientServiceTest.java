package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngredientServiceTest {
    @InjectMocks
    private IngredientService ingredientService;

    @Mock
    private MySQLConnector mySQLConnector;
    @Mock
    private IngredientManager ingredientManager;
    private IngredientDto exampleIngredientDto;
    private IngredientDao exampleIngredientDao;

    @BeforeEach
    public void init() {
        ingredientService = new IngredientService(mySQLConnector, ingredientManager);

        exampleIngredientDto = new IngredientDto();
        exampleIngredientDto.setId(3);
        exampleIngredientDto.setName("Tomate");

        exampleIngredientDao = new IngredientDao();
        exampleIngredientDao.setId(3);
        exampleIngredientDao.setName("Tomate");

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

   /*@Test
    public void testCreateIngredient_validName_ingredientDtoException() throws SQLException, ClassNotFoundException {

        //Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(Exception.class);

        //Asserts
        assertThrows(Exception.class, () -> ingredientService.createIngredient(exampleIngredientDto.getName()));
    }*/

   /*@Test
    public void testCreateIngredient_validName_ingredientDtoAlreadyExistsException() throws Exception {

        //Prepare method dependencies
        when(ingredientManager.insertIngredient(any(),anyString())).thenThrow(AlreadyExistsException.class);

        //Execute method
        IngredientDto ingredientDtoRs = ingredientService.createIngredient(exampleIngredientDto.getName());

        //Asserts
        assertThrows(AlreadyExistsException.class, () ->ingredientService.createIngredient(exampleIngredientDto.getName()));
    }*/


}
