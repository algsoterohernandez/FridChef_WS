package com.fpdual.service;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.manager.AllergenManager;
import com.fpdual.utils.MappingUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AllergenServiceTest {
    @Mock
    private MySQLConnector mockConnector;
    @Mock
    private AllergenManager mockAllergenManager;
    @Mock
    private Connection mockConnection;

    private AllergenService allergenService;

    @BeforeEach
    void setUp() throws SQLException, ClassNotFoundException {
        MockitoAnnotations.openMocks(this);
        when(mockConnector.getMySQLConnection()).thenReturn(mockConnection);
        allergenService = new AllergenService(mockConnector, mockAllergenManager);
    }

    @Test
    void testFindAllAllergens(){
        // Mock de datos
        List<AllergenDao> allergenDaos = new ArrayList<>();
        //allergenDaos.add(new AllergenDao(1, "Allergen 1"));
        //allergenDaos.add(new AllergenDao(2, "Allergen 2"));

        List<AllergenDto> expectedAllergenDtoList = MappingUtils.mapAllergenListDto(allergenDaos);

        // Mock del comportamiento del objeto AllergenManager
        when(mockAllergenManager.findAllAllergens(any())).thenReturn(allergenDaos);

        // Llamada al método que queremos probar
        List<AllergenDto> actualAllergenDtoList = allergenService.findAllAllergens();

        // Verificar el resultado
        assertEquals(expectedAllergenDtoList, actualAllergenDtoList);
    }

    @Test
    void testFindAllAllergensWithException() throws SQLException, ClassNotFoundException {
        // Mock del comportamiento del objeto MySQLConnector
        when(mockConnector.getMySQLConnection()).thenThrow(new SQLException());

        // Llamada al método que queremos probar
        List<AllergenDto> actualAllergenDtoList = allergenService.findAllAllergens();

        // Verificar el resultado
        assertEquals(null, actualAllergenDtoList);
    }
}
