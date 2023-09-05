package com.example.CloudStorage.service;

import com.example.CloudStorage.DTO.AuthorizationRequestDTO;
import com.example.CloudStorage.entity.File;
import com.example.CloudStorage.exception.AuthorizationError;
import com.example.CloudStorage.exception.CloudException;
import com.example.CloudStorage.repository.CloudRepository;
import com.example.CloudStorage.security.JwtUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;


@Service
public class CloudService {
    private final CloudRepository cloudRepository;

    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    JwtUtils jwtUtils;

    public CloudService(CloudRepository cloudRepository) {
        this.cloudRepository = cloudRepository;
    }

    public String login(AuthorizationRequestDTO authorization) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorization.getLogin(), authorization.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userPrincipal);
            cloudRepository.login(jwt, userPrincipal);
            return jwt;
        } catch (BadCredentialsException e) {
            throw new AuthorizationError("Bad credentials");
        }
    }

    public void logout(String authToken) {
        cloudRepository.logout(authToken).orElseThrow(() -> new AuthorizationError("Authorization error occurred: couldn't remove JWT"));
    }

    public File uploadFile(String authToken, String fileName, MultipartFile file) throws IOException {
        File filePOJO = new File(fileName, file.getContentType(), file.getBytes(), file.getSize());
        File savedFile = cloudRepository.uploadFile(filePOJO, authToken).orElseThrow(() -> new CloudException("Couldn't save the file " + fileName));
        return savedFile;
    }

    public List<File> getFiles(String authToken, int limit) {
        List<File> files = cloudRepository.getFiles(authToken, limit).orElseThrow(() -> new CloudException("Couldn't get list of files"));
        return files;
    }
}
