package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredientManagerTest {

    @Mock
    private Connection mockConnection;

    @Mock
    private PreparedStatement mockStatement;

    @Mock
    private ResultSet mockResultSet;

    private IngredientManager ingredientManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientManager = new IngredientManager();
    }

    @Test
    void findAll_ShouldReturnListOfIngredients() throws SQLException {

        List<IngredientDao> expectedIngredients = new ArrayList<>();

        IngredientDao ingredient1 = new IngredientDao();

        ingredient1.setId(1);
        ingredient1.setName("aguacate");

        IngredientDao ingredient2 = new IngredientDao();

        ingredient2.setId(2);
        ingredient2.setName("tomate");

        expectedIngredients.add(ingredient1);
        expectedIngredients.add(ingredient2);

        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false);
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("aguacate", "tomate");

        List<IngredientDao> ingredients = ingredientManager.findAll(mockConnection);

        assertNotNull(ingredients);
        assertEquals(expectedIngredients.size(), ingredients.size());
        assertEquals(expectedIngredients.get(0), ingredients.get(0));
        assertEquals(expectedIngredients.get(1), ingredients.get(1));

        verify(mockConnection).createStatement();
        verify(mockStatement).executeQuery(anyString());
        verify(mockResultSet, times(3)).next();
        verify(mockResultSet, times(2)).getInt("id");
        verify(mockResultSet, times(2)).getString("name");
    }

    @Test
    void testGetIngredientIdByName() throws SQLException {
        // Datos de prueba
        String ingredientName = "lechuga";
        int expectedId = 1;

        // Crear una instancia mock de Connection, Statement y ResultSet
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);

        // Configurar el comportamiento simulado del mock Statement y ResultSet
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt("id")).thenReturn(expectedId);

        // Instanciar el objeto IngredientManager a probar
        IngredientManager ingredientManager = new IngredientManager();

        // Llamar al método bajo prueba
        Integer actualId = ingredientManager.getIngredientIdByName(mockConnection, ingredientName);

        // Verificar el resultado
        assertNotNull(actualId);
        assertEquals(expectedId, actualId);
    }

    @Test
    void testGetIngredientIdByName_WithNullResult() throws SQLException {
        // Datos de prueba
        String ingredientName = "coche";

        // Crear una instancia mock de Connection y Statement
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);

        // Configurar el comportamiento simulado del mock Statement para devolver un ResultSet vacío
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenReturn(mock(ResultSet.class));

        // Instanciar el objeto IngredientManager a probar
        IngredientManager ingredientManager = new IngredientManager();

        // Llamar al método bajo prueba
        Integer actualId = ingredientManager.getIngredientIdByName(mockConnection, ingredientName);

        // Verificar el resultado
        assertNull(actualId);
    }
}
