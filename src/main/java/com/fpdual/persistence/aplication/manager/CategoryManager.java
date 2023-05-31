package com.fpdual.persistence.aplication.manager;

import com.fpdual.api.dto.CategoryDto;
import com.fpdual.api.dto.RecipeDto;
import com.fpdual.exceptions.CategoryAlreadyExistsException;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class CategoryManager {
    public CategoryDto findCategoryById(Connection con, int id) {
        try(PreparedStatement stm = con.prepareStatement("SELECT * FROM category WHERE id= ? ORDER BY name ASC")){
            stm.setInt(1, id);
            ResultSet result = stm.executeQuery();

            if(result.next()){
                CategoryDto categoryDto = new CategoryDto();
                categoryDto.setId(result.getInt("id"));
                categoryDto.setName(result.getString("name"));
                return categoryDto;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
       return null;
    }

    public CategoryDto createCategory(Connection con, CategoryDto categoryDto) throws CategoryAlreadyExistsException {
        try(PreparedStatement stm = con.prepareStatement("INSERT INTO category (id, name) VALUES (?,?)", Statement.RETURN_GENERATED_KEYS)){
            stm.setInt(1, categoryDto.getId());
            stm.setString(2, categoryDto.getName());
            stm.executeUpdate();

            ResultSet result = stm.getGeneratedKeys();

            if(result.next()){
                categoryDto.setId(result.getInt(1));
                return categoryDto;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
    return null;
    }


    public CategoryDto updateCategory(Connection con,int id, CategoryDto categoryDto) {
        try(PreparedStatement stm = con.prepareStatement("UPDATE category SET name = ? WHERE id = ?")){
            stm.setString(1, categoryDto.getName());
            stm.setInt(2, id);
            int result = stm.executeUpdate();

            if(result > 0){
                return categoryDto;
            }
        }catch (SQLException e){
            System.out.println(e.getMessage());
        }
        return null;
    }

    public boolean deleteCategory(Connection con, int id) {
        try(PreparedStatement stm = con.prepareStatement("DELETE FROM category WHERE id= ?")){
            stm.setInt(1, id);
            int result = stm.executeUpdate();
            if(result>0){
                return true;
            }
        }catch(SQLException e){
            System.out.println(e.getMessage());
        }
        return  false;
    }


    public List<CategoryDto> findAllCategories(Connection con) {
        try(PreparedStatement stm = con.prepareStatement("SELECT * FROM category")){
            ResultSet result = stm.executeQuery();

            List<CategoryDto> categories = new ArrayList<>();

            while(result.next()){
                Integer id = result.getInt("id");
                String name = result.getString("name");
                CategoryDto category = new CategoryDto(id, name);
                categories.add(category);
            }
            return categories;
        }catch (SQLException e){
            System.out.println(e.getMessage());
            return null;
        }
    }
}
