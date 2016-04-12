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

    public static final String MOVIE_INFO = "";

    public static final String MOVIE_IMAGE = "movies";

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
        String url = buildMovieInfoUrl(id, VIDEOS_INFO_MOVIE);
        Timber.d("movie video url : " + url);
        return url;
    }

    public static String getMovieReviewUrl(int id) {
        String url = buildMovieInfoUrl(id, REVIEWS_INFO_MOVIE);
        Timber.d("movie reviews url : " + url);
        return url;
    }

    public static String getMovieInfoUrl(int id){
        String url = buildMovieInfoUrl(id,MOVIE_INFO);
        Timber.d("movie info url : " + url);
        return url;
    }

    public static String getMovieImage(int id){
        String url = buildMovieInfoUrl(id,MOVIE_IMAGE);
        Timber.d("movie images :" + url);
        return url;
    }

    private static String buildMovieInfoUrl(int id, String type) {
        Uri.Builder builder =  new Uri.Builder()
                .scheme("http")
                .authority(Config.MOVIE_WEBSITE)
                .appendPath("3")
                .appendPath("movie")
                .appendPath("" + id);
        if(type != MOVIE_INFO && type != MOVIE_IMAGE)
            builder.appendPath(type);
        else if(type == MOVIE_IMAGE)
            builder.appendPath("images");
        return builder.appendQueryParameter("api_key", Config.API_KEY)
                .build().toString();
    }

    public static String buildImageUrl(String posterPath,String posterSize) {
        String url = new Uri.Builder()
                .scheme("http")
                .authority(Config.POSTER_URL)
                .appendPath("t")
                .appendPath("p")
                .appendPath(posterSize)
                .appendEncodedPath(posterPath)
                .build().toString();
        Timber.tag("MovieInfo adapter").d("poster path : %s", posterPath);
        Timber.tag("MovieInfo adapter").d("poster url : %s", url);
        return url;
    }
}
