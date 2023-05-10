package com.fpdual.persistence.aplication.dao;

import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

@Data
@Getter
@Setter
@NoArgsConstructor

public class RoleUserDao {
    private int idRole;
    private int idUser;
    private List<RoleDao> roles;
    private List<UserDao> users;

    public RoleUserDao(ResultSet result) {

        try {
            this.idRole = result.getInt("id_role");
            this.idUser = result.getInt("id_user");
            this.roles  = new ArrayList<>();
            this.users = new ArrayList<>();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
