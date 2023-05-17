package com.fpdual.service;

import com.fpdual.api.dto.UserDto;
import com.fpdual.exceptions.UserAlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.UserManager;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;

public class UserService {

    private final MySQLConnector connector;
    private final UserManager userManager;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MessageDigest md5;

    public UserService(MySQLConnector connector, UserManager userManager) {
        this.connector = connector;
        this.userManager = userManager;
    }

    public UserDto createUser(UserDto userDto) throws SQLException, ClassNotFoundException {

        try (Connection con = connector.getMySQLConnection()) {

            UserDao userDao = this.userManager.insertUser(con, mapToDao(userDto));
            userDto = mapToDto(userDao);


        } catch (UserAlreadyExistsException uaee) {

            if (userDto != null) {

                userDto.setAlreadyExists(true);

            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
            throw e;
        }

        return userDto;

    }

    public boolean deleteUser(String email) throws SQLException, ClassNotFoundException {
        boolean deleted;

        try (Connection con = connector.getMySQLConnection()) {

            deleted = this.userManager.deleteUser(con, email);

        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return deleted;

    }

    public UserDto findUser(String email, String password) throws SQLException, ClassNotFoundException {
        UserDto userDto = null;

        try (Connection con = connector.getMySQLConnection()) {

            UserDao userDao = this.userManager.findByEmailPassword(con, email, password);

            if (userDao != null) {

                userDto = mapToDto(userDao);

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
            throw e;
        }

        return userDto;

    }

    private UserDao mapToDao(UserDto userDto) {
        UserDao userDao = new UserDao();

        userDao.setId(userDto.getId());
        userDao.setName(userDto.getName());
        userDao.setSurname1(userDto.getSurname1());
        userDao.setSurname2(userDto.getSurname2());
        userDao.setEmail(userDto.getEmail());
        userDao.setPassword(userDto.getPassword());
        userDao.setCreateDate(new Date(System.currentTimeMillis()));

        return userDao;
    }

    private UserDto mapToDto(UserDao userDao) {
        UserDto userDto = new UserDto();

        userDto.setId(userDao.getId());
        userDto.setName(userDao.getName());
        userDto.setSurname1(userDao.getSurname1());
        userDto.setSurname2(userDao.getSurname2());
        userDto.setEmail(userDao.getEmail());
        userDto.setPassword(userDao.getPassword());

        return userDto;
    }

}
