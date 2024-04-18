package com.example.beerApp.utility;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataAccessException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimNames;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserUtil {
    @Value("${jwt.auth.converter.resource-id}")
    private String resourceId;

    public String getUserId() {
        Jwt jwtToken = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String userId = "";
        if (jwtToken.hasClaim(JwtClaimNames.SUB)) {
            userId = jwtToken.getClaim(JwtClaimNames.SUB);
        }
        return userId;
    }

    public String getUserName() {
        Jwt jwtToken = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        String username = "";
        if (jwtToken.hasClaim("preferred_username")) {
            username = jwtToken.getClaim("preferred_username");
        }
        return username;
    }

    public List<String> getRoles() {
        Jwt jwtToken = (Jwt) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        Map<String, Map<String, List<String>>> resourceAccess = jwtToken.getClaim("resource_access");
        if(resourceAccess != null && !resourceAccess.isEmpty()){
            List<String> roles = new ArrayList<>();
            resourceAccess.forEach((key, value) -> {
                if(key.equals(resourceId)){
                    roles.addAll(value.get("roles"));
                }
            });
            return roles;
        }
        throw new RuntimeException("Resource access claim not found");
    }
}
