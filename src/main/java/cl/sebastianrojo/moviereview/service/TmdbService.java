package cl.sebastianrojo.moviereview.service;

import cl.sebastianrojo.moviereview.dto.tmdb.*;
import cl.sebastianrojo.moviereview.exception.ExternalApiException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@Service
public class TmdbService {

    private static final Logger log = LoggerFactory.getLogger(TmdbService.class);

    private final WebClient webClient;
    private final String apiKey;
    private final String imageBaseUrl;

    public TmdbService(
            @Value("${tmdb.api.key}") String apiKey,
            @Value("${tmdb.api.base-url}") String baseUrl,
            @Value("${tmdb.api.image-base-url}") String imageBaseUrl
    ) {
        this.apiKey = apiKey;
        this.imageBaseUrl = imageBaseUrl;
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    /**
     * Busca películas por término de búsqueda
     */
    public TmdbSearchResponse searchMovies(String query, Integer page) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/movie")
                            .queryParam("api_key", apiKey)
                            .queryParam("query", query)
                            .queryParam("language", "es-ES")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .onErrorMap(WebClientResponseException.class, ex -> 
                        new ExternalApiException("Error al buscar películas en TMDB: " + ex.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Error searching movies: {}", e.getMessage());
            throw new ExternalApiException("Error al conectar con TMDB", e);
        }
    }

    /**
     * Busca series por término de búsqueda
     */
    public TmdbSearchResponse searchSeries(String query, Integer page) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/search/tv")
                            .queryParam("api_key", apiKey)
                            .queryParam("query", query)
                            .queryParam("language", "es-ES")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .onErrorMap(WebClientResponseException.class, ex -> 
                        new ExternalApiException("Error al buscar series en TMDB: " + ex.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Error searching series: {}", e.getMessage());
            throw new ExternalApiException("Error al conectar con TMDB", e);
        }
    }

    /**
     * Obtiene detalles de una película específica
     */
    public TmdbMovieDetails getMovieDetails(Long movieId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/{id}")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", "es-ES")
                            .build(movieId))
                    .retrieve()
                    .bodyToMono(TmdbMovieDetails.class)
                    .onErrorMap(WebClientResponseException.class, ex -> 
                        new ExternalApiException("Error al obtener detalles de película: " + ex.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Error getting movie details for ID {}: {}", movieId, e.getMessage());
            throw new ExternalApiException("Error al conectar con TMDB", e);
        }
    }

    /**
     * Obtiene detalles de una serie específica
     */
    public TmdbSeriesDetails getSeriesDetails(Long seriesId) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tv/{id}")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", "es-ES")
                            .build(seriesId))
                    .retrieve()
                    .bodyToMono(TmdbSeriesDetails.class)
                    .onErrorMap(WebClientResponseException.class, ex -> 
                        new ExternalApiException("Error al obtener detalles de serie: " + ex.getMessage()))
                    .block();
        } catch (Exception e) {
            log.error("Error getting series details for ID {}: {}", seriesId, e.getMessage());
            throw new ExternalApiException("Error al conectar con TMDB", e);
        }
    }

    /**
     * Obtiene películas populares
     */
    public TmdbSearchResponse getPopularMovies(Integer page) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/movie/popular")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", "es-ES")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error getting popular movies: {}", e.getMessage());
            throw new ExternalApiException("Error al conectar con TMDB", e);
        }
    }

    /**
     * Obtiene series populares
     */
    public TmdbSearchResponse getPopularSeries(Integer page) {
        try {
            return webClient.get()
                    .uri(uriBuilder -> uriBuilder
                            .path("/tv/popular")
                            .queryParam("api_key", apiKey)
                            .queryParam("language", "es-ES")
                            .queryParam("page", page != null ? page : 1)
                            .build())
                    .retrieve()
                    .bodyToMono(TmdbSearchResponse.class)
                    .block();
        } catch (Exception e) {
            log.error("Error getting popular series: {}", e.getMessage());
            throw new ExternalApiException("Error al conectar con TMDB", e);
        }
    }

    /**
     * Construye la URL completa de una imagen
     */
    public String buildImageUrl(String imagePath, String size) {
        if (imagePath == null) {
            return null;
        }
        // Tamaños comunes: w92, w154, w185, w342, w500, w780, original
        return imageBaseUrl + "/" + size + imagePath;
    }

    /**
     * Construye la URL del poster con tamaño por defecto
     */
    public String buildPosterUrl(String posterPath) {
        return buildImageUrl(posterPath, "w500");
    }

    /**
     * Construye la URL del backdrop con tamaño por defecto
     */
    public String buildBackdropUrl(String backdropPath) {
        return buildImageUrl(backdropPath, "w780");
    }
}