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
    public void setUp(){
        MockitoAnnotations.initMocks(this);
        valorationManager = new ValorationManager();
    }

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
