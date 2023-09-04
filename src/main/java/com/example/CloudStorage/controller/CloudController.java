package com.example.CloudStorage.controller;

import com.example.CloudStorage.entity.AuthorizationRequestEntity;
import com.example.CloudStorage.entity.AuthorizationToken;
import com.example.CloudStorage.entity.ErrorResponseEntity;
import com.example.CloudStorage.exception.AuthorizationError;
import com.example.CloudStorage.service.CloudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/")
@Validated
public class CloudController {

    private final CloudService cloudService;

    public CloudController(CloudService cloudService) {
        this.cloudService = cloudService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorizationToken> login(@Valid @RequestBody AuthorizationRequestEntity authorization) {
        String authToken = cloudService.login(authorization);
        return authToken != null ? new ResponseEntity<>(new AuthorizationToken(authToken), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/logout")
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        cloudService.logout(authToken);
        return HttpStatus.OK;
    }

    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename, @RequestBody MultipartFile file) {
        //cloudService.uploadFile(authToken, filename, file);


        return new ResponseEntity<>("Hello", HttpStatus.OK);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponseEntity> handleValidationExc(MethodArgumentNotValidException exc) {
        return handleRunTimeExc(new AuthorizationError(exc.getLocalizedMessage()));
    }

    @ExceptionHandler(AuthorizationError.class)
    ResponseEntity<ErrorResponseEntity> handleRunTimeExc(AuthorizationError exc) {
        return new ResponseEntity<>(new ErrorResponseEntity(exc.getLocalizedMessage(), exc.getId()), HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }
}
