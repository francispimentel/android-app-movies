package com.arctouch.codechallenge.home;

import com.arctouch.codechallenge.api.TmdbApi;
import com.arctouch.codechallenge.data.Cache;
import com.arctouch.codechallenge.model.Genre;
import com.arctouch.codechallenge.model.Movie;

import java.util.ArrayList;

import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class HomePresenterImpl implements HomePresenter {

    protected TmdbApi api = new Retrofit.Builder()
            .baseUrl(TmdbApi.URL)
            .client(new OkHttpClient.Builder().build())
            .addConverterFactory(MoshiConverterFactory.create())
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .build()
            .create(TmdbApi.class);

    private HomeView view;

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

    private void retrieveGenres() {
        api.genres(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(response -> {
                    Cache.setGenres(response.genres);
                    retrieveUpcomingMovies();
                });
    }

    private void retrieveUpcomingMovies() {
        api.upcomingMovies(TmdbApi.API_KEY, TmdbApi.DEFAULT_LANGUAGE, 1L, TmdbApi.DEFAULT_REGION)
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
    }
}
