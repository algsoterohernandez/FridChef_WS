package com.fpdual.persistence.aplication.manager;

import com.fpdual.enums.Rol;
import com.fpdual.exceptions.FridChefException;
import com.fpdual.persistence.aplication.dao.RolUserDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RolManager {
    public boolean insertRol(Connection con, int idUser) throws FridChefException, SQLException {
        boolean insertRolOk;

        try (PreparedStatement stm = con.prepareStatement("INSERT INTO rol_user (id_user, id_rol) " +
                "VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, idUser);
            stm.setInt(2, Rol.STANDARD.getUserRol());

            stm.executeUpdate();

            insertRolOk = true;

        } catch (SQLIntegrityConstraintViolationException sqlicve) {

            throw new FridChefException("Ha surgido una incidencia al asignar el rol.");

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            throw e;

        }

        return insertRolOk;
    }

    public List<RolUserDao> findRolesById(Connection con, int idUser) {

        try (PreparedStatement stm = con.prepareStatement("SELECT * FROM rol_user WHERE id_user = ?;")) {

            stm.setInt(1, idUser);

            ResultSet result = stm.executeQuery();

            RolUserDao rolUserDao;

            List<RolUserDao> rolUserDaoList = new ArrayList<>();

            while (result.next()) {
                rolUserDao = new RolUserDao(result);
                rolUserDaoList.add(rolUserDao);

            }

            return rolUserDaoList;

        } catch (SQLException e) {

            System.out.println(e.getMessage());
            return null;
        }

    }
}
