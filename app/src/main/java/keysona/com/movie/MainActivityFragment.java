package keysona.com.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import keysona.com.movie.data.Config;
import keysona.com.movie.data.Movie;
import keysona.com.movie.data.MovieAdapter;
import keysona.com.movie.net.NetworkConnector;

/**
 * A placeholder fragment containing a simple view.
 */
public class MainActivityFragment extends Fragment {

    private ArrayList<Movie> movies = new ArrayList<Movie>();

    private MovieAdapter movieAdapter;

    private static String TAG = "MainActivityFragment";

    public MainActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        new FetchPopularMoviesTsk().execute();
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_main, container, false);
        movieAdapter = new MovieAdapter(getActivity(), movies);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager = new GridLayoutManager(getActivity(),3);

        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(movieAdapter);
        Log.d("Test", recyclerView == null ? "null" : "object");
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

    class FetchPopularMoviesTsk extends AsyncTask<Void, Void, Void> {

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            movieAdapter.notifyDataSetChanged();
        }

        @Override
        protected Void doInBackground(Void... params) {

            //constuct popular movie url
            String url = getMoviesUrl(getMovieSortType());
            String jsonData = NetworkConnector.get(url);
            getMoviesFromJsons(jsonData);
            return null;
        }

        private String getMoviesUrl(String sortType) {

            /*
            * example : http://api.themoviedb.org/3/movie/popular?api_key=[YOUR_API_KEY]
            * */
            String url = new Uri.Builder()
                    .scheme("http")
                    .authority(Config.MOVIE_WEBSITE)
                    .appendPath("3")
                    .appendPath("movie")
                    .appendPath(sortType)
                    .appendQueryParameter("api_key", Config.API_KEY)
                    .build().toString();
            Log.d(TAG, url);
            return url;

        }

        private void getMoviesFromJsons(String data) {
            try {
                JSONObject temp = new JSONObject(data);
                JSONArray moviesJson = temp.getJSONArray("results");
                for (int i = 0; i < moviesJson.length(); i++) {
                    JSONObject movieJson = moviesJson.getJSONObject(i);
                    movies.add(Movie.fromJson(movieJson));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        private String getMovieSortType() {
            SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortType = sharedPreferences.getString(getString(R.string.pref_sort_key)
                    , getString(R.string.pref_sort_default));

            Log.d(TAG, sortType);
            return sortType;
        }
    }


}
