package com.example.CloudStorage.exception;

import lombok.Data;
import lombok.Getter;

@Data
public class CloudException extends RuntimeException {
    private static int errorCounter = 0;
    @Getter
    private int id;

    public CloudException(String msg) {
        super(msg);
        id = ++errorCounter;
    }

}
