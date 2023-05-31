package com.fpdual.fridchefapi.service;

import com.fpdual.fridchefapi.api.dto.ValorationDto;
import com.fpdual.fridchefapi.persistence.aplication.connector.MySQLConnector;
import com.fpdual.fridchefapi.persistence.aplication.dao.ValorationDao;
import com.fpdual.fridchefapi.persistence.aplication.manager.ValorationManager;
import com.fpdual.fridchefapi.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class ValorationService{
    private final MySQLConnector connector;
    private final ValorationManager valorationManager;

    public ValorationService(MySQLConnector connector, ValorationManager valorationManager) {
        this.connector = connector;
        this.valorationManager = valorationManager;
    }

    public ValorationDto createValoration(ValorationDto valorationDto) throws SQLException, ClassNotFoundException {

        try (Connection con = connector.getMySQLConnection()) {

            ValorationDao valorationDao = this.valorationManager.createValoration(con, MappingUtils.mapValorationToDao(valorationDto));
            valorationDto = MappingUtils.mapValorationToDto(valorationDao);

        } catch (Exception e) {
            throw e;
        }

        return valorationDto;
    }
    }
