package io.datapirates.moviecatalogservice.resources;

import io.datapirates.moviecatalogservice.models.CatalogItem;
import io.datapirates.moviecatalogservice.models.Movie;
import io.datapirates.moviecatalogservice.models.Rating;
import io.datapirates.moviecatalogservice.models.UserRating;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/catalog")
public class MovieCatalogResources {

    @Autowired
    private RestTemplate restTemplate;

    @Autowired
    private WebClient.Builder webClientBuilder;

    @RequestMapping("/{userId}")
    List<CatalogItem> getCatalog(@PathVariable("userId") String userId) {

        /*List<Rating> ratings = Arrays.asList(
                new Rating("1234", 4),
                new Rating("5678", 3)
        );*/

        //UserRating ratings = restTemplate.getForObject("http://localhost:8083/ratingsdata/users/"+userId, UserRating.class);
        UserRating ratings = restTemplate.getForObject("http://rating-data-service/ratingsdata/users/"+userId, UserRating.class);
        return ratings.getUserRating().stream().map(rating -> {
            //Movie movie = restTemplate.getForObject("http://localhost:8082/movies/"+rating.getMovieId(), Movie.class);
            Movie movie = restTemplate.getForObject("http://movie-info-service/movies/"+rating.getMovieId(), Movie.class);
            /*Movie movie = webClientBuilder.build()
                    .get()
                    .uri("http://localhost:8082/movies/"+rating.getMovieId())
                    .retrieve()
                    .bodyToMono(Movie.class)
                    .block();*/

            return new CatalogItem(movie.getName(), "Desc", rating.getRating());
        }).collect(Collectors.toList());

        /*return Collections.singletonList(
                new CatalogItem("Transformer", "Test", 4)
        );*/
    }
}
