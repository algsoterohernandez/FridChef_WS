package com.fpdual.persistence.aplication.manager;
import com.fpdual.persistence.aplication.dao.ValorationDao;

import java.sql.*;

/**
 * Clase encargada de administrar la creación de valoraciones en la base de datos.
 */
public class ValorationManager {

    /**
     * Crea una nueva valoración en la base de datos.
     *
     * @param con        Conexión a la base de datos.
     * @param valoration Objeto ValorationDao que contiene los datos de la valoración a crear.
     * @return El objeto ValorationDao con el identificador de la valoración asignado, o null si ocurre un error.
     * @throws SQLException Si ocurre un error en la ejecución de la consulta SQL.
     */
    public ValorationDao createValoration(Connection con, ValorationDao valoration) throws SQLException {
        try (PreparedStatement stm = con.prepareStatement("INSERT INTO valoration (id_recipe, id_user, comment, valoration) VALUES (?,?,?,?)", Statement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, valoration.getIdRecipe());
            stm.setInt(2, valoration.getIdUser());
            stm.setString(3, valoration.getComment());
            stm.setDouble(4, valoration.getValoration());
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
}