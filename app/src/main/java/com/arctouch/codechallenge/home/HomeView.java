package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.model.Movie;

import java.util.List;

interface HomeView {

    void setMoviesList(List<Movie> results);

    void appendMoviesList(List<Movie> results);

    void setAllPagesLoaded(boolean allPagesLoaded);
}
