package ee.aleksale.betgame.auth.service.validator;

import ee.aleksale.betgame.auth.exception.GameAuthorizationException;
import ee.aleksale.betgame.common.repository.PlayerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationValidationService {

    private final PlayerRepository playerRepository;

    public void validateUserDoesNotExist(String username) {
        if (playerRepository.existsByUsername(username)) {
            throw new GameAuthorizationException("User already registered, consider using different username.");
        }
    }
}
