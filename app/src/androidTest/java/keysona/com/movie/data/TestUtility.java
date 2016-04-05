package keysona.com.movie.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.test.AndroidTestCase;

import java.util.Map;
import java.util.Set;

/**
 * Created by key on 16-4-5.
 */
public class TestUtility extends AndroidTestCase {

    public static ContentValues createMovieInfo() {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID, 135397);
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_ORIGINAL_TITLE, "Jurassic World");
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_OVERVIEW, "Twenty-two years after the events of Jurassic Park, Isla Nublar now features a fully functioning dinosaur theme park, Jurassic World, as originally envisioned by John Hammond.");
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_LIKE, 1);
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_POPULARITY, 2);
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_VOTE_AVERAGE, 8.2);
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_POSTER_PATH, "/uXZYawqUsChGSj54wcuBtEdUJbh.jpg");
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_RELEASE_DATE, "2015-06-12");
        contentValues.put(MovieContract.MovieInfoEntry.COLUMN_VOTE_COUNT, 119);
        return contentValues;
    }

    public static ContentValues createMovieReview(long movieRowId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieReviewEntry.COLUMN_AUTHOR, "Travis Bell");
        contentValues.put(MovieContract.MovieReviewEntry.COLUMN_CONTENT, "I felt like this was a tremendous end to Nolan's Batman trilogy. The Dark Knight Rises may very well have been the weakest of all 3 films but when you're talking about a scale of this magnitude, it still makes this one of the best movies I've seen in the past few years.\\r\\n\\r\\nI expected a little more _Batman_ than we got (especially with a runtime of 2:45) but while the story around the fall of Bruce Wayne and Gotham City was good I didn't find it amazing. This might be in fact, one of my only criticisms—it was a long movie but still, maybe too short for the story I felt was really being told. I feel confident in saying this big of a story could have been split into two movies.\\r\\n\\r\\nThe acting, editing, pacing, soundtrack and overall theme were the same 'as-close-to-perfect' as ever with any of Christopher Nolan's other films. Man does this guy know how to make a movie!\\r\\n\\r\\nYou don't have to be a Batman fan to enjoy these movies and I hope any of you who feel this way re-consider. These 3 movies are without a doubt in my mind, the finest display of comic mythology ever told on the big screen. They are damn near perfect.\",\n");
        contentValues.put(MovieContract.MovieReviewEntry.COLUMN_REVIEW_ID, "5010553819c2952d1b000451");
        contentValues.put(MovieContract.MovieReviewEntry.COLUMN_URL, "http://j.mp/QSjAK2");
        contentValues.put(MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID, movieRowId);
        return contentValues;
    }

    public static ContentValues createMovieVideo(long movieRowId) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_VIDEO_ID, "533ec654c3a36854480003eb");
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_KEY, "SUXWAEX2jlg");
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_ISO_639_1, "en");
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_NAME, "Trailer 1");
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_SITE, "YouTube");
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_SIZE, 720);
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_TYPE, "Trailer");
        contentValues.put(MovieContract.MovieVideoEntry.COLUMN_MOVIE_ID, movieRowId);

        return contentValues;
    }

    public static void validateCurrentRecord(String error, Cursor valueCursor, ContentValues expectedValues) {
        Set<Map.Entry<String, Object>> valueSet = expectedValues.valueSet();
        for (Map.Entry<String, Object> entry : valueSet) {
            String columnName = entry.getKey();
            int idx = valueCursor.getColumnIndex(columnName);
            assertFalse("Column '" + columnName + "' not found. " + error, idx == -1);
            String expectedValue = entry.getValue().toString();
            assertEquals("Value '" + entry.getValue().toString() +
                    "' did not match the expected value '" +
                    expectedValue + "'. " + error, expectedValue, valueCursor.getString(idx));
        }

    }
}
