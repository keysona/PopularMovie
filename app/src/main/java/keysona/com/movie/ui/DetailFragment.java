package keysona.com.movie.ui;

import android.app.Fragment;
import android.app.LoaderManager;
import android.content.CursorLoader;
import android.content.Loader;
import android.database.Cursor;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

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

    private MovieInfo movieInfo;

    private ArrayList<MovieVideo> movieVideos;

    private ArrayList<MovieReview> movieReviews;

    private MovieReviewAdapter movieReviewAdapter;

    private MovieVideoAdapter movieVideoAdapter;

    private static final int LOADER_VIDEOS = 0;

    private static final int LOADER_REVIEWS = 1;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        movieVideos = new ArrayList<MovieVideo>();
        movieReviews = new ArrayList<MovieReview>();

        Bundle bundle = getArguments();
        movieInfo = bundle.getParcelable("movie_info");
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
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
        getLoaderManager().initLoader(LOADER_VIDEOS, null, this);
        getLoaderManager().initLoader(LOADER_REVIEWS, null, this);
    }

    @Override
    public void onPause() {
        super.onPause();
        Timber.d("hello");
        ((DetailActivity )getActivity()).saveLikeState();
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

    interface saveLikeState {
        public void saveLikeState();
    }
}

