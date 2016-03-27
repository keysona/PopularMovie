package keysona.com.movie.data;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

import org.json.JSONException;
import org.json.JSONObject;

import timber.log.Timber;

/**
 * Created by key on 16-3-25.
 */
public class Movie implements Parcelable {

    private int id;
    private String posterPath;
    private String overview;
    private String releaseDate;
    private String originalTitle;
    private int voteCount;

    public Movie() {

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

    public int getId() {
        return id;
    }

    public static Movie fromJson(JSONObject object) {
        try {
            Movie movie = new Movie();
            movie.id = object.getInt("id");
            movie.posterPath = object.getString("poster_path");
            movie.overview = object.getString("overview");
            movie.originalTitle = object.getString("original_title");
            movie.releaseDate = object.getString("release_date");
            movie.voteCount = object.getInt("vote_count");
            return movie;
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return "id:" + id + "\t" + "title:" + originalTitle;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(posterPath);
        dest.writeString(overview);
        dest.writeString(releaseDate);
        dest.writeString(originalTitle);
        dest.writeInt(voteCount);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<Movie> CREATOR =
            new Parcelable.Creator<Movie>() {
                @Override
                public Movie[] newArray(int size) {
                    return new Movie[size];
                }

                @Override
                public Movie createFromParcel(Parcel source) {
                    return new Movie(source);
                }
            };

    private Movie(Parcel in) {
        id = in.readInt();
        posterPath = in.readString();
        overview = in.readString();
        releaseDate = in.readString();
        originalTitle = in.readString();
        voteCount = in.readInt();
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
        Timber.tag("Movie adapter").d("poster path : %s", posterPath);
        Timber.tag("Movie adapter").d("poster url : %s", url);
        return url;
    }
}
