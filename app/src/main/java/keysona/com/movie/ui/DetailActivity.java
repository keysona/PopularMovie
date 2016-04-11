package keysona.com.movie.ui;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import keysona.com.movie.R;
import keysona.com.movie.data.MovieContract;
import keysona.com.movie.data.MovieInfo;


public class DetailActivity extends AppCompatActivity implements DetailFragment.saveLikeState {

    private static final int LIKE = 1;

    private static final int DISLIKE = 0;

    private int like;

    private MovieInfo movieInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

                changeFab(fab, false, view);
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null) {
            movieInfo = intent.getParcelableExtra("movie_info");
            getSupportActionBar().setTitle(movieInfo.getOriginalTitle());
            ImageView posterImageView = (ImageView) findViewById(R.id.poster);
            Picasso.with(this).load(movieInfo.getPosterPath()).into(posterImageView);

            Bundle bundle = new Bundle();
            bundle.putParcelable("movie_info", movieInfo);
            Fragment ff = new DetailFragment();
            ff.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, ff)
                    .commit();
            like = movieInfo.getLike();
            changeFab(fab, true, null);
        }
    }

    private void changeFab(final FloatingActionButton fab, boolean init, View view) {
        if (init) {
            switch (like) {
                case LIKE:
                    fab.setImageResource(R.drawable.like);
                    break;
                case DISLIKE:
                    fab.setImageResource(R.drawable.dislike);
                    break;
            }
            return;
        }
        switch (like) {
            case LIKE:
                fab.setImageResource(R.drawable.dislike);
                like = DISLIKE;
                movieInfo.setLike(like);
                Snackbar.make(view, "Remove from your favourite", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                break;
            case DISLIKE:
                fab.setImageResource(R.drawable.like);
                like = LIKE;
                movieInfo.setLike(like);
                Snackbar.make(view, "Add your favourite", Snackbar.LENGTH_SHORT)
                        .setAction("Action", null).show();
                break;
        }
    }

    @Override
    public void saveLikeState() {
        if (movieInfo != null) {
            getContentResolver().update(MovieContract.MovieInfoEntry.CONTENT_URI,
                    movieInfo.toContentValues(),
                    "movie_id = ?", new String[]{"" + movieInfo.getMovieId()});
            movieInfo.setLike(like);
        }
    }
}
