package com.example.CloudStorage.controller;

import com.example.CloudStorage.DTO.AuthorizationRequestDTO;
import com.example.CloudStorage.DTO.AuthorizationTokenDTO;
import com.example.CloudStorage.DTO.ErrorResponseDTO;
import com.example.CloudStorage.entity.File;
import com.example.CloudStorage.exception.AuthorizationError;
import com.example.CloudStorage.exception.CloudException;
import com.example.CloudStorage.service.CloudService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/")
@Validated
public class CloudController {

    private final CloudService cloudService;

    public CloudController(CloudService cloudService) {
        this.cloudService = cloudService;
    }

    @PostMapping(value = "/login", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<AuthorizationTokenDTO> login(@Valid @RequestBody AuthorizationRequestDTO authorization) {
        String authToken = cloudService.login(authorization);
        return authToken != null ? new ResponseEntity<>(new AuthorizationTokenDTO(authToken), HttpStatus.OK) : new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
    }

    @PostMapping(value = "/logout")
    public HttpStatus logout(@RequestHeader("auth-token") String authToken) {
        cloudService.logout(authToken);
        return HttpStatus.OK;
    }

    @PostMapping(value = "/file", produces = MediaType.APPLICATION_JSON_VALUE, consumes = {"multipart/form-data"})
    public ResponseEntity<?> uploadFile(@RequestHeader("auth-token") String authToken, @Valid @RequestParam String filename, @RequestBody MultipartFile file) throws IOException {
        cloudService.uploadFile(authToken, filename, file);
        return new ResponseEntity<>(HttpStatus.OK);
    }

    @GetMapping("/list")
    public List<File> getFiles(@RequestHeader("auth-token") String authToken, @RequestParam("limit") int limit) {
        return cloudService.getFiles(authToken, limit);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    ResponseEntity<ErrorResponseDTO> handleValidationExc(MethodArgumentNotValidException exc) {
        return handleRunTimeExc(new AuthorizationError(exc.getLocalizedMessage()));
    }

    @ExceptionHandler(AuthorizationError.class)
    ResponseEntity<ErrorResponseDTO> handleRunTimeExc(AuthorizationError exc) {
        return new ResponseEntity<>(new ErrorResponseDTO(exc.getLocalizedMessage(), exc.getId()), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(CloudException.class)
    ResponseEntity<ErrorResponseDTO> handleRunTimeExc(CloudException exc) {
        return new ResponseEntity<>(new ErrorResponseDTO(exc.getLocalizedMessage(), exc.getId()), HttpStatus.BAD_REQUEST);
    }
//    @ExceptionHandler(StorageFileNotFoundException.class)
//    public ResponseEntity<?> handleStorageFileNotFound(StorageFileNotFoundException exc) {
//        return ResponseEntity.notFound().build();
//    }

//    @ExceptionHandler(MaxUploadSizeExceededException.class)
//    public ResponseEntity<ResponseMessage> handleMaxSizeException(MaxUploadSizeExceededException exc) {
//        return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage("File too large!"));
//    }
}
