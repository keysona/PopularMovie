package keysona.com.movie.data;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.test.AndroidTestCase;

import java.util.HashSet;

import timber.log.Timber;

/**
 * Created by key on 16-4-5.
 */
public class TestDB extends AndroidTestCase{

    void deleteDatabase(){
        mContext.deleteDatabase(MovieDbHelper.DATABASE_NAME);
    }

    public void setUp(){
        deleteDatabase();
    }

    public void testCreateDb() {
        final HashSet<String> tableNameHashSet = new HashSet<String>();
        tableNameHashSet.add(MovieContract.MovieInfoEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.MovieReviewEntry.TABLE_NAME);
        tableNameHashSet.add(MovieContract.MovieVideoEntry.TABLE_NAME);

        deleteDatabase();

        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();

        assertEquals(true, db.isOpen());

        Cursor c = db.rawQuery("SELECT name FROM sqlite_master WHERE type='table'", null);
        assertTrue("Error: This means that the database has not been created correctly", c.moveToFirst());

        do {
            tableNameHashSet.remove(c.getString(0));
        } while (c.moveToNext());

        assertTrue("Error: Your database was created without the movie info  entry , movie review and movid video entry tables",
                tableNameHashSet.isEmpty());

        // query table column  --  MovieInfoEntry
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieInfoEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> movieInfoColumnHashSet = new HashSet<String>();
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry._ID);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_ORIGINAL_TITLE);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_RELEASE_DATE);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_OVERVIEW);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_POSTER_PATH);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_VOTE_COUNT);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_LIKE);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_POPULARITY);
        movieInfoColumnHashSet.add(MovieContract.MovieInfoEntry.COLUMN_VOTE_AVERAGE);

        int columnNameIndex = c.getColumnIndex("name");
        do {
            movieInfoColumnHashSet.remove(c.getString(columnNameIndex));
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns",
                movieInfoColumnHashSet.isEmpty());


        // query table column -- MovieReviewEntry
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieReviewEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> movieReviewColumnHashSet = new HashSet<String>();
        movieReviewColumnHashSet.add(MovieContract.MovieReviewEntry._ID);
        movieReviewColumnHashSet.add(MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID);
        movieReviewColumnHashSet.add(MovieContract.MovieReviewEntry.COLUMN_AUTHOR);
        movieReviewColumnHashSet.add(MovieContract.MovieReviewEntry.COLUMN_CONTENT);
        movieReviewColumnHashSet.add(MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID);
        movieReviewColumnHashSet.add(MovieContract.MovieReviewEntry.COLUMN_URL);


        do {
            movieReviewColumnHashSet.remove(c.getString(columnNameIndex));
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns", movieReviewColumnHashSet.isEmpty());

        // query table column -- MovieVideoEntry
        c = db.rawQuery("PRAGMA table_info(" + MovieContract.MovieVideoEntry.TABLE_NAME + ")",
                null);

        assertTrue("Error: This means that we were unable to query the database for table information.",
                c.moveToFirst());

        final HashSet<String> movieVideoColumnHashSet = new HashSet<String>();
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry._ID);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_VIDEO_ID);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_NAME);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_ISO_639_1);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_KEY);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_MOVIE_ID);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_SITE);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_SIZE);
        movieVideoColumnHashSet.add(MovieContract.MovieVideoEntry.COLUMN_TYPE);

        do {
            movieVideoColumnHashSet.remove(c.getString(columnNameIndex));
        } while (c.moveToNext());

        assertTrue("Error: The database doesn't contain all of the required location entry columns", movieVideoColumnHashSet.isEmpty());

        c.close();
        db.close();
    }

    public void testMovieInfo(){
        insertMovieInfo();
    }

    public void testMovieReview(){

        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();

        ContentValues testValues = TestUtility.createMovieReview(insertMovieInfo());

        long movieReviewRowId = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, testValues);

        assertTrue(movieReviewRowId != -1);

        Cursor c = db.query(MovieContract.MovieReviewEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: MovieInfo Review Query Validation Failed", c.moveToFirst());
        TestUtility.validateCurrentRecord("Error: MovieReview Query Validation Failed\"", c, testValues);
        Timber.d("testMovieReview");
        c.close();
        db.close();
    }

    public void testMovieVideoTable(){

        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();

        ContentValues testValues = TestUtility.createMovieVideo(insertMovieInfo());

        db.insert(MovieContract.MovieVideoEntry.TABLE_NAME, null, testValues);

        Cursor c = db.query(MovieContract.MovieVideoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null);

        assertTrue("Error: MovieInfo Video Query Validation Failed", c.moveToFirst());
        TestUtility.validateCurrentRecord("Error : MovieInfo Video  Validation Failed", c, testValues);
        Timber.d("testMovieVideoTable");
        c.close();
        db.close();
    }

    public long insertMovieInfo(){

        SQLiteDatabase db = new MovieDbHelper(mContext).getWritableDatabase();

        ContentValues testValues = TestUtility.createMovieInfo();

        long movieInfoRowId = testValues.getAsLong(MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID);

        db.insert(MovieContract.MovieInfoEntry.TABLE_NAME,null,testValues);

        assertTrue(movieInfoRowId != -1);

        Cursor c = db.query(MovieContract.MovieInfoEntry.TABLE_NAME,
                null,
                null,
                null,
                null,
                null,
                null
                );

        assertTrue("Error: No Records returned from MovieInfo query", c.moveToFirst());

        TestUtility.validateCurrentRecord("Error: MovieInfo Query Validation Failed",
                c, testValues);

        // Move the cursor to demonstrate that there is only one record in the database
        assertFalse( "Error: More than one record returned from location query",
                c.moveToNext() );

        // Sixth Step: Close Cursor and Database
        c.close();
        db.close();
        return movieInfoRowId;
    }
}
