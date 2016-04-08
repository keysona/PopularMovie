package keysona.com.movie.data;

import android.database.Cursor;
import android.os.Parcel;
import android.os.Parcelable;

import keysona.com.movie.net.FetchDataTask;

/**
 * Created by key on 16-4-7.
 */
public class MovieVideo implements Parcelable {

    private String video_id;
    private int movie_id;
    private String name;
    private String ISO_639_1;
    private String key;
    private String site;
    private int size;
    private String type;

    // 4 images
    private static final int videoImage = 4;



    public MovieVideo(){

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(video_id);
        dest.writeInt(movie_id);
        dest.writeString(name);
        dest.writeString(ISO_639_1);
        dest.writeString(key);
        dest.writeString(site);
        dest.writeInt(size);
        dest.writeString(type);
    }

    private MovieVideo(Parcel in) {
        video_id = in.readString();
        movie_id = in.readInt();
        name = in.readString();
        ISO_639_1 = in.readString();
        key = in.readString();
        site = in.readString();
        size = in.readInt();
        type = in.readString();
    }

    public String getVideo_id() {
        return video_id;
    }

    public int getMovie_id() {
        return movie_id;
    }

    public String getName() {
        return name;
    }

    public String getISO_639_1() {
        return ISO_639_1;
    }

    public String getKey() {
        return key;
    }

    public String getSite() {
        return site;
    }

    public int getSize() {
        return size;
    }

    public String getType() {
        return type;
    }

    public static final Parcelable.Creator<MovieVideo> CREATOR =
            new Parcelable.Creator<MovieVideo>() {
                @Override
                public MovieVideo[] newArray(int size) {
                    return new MovieVideo[size];
                }

                @Override
                public MovieVideo createFromParcel(Parcel source) {
                    return new MovieVideo(source);
                }
            };

    public static MovieVideo fromCursor(Cursor c){

        MovieVideo movieVideo = new MovieVideo();
        movieVideo.movie_id = c.getInt(FetchDataTask.MOVIE_VIDEO_COL_MOVIE_ID);
        movieVideo.ISO_639_1 = c.getString(FetchDataTask.MOVIE_VIDEO_COL_ISO_639_1);
        movieVideo.video_id = c.getString(FetchDataTask.MOVIE_VIDEO_COL_VIDEO_ID);
        movieVideo.key = c.getString(FetchDataTask.MOVIE_VIDEO_COL_KEY);
        movieVideo.site = c.getString(FetchDataTask.MOVIE_VIDEO_COL_SITE);
        movieVideo.size = c.getInt(FetchDataTask.MOVIE_VIDEO_COL_SIZE);
        movieVideo.name = c.getString(FetchDataTask.MOVIE_VIDEO_COL_NAME);
        movieVideo.type= c.getString(FetchDataTask.MOVIE_VIDEO_COL_TYPE);
        return movieVideo;
    }

    @Override
    public String toString() {
        return "movie id : " + movie_id + "\t" + "video id : " + video_id;
    }

    public String buildVideoUrl(){

        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append("http://img.youtube.com/vi/");
        stringBuilder.append(key);
        stringBuilder.append("/");
        stringBuilder.append(0);
        stringBuilder.append(".jpg");

        /**
         *  example ： http://img.youtube.com/vi/<insert-youtube-video-id-here>/3.jpg
         */

        return stringBuilder.toString();
    }
}
