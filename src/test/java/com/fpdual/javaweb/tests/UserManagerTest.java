package com.fpdual.javaweb.tests;

import com.fpdual.exceptions.UserAlreadyExistsException;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {

    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stm;
    private UserDao exampleUserDao;
    private UserManager userManager;

    @BeforeEach
    public void init() {
        userManager = new UserManager();


        exampleUserDao = new UserDao();
        exampleUserDao.setName("example");
        exampleUserDao.setSurname1("aaaa");
        exampleUserDao.setSurname2("bbbbb");
        exampleUserDao.setPassword("example");
        exampleUserDao.setEmail("example@a.com");

    }

    @Test
    public void testInsertUser_validConnectionAndUserDao_userDaoNotNull() throws UserAlreadyExistsException, SQLException {

        //Prepare method dependencies
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(anyInt())).thenReturn(100);

        when(stm.executeUpdate()).thenReturn(1);
        when(stm.getGeneratedKeys()).thenReturn(resultSetMock);

        when(con.prepareStatement(anyString(), anyInt())).thenReturn(stm);

        //Execute method
        UserDao userDaoRs = userManager.insertUser(con, exampleUserDao);

        //Asserts
        assertNotNull(userDaoRs);
        assertTrue(userDaoRs.getId() == 100);
    }

    @Test
    public void testInsertUser_validConnectionAndUserDao_userSQLIntegrityConstraintViolationException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString(), anyInt())).thenThrow(SQLIntegrityConstraintViolationException.class);

        //Execute method

        //Asserts
        assertThrows(UserAlreadyExistsException.class, () -> userManager.insertUser(con, exampleUserDao));
    }

    @Test
    public void testInsertUser_validConnectionAndUserDao_userSQLException() throws UserAlreadyExistsException, SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);

        //Execute method
        UserDao userDaoRs = userManager.insertUser(con, exampleUserDao);

        //Asserts
        assertNull(userDaoRs);
    }


    @Test
    public void testDeleteUser_validConnectionAndEmail_true() throws SQLException {

        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        boolean deleted = userManager.deleteUser(con, exampleUserDao.getEmail());

        //Asserts
        assertTrue(deleted);
    }

    @Test
    public void testDeleteUser_validConnectionAndEmail_false() throws SQLException {

        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(0);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        boolean deleted = userManager.deleteUser(con, exampleUserDao.getEmail());

        //Asserts
        assertTrue(!deleted);
    }

    @Test
    public void testDeleteUser_validConnectionAndEmail_userSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString())).thenThrow(SQLException.class);

        //Execute method

        //Asserts
        assertThrows(SQLException.class, () -> userManager.deleteUser(con, exampleUserDao.getEmail()));
    }

    @Test
    public void testFindUser_validConnectionEmailAndPassword_userDaoNotNull() throws SQLException {

        //Prepare method dependencies
        ResultSet resultSetMock = Mockito.mock(ResultSet.class);

        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getString(anyString())).thenReturn(exampleUserDao.getName()).
                thenReturn(exampleUserDao.getSurname1()).thenReturn(exampleUserDao.getSurname2()).
                thenReturn(exampleUserDao.getEmail()).thenReturn(exampleUserDao.getPassword());

        when(stm.executeQuery()).thenReturn(resultSetMock);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        UserDao userDaoRs = userManager.findByEmailPassword(con, exampleUserDao.getEmail(), exampleUserDao.getPassword());

        //Asserts
        assertNotNull(userDaoRs);
        assertTrue(userDaoRs.getEmail().equals("example@a.com") && userDaoRs.getPassword().equals("example"));
    }

    @Test
    public void testFindUser_validConnectionEmailAndPassword_userSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString())).thenThrow(SQLException.class);

        //Execute method
        UserDao userDaoRs = userManager.findByEmailPassword(con, exampleUserDao.getEmail(),exampleUserDao.getPassword());

        //Asserts
        assertNull(userDaoRs);
    }

}