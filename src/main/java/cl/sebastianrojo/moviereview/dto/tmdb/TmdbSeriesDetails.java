package cl.sebastianrojo.moviereview.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

public record TmdbSeriesDetails(
    Long id,
    String name,
    
    @JsonProperty("original_name")
    String originalName,
    
    String overview,
    
    @JsonProperty("poster_path")
    String posterPath,
    
    @JsonProperty("backdrop_path")
    String backdropPath,
    
    @JsonProperty("first_air_date")
    String firstAirDate,
    
    @JsonProperty("last_air_date")
    String lastAirDate,
    
    @JsonProperty("vote_average")
    Double voteAverage,
    
    @JsonProperty("vote_count")
    Integer voteCount,
    
    @JsonProperty("number_of_seasons")
    Integer numberOfSeasons,
    
    @JsonProperty("number_of_episodes")
    Integer numberOfEpisodes,
    
    List<Genre> genres,
    
    String tagline,
    
    String status,
    
    @JsonProperty("in_production")
    Boolean inProduction
) {}