package com.fpdual.service;

import com.fpdual.api.dto.RolUserDto;
import com.fpdual.api.dto.UserDto;
import com.fpdual.exceptions.FridChefException;
import com.fpdual.exceptions.AlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RolUserDao;
import com.fpdual.persistence.aplication.dao.UserDao;
import com.fpdual.persistence.aplication.manager.FavoriteManager;
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

/**
 * Servicio para manejar las operaciones relacionadas con los usuarios.
 */
public class UserService {

    private final MySQLConnector connector;
    private final UserManager userManager;
    private final RolManager rolManager;
    private final FavoriteManager favoriteManager;
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    private MessageDigest md5;

    /**
     * Constructor de la clase UserService.
     *
     * @param connector   Conector MySQL para establecer la conexión con la base de datos.
     * @param userManager Gestor de usuarios para realizar operaciones de inserción, eliminación y búsqueda.
     * @param favoriteManager  Lista de recetas favoritas
     * @param rolManager  Gestor de roles para realizar operaciones de inserción y búsqueda de roles.
     */
    public UserService(MySQLConnector connector, UserManager userManager, RolManager rolManager, FavoriteManager favoriteManager) {
        this.connector = connector;
        this.userManager = userManager;
        this.rolManager = rolManager;
        this.favoriteManager = favoriteManager;
    }

    /**
     * Crea un nuevo usuario.
     *
     * @param userDto Objeto UserDto que contiene los datos del usuario.
     * @return El objeto UserDto creado.
     * @throws SQLException            Si ocurre un error de SQL al interactuar con la base de datos.
     * @throws ClassNotFoundException Si no se encuentra la clase del controlador JDBC.
     * @throws FridChefException       Si ocurre una excepción específica de FridChef.
     */
    public UserDto createUser(UserDto userDto) throws SQLException, ClassNotFoundException, FridChefException {

        try (Connection con = connector.getMySQLConnection()) {

            UserDao userDao = this.userManager.insertUser(con, MappingUtils.mapUserToDao(userDto));
            userDto = MappingUtils.mapUserToDto(userDao);

            boolean insertRolOk =this.rolManager.insertRol(con, userDao.getId());


        } catch (AlreadyExistsException uaee) {

            if (userDto != null) {

                userDto.setAlreadyExists(true);

            }
        } catch (Exception e) {

            System.out.println(e.getMessage());
            throw e;

        }

        return userDto;

    }

    /**
     * Elimina un usuario.
     *
     * @param email Email del usuario a eliminar.
     * @return `true` si el usuario se eliminó correctamente, `false` de lo contrario.
     * @throws SQLException        Si ocurre un error de SQL durante la eliminación del usuario.
     * @throws ClassNotFoundException Si no se encuentra la clase durante la eliminación del usuario.
     */
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

    /**
     * Busca un usuario por su email y contraseña.
     *
     * @param email    El email del usuario.
     * @param password La contraseña del usuario.
     * @return El objeto UserDto que representa al usuario encontrado, o null si no se encuentra.
     * @throws SQLException             Si ocurre un error de SQL durante la búsqueda.
     * @throws ClassNotFoundException Si no se encuentra la clase del gestor de usuarios.
     */
    public UserDto findUser(String email, String password) throws Exception {
        UserDto userDto = null;
        List<RolUserDto> rolUserDto;

        try (Connection con = connector.getMySQLConnection()) {

            UserDao userDao = this.userManager.findByEmailPassword(con, email, password);

            List<RolUserDao> rolUserDao=this.rolManager.findRolesById(con, userDao.getId());

            List<Integer> favoritesUserDao = this.favoriteManager.findFavoriteList(con, userDao.getId());

            if (userDao != null) {

                userDto = MappingUtils.mapUserToDto(userDao);
                rolUserDto = MappingUtils.mapRolUserListDto(rolUserDao);

                userDto.setRolUserDto(rolUserDto);
                userDto.setFavoriteList(favoritesUserDao);
            }

        } catch (Exception e) {
            throw e;
        }

        return userDto;

    }

}