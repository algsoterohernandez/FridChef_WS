package com.fpdual.persistence.aplication.manager;

import com.fpdual.exceptions.AlreadyExistsException;
import com.fpdual.persistence.aplication.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserManagerTest {
    @InjectMocks
    private UserManager userManager;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stm;
    @Mock
    private ResultSet resultSetMock;
    private UserDao exampleUserDao;

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
    public void testInsertUser_validConnectionAndUserDao_userDaoNotNull() throws AlreadyExistsException, SQLException {

        //Prepare method dependencies
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(anyInt())).thenReturn(100);

        when(stm.getGeneratedKeys()).thenReturn(resultSetMock);
        when(stm.executeUpdate()).thenReturn(1);

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

        //Asserts
        assertThrows(AlreadyExistsException.class, () -> userManager.insertUser(con, exampleUserDao));
    }

    @Test
    public void testInsertUser_validConnectionAndUserDao_userSQLException() throws AlreadyExistsException, SQLException {

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

        //Asserts
        assertThrows(SQLException.class, () -> userManager.deleteUser(con, exampleUserDao.getEmail()));
    }

    @Test
    public void testFindUser_validConnectionEmailAndPassword_userDaoNotNull() throws SQLException {

        //Prepare method dependencies
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