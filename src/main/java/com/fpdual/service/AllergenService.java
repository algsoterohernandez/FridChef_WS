package com.fpdual.service;

import com.fpdual.api.dto.AllergenDto;
import com.fpdual.persistence.aplication.dao.AllergenDao;
import com.fpdual.persistence.aplication.manager.AllergenManager;
import com.fpdual.utils.MappingUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class AllergenService {

    private final AllergenManager allergenManager;

    public AllergenService() {
        allergenManager = new AllergenManager();
    }

    public List<AllergenDto> findAllAllergens() {
        List<AllergenDto> allergenDtoList = null;

        List<AllergenDao> allergenDaos = allergenManager.findAllAllergens();

        if (allergenDaos != null) {
            allergenDtoList =  MappingUtils.mapAllergenDto(allergenDaos);
        }

        return allergenDtoList;
    }

}
