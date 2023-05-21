package com.fpdual.utils;

import com.fpdual.api.dto.UserDto;
import com.fpdual.persistence.aplication.dao.UserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import static org.junit.jupiter.api.Assertions.*;

public class MappingUtilsTest {
    private UserDto exampleUserDto;
    private UserDao exampleUserDao;
    private MappingUtils mappingUtils;

    @BeforeEach
    public void init() {

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

        mappingUtils = new MappingUtils();

    }

    @Test
    public void testMapToDto_validUserDao_userDto() {


        //Execute method
        UserDto userDto = mappingUtils.mapToDto(exampleUserDao);

        //Asserts
        assertNotNull(exampleUserDto);
        assertAll(
                () -> assertTrue(userDto.getId() == exampleUserDto.getId()),
                () -> assertTrue(userDto.getName().equals(exampleUserDto.getName())),
                () -> assertTrue(userDto.getSurname1().equals(exampleUserDto.getSurname1())),
                () -> assertTrue(userDto.getSurname2().equals(exampleUserDto.getSurname2())),
                () -> assertTrue(userDto.getEmail().equals(exampleUserDto.getEmail())),
                () -> assertTrue(userDto.getPassword().equals(exampleUserDto.getPassword()))
        );

    }

    @Test
    public void testMapToDao_validUserDto_userDao() {


        //Execute method
        UserDao userDao = mappingUtils.mapToDao(exampleUserDto);

        //Asserts
        assertNotNull(exampleUserDao);
        assertAll(
                () -> assertTrue(userDao.getId() == exampleUserDao.getId()),
                () -> assertTrue(userDao.getName().equals(exampleUserDao.getName())),
                () -> assertTrue(userDao.getSurname1().equals(exampleUserDao.getSurname1())),
                () -> assertTrue(userDao.getSurname2().equals(exampleUserDao.getSurname2())),
                () -> assertTrue(userDao.getEmail().equals(exampleUserDao.getEmail())),
                () -> assertTrue(userDao.getPassword().equals(exampleUserDao.getPassword()))
        );

    }

}