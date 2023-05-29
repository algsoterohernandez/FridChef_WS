package com.fpdual.service;

import com.fpdual.api.dto.ValorationDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.RecipeDao;
import com.fpdual.persistence.aplication.dao.ValorationDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.persistence.aplication.manager.RecipeManager;
import com.fpdual.persistence.aplication.manager.ValorationManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;

public class ValorationService{
    private final MySQLConnector connector;
    private final ValorationManager valorationManager;

    public ValorationService(MySQLConnector connector, ValorationManager valorationManager) {
        this.connector = connector;
        this.valorationManager = valorationManager;
    }

    //TODO: añadir CRUD de valoración
    }
