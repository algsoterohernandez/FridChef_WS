package com.fpdual.persistence.aplication.manager;

import com.fpdual.api.dto.RolUserDto;
import com.fpdual.exceptions.FridChefException;
import com.fpdual.persistence.aplication.dao.RolUserDao;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RolManagerTest {
    @InjectMocks
    private RolManager rolManager;
    @Mock
    private Connection con;
    @Mock
    private PreparedStatement stm;
    @Mock
    private ResultSet resultSet;
    private RolUserDto exampleRolUserDto;
    private RolUserDao exampleRolUserDao;
    private List<RolUserDao> rolUserDaoList;

    @BeforeEach
    public void init() {
        rolManager = new RolManager();

        exampleRolUserDto = new RolUserDto();
        exampleRolUserDto.setIdUser(1);
        exampleRolUserDto.setIdUser(1);

        exampleRolUserDao = new RolUserDao();
        exampleRolUserDao.setIdUser(2);
        exampleRolUserDao.setIdRol(1);

        rolUserDaoList = new ArrayList<>();
        rolUserDaoList.add(exampleRolUserDao);

    }

    @Test
    public void testInsertRol_validConnectionAndUserDto_true() throws FridChefException, SQLException {

        //Prepare method dependencies
        when(stm.executeUpdate()).thenReturn(1);

        when(con.prepareStatement(anyString(), anyInt())).thenReturn(stm);

        //Execute method
        boolean insertRolOk = rolManager.insertRol(con, exampleRolUserDto.getIdUser());

        //Asserts
        assertTrue(insertRolOk);
    }

    @Test
    public void testInsertRol_validConnectionAndUserDto_rolUserSQLIntegrityConstraintViolationException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString(), anyInt())).thenThrow(SQLIntegrityConstraintViolationException.class);

        //Asserts
        assertThrows(FridChefException.class, () -> rolManager.insertRol(con, exampleRolUserDto.getIdUser()));
    }
    @Test
    public void testInsertRol_validConnectionAndUserDto_rolUserSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString(), anyInt())).thenThrow(SQLException.class);

        //Asserts
        assertThrows(SQLException.class, () -> rolManager.insertRol(con, exampleRolUserDto.getIdUser()));
    }

    @Test
    public void testFindRolesById_validConnectionAndIdUser_listRolUserDao() throws SQLException {

        //Prepare method dependencies
        when(resultSet.next()).thenReturn(true,false);

        when(stm.executeQuery()).thenReturn(resultSet);

        when(con.prepareStatement(anyString())).thenReturn(stm);

        //Execute method
        rolUserDaoList = rolManager.findRolesById(con, exampleRolUserDao.getIdUser());

        //Asserts
        assertNotNull(rolUserDaoList);

    }

    @Test
    public void testFindRolesById_validConnectionAndIdUser_rolUserSQLException() throws SQLException {

        //Prepare method dependencies
        when(con.prepareStatement(anyString())).thenThrow(SQLException.class);

        //Asserts
        assertNull(rolManager.findRolesById(con, exampleRolUserDao.getIdUser()));
    }
}