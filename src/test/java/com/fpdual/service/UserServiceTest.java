package com.fpdual.service;

import com.fpdual.api.dto.UserDto;
import com.fpdual.exceptions.UserAlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)

public class UserServiceTest {

    @Mock
    private MySQLConnector mySQLConnector;
    @Mock
    private UserManager userManager;

    private UserDto exampleUserDto;
    private UserDao exampleUserDao;
    private UserService userService;

    @BeforeEach
    public void init() {
        userService = new UserService(mySQLConnector, userManager);

        exampleUserDto = new UserDto();
        exampleUserDto.setName("example");
        exampleUserDto.setSurname1("aaaa");
        exampleUserDto.setSurname2("bbbbb");
        exampleUserDto.setPassword("example");
        exampleUserDto.setEmail("example@a.com");

        exampleUserDao = new UserDao();
        exampleUserDao.setName("example");
        exampleUserDao.setSurname1("aaaa");
        exampleUserDao.setSurname2("bbbbb");
        exampleUserDao.setPassword("example");
        exampleUserDao.setEmail("example@a.com");

    }

    @Test
    public void testCreateUser_validUserDto_userDtoNotNull() throws Exception {

        //Prepare method dependencies
        when(userManager.insertUser(any(), any())).thenReturn(exampleUserDao);

        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        UserDto userDtoRs = userService.createUser(exampleUserDto);

        //Asserts
        assertNotNull(userDtoRs);
        assertTrue(userDtoRs.getName().equals("example"));

    }

    @Test
    public void testCreateUser_validUserDto_userAlreadyExistsException() throws Exception {

        //Prepare method dependencies
        when(userManager.insertUser(any(), any())).thenThrow(UserAlreadyExistsException.class);

        //Execute method
        UserDto userDtoRs = userService.createUser(exampleUserDto);

        //Asserts
        assertNotNull(userDtoRs);
        assertTrue(userDtoRs.isAlreadyExists());
    }

    @Test
    public void testCreateUser_validUserDto_userException() throws Exception {

        //Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(ClassNotFoundException.class);

        //Execute method

        //Asserts
        assertThrows(ClassNotFoundException.class, () -> userService.createUser(exampleUserDto));
    }

    @Test
    public void testDeleteUser_validEmail_true() throws Exception {

        //Prepare method dependencies
        when(userManager.deleteUser(any(),anyString())).thenReturn(true);

        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        boolean deleted = userService.deleteUser(exampleUserDto.getEmail());

        //Asserts
        assertTrue(deleted);
    }

    @Test
    public void testDeleteUser_validEmail_false() throws Exception {

        //Prepare method dependencies
        when(userManager.deleteUser(any(),anyString())).thenReturn(false);

        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        boolean deleted = userService.deleteUser(exampleUserDto.getEmail());

        //Asserts
        assertTrue(!deleted);
    }

    @Test
    public void testDeleteUser_validEmail_userException() throws Exception {

        //Prepare method dependencies
        when(userManager.deleteUser(any(),anyString())).thenThrow(SQLException.class);

        //Execute method

        //Asserts
        assertThrows(SQLException.class, () -> userService.deleteUser(exampleUserDto.getEmail()));
    }

    @Test
    public void testFindUser_validEmailPassword_userDtoNotNull() throws Exception {

        //Prepare method dependencies
        when(userManager.findByEmailPassword(any(),anyString(), anyString())).thenReturn(exampleUserDao);

        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        UserDto userDtoRs = userService.findUser(exampleUserDto.getEmail(),exampleUserDto.getPassword());

        //Asserts
        assertNotNull(userDtoRs);
        assertTrue(userDtoRs.getEmail().equals("example@a.com") &&
                userDtoRs.getPassword().equals("example"));

    }

    @Test
    public void testFindUser_validEmailPassword_userException() throws Exception {

        //Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(ClassNotFoundException.class);

        //Execute method

        //Asserts
        assertThrows(ClassNotFoundException.class,
                () -> userService.findUser(exampleUserDto.getEmail(),exampleUserDto.getPassword()));
    }

}