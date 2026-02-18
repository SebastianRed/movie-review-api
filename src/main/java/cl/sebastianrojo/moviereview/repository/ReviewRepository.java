package cl.sebastianrojo.moviereview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import cl.sebastianrojo.moviereview.entity.ContentType;
import cl.sebastianrojo.moviereview.entity.Review;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

    Optional<Review> findByUserIdAndExternalContentIdAndContentType(
        Long userId, 
        String externalContentId, 
        ContentType contentType
    );

    List<Review> findByExternalContentIdAndContentType(
        String externalContentId, 
        ContentType contentType
    );

    List<Review> findByUserId(Long userId);

    List<Review> findByUserIdOrderByCreatedAtDesc(Long userId);

    @Query("SELECT AVG(r.rating) FROM Review r WHERE r.externalContentId = :externalContentId AND r.contentType = :contentType")
    Optional<Double> calculateAverageRating(
        @Param("externalContentId") String externalContentId,
        @Param("contentType") ContentType contentType
    );

    long countByExternalContentIdAndContentType(
        String externalContentId, 
        ContentType contentType
    );
}