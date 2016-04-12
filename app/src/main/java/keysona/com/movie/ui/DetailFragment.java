package keysona.com.movie.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import keysona.com.movie.R;
import keysona.com.movie.adapter.MovieReviewAdapter;
import keysona.com.movie.adapter.MovieVideoAdapter;
import keysona.com.movie.data.MovieContract;
import keysona.com.movie.data.MovieInfo;
import keysona.com.movie.data.MovieReview;
import keysona.com.movie.data.MovieVideo;
import keysona.com.movie.net.FetchDataTask;
import timber.log.Timber;


/**
 * Created by key on 16-4-7.
 */
public class DetailFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor> {

    // loader id
    private static final int LOADER_VIDEOS = 0;
    private static final int LOADER_REVIEWS = 1;

    // star
    private static final int HALF_STAR = 0;
    private static final int FULL_STAR = 1;

    // movie data
    private MovieInfo movieInfo;
    private ArrayList<MovieVideo> movieVideos;
    private ArrayList<MovieReview> movieReviews;
    private MovieReviewAdapter movieReviewAdapter;
    private MovieVideoAdapter movieVideoAdapter;

    private ShareActionProvider mShareActionProvider;

    public static DetailFragment newInstance(MovieInfo movieInfo) {
        Bundle args = new Bundle();
        args.putParcelable("movie_info", movieInfo);
        DetailFragment fragment = new DetailFragment();
        fragment.setArguments(args);
        return fragment;
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        setHasOptionsMenu(true);

        movieVideos = new ArrayList<MovieVideo>();
        movieReviews = new ArrayList<MovieReview>();

        Bundle bundle = getArguments();
        movieInfo = bundle.getParcelable("movie_info");

        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        //titile text view
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        if (MainActivity.mTwoPanel) {
            titleTextView = (TextView) view.findViewById(R.id.title);
            titleTextView.setText(movieInfo.getOriginalTitle());
        } else {
            titleTextView.setVisibility(View.GONE);
            new FetchDataTask(getActivity()).execute(FetchDataTask.MOVIE_IMAGE, movieInfo.getMovieId());
        }

        // handle stars
        handleStars(view, movieInfo);
        TextView voteAvgTextView = (TextView) view.findViewById(R.id.vote_average);
        voteAvgTextView.setText("" + movieInfo.getVoteAverage());

        //poster
        ImageView posterImageView = (ImageView) view.findViewById(R.id.poster);
        Picasso.with(getActivity()).load(movieInfo.getPosterPath()).into(posterImageView);

        //date
        TextView dateTextView = (TextView) view.findViewById(R.id.date);
        dateTextView.setText(movieInfo.getReleaseDate());

        TextView overviewTextView = (TextView) view.findViewById(R.id.overview);
        overviewTextView.setText(movieInfo.getOverview());

        RecyclerView reviewRecycleView = (RecyclerView) view.findViewById(R.id.review);
        RecyclerView videoRecycleView = (RecyclerView) view.findViewById(R.id.trailers);


        //set LayoutManger
        RecyclerView.LayoutManager reviewLinearLayoutManager = new LinearLayoutManager(getActivity());
        RecyclerView.LayoutManager videoLinearLayoutManager = new LinearLayoutManager(getActivity());
        reviewRecycleView.setLayoutManager(reviewLinearLayoutManager);
        videoRecycleView.setLayoutManager(videoLinearLayoutManager);

        //set Adapter
        movieReviewAdapter = new MovieReviewAdapter(getActivity(), movieReviews);
        movieVideoAdapter = new MovieVideoAdapter(getActivity(), movieVideos);
        reviewRecycleView.setAdapter(movieReviewAdapter);
        videoRecycleView.setAdapter(movieVideoAdapter);

        return view;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        new FetchDataTask(getActivity()).execute(FetchDataTask.MOVIE_REVIEW, movieInfo.getMovieId());
        new FetchDataTask(getActivity()).execute(FetchDataTask.MOVIE_VIDEO, movieInfo.getMovieId());
        new FetchDataTask(getActivity()).execute(FetchDataTask.MOVIE_INFO, movieInfo.getMovieId());
        getLoaderManager().initLoader(LOADER_VIDEOS, null, this);
        getLoaderManager().initLoader(LOADER_REVIEWS, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("DetailFragment : onPause()");
        if (MainActivity.mTwoPanel) {
            ((MainActivity) getActivity()).saveLikeState();
        } else {
            ((DetailActivity) getActivity()).saveLikeState();
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_detail, menu);

        MenuItem menuItem = menu.findItem(R.id.action_share);

        mShareActionProvider = (ShareActionProvider) MenuItemCompat.getActionProvider(menuItem);
        if (movieInfo != null && mShareActionProvider != null) {
            mShareActionProvider.setShareIntent(createShareMovieIntent());
        }

        if (mShareActionProvider == null) {
            Timber.d("mShareActionProvider is null");
        }
    }

    private Intent createShareMovieIntent() {
        Intent shareIntent = new Intent(Intent.ACTION_SEND);
        shareIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_DOCUMENT);
        shareIntent.setType("text/plain");
        shareIntent.putExtra(Intent.EXTRA_TEXT, movieInfo.getOriginalTitle());
        return shareIntent;
    }

    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        CursorLoader cursorLoader = null;
        switch (id) {
            case LOADER_VIDEOS:
                cursorLoader = new CursorLoader(
                        getActivity(),
                        MovieContract.MovieVideoEntry.buildVideoWithMovieIdUri(movieInfo.getMovieId()),
                        FetchDataTask.COLS_MOVIE_VIDEO,
                        null,
                        null,
                        null
                );
                break;

            case LOADER_REVIEWS:
                cursorLoader = new CursorLoader(
                        getActivity(),
                        MovieContract.MovieReviewEntry.buildReviewWithMovieIdUri(movieInfo.getMovieId()),
                        FetchDataTask.COLS_MOVIE_REVIEW,
                        null,
                        null,
                        null
                );
                break;
        }
        return cursorLoader;
    }

    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

        switch (loader.getId()) {
            case LOADER_VIDEOS:
                // clear list
                movieVideos.clear();
                Timber.d("loader id : " + loader.getId() + " data : " + data.getCount());
                while (data.moveToNext()) {
                    MovieVideo movieVideo = MovieVideo.fromCursor(data);
                    Timber.d("Movie Video : " + movieVideo);
                    movieVideos.add(movieVideo);
                }
                // notify data changed
                movieVideoAdapter.notifyDataSetChanged();
                break;
            case LOADER_REVIEWS:
                // clear list
                movieReviews.clear();
                while (data.moveToNext()) {
                    MovieReview movieReview = MovieReview.fromCursor(data);
                    movieReviews.add(movieReview);
                }

                // notify data changed.
                movieReviewAdapter.notifyDataSetChanged();
                break;
        }
    }

    @Override
    public void onLoaderReset(Loader<Cursor> loader) {
        switch (getId()) {
            case LOADER_VIDEOS:
                movieVideos.clear();
                movieVideoAdapter.notifyDataSetChanged();
                break;
            case LOADER_REVIEWS:
                movieReviews.clear();
                movieReviewAdapter.notifyDataSetChanged();
                break;
        }
    }

    private void handleStars(View view, MovieInfo movieInfo) {
        ImageView[] stars = new ImageView[]{
                (ImageView) view.findViewById(R.id.star_1),
                (ImageView) view.findViewById(R.id.star_2),
                (ImageView) view.findViewById(R.id.star_3),
                (ImageView) view.findViewById(R.id.star_4),
                (ImageView) view.findViewById(R.id.star_5)
        };
        double voteAverage = movieInfo.getVoteAverage();
        int index = 0;
        while (voteAverage >= 0 && index < 5) {
            if (voteAverage - 2 >= 0) {
                voteAverage -= 2;
                selectStarIcon(stars[index], FULL_STAR);
            } else if (voteAverage - 1 >= 0) {
                voteAverage -= 1;
                selectStarIcon(stars[index], HALF_STAR);
            }
            index++;
        }

    }

    private void selectStarIcon(ImageView imageView, int starType) {
        switch (starType) {
            case HALF_STAR:
                imageView.setImageResource(R.drawable.star_half);
                break;
            case FULL_STAR:
                imageView.setImageResource(R.drawable.star_full);
                break;
        }
    }

    // call back for activity to notify floating action button change.
    interface saveLikeState {
        public void saveLikeState();
    }
}

