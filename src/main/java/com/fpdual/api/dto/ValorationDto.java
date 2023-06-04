package com.fpdual.api.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@NoArgsConstructor
public class ValorationDto {
    private int id;
    private int idRecipe;
    private int idUser;
    private String nameUser;
    private String comment;
    private double valoration;
    private Date createTime;
}
