package com.arctouch.codechallenge.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private RecyclerView recyclerView;
    private ProgressBar progressBar;
    private HomePresenter presenter;

    public HomeActivity() {
        presenter = new HomePresenterImpl(this);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        this.recyclerView = findViewById(R.id.recyclerView);
        this.progressBar = findViewById(R.id.progressBar);

        presenter.loadInitialData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }


    @Override
    public void setMoviesList(List<Movie> results) {
        recyclerView.setAdapter(new HomeAdapter(results, this));
        progressBar.setVisibility(View.GONE);
    }
}
