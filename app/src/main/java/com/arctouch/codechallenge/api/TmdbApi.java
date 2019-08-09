package com.arctouch.codechallenge.api;

import com.arctouch.codechallenge.model.GenreResponse;
import com.arctouch.codechallenge.model.MoviesResponse;

import io.reactivex.Observable;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface TmdbApi {

    @GET("genre/movie/list")
    Observable<GenreResponse> genres(
            @Query("api_key") String apiKey,
            @Query("language") String language
    );

    @GET("movie/upcoming")
    Observable<MoviesResponse> upcomingMovies(
            @Query("api_key") String apiKey,
            @Query("language") String language,
            @Query("page") Long page,
            @Query("region") String region
    );

    @GET("search/movie")
    Observable<MoviesResponse> searchMovies(
            @Query("api_key") String apiKey,
            @Query("query") String query,
            @Query("page") Long page,
            @Query("language") String language,
            @Query("region") String region
    );
}
