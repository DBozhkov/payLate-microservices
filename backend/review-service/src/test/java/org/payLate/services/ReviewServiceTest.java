package org.payLate.services;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.payLate.entity.Review;
import org.payLate.repository.ReviewRepository;
import org.payLate.requestmodels.ReviewRequest;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class ReviewServiceTest {

    private ReviewRepository reviewRepository;
    private WebClient productServiceWebClient;
    private ReviewService reviewService;

    @BeforeEach
    void setUp() {
        reviewRepository = mock(ReviewRepository.class);

        WebClient.RequestHeadersUriSpec uriSpec = mock(WebClient.RequestHeadersUriSpec.class);
        WebClient.RequestHeadersSpec headersSpec = mock(WebClient.RequestHeadersSpec.class);
        WebClient.ResponseSpec responseSpec = mock(WebClient.ResponseSpec.class);

        productServiceWebClient = mock(WebClient.class);
        when(productServiceWebClient.get()).thenReturn(uriSpec);
        when(uriSpec.uri(anyString(), any(), any())).thenReturn(headersSpec);
        when(headersSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.bodyToMono(Boolean.class)).thenReturn(Mono.just(true));

        reviewService = new ReviewService(reviewRepository, productServiceWebClient);
    }

    @Test
    void userReviewListed_returnsTrueIfExists() {
        when(reviewRepository.findByUserEmailAndProductId("test@user.com", 1L)).thenReturn(new Review());
        assertTrue(reviewService.userReviewListed("test@user.com", 1L));
    }

    @Test
    void userReviewListed_returnsFalseIfNotExists() {
        when(reviewRepository.findByUserEmailAndProductId("test@user.com", 1L)).thenReturn(null);
        assertFalse(reviewService.userReviewListed("test@user.com", 1L));
    }

    @Test
    void postReview_savesReviewIfValid() throws Exception {
        ReviewRequest req = new ReviewRequest();
        req.setProductId(1L);
        req.setRating(5);
        req.setReviewDescription("Great!");

        when(reviewRepository.findByUserEmailAndProductId("test@user.com", 1L)).thenReturn(null);

        reviewService.postReview("test@user.com", req, "amazon");

        verify(reviewRepository, times(1)).save(any(Review.class));
    }

    @Test
    void postReview_throwsIfAlreadyExists() {
        ReviewRequest req = new ReviewRequest();
        req.setProductId(1L);
        req.setRating(5);
        req.setReviewDescription("Nice");

        when(reviewRepository.findByUserEmailAndProductId("test@user.com", 1L)).thenReturn(new Review());

        Exception exception = assertThrows(Exception.class, () ->
                reviewService.postReview("test@user.com", req, "amazon")
        );
        assertTrue(exception.getMessage().contains("Review already created"));
    }
}