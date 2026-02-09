package cl.sebastianrojo.moviereview.service;

import java.util.List;

import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

import cl.sebastianrojo.moviereview.dto.ReviewRequest;
import cl.sebastianrojo.moviereview.dto.ReviewResponse;
import cl.sebastianrojo.moviereview.entity.ContentType;
import cl.sebastianrojo.moviereview.entity.Review;
import cl.sebastianrojo.moviereview.entity.User;
import cl.sebastianrojo.moviereview.repository.ReviewRepository;
import cl.sebastianrojo.moviereview.repository.UserRepository;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;

    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    public ReviewResponse createReview(ReviewRequest request, Authentication authentication) {
        User user = userRepository.findByUsername(authentication.getName()).orElseThrow();

        reviewRepository.findByUserIdAndExternalContentIdAndContentType(
                user.getId(), request.externalContentId(), request.contentType()).ifPresent(r -> {
                    throw new IllegalArgumentException("Ya existe una reseña para este usuario y contenido.");
                });

        Review review = new Review();
        review.setExternalContentId(request.externalContentId());
        review.setContentType(request.contentType());
        review.setRating(request.rating());
        review.setComment(request.comment());
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        return mapToResponse(savedReview);
    }

    public List<ReviewResponse> getReviewsForContent(String externalContentId, ContentType contentType) {
        return reviewRepository.findByExternalContentIdAndContentType(externalContentId, contentType)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getUsername(),
                review.getExternalContentId(),
                review.getContentType(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt()
            );
    }
}