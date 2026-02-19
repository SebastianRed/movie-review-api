package cl.sebastianrojo.moviereview.dto.tmdb;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbSearchResponse(
    int page,
    List<TmdbContentItem> results,
    
    @JsonProperty("total_pages")
    int totalPages,
    
    @JsonProperty("total_results")
    int totalResults
) {}