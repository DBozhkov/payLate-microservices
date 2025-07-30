package org.payLate.controllers;

import org.junit.jupiter.api.Test;
import org.payLate.services.ReviewService;
import org.payLate.config.TestSecurityConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.web.servlet.MockMvc;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(ReviewController.class)
@Import(TestSecurityConfig.class) // This disables OAuth2 for the test context
class ReviewControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReviewService reviewService;

    @Test
    void reviewProductByUser_returnsTrue() throws Exception {
        // Realistic Okta-style payload (minified, with multiple claims)
        String payloadJson = "{\"ver\":1,\"jti\":\"dummyjti\",\"iss\":\"https://dev-73369143.okta.com/oauth2/default\",\"aud\":\"api://default\",\"iat\":1751798806,\"exp\":1751802406,\"cid\":\"dummycid\",\"uid\":\"dummyuid\",\"scp\":[\"openid\",\"email\",\"profile\"],\"auth_time\":1751798804,\"sub\":\"test@user.com\",\"userType\":\"admin\"}";
        String base64Payload = Base64.getUrlEncoder().withoutPadding().encodeToString(payloadJson.getBytes(StandardCharsets.UTF_8));
        String fakeJwt = "aaa." + base64Payload + ".ccc";

        when(reviewService.userReviewListed("test@user.com", 1L)).thenReturn(true);

        mockMvc.perform(get("/api/reviews/secure/user/product")
                        .header("Authorization", fakeJwt)
                        .param("productId", "1"))
                .andExpect(status().isOk());
    }
}