package keysona.com.movie.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import keysona.com.movie.net.FetchDataTask;

/**
 * Created by key on 16-4-7.
 */
public class MovieReview implements Parcelable {

    private String review_id;
    private int movie_id;
    private String author;
    private String content;
    private String url;

    public MovieReview(){

    }

    private MovieReview(Parcel in) {
        review_id = in.readString();
        movie_id = in.readInt();
        author = in.readString();
        content = in.readString();
        url = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(review_id);
        dest.writeInt(movie_id);
        dest.writeString(author);
        dest.writeString(content);
        dest.writeString(url);
    }

    public String getReview_id() {
        return review_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getAuthor() {
        return author;
    }

    public String getUrl() {
        return url;
    }

    public String getContent() {
        return content;
    }

    public static final Parcelable.Creator<MovieReview> CREATOR =
            new Parcelable.Creator<MovieReview>() {
                @Override
                public MovieReview[] newArray(int size) {
                    return new MovieReview[size];
                }

                @Override
                public MovieReview createFromParcel(Parcel source) {
                    return new MovieReview(source);
                }
            };

    public static MovieReview fromCursor(Cursor c){
        MovieReview movieReview = new MovieReview();

        movieReview.movie_id = c.getInt(FetchDataTask.MOVIE_REVIEW_COL_MOVIE_ID);
        movieReview.review_id = c.getString(FetchDataTask.MOVIE_REVIEW_REVIEW_ID);
        movieReview.author = c.getString(FetchDataTask.MOVIE_REVIEW_AUTHOR);
        movieReview.content = c.getString(FetchDataTask.MOVIE_REVIEW_CONTENT);
        movieReview.url = c.getString(FetchDataTask.MOVIE_REVIEW_URL);

        return movieReview;
    }

    @Override
    public String toString() {
        return "movie id : " + movie_id + "\t" + "review id : " + review_id;
    }
}
