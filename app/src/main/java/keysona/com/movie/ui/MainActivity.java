package keysona.com.movie.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import keysona.com.movie.R;
import keysona.com.movie.data.MovieContract;
import keysona.com.movie.data.MovieInfo;


public class MainActivity extends AppCompatActivity implements MainActivityFragment.Callback, DetailFragment.saveLikeState {

    public static boolean mTwoPanel = false;

    private static final int LIKE = 1;

    private static final int DISLIKE = 0;

    private int like;

    private MovieInfo movieInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        if (findViewById(R.id.detail_fragment_container) != null) {
            mTwoPanel = true;
        }
    }

    // save in content provider
    @Override
    protected void onPause() {
        super.onPause();
        if(MainActivityFragment.movies == null)
            return;
        for (int i = 0; i < MainActivityFragment.movies.size(); i++) {
            MovieInfo temp = MainActivityFragment.movies.get(i);
            getContentResolver().update(MovieContract.MovieInfoEntry.CONTENT_URI,
                    temp.toContentValues(),
                    "movie_id = ?", new String[]{"" + temp.getMovieId()});
        }
    }

    @Override
    public void onItemSelected(MovieInfo m) {
        movieInfo = m;
        if (mTwoPanel) {
            final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    changeFab(fab, false, view);
                }
            });
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, DetailFragment.newInstance(movieInfo), "detail")
                    .commit();
            like = movieInfo.getLike();
            changeFab(fab, true, null);
        } else {
            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("movie_info", movieInfo);
            startActivity(intent);
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


    // don't call content provider to update like column.
    // But in activity's onPause() dose this.
    @Override
    public void saveLikeState() {
        if (movieInfo != null) {
            movieInfo.setLike(like);
        }
    }

}
