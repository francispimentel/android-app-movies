package com.arctouch.codechallenge.home;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.widget.ProgressBar;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;

import java.util.List;

public class HomeActivity extends AppCompatActivity implements HomeView {

    private RecyclerView recyclerView;
    private HomeAdapter homeAdapter;
    private ProgressBar progressBar;
    private SearchView searchView;
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
        this.searchView = findViewById(R.id.search);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                progressBar.setVisibility(View.VISIBLE);
                presenter.triggerSearch(newText);
                return false;
            }
        });

        if (savedInstanceState != null) {
            presenter.setSearchTerm(savedInstanceState.getString("query"));
        }

        presenter.loadInitialData();
    }

    @Override
    protected void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        savedInstanceState.putString("query", searchView.getQuery().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        presenter.dispose();
    }


    @Override
    public void setMoviesList(List<Movie> results) {
        homeAdapter = new HomeAdapter(results, this);
        recyclerView.setAdapter(homeAdapter);
        recyclerView.clearOnScrollListeners();
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            private int mPreviousTotal = 0;
            private boolean mLoading = true;

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                int visibleItemCount = recyclerView.getChildCount();
                int totalItemCount = recyclerView.getLayoutManager().getItemCount();
                int firstVisibleItem = ((LinearLayoutManager) recyclerView.getLayoutManager()).findFirstVisibleItemPosition();

                if (mLoading && totalItemCount > mPreviousTotal) {
                    mLoading = false;
                    mPreviousTotal = totalItemCount;
                    progressBar.setVisibility(View.GONE);
                }
                int visibleThreshold = 1;
                if (!mLoading && (totalItemCount - visibleItemCount) <= (firstVisibleItem + visibleThreshold) && !presenter.allPagesLoaded()) {
                    progressBar.setVisibility(View.VISIBLE);
                    presenter.loadNextPage();
                    mLoading = true;
                }
            }
        });
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void appendMoviesList(List<Movie> results) {
        homeAdapter.appendData(results);
        progressBar.setVisibility(View.GONE);
    }

    @Override
    public void hideMask() {
        progressBar.setVisibility(View.GONE);
    }
}
