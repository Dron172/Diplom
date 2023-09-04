package com.example.CloudStorage.entity;

import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizationRequestEntity {
//    @Getter
//    @Setter
    @NotBlank(message = "login couldn't be blank")
    @Size(min = 2, max = 30)
    private String login;
//    @Getter
//    @Setter
    @NotBlank(message = "login couldn't be blank")
    @Pattern(regexp = "^(?=.*\\d)(?=.*[A-ZА-Я]).{8,}$")
    //@Max(40)
    private String password;
}
