package cl.sebastianrojo.moviereview.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbMovieDetails(
    Long id,
    String title,
    
    @JsonProperty("original_title")
    String originalTitle,
    
    String overview,
    
    @JsonProperty("poster_path")
    String posterPath,
    
    @JsonProperty("backdrop_path")
    String backdropPath,
    
    @JsonProperty("release_date")
    String releaseDate,
    
    @JsonProperty("vote_average")
    Double voteAverage,
    
    @JsonProperty("vote_count")
    Integer voteCount,
    
    Integer runtime,
    
    List<Genre> genres,
    
    String tagline,
    
    Long budget,
    
    Long revenue,
    
    String status
) {}