package cl.sebastianrojo.moviereview.dto;

import cl.sebastianrojo.moviereview.entity.ContentType;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record ReviewRequest(@NotBlank String externalContentId, @NotNull ContentType contentType, @Min(1) @Max(5) int rating, @Size(max = 1000) String comment) {}