package com.fpdual.service;

import com.fpdual.api.dto.ValorationDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.ValorationDao;
import com.fpdual.persistence.aplication.manager.ValorationManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

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

    public List<ValorationDto> findValorations(int id, int limit) throws SQLException, ClassNotFoundException {
        List<ValorationDto> valorationDtoList;
        try(Connection con = connector.getMySQLConnection()){
            List<ValorationDao> valorationDaoList = this.valorationManager.findValorationById(con, id, limit);
            valorationDtoList = MappingUtils.mapValorationListToDto(valorationDaoList);
        }catch (Exception e){
            throw e;
        }
        return valorationDtoList;
    }
}
