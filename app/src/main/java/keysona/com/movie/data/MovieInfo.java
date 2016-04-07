package keysona.com.movie.data;

import android.database.Cursor;
import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import keysona.com.movie.net.FetchDataTask;
import timber.log.Timber;

/**
 * Created by key on 16-3-25.
 */
public class MovieInfo implements Parcelable {

    private int movieId;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private int voteCount;
    private double popularity;
    private double voteAverage;
    private int like;   // 0 is dislike , 1 is like

    public MovieInfo() {

    }

    public double getPopularity() {
        return popularity;
    }

    public double getVoteAverage() {
        return voteAverage;
    }

    public String getPosterPath() {
        return posterPath;

    }

    public String getOverview() {
        return overview;
    }

    public String getReleaseDate() {
        return releaseDate;
    }

    public String getOriginalTitle() {
        return originalTitle;
    }

    public int getVoteCount() {
        return voteCount;
    }

    public int getMovieId() {
        return movieId;
    }


    public int getLike() {

        return like;
    }

    public void setPosterPath(String posterPath) {
        this.posterPath = posterPath;
    }

    public static MovieInfo fromCursor(Cursor cursor) {

        MovieInfo movieInfo = new MovieInfo();

        movieInfo.movieId = cursor.getInt(FetchDataTask.MOVIE_INFO_COL_MOVIE_ID);
        movieInfo.posterPath = cursor.getString(FetchDataTask.MOVIE_INFO_COL_POSTER_PATH);
        movieInfo.overview = cursor.getString(FetchDataTask.MOVIE_INFO_COL_OVERVIEW);
        movieInfo.originalTitle = cursor.getString(FetchDataTask.MOVIE_INFO_COL_ORIGINAL_TITLE);
        movieInfo.releaseDate = cursor.getString(FetchDataTask.MOVIE_INFO_COL_RELEASE_DATE);
        movieInfo.voteCount = cursor.getInt(FetchDataTask.MOVIE_INFO_COL_VOTE_COUNT);
        movieInfo.voteAverage = cursor.getDouble(FetchDataTask.MOVIE_INFO_COL_VOTE_AVERAGE);
        movieInfo.popularity = cursor.getDouble(FetchDataTask.MOVIE_INFO_COL_POPULARITY);
        movieInfo.like = cursor.getInt(FetchDataTask.MOVIE_INFO_COL_LIKE);
        return movieInfo;
    }

    @Override
    public String toString() {
        return "movieId:" + movieId + "\t" + "title:" + originalTitle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(movieId);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeInt(voteCount);
        dest.writeDouble(voteAverage);
        dest.writeDouble(popularity);
        dest.writeInt(like);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<MovieInfo> CREATOR =
            new Parcelable.Creator<MovieInfo>() {
                @Override
                public MovieInfo[] newArray(int size) {
                    return new MovieInfo[size];
                }

                @Override
                public MovieInfo createFromParcel(Parcel source) {
                    return new MovieInfo(source);
                }
            };
    private MovieInfo(Parcel in) {
        movieId = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        voteCount = in.readInt();
        voteAverage = in.readDouble();
        popularity = in.readDouble();
        like = in.readInt();
    }

    public String buildImageUrl(String posterSize) {
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
