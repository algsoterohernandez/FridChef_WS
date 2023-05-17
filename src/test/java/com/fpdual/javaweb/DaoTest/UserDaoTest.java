package com.fpdual.javaweb.DaoTest;

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
public class UserDaoTest {

    @Mock
    private static ResultSet result;


    @Test
    public void testUserDaoConstructor_validRsultDao_userSqlException() throws SQLException{
        //Prepare method dependencies
        when(result.getInt(any())).thenThrow(SQLException.class);

        //Execute methods

        //Asserts
        assertThrows(SQLException.class, () -> result.getInt("email"));

    }


}