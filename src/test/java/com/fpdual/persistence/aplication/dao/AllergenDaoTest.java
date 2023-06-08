package com.fpdual.persistence.aplication.dao;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

import java.sql.ResultSet;
import java.sql.SQLException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class AllergenDaoTest {


    @Mock
    private ResultSet resultSetMock;


    @Test
    public void testConstructorWithResultSet() throws SQLException {
        when(resultSetMock.getInt("id")).thenReturn(0);
        when(resultSetMock.getString("name")).thenReturn("Gluten");

        AllergenDao allergenDao = new AllergenDao(resultSetMock);

        assertEquals(0, allergenDao.getId());
        assertEquals("Gluten", allergenDao.getName());
    }


    @Test
    public void testSetId() {
        AllergenDao allergenDao = new AllergenDao(resultSetMock);
        allergenDao.setId(2);
        assertEquals(2, allergenDao.getId());
    }

    @Test
    public void testSetName() {
        AllergenDao allergenDao = new AllergenDao(resultSetMock);
        allergenDao.setName("Lactosa");
        assertEquals("Lactosa", allergenDao.getName());
    }

    @Test
    public void testConstructorWithoutResultSet() throws SQLException {
        when(resultSetMock.getInt("id")).thenReturn(0);
        when(resultSetMock.getString("name")).thenReturn(null);

        AllergenDao allergenDao = new AllergenDao(resultSetMock);
        assertEquals(0, allergenDao.getId());
        assertNull(allergenDao.getName());
    }

    @Test
    public void testConstructorWithResultSetThrowsSQLException() throws SQLException {
        when(resultSetMock.getInt("id")).thenReturn(0);
        when(resultSetMock.getString("name")).thenReturn(null);

        AllergenDao allergenDao = new AllergenDao(resultSetMock);

        assertEquals(0, allergenDao.getId());
        assertNull(allergenDao.getName());
    }
}
