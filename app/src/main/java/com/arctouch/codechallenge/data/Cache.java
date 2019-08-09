package com.arctouch.codechallenge.data;

import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Cache {

    private static List<Genre> genres = new ArrayList<>();

    private static List<Movie> allMovies = new ArrayList<>();

    private static Map<Long, List<Movie>> pages = new ConcurrentHashMap<>();

    public static List<Genre> getGenres() {
        return genres;
    }

    public static List<Movie> getAllMovies() {
        return allMovies;
    }

    public static void setGenres(List<Genre> genres) {
        Cache.genres.clear();
        Cache.genres.addAll(genres);
    }

    public static void addMovies(Long page, List<Movie> movies) {
        if (!pages.containsKey(page)) {
            pages.put(page, movies);
            allMovies.addAll(movies);
        }
    }
}
