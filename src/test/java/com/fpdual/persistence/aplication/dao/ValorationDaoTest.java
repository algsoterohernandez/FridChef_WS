package com.fpdual.persistence.aplication.dao;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

class ValorationDaoTest {

    @Test
    void testValorationDaoConstructor() throws SQLException {
        // Arrange
        ResultSet mockResultSet = Mockito.mock(ResultSet.class);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getInt("id_recipe")).thenReturn(1);
        when(mockResultSet.getInt("id_user")).thenReturn(2);
        when(mockResultSet.getString("comment")).thenReturn("testComment");
        when(mockResultSet.getDouble("valoration")).thenReturn(4.5);

        // Act
        ValorationDao valoration = new ValorationDao(mockResultSet);

        // Assert
        assertEquals(1, valoration.getId());
        assertEquals(1, valoration.getIdRecipe());
        assertEquals(2, valoration.getIdUser());
        assertEquals("testComment", valoration.getComment());
        assertEquals(4.5, valoration.getValoration());
    }
}
