package com.dodonov.oogosu.dto;

import io.swagger.annotations.ApiModelProperty;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CitizenDto {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;
    @ApiModelProperty(value = "Имя")
    private String firstName;
    @ApiModelProperty(value = "Фамилия")
    private String lastName;
    @ApiModelProperty(value = "Отчество")
    private String middleName;
    @ApiModelProperty(value = "Адрес")
    private String address;
    @ApiModelProperty(value = "Емейл")
    private String email;
    @ApiModelProperty(value = "Телефон")
    private String phone;
}
