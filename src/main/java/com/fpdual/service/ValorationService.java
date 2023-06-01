package com.fpdual.service;


import com.fpdual.api.dto.ValorationDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.ValorationDao;
import com.fpdual.persistence.aplication.manager.ValorationManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;


/**
 * Servicio para gestionar valoraciones.
 */
public class ValorationService {
    private final MySQLConnector connector;
    private final ValorationManager valorationManager;

    /**
     * Constructor de ValorationService.
     *
     * @param connector         Conector de MySQL utilizado para la conexión a la base de datos.
     * @param valorationManager Gestor de valoraciones utilizado para realizar operaciones de CRUD.
     */
    public ValorationService(MySQLConnector connector, ValorationManager valorationManager) {
        this.connector = connector;
        this.valorationManager = valorationManager;
    }

    /**
     * Crea una nueva valoración en la base de datos.
     *
     * @param valorationDto Objeto ValorationDto que contiene los datos de la valoración a crear.
     * @return Objeto ValorationDto que representa la valoración creada.
     * @throws SQLException Si ocurre un error al interactuar con la base de datos.
     * @throws ClassNotFoundException Si no se encuentra el driver de la base de datos.
     */
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