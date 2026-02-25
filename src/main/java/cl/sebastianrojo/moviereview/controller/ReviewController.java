package cl.sebastianrojo.moviereview.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sebastianrojo.moviereview.dto.review.ContentReviewSummary;
import cl.sebastianrojo.moviereview.dto.review.CreateReviewRequest;
import cl.sebastianrojo.moviereview.dto.review.ReviewResponse;
import cl.sebastianrojo.moviereview.dto.review.UpdateReviewRequest;
import cl.sebastianrojo.moviereview.entity.ContentType;
import cl.sebastianrojo.moviereview.service.ReviewService;
import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reviews")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }

    /**
     * POST /api/reviews - Crea una nueva review
     */
    @PostMapping
    public ResponseEntity<ReviewResponse> createReview(
            @Valid @RequestBody CreateReviewRequest request,
            Authentication authentication
    ) {
        ReviewResponse response = reviewService.createReview(request, authentication);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    /**
     * PUT /api/reviews/{id} - Actualiza una review existente
     */
    @PutMapping("/{id}")
    public ResponseEntity<ReviewResponse> updateReview(
            @PathVariable Long id,
            @Valid @RequestBody UpdateReviewRequest request,
            Authentication authentication
    ) {
        ReviewResponse response = reviewService.updateReview(id, request, authentication);
        return ResponseEntity.ok(response);
    }

    /**
     * DELETE /api/reviews/{id} - Elimina una review
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteReview(
            @PathVariable Long id,
            Authentication authentication
    ) {
        reviewService.deleteReview(id, authentication);
        return ResponseEntity.noContent().build();
    }

    /**
     * GET /api/reviews/{id} - Obtiene una review por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<ReviewResponse> getReviewById(@PathVariable Long id) {
        ReviewResponse response = reviewService.getReviewById(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/reviews/content - Obtiene reviews y resumen de un contenido
     * Query params: externalContentId, contentType
     */
    @GetMapping("/content")
    public ResponseEntity<ContentReviewSummary> getReviewsByContent(
            @RequestParam String externalContentId,
            @RequestParam ContentType contentType
    ) {
        ContentReviewSummary summary = reviewService.getReviewsForContent(externalContentId, contentType);
        return ResponseEntity.ok(summary);
    }

    /**
     * GET /api/reviews/user/{username} - Obtiene todas las reviews de un usuario
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<List<ReviewResponse>> getUserReviews(@PathVariable String username) {
        List<ReviewResponse> reviews = reviewService.getUserReviews(username);
        return ResponseEntity.ok(reviews);
    }

    /**
     * GET /api/reviews/my - Obtiene las reviews del usuario autenticado
     */
    @GetMapping("/my")
    public ResponseEntity<List<ReviewResponse>> getMyReviews(Authentication authentication) {
        List<ReviewResponse> reviews = reviewService.getMyReviews(authentication);
        return ResponseEntity.ok(reviews);
    }

    /**
     * GET /api/reviews/check - Verifica si el usuario ya ha dejado una review
     * Query params: externalContentId, contentType
     */
    @GetMapping("/check")
    public ResponseEntity<Boolean> hasUserReviewed(
            @RequestParam String externalContentId,
            @RequestParam ContentType contentType,
            Authentication authentication
    ) {
        boolean hasReviewed = reviewService.hasUserReviewed(externalContentId, contentType, authentication);
        return ResponseEntity.ok(hasReviewed);
    }
}