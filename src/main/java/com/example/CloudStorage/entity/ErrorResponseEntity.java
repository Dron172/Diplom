package com.example.CloudStorage.entity;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponseEntity {
    @Getter
    @Setter
    private String message;
    @Getter
    @Setter
    private int id;
}
