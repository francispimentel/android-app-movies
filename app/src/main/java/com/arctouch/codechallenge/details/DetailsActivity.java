package com.arctouch.codechallenge.details;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.arctouch.codechallenge.R;
import com.arctouch.codechallenge.model.Movie;
import com.arctouch.codechallenge.util.MovieImageUrlBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

public class DetailsActivity extends AppCompatActivity {

    private final MovieImageUrlBuilder movieImageUrlBuilder = new MovieImageUrlBuilder();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.details_activity);

        TextView titleTextView = findViewById(R.id.titleTextView);
        TextView genresTextView = findViewById(R.id.genresTextView);
        TextView releaseDateTextView = findViewById(R.id.releaseDateTextView);
        TextView overviewTextView = findViewById(R.id.overviewTextView);
        ImageView posterImageView = findViewById(R.id.posterImageView);
        ImageView backdropImageView = findViewById(R.id.backdropImageView);

        Bundle bundle = getIntent().getExtras();

        Movie movie = (Movie) bundle.getSerializable("movie");

        titleTextView.setText(movie.title);
        genresTextView.setText(TextUtils.join(", ", movie.genres));
        releaseDateTextView.setText(movie.releaseDate);
        overviewTextView.setText(movie.overview);

        String posterPath = movie.posterPath;
        if (TextUtils.isEmpty(posterPath) == false) {
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildPosterUrl(posterPath))
                    .apply(new RequestOptions().placeholder(R.drawable.ic_image_placeholder))
                    .into(posterImageView);
        }

        String backdropPath = movie.backdropPath;
        if (TextUtils.isEmpty(backdropPath) == false) {
            Glide.with(this)
                    .load(movieImageUrlBuilder.buildBackdropUrl(backdropPath))
                    .into(backdropImageView);
        }
    }
}
