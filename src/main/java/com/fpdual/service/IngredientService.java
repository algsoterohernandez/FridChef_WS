package com.fpdual.service;

import com.fpdual.api.dto.IngredientDto;
import com.fpdual.exceptions.AlreadyExistsException;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.IngredientDao;
import com.fpdual.persistence.aplication.manager.IngredientManager;
import com.fpdual.utils.MappingUtils;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Servicio que proporciona funcionalidades relacionadas con los ingredientes.
 */
public class IngredientService {

    private final MySQLConnector connector;
    private final IngredientManager ingredientManager;

    /**
     * Constructor de IngredientService.
     *
     * @param connector        Conector MySQL utilizado para la conexión a la base de datos.
     * @param ingredientManager Manager de ingredientes utilizado para realizar las operaciones en la base de datos.
     */
    public IngredientService(MySQLConnector connector, IngredientManager ingredientManager) {
        this.ingredientManager = ingredientManager;
        this.connector = connector;
    }

    /**
     * Obtiene todos los ingredientes.
     *
     * @return Lista de objetos IngredientDto que representan los ingredientes encontrados, o null si no se encontraron ingredientes.
     */
    public List<IngredientDto> findAll() {
        List<IngredientDto> ingredientDtos = null;

        try (Connection con = connector.getMySQLConnection()) {
            List<IngredientDao> ingredientDaos = ingredientManager.findAll(con);

            if (ingredientDaos != null) {
                ingredientDtos = MappingUtils.mapIngredientListToDto(ingredientDaos);
            }
        } catch (SQLException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        return ingredientDtos;
    }

    /**
     * Elimina un ingrediente por su ID.
     *
     * @param id ID del ingrediente a eliminar.
     * @return true si se eliminó el ingrediente correctamente, false en caso contrario.
     * @throws SQLException           Si ocurre un error de SQL al eliminar el ingrediente.
     * @throws ClassNotFoundException Si no se encuentra la clase del controlador de la base de datos.
     */
    public boolean deleteIngredient(int id) throws SQLException, ClassNotFoundException {
        boolean deleted;

        try (Connection con = connector.getMySQLConnection()) {
            // Borramos el ingrediente
            deleted = this.ingredientManager.deleteIngredient(con, id);
        } catch (Exception e) {
            System.out.println(e.getMessage());
            throw e;
        }

        return deleted;
    }

    /**
     * Crea un nuevo ingrediente con el nombre especificado.
     *
     * @param name Nombre del ingrediente a crear.
     * @return Objeto IngredientDto que representa el ingrediente creado, o null si no se pudo crear el ingrediente.
     * @throws SQLException           Si ocurre un error de SQL al insertar el ingrediente.
     * @throws ClassNotFoundException Si no se encuentra la clase del controlador de la base de datos.
     * @throws AlreadyExistsException  Si ya existe un ingrediente con el mismo nombre.
     */
    public IngredientDto createIngredient(String name) throws SQLException, ClassNotFoundException, AlreadyExistsException {
        IngredientDto ingredientDto = null;

        List<IngredientDto> ingredients = findAll();

        try {
            // Si no hay un ingrediente con el mismo nombre, lo creamos
            if (!ingredients.stream().anyMatch(o -> o.getName().equals(name))) {
                try (Connection con = connector.getMySQLConnection()) {
                    IngredientDao ingredientDao = this.ingredientManager.insertIngredient(con, name);
                    if (ingredientDao != null) {
                        ingredientDto = MappingUtils.mapIngredientToDto(ingredientDao);
                    }
                } catch (Exception e) {
                    System.out.println(e.getMessage());
                    throw e;
                }
            } else {
                throw new AlreadyExistsException("El ingrediente ya existe.");
            }
        } catch (NullPointerException npe) {
            throw npe;
        }

        return ingredientDto;
    }
}