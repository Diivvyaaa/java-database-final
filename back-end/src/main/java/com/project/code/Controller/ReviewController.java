package com.project.code.Controller;

import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    @Autowired
    private ReviewRepository reviewRepository;

    @Autowired
    private CustomerRepository customerRepository;

    @GetMapping("/{storeId}/{productId}")
    public Map<String, Object> getReviews(
            @PathVariable long storeId,
            @PathVariable long productId) {

        Map<String, Object> response = new HashMap<>();

        List<Review> reviews = reviewRepository
                .findByStoreIdAndProductId(storeId, productId);

        List<Map<String, Object>> reviewsWithCustomerNames =
                new ArrayList<>();

        for (Review review : reviews) {
            Map<String, Object> reviewMap = new HashMap<>();

            reviewMap.put("comment", review.getComment());
            reviewMap.put("rating", review.getRating());

            Customer customer = customerRepository
                    .findById(review.getCustomerId())
                    .orElse(null);

            if (customer != null) {
                reviewMap.put("customerName", customer.getName());
            } else {
                reviewMap.put("customerName", "Unknown");
            }

            reviewsWithCustomerNames.add(reviewMap);
        }

        response.put("reviews", reviewsWithCustomerNames);

        return response;
    }

    @GetMapping
    public Map<String, Object> getAllReviews() {
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewRepository.findAll());

        return response;
    }
}
