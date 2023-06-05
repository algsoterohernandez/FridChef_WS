package com.fpdual.service;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.manager.AllergenManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

public class AllergenService {
    private final MySQLConnector connector;
    private final AllergenManager allergenManager;

    public AllergenService(MySQLConnector connector, AllergenManager allergenManager) {
        this.allergenManager = allergenManager;
        this.connector = connector;
    }

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