package ee.aleksale.betgame.unit.auth.service.validator;

import ee.aleksale.betgame.auth.exception.GameAuthorizationException;
import ee.aleksale.betgame.auth.service.validator.AuthorizationValidationService;
import ee.aleksale.betgame.common.repository.PlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

@ExtendWith(MockitoExtension.class)
class AuthorizationValidationServiceTest {

    private static final String USERNAME = "AnyUsername";

    private PlayerRepository playerRepository;

    private AuthorizationValidationService authorizationValidationService;

    @BeforeEach
    void init() {
        playerRepository = mock(PlayerRepository.class);

        authorizationValidationService = new AuthorizationValidationService(
                playerRepository
        );
    }

    @Test
    void validateUserDoesNotExist() {
        doReturn(false)
                .when(playerRepository)
                .existsByUsername(USERNAME);

        assertDoesNotThrow(() ->
                authorizationValidationService.validateUserDoesNotExist(USERNAME));
    }

    @Test
    void validateUserDoesNotExist_exception() {
        doReturn(true)
                .when(playerRepository)
                .existsByUsername(USERNAME);

        assertThrows(GameAuthorizationException.class, () ->
                authorizationValidationService.validateUserDoesNotExist(USERNAME));
    }
}
