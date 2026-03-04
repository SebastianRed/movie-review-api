package cl.sebastianrojo.moviereview.dto.review;

import cl.sebastianrojo.moviereview.entity.ContentType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record CreateReviewRequest(
    @NotBlank(message = "El ID del contenido externo es obligatorio")
    String externalContentId,
    
    @NotNull(message = "El tipo de contenido es obligatorio")
    ContentType contentType,
    
    @Min(value = 1, message = "La calificación mínima es 1")
    @Max(value = 5, message = "La calificación máxima es 5")
    int rating,
    
    @Size(max = 1000, message = "El comentario no puede exceder los 1000 caracteres")
    String comment
) {}