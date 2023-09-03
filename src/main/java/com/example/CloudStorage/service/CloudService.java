package com.example.CloudStorage.service;

import com.example.CloudStorage.entity.AuthorizationRequestEntity;
import com.example.CloudStorage.exception.AuthorizationError;
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

//    public String login(AuthorizationRequestEntity authorization){
//        return cloudRepository.login(authorization.getLogin(),authorization.getPassword()).orElseThrow(() -> new AuthorizationError("Bad credentials"));
//    }

    public String login(AuthorizationRequestEntity authorization) {
        try {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authorization.getLogin(), authorization.getPassword()));
            SecurityContextHolder.getContext().setAuthentication(authentication);
            UserDetails userPrincipal = (UserDetails) authentication.getPrincipal();
            String jwt = jwtUtils.generateJwtToken(userPrincipal);

//        List<String> roles = userPrincipal.getAuthorities().stream()
//                .map(GrantedAuthority::getAuthority)
//                .collect(Collectors.toList());

            return jwt;
        } catch (BadCredentialsException e) {
            throw new AuthorizationError("Bad credentials");
        }
    }


    public void logout(String authToken) {
        cloudRepository.logout(authToken);
    }
}
