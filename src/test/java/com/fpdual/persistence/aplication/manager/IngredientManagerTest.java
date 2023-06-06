package com.fpdual.persistence.aplication.manager;


import com.fpdual.persistence.aplication.dao.IngredientDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class IngredientManagerTest {

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
    public void init() {
        ingredientManager = new IngredientManager();


        exampleIngredientDao = new IngredientDao();
        exampleIngredientDao.setId(6);
        exampleIngredientDao.setName("Lechuga");

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


