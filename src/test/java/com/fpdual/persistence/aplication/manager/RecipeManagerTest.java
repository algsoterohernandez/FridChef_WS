package com.fpdual.persistence.aplication.manager;

import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.dao.IngredientRecipeDao;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeManagerTest {
    @InjectMocks
    private RecipeManager recipeManager;

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockStatement;
    @Mock
    private ResultSet mockResultSet;
    @Mock
    private IngredientManager mockIngredientManager;

    private RecipeDao exampleRecipeDao;
    private List<RecipeDao> recipeDaoList;

    @BeforeEach
    public void init() {
        recipeManager = new RecipeManager(mockIngredientManager);

        exampleRecipeDao = new RecipeDao();
        exampleRecipeDao.setId(1);
        exampleRecipeDao.setStatus(RecipeStatus.PENDING);

        recipeDaoList = new ArrayList<>();
        recipeDaoList.add(exampleRecipeDao);
    }

    @Test
    public void testFindByStatusPending_validConnection_recipeDaoListNotNull() throws SQLException {

        //Prepare method dependencies
        when(mockResultSet.next()).thenReturn(true,false);

        when(mockResultSet.getString(anyString())).thenReturn(RecipeStatus.PENDING.name());

        when(mockStatement.executeQuery()).thenReturn(mockResultSet);

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);

        //Execute method
        recipeDaoList = recipeManager.findByStatusPending(mockConnection);

        //Asserts
        assertNotNull(recipeDaoList);
        assertTrue(recipeDaoList.size() > 0);
    }

    @Test
    public void testFindByStatusPending_validConnection_recipeDaoListSQLException() throws SQLException {

        //Prepare method dependencies
        when(mockConnection.prepareStatement(anyString())).thenThrow(SQLException.class);

        //Asserts
        assertNull(recipeManager.findByStatusPending(mockConnection));
    }

    @Test
    void testFindAll() throws SQLException {
        // Crear una instancia mock de Connection, Statement y ResultSet
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);
        ResultSet mockResultSet = mock(ResultSet.class);


        // Configurar el comportamiento simulado del mock Statement y ResultSet
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, true, false); // Simular dos filas en el ResultSet

        // Configurar el comportamiento simulado del mock ResultSet para devolver los valores de las columnas
        when(mockResultSet.getInt("id")).thenReturn(1, 2);
        when(mockResultSet.getString("name")).thenReturn("Recipe 1", "Recipe 2");

        when(mockIngredientManager.findIngredientsByRecipeId(any(), anyInt())).thenReturn(new ArrayList<>());

        // Llamar al método bajo prueba
        List<RecipeDao> recipes = recipeManager.findAll(mockConnection);

        // Verificar el resultado
        assertNotNull(recipes);
        assertEquals(2, recipes.size());

    }

    @Test
    void testFindAllWithSQLException() throws SQLException {
        // Crear una instancia mock de Connection y Statement
        Connection mockConnection = mock(Connection.class);
        Statement mockStatement = mock(Statement.class);

        // Configurar el comportamiento simulado del mock Statement para lanzar una SQLException
        when(mockConnection.createStatement()).thenReturn(mockStatement);
        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenThrow(new SQLException());

        // Llamar al método bajo prueba
        List<RecipeDao> recipes = recipeManager.findAll(mockConnection);

        // Verificar el resultado
        assertNull(recipes);
    }

    @Test
    public void testFindRecipesByIngredients() throws SQLException {
        // Mock de datos
        List<Integer> ingredientIds = new ArrayList<>();
        ingredientIds.add(1);
        ingredientIds.add(2);
        ingredientIds.add(3);

        LocalDate specificDate = LocalDate.of(2022, 5, 10);
        Date specificSqlDate = Date.valueOf(specificDate);

        RecipeDao recipe1 = new RecipeDao();
        recipe1.setId(1);
        recipe1.setName("ensalada");
        recipe1.setDescription("ensalada");
        recipe1.setDifficulty(1);
        recipe1.setTime(1);
        recipe1.setUnitTime("h");
        recipe1.setIdCategory(1);
        recipe1.setCreateTime(specificSqlDate);
        recipe1.setImage(null);
        recipe1.setStatus(RecipeStatus.valueOf("PENDING"));
        recipe1.setValoration(5.0);


        List<RecipeDao> expectedRecipes = new ArrayList<>();
        expectedRecipes.add(recipe1);

        // Mock del comportamiento del objeto Connection, PreparedStatement y ResultSet
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true,  false);
        when(mockResultSet.getInt("id")).thenReturn(1 );
        when(mockResultSet.getString("name")).thenReturn("ensalada");
        when(mockResultSet.getString("description")).thenReturn("ensalada");
        when(mockResultSet.getInt("difficulty")).thenReturn(1);
        when(mockResultSet.getInt("time")).thenReturn(1);
        when(mockResultSet.getString("unit_time")).thenReturn("h");
        when(mockResultSet.getInt("id_category")).thenReturn(1);
        when(mockResultSet.getDate("create_time")).thenReturn(specificSqlDate);
        when(mockResultSet.getBlob("image")).thenReturn(null);
        when(mockResultSet.getString("status")).thenReturn("PENDING");
        when(mockResultSet.getDouble("valoration")).thenReturn(5.0);

        // Llamada al método que queremos probar
        List<RecipeDao> actualRecipes = recipeManager.findRecipesByIngredients(mockConnection, ingredientIds);

        // Verificar el resultado
        assertEquals(expectedRecipes, actualRecipes);
    }

    @Test
    void testFindRecipesByIngredientsWithSQLException() throws SQLException {
        // Mock de datos
        List<Integer> ingredientIds = new ArrayList<>();
        ingredientIds.add(1);
        ingredientIds.add(2);
        ingredientIds.add(3);

        // Mock del comportamiento del objeto Connection y PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        // Llamada al método que queremos probar
        List<RecipeDao> actualRecipes = recipeManager.findRecipesByIngredients(mockConnection, ingredientIds);

        // Verificar el resultado
        assertEquals(Collections.emptyList(), actualRecipes);
    }


    @Test
    public void testFindRecipeSuggestions() throws SQLException {
        // Mock de datos
        List<Integer> ingredientIds = new ArrayList<>();
        ingredientIds.add(1);
        ingredientIds.add(2);
        ingredientIds.add(3);

        RecipeDao recipe1 = new RecipeDao();
        recipe1.setId(1);
        recipe1.setName("ensalada");
        recipe1.setDescription("ensalada");
        recipe1.setDifficulty(1);
        recipe1.setTime(1);
        recipe1.setUnitTime("h");
        recipe1.setIdCategory(1);
        recipe1.setCreateTime(null);
        recipe1.setImage(null);
        recipe1.setStatus(RecipeStatus.ACCEPTED);
        recipe1.setValoration(0.0);

        List<RecipeDao> expectedRecipes = new ArrayList<>();
        expectedRecipes.add(recipe1);

        // Mock del comportamiento del objeto Connection, PreparedStatement y ResultSet
        when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
        when(mockStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("name")).thenReturn("ensalada");
        when(mockResultSet.getString("description")).thenReturn("ensalada");
        when(mockResultSet.getInt("difficulty")).thenReturn(1);
        when(mockResultSet.getInt("time")).thenReturn(1);
        when(mockResultSet.getString("unit_time")).thenReturn("h");
        when(mockResultSet.getInt("id_category")).thenReturn(1);
        when(mockResultSet.getDate("create_time")).thenReturn(null);
        when(mockResultSet.getBlob("image")).thenReturn(null);
        when(mockResultSet.getString("status")).thenReturn("ACCEPTED");
        when(mockResultSet.getDouble("valoration")).thenReturn(0.0);

        // Llamada al método que queremos probar
        List<RecipeDao> actualRecipes = recipeManager.findRecipeSuggestions(mockConnection, ingredientIds);

        // Verificar el resultado
        assertEquals(expectedRecipes, actualRecipes);
    }
    @Test
    void testFindRecipeSuggestionsWithSQLException() throws SQLException {
        // Mock de datos
        List<Integer> ingredientIds = new ArrayList<>();
        ingredientIds.add(1);
        ingredientIds.add(2);
        ingredientIds.add(3);

        // Mock del comportamiento del objeto Connection y PreparedStatement
        when(mockConnection.prepareStatement(anyString())).thenThrow(new SQLException());

        // Llamada al método que queremos probar
        List<RecipeDao> actualRecipes = recipeManager.findRecipeSuggestions(mockConnection, ingredientIds);

        // Verificar el resultado
        assertEquals(Collections.emptyList(), actualRecipes);
    }

}
