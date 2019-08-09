package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.api.TmdbApiClient;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class HomePresenterImpl implements HomePresenter {

    private HomeView view;
    private CompositeDisposable compositeDisposable = new CompositeDisposable();

    public HomePresenterImpl(HomeView view) {
        this.view = view;
    }

    @Override
    public void loadInitialData() {
        if (Cache.getGenres().isEmpty()) {
            retrieveGenres();
        } else if (Cache.getAllMovies().isEmpty()) {
            retrieveUpcomingMovies();
        } else {
            view.setMoviesList(Cache.getAllMovies());
        }
    }

    @Override
    public void dispose() {
        compositeDisposable.dispose();
    }

    private void retrieveGenres() {
        Disposable disposable = TmdbApiClient.getApi().genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                    retrieveUpcomingMovies();
                });
        compositeDisposable.add(disposable);
    }

    private void retrieveUpcomingMovies() {
        Disposable disposable = TmdbApiClient.getApi().upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1L, TmdbApi.DEFAULT_REGION)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    for (Movie movie : response.results) {
                        movie.genres = new ArrayList<>();
                        for (Genre genre : Cache.getGenres()) {
                            if (movie.genreIds.contains(genre.id)) {
                                movie.genres.add(genre);
                            }
                        }
                    }
                    Cache.addMovies(1L, response.results);
                    view.setMoviesList(response.results);
                });
        compositeDisposable.add(disposable);
    }
}
