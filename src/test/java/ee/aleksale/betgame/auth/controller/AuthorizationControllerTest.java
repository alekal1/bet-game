package ee.aleksale.betgame.auth.controller;

import ee.aleksale.betgame.auth.model.api.AuthRequest;
import ee.aleksale.betgame.auth.service.AuthorizationService;
import ee.aleksale.betgame.common.utils.JsonUtils;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import static org.mockito.Mockito.verify;

@WebMvcTest(AuthorizationController.class)
class AuthorizationControllerTest {

    private static final String USERNAME = "AnyUsername";

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AuthorizationService authorizationService;

    @Test
    void auth() throws Exception {
        var request = getAuthRequest();

        RequestBuilder requestBuilder = MockMvcRequestBuilders.post("/v1/auth")
                .accept(MediaType.APPLICATION_JSON)
                .contentType(MediaType.APPLICATION_JSON)
                .content(JsonUtils.writeValuesAsString(request));

        mockMvc.perform(requestBuilder)
                .andExpect(MockMvcResultMatchers.status().isCreated());

        verify(authorizationService).fetchToken(request);
    }

    private AuthRequest getAuthRequest() {
        var request = new AuthRequest();
        request.setUsername(USERNAME);
        return request;
    }
}
