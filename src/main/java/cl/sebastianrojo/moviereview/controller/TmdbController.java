package cl.sebastianrojo.moviereview.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import cl.sebastianrojo.moviereview.dto.tmdb.TmdbMovieDetails;
import cl.sebastianrojo.moviereview.dto.tmdb.TmdbSearchResponse;
import cl.sebastianrojo.moviereview.dto.tmdb.TmdbSeriesDetails;
import cl.sebastianrojo.moviereview.service.TmdbService;

@RestController
@RequestMapping("/api/tmdb")
public class TmdbController {

    private final TmdbService tmdbService;

    public TmdbController(TmdbService tmdbService) {
        this.tmdbService = tmdbService;
    }

    /**
     * GET /api/tmdb/search/movies - Busca películas
     */
    @GetMapping("/search/movies")
    public ResponseEntity<TmdbSearchResponse> searchMovies(
            @RequestParam String query,
            @RequestParam(required = false) Integer page
    ) {
        TmdbSearchResponse response = tmdbService.searchMovies(query, page);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/tmdb/search/series - Busca series
     */
    @GetMapping("/search/series")
    public ResponseEntity<TmdbSearchResponse> searchSeries(
            @RequestParam String query,
            @RequestParam(required = false) Integer page
    ) {
        TmdbSearchResponse response = tmdbService.searchSeries(query, page);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/tmdb/movie/{id} - Obtiene detalles de una película
     */
    @GetMapping("/movie/{id}")
    public ResponseEntity<TmdbMovieDetails> getMovieDetails(@PathVariable Long id) {
        TmdbMovieDetails details = tmdbService.getMovieDetails(id);
        return ResponseEntity.ok(details);
    }

    /**
     * GET /api/tmdb/series/{id} - Obtiene detalles de una serie
     */
    @GetMapping("/series/{id}")
    public ResponseEntity<TmdbSeriesDetails> getSeriesDetails(@PathVariable Long id) {
        TmdbSeriesDetails details = tmdbService.getSeriesDetails(id);
        return ResponseEntity.ok(details);
    }

    /**
     * GET /api/tmdb/popular/movies - Obtiene películas populares
     */
    @GetMapping("/popular/movies")
    public ResponseEntity<TmdbSearchResponse> getPopularMovies(
            @RequestParam(required = false) Integer page
    ) {
        TmdbSearchResponse response = tmdbService.getPopularMovies(page);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/tmdb/popular/series - Obtiene series populares
     */
    @GetMapping("/popular/series")
    public ResponseEntity<TmdbSearchResponse> getPopularSeries(
            @RequestParam(required = false) Integer page
    ) {
        TmdbSearchResponse response = tmdbService.getPopularSeries(page);
        return ResponseEntity.ok(response);
    }
}