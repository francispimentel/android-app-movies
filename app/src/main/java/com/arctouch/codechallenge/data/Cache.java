package com.arctouch.codechallenge.data;

import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Cache {

    private static List<Genre> genres = new ArrayList<>();

    private static List<Movie> allMovies = new ArrayList<>();

    private static int loadedPageCount = 0;
    private static int totalPages;

    private static Set<Long> pages = new HashSet<>();

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
        if (!pages.contains(page)) {
            pages.add(page);
            allMovies.addAll(movies);
            loadedPageCount++;
        }
    }

    public static int getLoadedPageCount() {
        return loadedPageCount;
    }

    public static void setTotalPages(int totalPages) {
        Cache.totalPages = totalPages;
    }

    public static boolean isAllPagesLoaded() {
        return loadedPageCount == totalPages;
    }
}
