package com.fpdual.api.dto;

import com.fpdual.persistence.aplication.dao.AllergenDao;
import lombok.*;

import java.util.List;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
@ToString
@Builder
public class IngredientDto {

    private int id;
    private String name;
    private List<AllergenDao> allergens;


}
