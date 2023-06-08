package com.fpdual.persistence.aplication.manager;

import com.fpdual.api.dto.CategoryDto;
import com.fpdual.exceptions.AlreadyExistsException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class CategoryManagerTest {
    @InjectMocks
    private CategoryManager categoryManager;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stm;
    @Mock
    private ResultSet resultSetMock;
    private CategoryDto exampleCategoryDto;

    @BeforeEach
    public void init() {
        categoryManager = new CategoryManager();

        exampleCategoryDto = new CategoryDto();
        exampleCategoryDto.setId(1);
        exampleCategoryDto.setName("Example Category");
    }

    @Test
    public void testFindCategoryById_validConnectionAndId_categoryDtoNotNull() throws SQLException {
        //Prepare method dependencies
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt("id")).thenReturn(1);
        when(resultSetMock.getString("name")).thenReturn("Example Category");

        when(stm.executeQuery()).thenReturn(resultSetMock);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        CategoryDto categoryDtoRs = categoryManager.findCategoryById(con, 1);

        //Asserts
        assertNotNull(categoryDtoRs);
        assertEquals(1, categoryDtoRs.getId());
        assertEquals("Example Category", categoryDtoRs.getName());
    }

    @Test
    public void testFindCategoryById_validConnectionAndId_categoryDtoNull() throws SQLException {
        //Prepare method dependencies
        when(stm.executeQuery()).thenReturn(resultSetMock);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        CategoryDto categoryDtoRs = categoryManager.findCategoryById(con, 1);

        //Asserts
        assertNull(categoryDtoRs);
    }

    @Test
    public void testCreateCategory_validConnectionAndCategoryDto_categoryDtoNotNull() throws SQLException {
        //Prepare method dependencies
        when(resultSetMock.next()).thenReturn(true);
        when(resultSetMock.getInt(1)).thenReturn(1);

        when(stm.getGeneratedKeys()).thenReturn(resultSetMock);
        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString(), anyInt())).thenReturn(stm);

        //Execute method
        CategoryDto categoryDtoRs = categoryManager.createCategory(con, exampleCategoryDto);

        //Asserts
        assertNotNull(categoryDtoRs);
        assertEquals(1, categoryDtoRs.getId());
        assertEquals("Example Category", categoryDtoRs.getName());
    }

    @Test
    public void testUpdateCategory_validConnectionIdAndCategoryDto_categoryDtoNotNull() throws SQLException {
        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        CategoryDto categoryDtoRs = categoryManager.updateCategory(con, 1, exampleCategoryDto);

        //Asserts
        assertNotNull(categoryDtoRs);
        assertEquals(1, categoryDtoRs.getId());
        assertEquals("Example Category", categoryDtoRs.getName());
    }

    @Test
    public void testUpdateCategory_validConnectionIdAndCategoryDto_categoryDtoNull() throws SQLException {
        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(0);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        CategoryDto categoryDtoRs = categoryManager.updateCategory(con, 1, exampleCategoryDto);

        //Asserts
        assertNull(categoryDtoRs);
    }

    @Test
    public void testDeleteCategory_validConnectionAndId_true() throws SQLException {
        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        boolean deleted = categoryManager.deleteCategory(con, 1);

        //Asserts
        assertTrue(deleted);
    }

    @Test
    public void testDeleteCategory_validConnectionAndId_false() throws SQLException {
        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(0);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        boolean deleted = categoryManager.deleteCategory(con, 1);

        //Asserts
        assertFalse(deleted);
    }

    @Test
    public void testFindAllCategories_validConnection_categoryListNotEmpty() throws SQLException {
        //Prepare method dependencies
        when(resultSetMock.next()).thenReturn(true).thenReturn(false);
        when(resultSetMock.getInt("id")).thenReturn(1);
        when(resultSetMock.getString("name")).thenReturn("Example Category");

        when(stm.executeQuery()).thenReturn(resultSetMock);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        List<CategoryDto> categories = categoryManager.findAllCategories(con);

        //Asserts
        assertNotNull(categories);
        assertFalse(categories.isEmpty());
        assertEquals(1, categories.size());

        CategoryDto categoryDto = categories.get(0);
        assertEquals(1, categoryDto.getId());
        assertEquals("Example Category", categoryDto.getName());
    }

    @Test
    public void testFindAllCategories_validConnection_categoryListEmpty() throws SQLException {
        //Prepare method dependencies
        when(stm.executeQuery()).thenReturn(resultSetMock);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        List<CategoryDto> categories = categoryManager.findAllCategories(con);

        //Asserts
        assertNotNull(categories);
        assertTrue(categories.isEmpty());
    }
}
