package com.fpdual.persistence.aplication.dao;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RolUserDaoTest {
    @Mock
    private static ResultSet result;


    @Test
    public void testRolUserDaoConstructor_validResultDao_userSqlException() throws SQLException{
        //Prepare method dependencies
        when(result.getInt(any())).thenThrow(SQLException.class);

        //Asserts
        assertThrows(SQLException.class, () -> result.getInt("id_user"));

    }
}
