package com.fpdual.service;


import com.fpdual.api.dto.CategoryDto;
import com.fpdual.persistence.aplication.manager.CategoryManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.sql.Connection;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    @InjectMocks
    private CategoryService categoryService;

    @Mock
    private Connection connector;

    @Mock
    private CategoryManager categoryManager;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }


    @Test
    void testCreateCategory_validCategoryDto_categoryDtoNotNull() {
        // Datos de ejemplo
        CategoryDto categoryDto = new CategoryDto();
        categoryDto.setName("Category 1");

        // Comportamiento esperado de la dependencia simulada
        when(categoryManager.createCategory(connector, categoryDto))
                .thenReturn(categoryDto);

        // Llamada al método que queremos probar
        CategoryDto createdCategoryDto = categoryService.createCategory(categoryDto);

        // Verificar el resultado
        assertNotNull(createdCategoryDto);
        assertEquals(categoryDto, createdCategoryDto);
    }
}
