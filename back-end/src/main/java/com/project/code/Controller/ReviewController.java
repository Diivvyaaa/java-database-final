package com.project.code.Controller;

<<<<<<< HEAD
import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;
=======
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
>>>>>>> 2d88e4a (new code)

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

<<<<<<< HEAD
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
=======
import com.project.code.Model.Customer;
import com.project.code.Model.Review;
import com.project.code.Repo.CustomerRepository;
import com.project.code.Repo.ReviewRepository;
>>>>>>> 2d88e4a (new code)

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

<<<<<<< HEAD
        Map<String, Object> response = new HashMap<>();

        List<Review> reviews = reviewRepository
                .findByStoreIdAndProductId(storeId, productId);

        List<Map<String, Object>> reviewsWithCustomerNames =
                new ArrayList<>();
=======
        Map<String, Object> map = new HashMap<>();

        List<Review> reviews =
                reviewRepository.findByStoreIdAndProductId(storeId, productId);

        List<Map<String, Object>> reviewsWithCustomerNames = new ArrayList<>();
>>>>>>> 2d88e4a (new code)

        for (Review review : reviews) {
            Map<String, Object> reviewMap = new HashMap<>();

<<<<<<< HEAD
            reviewMap.put("comment", review.getComment());
=======
            reviewMap.put("review", review.getComment());
>>>>>>> 2d88e4a (new code)
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

<<<<<<< HEAD
        response.put("reviews", reviewsWithCustomerNames);

        return response;
=======
        map.put("reviews", reviewsWithCustomerNames);
        return map;
>>>>>>> 2d88e4a (new code)
    }

    @GetMapping
    public Map<String, Object> getAllReviews() {
<<<<<<< HEAD
        Map<String, Object> response = new HashMap<>();
        response.put("reviews", reviewRepository.findAll());

        return response;
    }
}
=======
        Map<String, Object> map = new HashMap<>();
        map.put("reviews", reviewRepository.findAll());

        return map;
    }
}
>>>>>>> 2d88e4a (new code)
