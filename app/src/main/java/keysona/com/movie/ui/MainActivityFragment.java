package keysona.com.movie.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
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

import keysona.com.movie.R;
import keysona.com.movie.data.Config;
import keysona.com.movie.data.Movie;
import keysona.com.movie.data.MovieAdapter;
import keysona.com.movie.net.FetchMoviesListTask;
import timber.log.Timber;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    public static ArrayList<Movie> movies = new ArrayList<Movie>();

    public static  MovieAdapter movieAdapter;

    private static final String POSTER_SIZE = "poster_size";

    private int screenWidth;

    private String posterSize;

    private static String TAG = "MainActivityFragment";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchMoviesListTask(getActivity()).execute();
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

        movieAdapter = new MovieAdapter(getActivity(), posterSize, movies);

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


}
