package org.payLate.controllers;

import org.payLate.requestmodels.ReviewRequest;
import org.payLate.services.ReviewService;
import org.payLate.utils.JWTExtractor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@CrossOrigin("http://localhost:3000")
@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    @Autowired
    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @GetMapping("/secure/user/product")
    public Boolean reviewProductByUser(@RequestHeader(value = "Authorization") String token,
                                       @RequestParam("productId") Long productId) throws Exception {
        String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing!");
        }

        return reviewService.userReviewListed(userEmail, productId);
    }

    @PostMapping("/secure")
    public void postReview(@RequestHeader(value = "Authorization") String token,
                           @RequestParam String partner,
                           @RequestBody ReviewRequest reviewRequest) throws Exception {
        String userEmail = JWTExtractor.payloadJWTExtraction(token, "\"sub\"");
        if (userEmail == null) {
            throw new Exception("User email is missing!");
        }

        reviewService.postReview(userEmail, reviewRequest, partner);
    }
}