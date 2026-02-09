package cl.sebastianrojo.moviereview.dto;

import java.time.LocalDateTime;

import cl.sebastianrojo.moviereview.entity.ContentType;

public record ReviewResponse(Long id, String username, String externalContentId, ContentType contentType, int rating, String comment, LocalDateTime createdAt) {}