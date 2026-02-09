package cl.sebastianrojo.moviereview.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import cl.sebastianrojo.moviereview.entity.ContentType;
import cl.sebastianrojo.moviereview.entity.Review;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    
    Optional<Review> findByUserIdAndExternalContentIdAndContentType(Long userId, String externalContentId, ContentType contentType);

    List<Review> findByExternalContentIdAndContentType(String externalContentId, ContentType contentType);

}