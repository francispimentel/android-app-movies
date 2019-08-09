package com.arctouch.codechallenge.home;

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
            retrieveUpcomingMovies(1L);
        } else {
            view.setMoviesList(Cache.getAllMovies());
        }
    }

    @Override
    public void dispose() {
        compositeDisposable.dispose();
    }

    @Override
    public void loadNextPage() {
        if (Cache.isAllPagesLoaded()) {
            view.setAllPagesLoaded(true);
        } else {
            retrieveUpcomingMovies(Cache.getLoadedPageCount() + 1l);
        }
    }

    private void retrieveGenres() {
        Disposable disposable = TmdbApiClient.getApi().genres(TmdbApiClient.API_KEY, TmdbApiClient.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                    retrieveUpcomingMovies(1L);
                });
        compositeDisposable.add(disposable);
    }

    private void retrieveUpcomingMovies(Long page) {
        Disposable disposable = TmdbApiClient.getApi().upcomingMovies(TmdbApiClient.API_KEY, TmdbApiClient.DEFAULT_LANGUAGE, page, TmdbApiClient.DEFAULT_REGION)
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
                    Cache.addMovies(page, response.results);
                    Cache.setTotalPages(response.totalPages);
                    if (page == 1L) {
                        view.setMoviesList(response.results);
                    } else {
                        view.appendMoviesList(response.results);
                    }
                });
        compositeDisposable.add(disposable);
    }
}
