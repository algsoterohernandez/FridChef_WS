package com.fpdual.service;

import com.fpdual.api.dto.UserDto;
import com.fpdual.exceptions.UserAlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.utils.MappingUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;

public class UserService {

    private final MySQLConnector connector;
    private final UserManager userManager;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MessageDigest md5;
    private MappingUtils mapper;

    public UserService(MySQLConnector connector, UserManager userManager) {
        this.connector = connector;
        this.userManager = userManager;
        this.mapper = new MappingUtils();
    }

    public UserDto createUser(UserDto userDto) throws SQLException, ClassNotFoundException {

        try (Connection con = connector.getMySQLConnection()) {

            UserDao userDao = this.userManager.insertUser(con, mapper.mapToDao(userDto));
            userDto = mapper.mapToDto(userDao);


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

                userDto = mapper.mapToDto(userDao);

            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
            throw e;
        }

        return userDto;

    }

}