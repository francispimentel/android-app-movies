package com.arctouch.codechallenge.util;

import com.arctouch.codechallenge.api.TmdbApiClient;

public class MovieImageUrlBuilder {

    private static final String BACKDROP_URL = "https://image.tmdb.org/t/p/w780";
    private static final String POSTER_URL = "https://image.tmdb.org/t/p/w154";

    public String buildPosterUrl(String posterPath) {
        return POSTER_URL + posterPath + "?api_key=" + TmdbApiClient.API_KEY;
    }

    public String buildBackdropUrl(String backdropPath) {
        return BACKDROP_URL + backdropPath + "?api_key=" + TmdbApiClient.API_KEY;
    }
}
