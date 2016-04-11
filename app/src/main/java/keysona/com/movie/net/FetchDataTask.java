package keysona.com.movie.net;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.AsyncTask;
import android.preference.PreferenceManager;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;
import java.util.Set;

import keysona.com.movie.R;
import keysona.com.movie.Utility;
import keysona.com.movie.data.MovieContract;
import timber.log.Timber;

/**
 * Created by key on 16-4-6.
 */
public class FetchDataTask extends AsyncTask<Integer, Void, Integer> {

    private Context mContext;

    public static final int MOVIE_LIST = 0;

    public static final int MOVIE_REVIEW = 1;

    public static final int MOVIE_VIDEO = 2;

    public static final int MOVIE_INFO = 3;

    public static final String TAG = "FetchTask";

    public static final String[] COLS_MOVIE_INFO = {
            MovieContract.MovieInfoEntry._ID,
            MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieInfoEntry.COLUMN_ORIGINAL_TITLE,
            MovieContract.MovieInfoEntry.COLUMN_RELEASE_DATE,
            MovieContract.MovieInfoEntry.COLUMN_OVERVIEW,
            MovieContract.MovieInfoEntry.COLUMN_POSTER_PATH,
            MovieContract.MovieInfoEntry.COLUMN_VOTE_COUNT,
            MovieContract.MovieInfoEntry.COLUMN_POPULARITY,
            MovieContract.MovieInfoEntry.COLUMN_VOTE_AVERAGE,
            MovieContract.MovieInfoEntry.COLUMN_LIKE
    };

    public static final String[] COLS_MOVIE_REVIEW = {
            MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieReviewEntry.COLUMN_REVIEW_ID,
            MovieContract.MovieReviewEntry.COLUMN_AUTHOR,
            MovieContract.MovieReviewEntry.COLUMN_URL,
            MovieContract.MovieReviewEntry.COLUMN_CONTENT
    };

    public static final String[] COLS_MOVIE_VIDEO = {
            MovieContract.MovieVideoEntry.COLUMN_MOVIE_ID,
            MovieContract.MovieVideoEntry.COLUMN_VIDEO_ID,
            MovieContract.MovieVideoEntry.COLUMN_NAME,
            MovieContract.MovieVideoEntry.COLUMN_TYPE,
            MovieContract.MovieVideoEntry.COLUMN_SIZE,
            MovieContract.MovieVideoEntry.COLUMN_SITE,
            MovieContract.MovieVideoEntry.COLUMN_KEY,
            MovieContract.MovieVideoEntry.COLUMN_ISO_639_1
    };

    public static final int MOVIE_VIDEO_COL_MOVIE_ID = 0;
    public static final int MOVIE_VIDEO_COL_VIDEO_ID = 1;
    public static final int MOVIE_VIDEO_COL_NAME = 2;
    public static final int MOVIE_VIDEO_COL_TYPE = 3;
    public static final int MOVIE_VIDEO_COL_SIZE = 4;
    public static final int MOVIE_VIDEO_COL_SITE = 5;
    public static final int MOVIE_VIDEO_COL_KEY = 6;
    public static final int MOVIE_VIDEO_COL_ISO_639_1 = 7;

    public static final int MOVIE_REVIEW_COL_MOVIE_ID = 0;
    public static final int MOVIE_REVIEW_REVIEW_ID = 1;
    public static final int MOVIE_REVIEW_AUTHOR = 2;
    public static final int MOVIE_REVIEW_URL = 3;
    public static final int MOVIE_REVIEW_CONTENT = 4;


    public static final int MOVIE_INFO_COL_ID = 0;
    public static final int MOVIE_INFO_COL_MOVIE_ID = 1;
    public static final int MOVIE_INFO_COL_ORIGINAL_TITLE = 2;
    public static final int MOVIE_INFO_COL_RELEASE_DATE = 3;
    public static final int MOVIE_INFO_COL_OVERVIEW = 4;
    public static final int MOVIE_INFO_COL_POSTER_PATH = 5;
    public static final int MOVIE_INFO_COL_VOTE_COUNT = 6;
    public static final int MOVIE_INFO_COL_POPULARITY = 7;
    public static final int MOVIE_INFO_COL_VOTE_AVERAGE = 8;
    public static final int MOVIE_INFO_COL_LIKE = 9;

    public FetchDataTask(Context context) {
        super();
        mContext = context;
    }

    @Override
    protected Integer doInBackground(Integer... params) {
        String url;
        String jsonData;
        int movieId;

        switch (params[0].intValue()) {
            case MOVIE_LIST:
                url = Utility.getMoviesUrl(getMovieSortType());
                jsonData = NetworkConnector.get(url);
                Timber.tag(TAG).d("save data : " + jsonData);
                saveMoviesInfoFromJson(jsonData);
                break;
            case MOVIE_REVIEW:
                movieId = params[1].intValue();
                Timber.tag(TAG).d("movie id : " + movieId);
                url = Utility.getMovieReviewUrl(movieId);
                jsonData = NetworkConnector.get(url);
                saveMovieReviewsFromJsons(jsonData, movieId);
                break;
            case MOVIE_VIDEO:
                movieId = params[1].intValue();
                Timber.tag(TAG).d("movie id : " + movieId);
                url = Utility.getMovieVideoUrl(movieId);
                jsonData = NetworkConnector.get(url);
                saveMovieVideosFromJsons(jsonData, movieId);
                break;

            case MOVIE_INFO:
                movieId = params[1].intValue();
                return getMovieRuntime(movieId);
        }
        return null;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if (integer != null) {
            TextView runtimeTextView = (TextView) ((Activity) mContext).findViewById(R.id.runtime);
            runtimeTextView.setText("" + integer + " Min");
        }
    }

    private void saveMoviesInfoFromJson(String data) {
        try {
            JSONObject temp = new JSONObject(data);
            JSONArray moviesInfoJsonArray = temp.getJSONArray("results");
            ContentValues[] movies = new ContentValues[moviesInfoJsonArray.length()];
            for (int i = 0; i < moviesInfoJsonArray.length(); i++) {
                JSONObject movieInfoJson = moviesInfoJsonArray.getJSONObject(i);
                movies[i] = makeMovieInfoContentValue(movieInfoJson);
                Timber.tag(TAG).d("MovieInfo - ContentValue : " + formatContentValues(movies[i]));
            }
            Timber.tag(TAG).d("MovieInfo - JSONArray length : " + moviesInfoJsonArray.length());
            ContentResolver provider = mContext.getContentResolver();
            provider.bulkInsert(MovieContract.MovieInfoEntry.CONTENT_URI, movies);
            Cursor cursor = provider.query(MovieContract.MovieInfoEntry.CONTENT_URI, COLS_MOVIE_INFO, null, null, null);
            Timber.d("total record : " + cursor.getCount());
            while (cursor.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                sb.append(cursor.getInt(MOVIE_INFO_COL_MOVIE_ID) + "\t");
                sb.append(cursor.getString(MOVIE_INFO_COL_ORIGINAL_TITLE) + "\t");
                sb.append(cursor.getString(MOVIE_INFO_COL_OVERVIEW) + "\t");
                sb.append(cursor.getString(MOVIE_INFO_COL_POSTER_PATH) + "\t");
                sb.append(cursor.getDouble(MOVIE_INFO_COL_POPULARITY) + "\t");
                sb.append(cursor.getDouble(MOVIE_INFO_COL_VOTE_AVERAGE) + "\t");
                sb.append(cursor.getInt(MOVIE_INFO_COL_VOTE_COUNT) + "\t");
                sb.append(cursor.getString(MOVIE_INFO_COL_RELEASE_DATE) + "\t");
                sb.append(cursor.getInt(MOVIE_INFO_COL_LIKE) + "\t");
                Timber.tag(TAG).d("MovieInfo cursor data : " + sb.toString());
            }
            cursor.close();
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.tag(TAG).e("MovieInfo - JSON error : " + e.toString());
        }
    }

    private void saveMovieReviewsFromJsons(String data, int movieId) {
        try {
            JSONObject temp = new JSONObject(data);
            if (temp.getInt("total_results") == 0)
                return;
            JSONArray reviewsJsonArray = temp.getJSONArray("results");
            Timber.tag(TAG).d("json array length : " + reviewsJsonArray.length());
            ContentValues[] reviews = new ContentValues[reviewsJsonArray.length()];
            for (int i = 0; i < reviewsJsonArray.length(); i++) {
                JSONObject reviewJson = reviewsJsonArray.getJSONObject(i);
                reviews[i] = makeMovieReviewContentValue(reviewJson, movieId);
                Timber.tag(TAG).d("MovieReview - ContentValue : " + formatContentValues(reviews[i]));
            }
            ContentResolver provider = mContext.getContentResolver();
            provider.bulkInsert(MovieContract.MovieReviewEntry.CONTENT_URI, reviews);
            Cursor cursor = provider.query(MovieContract.MovieReviewEntry.buildReviewWithMovieIdUri(movieId), COLS_MOVIE_REVIEW, null, null, null);
            while (cursor.moveToNext()) {
                StringBuilder sb = new StringBuilder();
                sb.append(cursor.getString(MOVIE_REVIEW_AUTHOR) + "\t");
                sb.append(cursor.getString(MOVIE_REVIEW_REVIEW_ID) + "\t");
                sb.append(cursor.getString(MOVIE_REVIEW_COL_MOVIE_ID) + "\t");
                sb.append(cursor.getString(MOVIE_REVIEW_CONTENT) + "\t");
                sb.append(cursor.getString(MOVIE_REVIEW_URL));
                Timber.tag(TAG).d("MovieReview cursor data : " + sb.toString());
            }
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.tag(TAG).e("json error : " + e.toString());
        }
    }

    private void saveMovieVideosFromJsons(String data, int movieId) {
        try {
            JSONObject temp = new JSONObject(data);
            JSONArray videosJsonArray = temp.getJSONArray("results");
            if (videosJsonArray.length() == 0)
                return;
            ContentValues[] videos = new ContentValues[videosJsonArray.length()];
            for (int i = 0; i < videosJsonArray.length(); i++) {
                JSONObject reviewJson = videosJsonArray.getJSONObject(i);
                videos[i] = makeMovieVideoContentValue(reviewJson, movieId);
                Timber.tag(TAG).d("MovieVideos - ContentValues : " + formatContentValues(videos[i]));
            }
            ContentResolver provider = mContext.getContentResolver();
            provider.bulkInsert(MovieContract.MovieVideoEntry.CONTENT_URI, videos);
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.tag(TAG).e("json error : " + e.toString());
        }
    }

    private void testMovieInfoInsert() {
        ContentResolver provider = mContext.getContentResolver();
        Cursor cursor = provider.query(MovieContract.MovieInfoEntry.CONTENT_URI, COLS_MOVIE_INFO, null, null, null);
        Timber.d("total record : " + cursor.getCount());
        while (cursor.moveToNext()) {
            StringBuilder sb = new StringBuilder();
            sb.append(cursor.getInt(MOVIE_INFO_COL_MOVIE_ID) + "\t");
            sb.append(cursor.getString(MOVIE_INFO_COL_ORIGINAL_TITLE) + "\t");
            sb.append(cursor.getString(MOVIE_INFO_COL_OVERVIEW) + "\t");
            sb.append(cursor.getString(MOVIE_INFO_COL_POSTER_PATH) + "\t");
            sb.append(cursor.getDouble(MOVIE_INFO_COL_POPULARITY) + "\t");
            sb.append(cursor.getDouble(MOVIE_INFO_COL_VOTE_AVERAGE) + "\t");
            sb.append(cursor.getInt(MOVIE_INFO_COL_VOTE_COUNT) + "\t");
            sb.append(cursor.getString(MOVIE_INFO_COL_RELEASE_DATE) + "\t");
            sb.append(cursor.getInt(MOVIE_INFO_COL_LIKE) + "\t");
            Timber.tag(TAG).d("MovieInfo cursor data : " + sb.toString());
        }
        cursor.close();
    }

    private void testMovieReviewInsert(int movieId) {
        ContentResolver provider = mContext.getContentResolver();
        Cursor cursor = provider.query(MovieContract.MovieReviewEntry.buildReviewWithMovieIdUri(movieId), COLS_MOVIE_REVIEW, null, null, null);
        while (cursor.moveToNext()) {
            StringBuilder sb = new StringBuilder();
            sb.append(cursor.getString(MOVIE_REVIEW_AUTHOR) + "\t");
            sb.append(cursor.getString(MOVIE_REVIEW_REVIEW_ID) + "\t");
            sb.append(cursor.getString(MOVIE_REVIEW_COL_MOVIE_ID) + "\t");
            sb.append(cursor.getString(MOVIE_REVIEW_CONTENT) + "\t");
            sb.append(cursor.getString(MOVIE_REVIEW_URL));
            Timber.tag(TAG).d("MovieReview cursor data : " + sb.toString());
        }
        cursor.close();
    }

    private void testMovieVideoInsert(int movieId) {
        ContentResolver provider = mContext.getContentResolver();
        Cursor c = provider.query(MovieContract.MovieVideoEntry.buildVideoWithMovieIdUri(movieId), COLS_MOVIE_VIDEO, null, null, null);
        Timber.tag(TAG).d("MovieVideo total record : " + c.getCount());
        while (c.moveToNext()) {
            StringBuilder sb = new StringBuilder();
            sb.append(c.getString(MOVIE_VIDEO_COL_NAME) + "\t");
            sb.append(c.getInt(MOVIE_VIDEO_COL_MOVIE_ID) + "\t");
            sb.append(c.getString(MOVIE_VIDEO_COL_VIDEO_ID) + "\t");
            sb.append(c.getString(MOVIE_VIDEO_COL_SITE) + "\t");
            sb.append(c.getString(MOVIE_VIDEO_COL_KEY) + "\t");
            sb.append(c.getInt(MOVIE_VIDEO_COL_SIZE) + "\t");
            sb.append(c.getString(MOVIE_VIDEO_COL_TYPE) + "\t");
            sb.append(c.getString(MOVIE_VIDEO_COL_ISO_639_1) + "\t");
            Timber.tag(TAG).d("MovieVideo cursor data : " + sb.toString());
        }
        c.close();
    }

    private ContentValues makeMovieVideoContentValue(JSONObject video, int movieId) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_VIDEO_ID, video.getString("id"));
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_ISO_639_1, video.getString("iso_639_1"));
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_MOVIE_ID, movieId);
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_SIZE, video.getInt("size"));
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_SITE, video.getString("site"));
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_TYPE, video.getString("type"));
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_NAME, video.getString("name"));
            contentValues.put(MovieContract.MovieVideoEntry.COLUMN_KEY, video.getString("key"));

        } catch (JSONException e) {
            e.printStackTrace();
            Timber.tag(TAG).e("MovieVideo - json error : " + e.toString());
        }
        return contentValues;
    }


    private ContentValues makeMovieReviewContentValue(JSONObject review, int movieId) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID, movieId);
            contentValues.put(MovieContract.MovieReviewEntry.COLUMN_AUTHOR, review.getString("author"));
            contentValues.put(MovieContract.MovieReviewEntry.COLUMN_URL, review.getString("url"));
            contentValues.put(MovieContract.MovieReviewEntry.COLUMN_CONTENT, review.getString("content"));
            contentValues.put(MovieContract.MovieReviewEntry.COLUMN_REVIEW_ID, review.getString("id"));
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.tag(TAG).e("MovieReview - json error : " + e.toString());
        }
        return contentValues;
    }

    private ContentValues makeMovieInfoContentValue(JSONObject movie) {
        ContentValues contentValues = new ContentValues();
        try {
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID, movie.getInt("id"));
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_VOTE_COUNT, movie.getInt("vote_count"));
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_RELEASE_DATE, movie.getString("release_date"));
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_ORIGINAL_TITLE, movie.getString("original_title"));
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_VOTE_AVERAGE, movie.getDouble("vote_average"));
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_POSTER_PATH, movie.getString("poster_path"));
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_OVERVIEW, movie.getString("overview"));
            contentValues.put(MovieContract.MovieInfoEntry.COLUMN_POPULARITY, movie.getDouble("popularity"));
        } catch (JSONException e) {
            e.printStackTrace();
            Timber.tag(TAG).e("MovieInfo - json error", e.toString());
        }
        return contentValues;
    }

    private String getMovieSortType() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(mContext);
        String sortType = sharedPreferences.getString(mContext.getString(R.string.pref_sort_key)
                , mContext.getString(R.string.pref_sort_default));

        Timber.d("movie sort type : " + sortType);
        return sortType;
    }

    private String formatContentValues(ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        StringBuilder res = new StringBuilder();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            res.append(columnName + " : " + entry.getValue() + "\t");
        }
        Timber.tag(TAG).d("result str : " + res.toString());
        return res.toString();
    }


    private int getMovieRuntime(int movieId) {
        String url = Utility.getMovieInfoUrl(movieId);
        String data = NetworkConnector.get(url);
        int runtime = 0;
        try {
            JSONObject temp = new JSONObject(data);
            runtime = temp.getInt("runtime");
            Timber.d("runtime : " + runtime);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return runtime;
    }


}
