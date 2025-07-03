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
        System.out.println("userEmail: " + userEmail);
        System.out.println("reviewRequest: " + reviewRequest);
        System.out.println("partner: " + partner);

        Review validateReview = reviewRepository.findByUserEmailAndProductId(userEmail, reviewRequest.getProductId());
        if (validateReview != null) {
            System.out.println("Review already exists for user " + userEmail + " and product " + reviewRequest.getProductId());
            throw new Exception("Review already created!");
        }

        Boolean productExists = null;
        try {
            productExists = productServiceWebClient.get()
                    .uri("/api/products/{partner}/{id}/exists", partner, reviewRequest.getProductId())
                    .retrieve()
                    .bodyToMono(Boolean.class)
                    .block();
        } catch (Exception e) {
            System.out.println("Error checking product existence: " + e.getMessage());
            throw new Exception("Error checking product existence: " + e.getMessage(), e);
        }

        System.out.println("productExists: " + productExists);

        if (Boolean.FALSE.equals(productExists)) {
            System.out.println("Product not found in product-service");
            throw new Exception("Product not found in product-service!");
        }

        Review review = new Review();
        review.setProductId(reviewRequest.getProductId());
        review.setRating(reviewRequest.getRating());
        review.setUserEmail(userEmail);
        review.setReviewDescription(reviewRequest.getReviewDescription());
        review.setDate(Date.valueOf(LocalDate.now()));
        System.out.println("Saving review: " + review);
        reviewRepository.save(review);
    }

    public Boolean userReviewListed(String userEmail, Long productId) {
        Review validateReview = reviewRepository.findByUserEmailAndProductId(userEmail, productId);
        return validateReview != null;
    }
}