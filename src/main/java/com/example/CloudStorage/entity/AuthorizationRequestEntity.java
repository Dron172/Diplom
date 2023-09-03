package com.example.CloudStorage.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
public class AuthorizationRequestEntity {
    @Getter
    @Setter
    @NotBlank(message = "login couldn't be blank")
    @Size(min = 2, max = 15)
    private String login;
    @Getter
    @Setter
    @NotBlank(message = "login couldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-ZА-Я]).{8,}$")
    private String password;
}
