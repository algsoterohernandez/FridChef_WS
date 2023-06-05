package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.utils.MappingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
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
    @Mock
    private MySQLConnector mockConnector;
    @Mock
    private IngredientManager mockIngredientManager;
    @Mock
    private Connection mockConnection;

    private IngredientService ingredientService;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        MockitoAnnotations.openMocks(this);
        when(mockConnector.getMySQLConnection()).thenReturn(mockConnection);
        ingredientService = new IngredientService(mockConnector, mockIngredientManager);
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
        when(mockIngredientManager.findAll(any())).thenReturn(ingredientDaos);

        // Llamada al método que queremos probar
        List<IngredientDto> actualIngredientDtos = ingredientService.findAll();

        // Verificar el resultado
        assertEquals(expectedIngredientDtos, actualIngredientDtos);
    }

    @Test
    void testDeleteIngredient() throws SQLException, ClassNotFoundException {
        // Mock de datos
        int ingredientId = 1;

        // Mock del comportamiento del objeto IngredientManager
        when(mockIngredientManager.deleteIngredient(any(), anyInt())).thenReturn(true);

        // Llamada al método que queremos probar
        boolean deleted = ingredientService.deleteIngredient(ingredientId);

        // Verificar el resultado
        assertTrue(deleted);
        verify(mockIngredientManager, times(1)).deleteIngredient(any(), eq(ingredientId));
    }


}
