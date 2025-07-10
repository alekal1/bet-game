package ee.aleksale.betgame.auth.service;

import ee.aleksale.betgame.auth.model.api.AuthRequest;
import ee.aleksale.betgame.auth.model.mapper.PlayerMapper;
import ee.aleksale.betgame.auth.service.validator.AuthorizationValidationService;
import ee.aleksale.betgame.common.repository.PlayerRepository;
import ee.aleksale.betgame.common.utils.TokenUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthorizationService {

    private final PlayerRepository playerRepository;
    private final AuthorizationValidationService authorizationValidationService;

    public String fetchToken(AuthRequest authRequest) {
        authorizationValidationService.validateUserDoesNotExist(authRequest.getUsername());

        var entity = PlayerMapper.INSTANCE.map(authRequest);
        var savedEntity = playerRepository.saveAndFlush(entity);

        return TokenUtil.generateToken(savedEntity);
    }
}
