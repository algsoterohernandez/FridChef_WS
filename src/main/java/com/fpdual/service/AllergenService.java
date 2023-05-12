package com.fpdual.service;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.manager.AllergenManager;

import java.util.ArrayList;
import java.util.List;

public class AllergenService {

    private final AllergenManager allergenManager;

    public AllergenService() {
        allergenManager = new AllergenManager();
    }

    public List<AllergenDto> findAllAllergens() {
        List<AllergenDto> allergenDtoList = null;

        List<AllergenDao> allergenDaos = allergenManager.findAllAllergens();

        if (allergenDaos != null) {
            allergenDtoList = mapToDto(allergenDaos);
        }

        return allergenDtoList;
    }

    private AllergenDto mapToDto(AllergenDao allergenDao) {
        AllergenDto allergenDto = new AllergenDto();

        allergenDto.setId(allergenDao.getId());
        allergenDto.setName(allergenDao.getName());


        return allergenDto;
    }

    private List<AllergenDto> mapToDto(List<AllergenDao> allergenDaos) {
        List<AllergenDto> allergenDtoList = new ArrayList<>();

        for (AllergenDao allergenDao : allergenDaos) {
            AllergenDto allergenDto = mapToDto(allergenDao);
            allergenDtoList.add(allergenDto);
        }

        return allergenDtoList;
    }
}
