package com.fpdual.persistence.aplication.dao;

import com.fpdual.persistence.aplication.dao.IngredientDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

public class IngredientDaoTest {

    @Mock
    ResultSet resultSet;

    @Before
    public void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void testConstructorWithResultSet() throws SQLException {
        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("ingredient");
        IngredientDao ingredientDao = new IngredientDao(resultSet);
        assertEquals(1, ingredientDao.getId());
        assertEquals("ingredient", ingredientDao.getName());
    }

    @Test
    public void testGetterAndSetterMethods() {
        IngredientDao ingredientDao = new IngredientDao();
        ingredientDao.setId(1);
        ingredientDao.setName("ingredient");
        assertEquals(1, ingredientDao.getId());
        assertEquals("ingredient", ingredientDao.getName());
    }
}
