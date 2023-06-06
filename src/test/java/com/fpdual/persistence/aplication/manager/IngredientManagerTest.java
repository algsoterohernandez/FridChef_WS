package com.fpdual.persistence.aplication.manager;



import com.fpdual.persistence.aplication.dao.IngredientDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class IngredientManagerTest {

    @InjectMocks
    private IngredientManager ingredientManager;
    @Mock
    private Connection con;

    @Mock
    private PreparedStatement stm;

    @Mock
    private ResultSet resultSetMock;
    private IngredientDao exampleIngredientDao;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ingredientManager = new IngredientManager();

        exampleIngredientDao = new IngredientDao();
        exampleIngredientDao.setId(6);
        exampleIngredientDao.setName("Lechuga");
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

        when(con.createStatement()).thenReturn(stm);
        when(stm.executeQuery(anyString())).thenReturn(resultSetMock);
        when(resultSetMock.next()).thenReturn(true, true, false);
        when(resultSetMock.getInt("id")).thenReturn(1, 2);
        when(resultSetMock.getString("name")).thenReturn("aguacate", "tomate");

        List<IngredientDao> ingredients = ingredientManager.findAll(con);

        assertNotNull(ingredients);
        assertEquals(expectedIngredients.size(), ingredients.size());
        assertEquals(expectedIngredients.get(0), ingredients.get(0));
        assertEquals(expectedIngredients.get(1), ingredients.get(1));

        verify(con).createStatement();
        verify(stm).executeQuery(anyString());
        verify(resultSetMock, times(3)).next();
        verify(resultSetMock, times(2)).getInt("id");
        verify(resultSetMock, times(2)).getString("name");
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

    @Test
    public void testInsertIngredient_validConnectionName_ingredientDaoNotNull() throws SQLException {

        //Prepare method dependencies
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(anyInt())).thenReturn(100);

        when(stm.getGeneratedKeys()).thenReturn(resultSetMock);
        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString(), anyInt())).thenReturn(stm);

        //Execute method
        IngredientDao ingredientDaoRs = ingredientManager.insertIngredient(con, exampleIngredientDao.getName());

        //Asserts
        assertNotNull(ingredientDaoRs);
    }

    @Test
    public void testInsertIngredient_validConnectionName_ingredientDaoSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);

        //Execute method
        IngredientDao ingredientDaoRs = ingredientManager.insertIngredient(con, exampleIngredientDao.getName());

        //Asserts
        assertNull(ingredientDaoRs);
    }

    @Test
    public void testDeleteUser_validConnectionAndEmail_true() throws SQLException {

        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        boolean deleted = ingredientManager.deleteIngredient(con, exampleIngredientDao.getId());

        //Asserts
        assertTrue(deleted);
    }

    @Test
    public void testDeleteUser_validConnectionAndEmail_false() throws SQLException {

        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(0);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        boolean deleted = ingredientManager.deleteIngredient(con, exampleIngredientDao.getId());

        //Asserts
        assertFalse(deleted);
    }

    @Test
    public void testDeleteUser_validConnectionAndEmail_userSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString())).thenThrow(SQLException.class);

        //Asserts
        assertThrows(SQLException.class, () -> ingredientManager.deleteIngredient(con, exampleIngredientDao.getId()));
    }
}