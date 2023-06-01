package com.fpdual.persistence.aplication.manager;

import com.fpdual.api.dto.CategoryDto;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Clase que gestiona las operaciones relacionadas con las categorías.
 */
public class CategoryManager {

    /**
     * Busca una categoría por su ID.
     *
     * @param con La conexión a la base de datos.
     * @param id El ID de la categoría a buscar.
     * @return La categoría encontrada, o null si no se encontró ninguna.
     */
    public CategoryDto findCategoryById(Connection con, int id) {
        try (PreparedStatement stm = con.prepareStatement("SELECT * FROM category WHERE id = ? ORDER BY name ASC")) {
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();

            if (result.next()) {
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(result.getInt("id"));
                categoryDto.setName(result.getString("name"));
                return categoryDto;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Crea una nueva categoría.
     *
     * @param con La conexión a la base de datos.
     * @param categoryDto El objeto CategoryDto que contiene los datos de la categoría a crear.
     * @return La categoría creada, o null si no se pudo crear.
     */
    public CategoryDto createCategory(Connection con, CategoryDto categoryDto){
        try (PreparedStatement stm = con.prepareStatement("INSERT INTO category (id, name) VALUES (?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stm.setInt(1, categoryDto.getId());
            stm.setString(2, categoryDto.getName());
            stm.executeUpdate();

            ResultSet result = stm.getGeneratedKeys();

            if (result.next()) {
                categoryDto.setId(result.getInt(1));
                return categoryDto;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Actualiza una categoría existente.
     *
     * @param con La conexión a la base de datos.
     * @param id El ID de la categoría a actualizar.
     * @param categoryDto El objeto CategoryDto que contiene los datos actualizados de la categoría.
     * @return La categoría actualizada, o null si no se pudo actualizar.
     */
    public CategoryDto updateCategory(Connection con, int id, CategoryDto categoryDto) {
        try (PreparedStatement stm = con.prepareStatement("UPDATE category SET name = ? WHERE id = ?")) {
            stm.setString(1, categoryDto.getName());
            stm.setInt(2, id);
            int result = stm.executeUpdate();

            if (result > 0) {
                return categoryDto;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return null;
    }

    /**
     * Elimina una categoría.
     *
     * @param con La conexión a la base de datos.
     * @param id El ID de la categoría a eliminar.
     * @return true si la categoría se eliminó correctamente, false de lo contrario.
     */
    public boolean deleteCategory(Connection con, int id) {
        try (PreparedStatement stm = con.prepareStatement("DELETE FROM category WHERE id = ?")) {
            stm.setInt(1, id);
            int result = stm.executeUpdate();
            if (result > 0) {
                return true;
            }
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return false;
    }

    /**
     * Obtiene todas las categorías.
     *
     * @param con La conexión a la base de datos.
     * @return Una lista de CategoryDto que contiene todas las categorías, o null si ocurrió un error.
     */
    public List<CategoryDto> findAllCategories(Connection con) {
        try (PreparedStatement stm = con.prepareStatement("SELECT * FROM category")) {
            ResultSet result = stm.executeQuery();

            List<CategoryDto> categories = new ArrayList<>();

            while (result.next()) {
                Integer id = result.getInt("id");
                String name = result.getString("name");
                CategoryDto category = new CategoryDto(id, name);
                categories.add(category);
            }
            return categories;
        } catch (SQLException e) {
            System.out.println(e.getMessage());
            return null;
        }
    }
}

