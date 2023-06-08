package com.fpdual.service;

import com.fpdual.api.dto.ValorationDto;
import com.fpdual.persistence.aplication.connector.MySQLConnector;
import com.fpdual.persistence.aplication.dao.ValorationDao;
import com.fpdual.persistence.aplication.manager.ValorationManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class ValorationServiceTest {
    @InjectMocks
    private ValorationService valorationService;

    @Mock
    private MySQLConnector mySQLConnector;
    @Mock
    private ValorationManager valorationManager;

    private ValorationDto exampleValorationDto;
    private ValorationDao exampleValorationDao;
    private List<ValorationDao> exampleValorationDaoList;

    @BeforeEach
    public void init() {
        valorationService = new ValorationService(mySQLConnector, valorationManager);

        exampleValorationDto = new ValorationDto();
        exampleValorationDto.setId(1);
        exampleValorationDto.setIdUser(2);
        exampleValorationDto.setIdRecipe(3);
        exampleValorationDto.setValoration(4);

        exampleValorationDao = new ValorationDao();
        exampleValorationDao.setId(1);
        exampleValorationDto.setIdUser(2);
        exampleValorationDto.setIdRecipe(3);
        exampleValorationDto.setValoration(4);

        exampleValorationDaoList = new ArrayList<>();
        exampleValorationDaoList.add(exampleValorationDao);
    }

    @Test
    public void testCreateValoration_validValorationDto_valorationDtoNotNull() throws Exception {
        // Prepare method dependencies
        when(valorationManager.createValoration(any(), any())).thenReturn(exampleValorationDao);
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        // Execute method
        ValorationDto valorationDtoRs = valorationService.createValoration(exampleValorationDto);

        // Asserts
        assertNotNull(valorationDtoRs);
    }

    @Test
    public void testCreateValoration_validValorationDto_valorationException() throws Exception {
        // Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(ClassNotFoundException.class);

        // Asserts
        assertThrows(ClassNotFoundException.class, () -> valorationService.createValoration(exampleValorationDto));
    }

    @Test
    public void testFindValorations_validIdLimit_valorationDtoListNotEmpty() throws Exception {
        // Prepare method dependencies
        when(valorationManager.findValorationById(any(), anyInt(), anyInt())).thenReturn(exampleValorationDaoList);
        when(mySQLConnector.getMySQLConnection()).thenReturn(null);

        // Execute method
        List<ValorationDto> valorationDtoListRs = valorationService.findValorations(1, 10);

        // Asserts
        assertNotNull(valorationDtoListRs);
        assertFalse(valorationDtoListRs.isEmpty());
    }

    @Test
    public void testFindValorations_validIdLimit_valorationException() throws Exception {
        // Prepare method dependencies
        when(mySQLConnector.getMySQLConnection()).thenThrow(ClassNotFoundException.class);

        // Asserts
        assertThrows(ClassNotFoundException.class, () -> valorationService.findValorations(1, 10));
    }
}
