package keysona.com.movie.data;

import android.test.AndroidTestCase;

import keysona.com.movie.net.FetchDataTask;

/**
 * Created by key on 16-4-6.
 */
public class TestFetchDataTask extends AndroidTestCase {

    public void testFetchDataTask(){

        FetchDataTask taskMovieList = new FetchDataTask(mContext);
        FetchDataTask taskMovieReview = new FetchDataTask(mContext);
        FetchDataTask taskMovieVideo = new FetchDataTask(mContext);

        //test Movie_LIST
        taskMovieList.execute(FetchDataTask.MOVIE_LIST);

        //test Movie_Review
        taskMovieReview.execute(FetchDataTask.MOVIE_REVIEW);

        //test Movie_Video
        taskMovieVideo.execute(FetchDataTask.MOVIE_VIDEO);
    }
}
