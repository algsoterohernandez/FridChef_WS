package com.fpdual.service;

import com.fpdual.api.dto.RolUserDto;
import com.fpdual.api.dto.UserDto;
import com.fpdual.exceptions.FridChefException;
import com.fpdual.exceptions.UserAlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RolUserDao;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.RolManager;
import com.fpdual.persistence.aplication.manager.UserManager;
import com.fpdual.utils.MappingUtils;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;

import java.security.MessageDigest;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class UserService {

    private final MySQLConnector connector;
    private final UserManager userManager;
    private final RolManager rolManager;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MessageDigest md5;

    public UserService(MySQLConnector connector, UserManager userManager, RolManager rolManager) {
        this.connector = connector;
        this.userManager = userManager;
        this.rolManager = rolManager;
    }

    public UserDto createUser(UserDto userDto) throws SQLException, ClassNotFoundException, FridChefException {

        try (Connection con = connector.getMySQLConnection()) {

            UserDao userDao = this.userManager.insertUser(con, MappingUtils.mapToDao(userDto));
            userDto = MappingUtils.mapToDto(userDao);

            boolean insertRolOk =this.rolManager.insertRol(con,userDto);


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
        List<RolUserDto> rolUserDto;

        try (Connection con = connector.getMySQLConnection()) {

            UserDao userDao = this.userManager.findByEmailPassword(con, email, password);

            List<RolUserDao> rolUserDao=this.rolManager.findRolesById(con, userDao.getId());

            if (userDao != null) {

                userDto = MappingUtils.mapToDto(userDao);
                rolUserDto = MappingUtils.mapRolUserDto(rolUserDao);

                userDto.setRolUserDto(rolUserDto);
            }

        } catch (Exception e) {

            System.out.println(e.getMessage());
            throw e;
        }

        return userDto;

    }

}