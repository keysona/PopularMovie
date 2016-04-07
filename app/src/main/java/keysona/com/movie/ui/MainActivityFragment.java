package keysona.com.movie.ui;


import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;

import keysona.com.movie.Adapter.MovieAdapter;
import keysona.com.movie.R;
import keysona.com.movie.data.Config;
import keysona.com.movie.data.MovieContract;
import keysona.com.movie.data.MovieInfo;
import keysona.com.movie.net.FetchDataTask;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment implements LoaderManager.LoaderCallbacks<Cursor>{

    public static MovieAdapter movieAdapter;

    public static ArrayList<MovieInfo> movies;

    private static final String POSTER_SIZE = "poster_size";

    private int screenWidth;

    private String posterSize;

    private static String TAG = "MainActivityFragment";

    private static final int testMovieId = 118340;

    private static final String SORT_TYPE = "sort";

    private static final String SORT_TYPE_POPULAR = "popular";

    private static final String SORT_TYPE_TOP_RATED = "top_rated";

    private static final String SORT_TYPE_LIKE = "like";

    private static final int LOADER_ID = 0;

    public MainActivityFragment() {
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Bundle bundle = new Bundle();
        SharedPreferences pref = PreferenceManager.getDefaultSharedPreferences(getContext());
        String sortType = pref.getString(getString(R.string.pref_sort_key), getString(R.string.pref_sort_default));
        bundle.putString(SORT_TYPE,sortType);
        getLoaderManager().initLoader(LOADER_ID, bundle, this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchDataTask(getContext()).execute(FetchDataTask.MOVIE_LIST);
        setHasOptionsMenu(true);

        // According to screen size,determine span count of GridLayoutManager.
        screenWidth = getScreenWidth();
        Timber.tag(TAG).d("calucate screen width : %s", screenWidth);
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
        if (sharedPreferences.contains(POSTER_SIZE)) {
            posterSize = sharedPreferences.getString("poster_size", Config.POSTER_SIZE_SMALL);
            Timber.tag(TAG).d("In sharedPreferences get poster size : %s", posterSize);
        } else {
            posterSize = getPosterSize(screenWidth);
            Timber.tag(TAG).d("calucate poster size : %s", posterSize);
        }
        sharedPreferences.edit()
                .putString(POSTER_SIZE, posterSize)
                .commit();
        Timber.tag(TAG).d("save poster size in prefs");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        //calculate span count in run time.
        int spanCount = getSpanCount(posterSize, screenWidth);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(), spanCount);
        Timber.tag(TAG).d("spanCount : " + spanCount);
        movies = new ArrayList<MovieInfo>();
        movieAdapter = new MovieAdapter(getContext(),movies,posterSize);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(movieAdapter);
        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent intent = new Intent(getActivity(), SettingActivity.class);
            startActivity(intent);
        }
        return super.onOptionsItemSelected(item);
    }

    private DisplayMetrics getDisplayMetric() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager()
                .getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics;
    }

    private int getScreenWidth() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().
                getDefaultDisplay().getMetrics(displayMetrics);
        return displayMetrics.widthPixels;
    }

    private String getPosterSize(int width) {
        if ((width / 342.0) > 2) {
            return Config.POSTER_SIZE_LARGE;
        }
        return Config.POSTER_SIZE_SMALL;
    }

    private int getSpanCount(String size, int width) {
        double temp = 0;
        switch (size) {
            case Config.POSTER_SIZE_SMALL:
                temp = width / 185.0;
                break;
            case Config.POSTER_SIZE_LARGE:
                temp = width / 342.0;
                break;
        }
        return (int) (temp + 0.2);
    }

    @Override
    public android.support.v4.content.Loader<Cursor> onCreateLoader(int id, Bundle args) {

        String sortType = args.getString(SORT_TYPE);

        String sortOrder = null;

        switch(sortType){
            case SORT_TYPE_TOP_RATED:
                sortOrder = MovieContract.MovieInfoEntry.COLUMN_VOTE_AVERAGE + " ASC";
                break;
            case SORT_TYPE_LIKE:
                sortOrder = MovieContract.MovieInfoEntry.COLUMN_LIKE + " ASC";
                break;
            case SORT_TYPE_POPULAR:
                sortOrder = MovieContract.MovieInfoEntry.COLUMN_POPULARITY + " ASC";
                break;
        }

        return new CursorLoader(
                getContext(),
                MovieContract.MovieInfoEntry.CONTENT_URI,
                FetchDataTask.COLS_MOVIE_INFO,
                null,
                null,
                sortOrder
        );
    }

    @Override
    public void onLoadFinished(android.support.v4.content.Loader<Cursor> loader, Cursor data) {
        movies.clear();
        if(data == null)
            return;
        while(data.moveToNext()){
            MovieInfo temp = MovieInfo.fromCursor(data);
            temp.setPosterPath(temp.buildImageUrl(posterSize));
            movies.add(temp);
        }

        Timber.d("movies List:" + movies.size());
        Timber.d("movies Cursor data : " + data.getCount());
        movieAdapter.notifyDataSetChanged();
    }

    @Override
    public void onLoaderReset(android.support.v4.content.Loader<Cursor> loader) {
        movies.clear();
        movieAdapter.notifyDataSetChanged();
    }
}
