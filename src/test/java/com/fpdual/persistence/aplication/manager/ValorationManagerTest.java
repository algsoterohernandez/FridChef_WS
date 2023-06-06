package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.dao.ValorationDao;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.*;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

public class ValorationManagerTest {

    private ValorationManager valorationManager;

    @Mock
    private Connection mockConnection;
    @Mock
    private PreparedStatement mockPreparedStatement;
    @Mock
    private ResultSet mockResultSet;

    @Before
    public void setUp() throws SQLException {
        MockitoAnnotations.initMocks(this);
        valorationManager = new ValorationManager();
    }

    /*@Test
    public void testCreateValoration() throws SQLException {
        ValorationDao valoration = new ValorationDao();
        valoration.setIdRecipe(1);
        valoration.setIdUser(1);
        valoration.setComment("Good recipe");
        valoration.setValoration(4.5);
        valoration.setCreateTime(new Date(System.currentTimeMillis()));

        when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeUpdate()).thenReturn(1);
        when(mockPreparedStatement.getGeneratedKeys()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true);
        when(mockResultSet.getInt(1)).thenReturn(1);

        ValorationDao createdValoration = valorationManager.createValoration(mockConnection, valoration);

        verify(mockPreparedStatement).setInt(1, 1);
        verify(mockPreparedStatement).setInt(2, 1);
        verify(mockPreparedStatement).setString(3, "Good recipe");
        verify(mockPreparedStatement).setDouble(4, 4.5);
        verify(mockPreparedStatement).setDate(5, valoration.getCreateTime());
        verify(mockPreparedStatement).close();

        assertEquals(1, createdValoration.getId());
    }*/

    @Test
    public void testFindValorationById() throws SQLException {
        int id = 1;
        int limit = 10;

        when(mockConnection.prepareStatement(anyString())).thenReturn(mockPreparedStatement);
        when(mockPreparedStatement.executeQuery()).thenReturn(mockResultSet);
        when(mockResultSet.next()).thenReturn(true, false);
        when(mockResultSet.getInt("id")).thenReturn(1);
        when(mockResultSet.getString("comment")).thenReturn("Good recipe");
        // Mock additional fields

        List<ValorationDao> valorationList = valorationManager.findValorationById(mockConnection, id, limit);

        verify(mockPreparedStatement).setInt(1, id);
        verify(mockPreparedStatement).setInt(2, limit);
        verify(mockPreparedStatement).close();

        assertEquals(1, valorationList.size());
        ValorationDao valoration = valorationList.get(0);
        assertEquals(1, valoration.getId());
        assertEquals("Good recipe", valoration.getComment());
        // Assert additional fields
    }
}
