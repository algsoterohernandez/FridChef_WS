package com.fpdual.persistence.aplication.manager;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.fpdual.persistence.aplication.dao.AllergenDao;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;

class AllergenManagerTest {

    @Test
    void testFindAllAllergens() throws SQLException {
        // Crear una instancia mock de Connection, Statement y ResultSet
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configurar el comportamiento simulado del mock Statement y ResultSet
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simular dos filas en el ResultSet
        when(mockResultSet.getString("name")).thenReturn("Allergen 1", "Allergen 2");

        // Instanciar el objeto AllergenManager a probar
        AllergenManager allergenManager = new AllergenManager();

        // Llamar al método bajo prueba
        List<AllergenDao> allergens = allergenManager.findAllAllergens(mockConnection);

        // Verificar el resultado
        assertNotNull(allergens);
        assertEquals(2, allergens.size());
        assertEquals("Allergen 1", allergens.get(0).getName());
        assertEquals("Allergen 2", allergens.get(1).getName());
    }

    @Test
    void testFindIngredientAllergens() throws SQLException {
        // Datos de prueba
        int ingredientId = 1;

        // Crear una instancia mock de Connection, Statement y ResultSet
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configurar el comportamiento simulado del mock Statement y ResultSet
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simular dos filas en el ResultSet
        when(mockResultSet.getString("name")).thenReturn("Allergen 1", "Allergen 2");

        // Instanciar el objeto AllergenManager a probar
        AllergenManager allergenManager = new AllergenManager();

        // Llamar al método bajo prueba
        List<AllergenDao> allergens = allergenManager.findIngredientAllergens(mockConnection, ingredientId);

        // Verificar el resultado
        assertNotNull(allergens);
        assertEquals(2, allergens.size());
        assertEquals("Allergen 1", allergens.get(0).getName());
        assertEquals("Allergen 2", allergens.get(1).getName());
    }
}
