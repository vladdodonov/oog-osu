package com.dodonov.oogosu.dto;

import com.dodonov.oogosu.config.security.UserRole;
import com.dodonov.oogosu.domain.enums.Qualification;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
@ApiModel(value = "EmployeeSaveDto", description = "DTO сохранения сотрудника")
public class EmployeeSaveDto {
    @ApiModelProperty(value = "Идентификатор")
    private Long id;
    @ApiModelProperty(value = "Имя")
    private String firstName;
    @ApiModelProperty(value = "Фамилия")
    private String lastName;
    @ApiModelProperty(value = "Отчество")
    private String middleName;
    @ApiModelProperty(value = "Емейл")
    private String email;
    @ApiModelProperty(value = "Телефон")
    private String phone;
    @ApiModelProperty(value = "Департамент")
    private DepartmentDto department;
    @ApiModelProperty(value = "Квалификация")
    private Qualification qualification;
    @ApiModelProperty(value = "Логин")
    private String username;
    @ApiModelProperty(value = "Пароль")
    private String password;
    @ApiModelProperty(value = "Роль")
    private UserRole role;
}
