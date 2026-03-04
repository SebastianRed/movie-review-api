package cl.sebastianrojo.moviereview.dto.review;

import java.util.List;

import cl.sebastianrojo.moviereview.entity.ContentType;

public record ContentReviewSummary(
    String externalContentId,
    ContentType contentType,
    long totalReviews,
    double averageRating,
    List<ReviewResponse> reviews
) {}