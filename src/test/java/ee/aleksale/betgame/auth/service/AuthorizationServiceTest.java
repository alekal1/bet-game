package ee.aleksale.betgame.auth.service;

import ee.aleksale.betgame.auth.exception.GameAuthorizationException;
import ee.aleksale.betgame.auth.model.api.AuthRequest;
import ee.aleksale.betgame.auth.model.domain.PlayerEntity;
import ee.aleksale.betgame.auth.service.validator.AuthorizationValidationService;
import ee.aleksale.betgame.common.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthorizationServiceTest {

    private static final String USERNAME = "AnyUsername";

    private PlayerRepository playerRepository;
    private AuthorizationValidationService authorizationValidationService;

    private AuthorizationService authorizationService;

    @BeforeEach
    void init() {
        playerRepository = mock(PlayerRepository.class);
        authorizationValidationService = mock(AuthorizationValidationService.class);
        authorizationService = new AuthorizationService(
                playerRepository,
                authorizationValidationService
        );
    }

    @Test
    void fetchToken_userAlreadyExists() {
        var request = getAuthRequest();

        doThrow(GameAuthorizationException.class)
                .when(authorizationValidationService)
                .validateUserDoesNotExist(request.getUsername());

        assertThrows(GameAuthorizationException.class, () ->
                authorizationService.fetchToken(request));
    }

    @Test
    void fetchToken_newUser() {
        var request = getAuthRequest();
        var savedPlayerEntity = getPlayerEntity();

        doReturn(savedPlayerEntity)
                .when(playerRepository)
                .saveAndFlush(any());

        var token = authorizationService.fetchToken(request);

        assertNotNull(token);
    }

    private AuthRequest getAuthRequest() {
        var request = new AuthRequest();
        request.setUsername(USERNAME);
        return request;
    }

    private PlayerEntity getPlayerEntity() {
        var entity = new PlayerEntity();
        entity.setUsername(USERNAME);
        entity.setTotalAmount(50.0);
        entity.setId(1L);
        return entity;
    }
}
