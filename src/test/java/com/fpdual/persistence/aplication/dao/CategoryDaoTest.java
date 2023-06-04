package com.fpdual.persistence.aplication.dao;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CategoryDaoTest {

    private CategoryDao categoryDao;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Test Category");

        categoryDao = new CategoryDao(resultSet);
    }

    @Test
    public void testGetId() {
        assertEquals(1, categoryDao.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Test Category", categoryDao.getName());
    }

    @Test
    public void testGetRecipes() {
        assertEquals(0, categoryDao.getRecipes().size());
    }
}
