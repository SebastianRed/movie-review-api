package cl.sebastianrojo.moviereview.controller;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sebastianrojo.moviereview.dto.ReviewRequest;
import cl.sebastianrojo.moviereview.dto.ReviewResponse;
import cl.sebastianrojo.moviereview.entity.ContentType;
import cl.sebastianrojo.moviereview.service.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    @PostMapping
    public ReviewResponse create(
            @Valid @RequestBody ReviewRequest request,
            Authentication authentication
    ) {
        return reviewService.createReview(request, authentication);
    }

    @GetMapping
    public List<ReviewResponse> getByContent(
            @RequestParam String externalContentId,
            @RequestParam ContentType contentType
    ) {
        return reviewService.getReviewsForContent(
                externalContentId,
                contentType
        );
    }
}