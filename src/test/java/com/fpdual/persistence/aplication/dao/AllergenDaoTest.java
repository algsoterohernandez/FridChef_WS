package com.fpdual.persistence.aplication.dao;

import com.fpdual.persistence.aplication.dao.AllergenDao;
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
public class AllergenDaoTest {

    private AllergenDao allergenDao;

    @Mock
    private ResultSet resultSet;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);

        when(resultSet.getInt("id")).thenReturn(1);
        when(resultSet.getString("name")).thenReturn("Peanuts");

        allergenDao = new AllergenDao(resultSet);
    }

    @Test
    public void testGetId() {
        assertEquals(1, allergenDao.getId());
    }

    @Test
    public void testGetName() {
        assertEquals("Peanuts", allergenDao.getName());
    }


}