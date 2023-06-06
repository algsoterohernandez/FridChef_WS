package com.fpdual.service;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.manager.AllergenManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio para gestionar los alérgenos.
 */
public class AllergenService {
    private final MySQLConnector connector;
    private final AllergenManager allergenManager;

    /**
     * Constructor de AllergenService.
     *
     * @param connector       Conector MySQL utilizado para la conexión a la base de datos.
     * @param allergenManager Manager de alérgenos utilizado para realizar las operaciones de base de datos.
     */
    public AllergenService(MySQLConnector connector, AllergenManager allergenManager) {
        this.allergenManager = allergenManager;
        this.connector = connector;
    }

    /**
     * Recupera todos los alérgenos existentes.
     *
     * @return Lista de objetos AllergenDto que representan los alérgenos.
     */
    public List<AllergenDto> findAllAllergens() {
        List<AllergenDto> allergenDtoList = null;

        try (Connection con = connector.getMySQLConnection()) {

            List<AllergenDao> allergenDaos = allergenManager.findAllAllergens(con);

            if (allergenDaos != null) {
                allergenDtoList = MappingUtils.mapAllergenListDto(allergenDaos);
            }
        } catch (SQLException | ClassNotFoundException e) {
            return allergenDtoList;
        }

        return allergenDtoList;
    }
}