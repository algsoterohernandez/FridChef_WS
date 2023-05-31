package com.fpdual.fridchefapi.persistence.aplication.manager;

import com.fpdual.fridchefapi.enums.RecipeStatus;
import com.fpdual.fridchefapi.persistence.aplication.dao.RecipeDao;
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

}
