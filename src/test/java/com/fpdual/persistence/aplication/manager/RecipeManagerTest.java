package com.fpdual.persistence.aplication.manager;

import com.fpdual.enums.RecipeStatus;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
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
    private PreparedStatement selectStm;
    @Mock
    private ResultSet result;

    private RecipeDao exampleRecipeDao;
    private RecipeDao exampleRecipeDaoAccepted;
    private RecipeDao exampleRecipeDaoDeclined;
    private List<RecipeDao> recipeDaoList;

    @BeforeEach
    public void init() {
        recipeManager = new RecipeManager();


        exampleRecipeDao = new RecipeDao();
        exampleRecipeDao.setId(1);
        exampleRecipeDao.setStatus(RecipeStatus.PENDING);

        exampleRecipeDaoAccepted = new RecipeDao();
        exampleRecipeDaoAccepted.setId(2);
        exampleRecipeDaoAccepted.setName("Tomate");
        exampleRecipeDaoAccepted.setDescription("En esta receta...");
        exampleRecipeDaoAccepted.setStatus(RecipeStatus.ACCEPTED);

        exampleRecipeDaoDeclined = new RecipeDao();
        exampleRecipeDaoDeclined.setId(3);
        exampleRecipeDaoDeclined.setName("Lechuga");
        exampleRecipeDaoDeclined.setDescription("En esta receta...");
        exampleRecipeDaoDeclined.setStatus(RecipeStatus.DECLINED);

        recipeDaoList = new ArrayList<>();
        recipeDaoList.add(exampleRecipeDao);
    }

    @Test
    public void testFindByStatusPending_validConnection_recipeDaoListNotNull() throws SQLException {

        //Prepare method dependencies
        when(result.next()).thenReturn(true,false);

        when(result.getString(anyString())).thenReturn(RecipeStatus.PENDING.name());

        when(stm.executeQuery()).thenReturn(result);

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

    /*@Test
    public void testUpdateRecipeStatus_validConnectionIdStatus_recipeDaoNotNullAccepted() throws SQLException {
        // Prepare method dependencies
        when(result.getString(anyInt())).thenReturn(RecipeStatus.ACCEPTED.name());
        when(result.next()).thenReturn(true);
        when(selectStm.executeQuery()).thenReturn(result);

        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString())).thenReturn(stm, selectStm);

        // Execute method
        RecipeDao recipeDaoRs = recipeManager.updateRecipeStatus(con, exampleRecipeDaoAccepted.getId(), String.valueOf(exampleRecipeDaoAccepted.getStatus()));

        // Asserts
        assertNotNull(recipeDaoRs);
    }*/


    @Test
    public void testUpdateRecipeStatus_validConnectionIdStatus_recipeDaoSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString())).thenThrow(SQLException.class);

        //Asserts
        assertNull(recipeManager.updateRecipeStatus(con, exampleRecipeDao.getId(), String.valueOf(exampleRecipeDao.getStatus())));
    }

}
