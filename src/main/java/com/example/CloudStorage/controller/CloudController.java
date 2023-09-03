package com.example.CloudStorage.controller;


import com.example.CloudStorage.entity.AuthorizationRequestEntity;
import com.example.CloudStorage.entity.AuthorizationResponseEntity;
import com.example.CloudStorage.entity.ErrorResponseEntity;
import com.example.CloudStorage.exception.AuthorizationError;
import com.example.CloudStorage.repository.RoleRepository;
import com.example.CloudStorage.repository.UserRepository;
import com.example.CloudStorage.security.JwtUtils;
import com.example.CloudStorage.service.CloudService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;

@RestController
@RequestMapping("/cloud")
@Validated
public class CloudController {

    private final CloudService cloudService;

    public CloudController(CloudService cloudService) {
        this.cloudService = cloudService;
    }

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    RoleRepository roleRepository;

    @Autowired
    JwtUtils jwtUtils;


//    @ResponseBody
//    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
//    public ResponseEntity<AuthorizationResponseEntity> login(@Valid @RequestBody AuthorizationRequestEntity authorization) {
//        return new ResponseEntity<>(new AuthorizationResponseEntity(cloudService.login(authorization)), HttpStatus.OK);
//    }

    @ResponseBody
    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorizationResponseEntity> login(@Valid @RequestBody AuthorizationRequestEntity authorization) {
        return new ResponseEntity<>(new AuthorizationResponseEntity(cloudService.login(authorization)), HttpStatus.OK);
    }

    @ResponseBody
    @PostMapping(value = "/logout")
    public HttpStatus logout(@RequestHeader(name = "auth-token", required = true) String authToken) {
        cloudService.logout(authToken);
        return HttpStatus.OK;
    }

    @ResponseBody
    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestHeader(name = "auth-token", required = true) String authToken, @Valid @RequestParam String filename, @RequestBody MultipartFile file) {
        //cloudService.uploadFile(authToken, filename, file);


        return new ResponseEntity<>(HttpStatus.OK);
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
