package com.fpdual.service;

import com.fpdual.api.dto.UserDto;
import com.fpdual.exceptions.AlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RolUserDao;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.FavoriteManager;
import com.fpdual.persistence.aplication.manager.RolManager;
import com.fpdual.persistence.aplication.manager.UserManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
    @InjectMocks
    private UserService userService;

    @Mock
    private MySQLConnector mySQLConnector;
    @Mock
    private UserManager userManager;
    @Mock
    private RolManager rolManager;
    @Mock
    private FavoriteManager favoriteManager;

    private UserDto exampleUserDto;
    private UserDao exampleUserDao;
    private RolUserDao rolUserDao;
    private List<RolUserDao> rolUserDaoList;

    @BeforeEach
    public void init() {
        userService = new UserService(mySQLConnector, userManager,rolManager, favoriteManager);

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

        rolUserDao = new RolUserDao();
        rolUserDao.setIdUser(2);
        rolUserDao.setIdRol(1);

        rolUserDaoList = new ArrayList<>();
        rolUserDaoList.add(rolUserDao);


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

    }

    @Test
    public void testCreateUser_validUserDto_userAlreadyExistsException() throws Exception {

        //Prepare method dependencies
        when(userManager.insertUser(any(), any())).thenThrow(AlreadyExistsException.class);

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

        //Asserts
        assertThrows(SQLException.class, () -> userService.deleteUser(exampleUserDto.getEmail()));
    }

    @Test
    public void testFindUser_validEmailPassword_userDtoNotNull() throws Exception {

        //Prepare method dependencies
        when(userManager.findByEmailPassword(any(),anyString(), anyString())).thenReturn(exampleUserDao);
        when(rolManager.findRolesById(any(),anyInt())).thenReturn(rolUserDaoList);
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        //Execute method
        UserDto userDtoRs = userService.findUser(exampleUserDto.getEmail(),exampleUserDto.getPassword());

        //Asserts
        assertNotNull(userDtoRs);
        assertNotNull(userService.findUser("example@a.com","example"));

    }

    @Test
    public void testFindUser_validEmailPassword_userException() throws Exception {

        //Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(ClassNotFoundException.class);

        //Asserts
        assertThrows(ClassNotFoundException.class,
                () -> userService.findUser(exampleUserDto.getEmail(),exampleUserDto.getPassword()));
    }

}