package keysona.com.movie.net;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import keysona.com.movie.R;
import keysona.com.movie.Utility;
import keysona.com.movie.data.Movie;
import keysona.com.movie.ui.MainActivityFragment;
import timber.log.Timber;

/**
 * Created by key on 16-4-5.
 */
public class FetchMoviesListTask extends AsyncTask<Void, Void, Void> {

    private Context mContext;

    public FetchMoviesListTask(Context c){
       mContext = c;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        MainActivityFragment.movieAdapter.notifyDataSetChanged();
    }

    @Override
    protected Void doInBackground(Void... params) {

        //constuct popular movie url
        String url = Utility.getMoviesUrl(getMovieSortType());
        String jsonData = NetworkConnector.get(url);
        getMoviesFromJsons(jsonData);
        return null;
    }

    private void getMoviesFromJsons(String data) {
        try {
            JSONObject temp = new JSONObject(data);
            JSONArray moviesJson = temp.getJSONArray("results");
            for (int i = 0; i < moviesJson.length(); i++) {
                JSONObject movieJson = moviesJson.getJSONObject(i);
                MainActivityFragment.movies.add(Movie.fromJson(movieJson));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private String getMovieSortType() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortType = sharedPreferences.getString(mContext.getString(R.string.pref_sort_key)
                , mContext.getString(R.string.pref_sort_default));

        Timber.d("movie sort type : " + sortType);
        return sortType;
    }
}
