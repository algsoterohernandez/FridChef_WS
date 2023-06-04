package com.fpdual.persistence.aplication.manager;

import com.fpdual.persistence.aplication.dao.ValorationDao;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class ValorationManager {
    public ValorationDao createValoration(Connection con, ValorationDao valoration) throws SQLException {

        try (PreparedStatement stm = con.prepareStatement("INSERT INTO valoration (id_recipe, id_user, comment, valoration, create_time) VALUES (?,?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {

            stm.setInt(1, valoration.getIdRecipe());
            stm.setInt(2, valoration.getIdUser());
            stm.setString(3, valoration.getComment());
            stm.setDouble(4, valoration.getValoration());
            stm.setDate(5, valoration.getCreateTime());
            stm.executeUpdate();

            ResultSet result = stm.getGeneratedKeys();
            if (result.next()) {
                int pk = result.getInt(1);
                valoration.setId(pk);
            }
            stm.close();

            return valoration;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }

    public List<ValorationDao> findValorationById(Connection con, int id, int limit) {
        List<ValorationDao> valorationDaoList = new ArrayList<>();

        try(PreparedStatement stm = con.prepareStatement("SELECT v.*, u.name FROM valoration v INNER JOIN user u ON u.id = v.id_user  WHERE id_recipe = ? AND comment is not null ORDER BY create_time DESC LIMIT ?")){
            stm.setInt(1, id);
            stm.setInt(2, limit);
            ResultSet result = stm.executeQuery();

            while(result.next()){
                ValorationDao valorationDao = new ValorationDao(result);
                valorationDaoList.add(valorationDao);
            }
        }catch (SQLException e){
            e.printStackTrace();
        }
        return valorationDaoList;
    }
}
