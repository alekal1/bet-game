package ee.aleksale.betgame.auth.controller;

import ee.aleksale.betgame.auth.model.api.AuthRequest;
import ee.aleksale.betgame.auth.service.AuthorizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthorizationController {

    private final AuthorizationService authorizationService;

    @PostMapping
    public ResponseEntity<String> auth(@RequestBody AuthRequest request) {
        var token = authorizationService.fetchToken(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(token);
    }
}
