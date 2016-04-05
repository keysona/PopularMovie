package keysona.com.movie;

import android.net.Uri;

import keysona.com.movie.data.Config;
import timber.log.Timber;

/**
 * Created by key on 16-4-5.
 */
public class Utility {

    public static final String REVIEWS_INFO_MOVIE = "reviews";

    public static final String VIDEOS_INFO_MOVIE = "videos";

    public static String getMoviesUrl(String sortType) {
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
        Timber.d("movies url : " + url);
        return url;
    }

    public static String getMovieVideoUrl(int id) {
        String url = buildMovieInfoUrl(id,VIDEOS_INFO_MOVIE);
        Timber.d("movie video url : " + url);
        return url;
    }

    public static String getMovieReviewUrl(int id) {
        String url = buildMovieInfoUrl(id, REVIEWS_INFO_MOVIE);
        Timber.d("movie reviews url : " + url);
        return url;
    }

    private static String buildMovieInfoUrl(int id, String type) {
        return new Uri.Builder()
                .scheme("http")
                .authority(Config.MOVIE_WEBSITE)
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + id)
                .appendPath(type)
                .appendQueryParameter("api_key", Config.API_KEY)
                .build().toString();

    }


}
