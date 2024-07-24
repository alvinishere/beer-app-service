package com.example.beerApp.service;

import com.example.beerApp.dto.authUser.AuthUserResponse;
import com.example.beerApp.dto.authUser.LoginRequest;
import com.example.beerApp.dto.authUser.SignUpRequest;
import com.example.beerApp.entity.AuthUser;
import com.example.beerApp.exception.ResourceExistException;
import com.example.beerApp.repository.AuthUserRepository;
import jakarta.ws.rs.BadRequestException;
import jakarta.ws.rs.NotAuthorizedException;
import jakarta.ws.rs.core.Response;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.CreatedResponseUtil;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.admin.client.resource.UsersResource;
import org.keycloak.representations.AccessTokenResponse;
import org.keycloak.representations.idm.CredentialRepresentation;
import org.keycloak.representations.idm.UserRepresentation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.stereotype.Service;

import java.net.URI;
import java.util.*;

@RequiredArgsConstructor
@Service
public class AuthUserService {
    @Value("${keycloak.client.id}")
    private String clientId;
    @Value("${keycloak.client.secret}")
    private String clientSecret;
    @Value("${keycloak.realm}")
    private String realm;
    @Value("${keycloak.url}")
    private String serverUrl;

    private final Keycloak keycloakAdmin;
    private final AuthUserRepository authUserRepository;

    public AuthUserResponse loginUser(LoginRequest loginRequest) {
        try (Keycloak keycloak = buildKeycloakCredential(loginRequest)) {
            AccessTokenResponse token = keycloak.tokenManager().getAccessToken();
            return getAuthUserResponse(token);
        } catch (NotAuthorizedException ex) {
            throw new NotAuthorizedException(ex);
        } catch (BadRequestException ex) {
            throw new BadRequestException(ex);
        }
    }

    public AuthUserResponse signUpUser(SignUpRequest signUpRequest) {
        UsersResource usersResource = getUsersResource();
        CredentialRepresentation credentialRepresentation = getCredentialRepresentation(signUpRequest.password());
        UserRepresentation newUser = getUserRepresentation(signUpRequest, credentialRepresentation);
        try (Response response = usersResource.create(newUser)) {
            if (Objects.equals(201, response.getStatus())) {
                // TODO REFACTOR
                authUserRepository.save(AuthUser.builder()
                        .id(UUID.fromString(CreatedResponseUtil.getCreatedId(response)))
                        .build());
                return loginUser(new LoginRequest(signUpRequest.username(), signUpRequest.password()));
            } else {
                throw new ResourceExistException("Username: " + signUpRequest.username() + " already exists.");
            }
        } catch (ResourceExistException ex) {
            throw ex;
        } catch (Exception ex) {
            throw new RuntimeException(ex);
        }
    }

    public UsersResource getUsersResource() {
        return keycloakAdmin.realm(realm).users();
    }

    private UserRepresentation getUserRepresentation(SignUpRequest signUpRequest, CredentialRepresentation credentialRepresentation) {
        UserRepresentation user = new UserRepresentation();
        user.setEnabled(true);
        user.setUsername(signUpRequest.username());
        user.setEmail(signUpRequest.email());
        user.setEmailVerified(true);
        user.setFirstName(signUpRequest.firstName());
        user.setLastName(signUpRequest.lastName());
        user.setCredentials(Collections.singletonList(credentialRepresentation));
        return user;
    }

    private CredentialRepresentation getCredentialRepresentation(String password) {
        CredentialRepresentation credentialRepresentation = new CredentialRepresentation();
        credentialRepresentation.setTemporary(false);
        credentialRepresentation.setValue(password);
        credentialRepresentation.setType(CredentialRepresentation.PASSWORD);
        return credentialRepresentation;
    }

    private AuthUserResponse getAuthUserResponse(AccessTokenResponse token) {
        return AuthUserResponse.builder()
                .accessToken(token.getToken())
                .accessExpiresIn(token.getExpiresIn())
                .refreshToken(token.getRefreshToken())
                .refreshExpiresIn(token.getRefreshExpiresIn())
                .build();
    }


    private Keycloak buildKeycloakCredential(LoginRequest loginRequest) {
        return KeycloakBuilder.builder()
                .serverUrl(serverUrl)
                .realm(realm)
                .clientId(clientId)
                .clientSecret(clientSecret)
                .username(loginRequest.username())
                .password(loginRequest.password())
                .build();
    }
}
