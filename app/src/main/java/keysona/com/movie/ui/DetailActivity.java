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
import keysona.com.movie.data.MovieInfo;


public class DetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Intent intent = getIntent();
        if (intent != null){
            MovieInfo movieInfo = intent.getParcelableExtra("movie_info");
            ImageView posterImageView = (ImageView)findViewById(R.id.poster);
            Picasso.with(this).load(movieInfo.getPosterPath()).into(posterImageView);
            Bundle bundle = new Bundle();
            bundle.putParcelable("movie_info", movieInfo);
            Fragment ff = new DetailFragment();
            ff.setArguments(bundle);
            getFragmentManager()
                    .beginTransaction()
                    .replace(R.id.detail_fragment_container, ff)
                    .commit();
        }
    }
}
