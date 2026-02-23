package cl.sebastianrojo.moviereview.service;

import cl.sebastianrojo.moviereview.dto.review.*;
import cl.sebastianrojo.moviereview.entity.ContentType;
import cl.sebastianrojo.moviereview.entity.Review;
import cl.sebastianrojo.moviereview.entity.User;
import cl.sebastianrojo.moviereview.exception.DuplicateResourceException;
import cl.sebastianrojo.moviereview.exception.ResourceNotFoundException;
import cl.sebastianrojo.moviereview.exception.UnauthorizedAccessException;
import cl.sebastianrojo.moviereview.repository.ReviewRepository;
import cl.sebastianrojo.moviereview.repository.UserRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final UserRepository userRepository;

    public ReviewService(ReviewRepository reviewRepository, UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.userRepository = userRepository;
    }

    /**
     * Crea una nueva review
     */
    @Transactional
    public ReviewResponse createReview(CreateReviewRequest request, Authentication authentication) {
        // Obtener usuario autenticado
        User user = getUserFromAuthentication(authentication);

        // Validar que no exista una review previa del mismo usuario para este contenido
        reviewRepository.findByUserIdAndExternalContentIdAndContentType(
                user.getId(),
                request.externalContentId(),
                request.contentType()
        ).ifPresent(existingReview -> {
            throw new DuplicateResourceException(
                "Ya existe una reseña tuya para este contenido. Puedes editarla o eliminarla."
            );
        });

        // Crear nueva review
        Review review = new Review();
        review.setExternalContentId(request.externalContentId());
        review.setContentType(request.contentType());
        review.setRating(request.rating());
        review.setComment(request.comment());
        review.setUser(user);

        Review savedReview = reviewRepository.save(review);

        return mapToResponse(savedReview);
    }

    /**
     * Actualiza una review existente
     */
    @Transactional
    public ReviewResponse updateReview(Long reviewId, UpdateReviewRequest request, Authentication authentication) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrada"));

        // Verificar que el usuario sea el propietario
        User user = getUserFromAuthentication(authentication);
        if (!review.getUser().getId().equals(user.getId())) {
            throw new UnauthorizedAccessException("No tienes permiso para editar esta review");
        }

        // Actualizar campos si están presentes
        if (request.rating() != null) {
            review.setRating(request.rating());
        }
        if (request.comment() != null) {
            review.setComment(request.comment());
        }

        Review updatedReview = reviewRepository.save(review);
        return mapToResponse(updatedReview);
    }

    /**
     * Elimina una review
     */
    @Transactional
    public void deleteReview(Long reviewId, Authentication authentication) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrada"));

        // Verificar que el usuario sea el propietario o admin
        User user = getUserFromAuthentication(authentication);
        if (!review.getUser().getId().equals(user.getId()) && !isAdmin(authentication)) {
            throw new UnauthorizedAccessException("No tienes permiso para eliminar esta review");
        }

        reviewRepository.delete(review);
    }

    /**
     * Obtiene una review por ID
     */
    public ReviewResponse getReviewById(Long reviewId) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new ResourceNotFoundException("Review no encontrada"));
        return mapToResponse(review);
    }

    /**
     * Obtiene todas las reviews de un contenido específico con resumen
     */
    public ContentReviewSummary getReviewsForContent(String externalContentId, ContentType contentType) {
        List<Review> reviews = reviewRepository.findByExternalContentIdAndContentType(
                externalContentId,
                contentType
        );

        List<ReviewResponse> reviewResponses = reviews.stream()
                .map(this::mapToResponse)
                .toList();

        // Calcular rating promedio
        double averageRating = reviewRepository.calculateAverageRating(externalContentId, contentType)
                .orElse(0.0);

        long totalReviews = reviews.size();

        return new ContentReviewSummary(
                externalContentId,
                contentType,
                totalReviews,
                averageRating,
                reviewResponses
        );
    }

    /**
     * Obtiene todas las reviews de un usuario
     */
    public List<ReviewResponse> getUserReviews(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));

        return reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Obtiene las reviews del usuario autenticado
     */
    public List<ReviewResponse> getMyReviews(Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return reviewRepository.findByUserIdOrderByCreatedAtDesc(user.getId())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    /**
     * Verifica si el usuario ya ha dejado una review para un contenido
     */
    public boolean hasUserReviewed(String externalContentId, ContentType contentType, Authentication authentication) {
        User user = getUserFromAuthentication(authentication);
        return reviewRepository.findByUserIdAndExternalContentIdAndContentType(
                user.getId(),
                externalContentId,
                contentType
        ).isPresent();
    }

    // ===== Métodos auxiliares =====

    private User getUserFromAuthentication(Authentication authentication) {
        String username = authentication.getName();
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("Usuario no encontrado"));
    }

    private boolean isAdmin(Authentication authentication) {
        return authentication.getAuthorities().stream()
                .anyMatch(authority -> authority.getAuthority().equals("ROLE_ADMIN"));
    }

    private ReviewResponse mapToResponse(Review review) {
        return new ReviewResponse(
                review.getId(),
                review.getUser().getUsername(),
                review.getExternalContentId(),
                review.getContentType(),
                review.getRating(),
                review.getComment(),
                review.getCreatedAt(),
                review.getUpdatedAt()
        );
    }
}