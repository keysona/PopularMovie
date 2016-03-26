package keysona.com.movie;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;

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

    private ArrayAdapter<Movie> movieArrayAdapter;

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
        movieArrayAdapter = new MovieAdapter(getActivity(), R.layout.thumb_view, movies);

        GridView gridView = (GridView) view.findViewById(R.id.grid_view);
        Log.d("Test", gridView == null ? "null" : "object");
        gridView.setAdapter(movieArrayAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movie movie = movies.get(position);
                Log.d(TAG, movie.toString());
                Intent intent = new Intent(getActivity(), DetailActivity.class);
                intent.putExtra("movie", movie);
                startActivity(intent);
            }
        });
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
            movieArrayAdapter.notifyDataSetChanged();
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
