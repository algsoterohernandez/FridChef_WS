package com.fpdual.persistence.aplication.manager;

import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RecipeManagerTest {
    @InjectMocks
    private RecipeManager recipeManager;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stm;
    @Mock
    private ResultSet resultSet;

    private RecipeDao exampleRecipeDao;
    private List<RecipeDao> recipeDaoList;

    @BeforeEach
    public void init() {
        recipeManager = new RecipeManager();


        exampleRecipeDao = new RecipeDao();
        exampleRecipeDao.setId(1);
        exampleRecipeDao.setStatus(RecipeStatus.PENDING);

        recipeDaoList = new ArrayList<>();
        recipeDaoList.add(exampleRecipeDao);
    }

    @Test
    public void testFindByStatusPending_validConnection_recipeDaoListNotNull() throws SQLException {

        //Prepare method dependencies
        when(resultSet.next()).thenReturn(true,false);

        when(resultSet.getString(anyString())).thenReturn(RecipeStatus.PENDING.name());

        when(stm.executeQuery()).thenReturn(resultSet);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        recipeDaoList = recipeManager.findByStatusPending(con);

        //Asserts
        assertNotNull(recipeDaoList);
        assertTrue(recipeDaoList.size() > 0);
    }

    @Test
    public void testFindByStatusPending_validConnection_recipeDaoListSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString())).thenThrow(SQLException.class);

        //Asserts
        assertNull(recipeManager.findByStatusPending(con));
    }

//    @Test
//    void testFindAll() throws SQLException {
//        // Crear una instancia mock de Connection, Statement y ResultSet
//        Connection mockConnection = mock(Connection.class);
//        Statement mockStatement = mock(Statement.class);
//        ResultSet mockResultSet = mock(ResultSet.class);
//
//        // Configurar el comportamiento simulado del mock Statement y ResultSet
//        when(mockConnection.createStatement()).thenReturn(mockStatement);
//        when(mockStatement.executeQuery(ArgumentMatchers.anyString())).thenReturn(mockResultSet);
//        when(mockResultSet.next()).thenReturn(true, true, false); // Simular dos filas en el ResultSet
//
//        // Configurar el comportamiento simulado del mock ResultSet para devolver los valores de las columnas
//        when(mockResultSet.getInt("id")).thenReturn(1, 2);
//        when(mockResultSet.getString("name")).thenReturn("Recipe 1", "Recipe 2");
//
//        // Instanciar el objeto RecipeManager a probar
//        RecipeManager recipeManager = new RecipeManager();
//
//        // Llamar al método bajo prueba
//        List<RecipeDao> recipes = recipeManager.findAll(mockConnection);
//
//        // Verificar el resultado
//        assertNotNull(recipes);
//        assertEquals(2, recipes.size());
//
//        // Verificar la primera receta
//        RecipeDao recipe1 = recipes.get(0);
//        assertEquals(1, recipe1.getId());
//        assertEquals("Recipe 1", recipe1.getName());
//
//        // Verificar la segunda receta
//        RecipeDao recipe2 = recipes.get(1);
//        assertEquals(2, recipe2.getId());
//        assertEquals("Recipe 2", recipe2.getName());
//    }


}
