package org.payLate.services;

import jakarta.transaction.Transactional;
import org.payLate.entity.Review;
import org.payLate.repository.ReviewRepository;
import org.payLate.requestmodels.ReviewRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.sql.Date;
import java.time.LocalDate;

@Service
@Transactional
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final WebClient productServiceWebClient;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository,
                         WebClient productServiceWebClient) {
        this.reviewRepository = reviewRepository;
        this.productServiceWebClient = productServiceWebClient;
    }

    public void postReview(String userEmail, ReviewRequest reviewRequest, String partner) throws Exception {
        Review validateReview = reviewRepository.findByUserEmailAndProductId(userEmail, reviewRequest.getProductId());
        if (validateReview != null) {
            throw new Exception("Review already created!");
        }

        Boolean productExists = productServiceWebClient.get()
                .uri("/api/products/{partner}/{id}/exists", partner, reviewRequest.getProductId())
                .retrieve()
                .bodyToMono(Boolean.class)
                .block();

        if (Boolean.FALSE.equals(productExists)) {
            throw new Exception("Product not found in product-service!");
        }

        Review review = new Review();
        review.setProductId(reviewRequest.getProductId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        if (reviewRequest.getReviewDescription() != null) {
            review.setReviewDescription(reviewRequest.getReviewDescription());
        }
        review.setDate(Date.valueOf(LocalDate.now()));
        reviewRepository.save(review);
    }

    public Boolean userReviewListed(String userEmail, Long productId) {
        Review validateReview = reviewRepository.findByUserEmailAndProductId(userEmail, productId);
        return validateReview != null;
    }
}