package cl.sebastianrojo.moviereview.dto.tmdb;

import com.fasterxml.jackson.annotation.JsonProperty;

public record TmdbContentItem(
    Long id,
    String title,
    
    @JsonProperty("name")
    String name,
    
    @JsonProperty("original_title")
    String originalTitle,
    
    @JsonProperty("original_name")
    String originalName,
    
    String overview,
    
    @JsonProperty("poster_path")
    String posterPath,
    
    @JsonProperty("backdrop_path")
    String backdropPath,
    
    @JsonProperty("release_date")
    String releaseDate,
    
    @JsonProperty("first_air_date")
    String firstAirDate,
    
    @JsonProperty("vote_average")
    Double voteAverage,
    
    @JsonProperty("vote_count")
    Integer voteCount,
    
    @JsonProperty("media_type")
    String mediaType
) {
    public String getDisplayTitle() {
        return title != null ? title : name;
    }
    
    public String getDisplayDate() {
        return releaseDate != null ? releaseDate : firstAirDate;
    }
}