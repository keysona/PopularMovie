package keysona.com.movie.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import timber.log.Timber;

/**
 * Created by key on 16-4-5.
 */
public class MovieDbHelper extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "movie.db";

    public static final int DATABASE_VERSION = 1;

    public MovieDbHelper(Context context){
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        final String SQL_CREATE_MOVIE_INFO_TABLE = "CREATE TABLE " + MovieContract.MovieInfoEntry.TABLE_NAME + " (" +
                MovieContract.MovieInfoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL , " +
                MovieContract.MovieInfoEntry.COLUMN_ORIGINAL_TITLE + " TEXT NOT NULL, " +
                MovieContract.MovieInfoEntry.COLUMN_OVERVIEW + " TEXT NOT NULL, " +
                MovieContract.MovieInfoEntry.COLUMN_RELEASE_DATE + " TEXT NOT NULL, " +
                MovieContract.MovieInfoEntry.COLUMN_VOTE_COUNT + " INTEGER NOT NULL, " +
                MovieContract.MovieInfoEntry.COLUMN_POSTER_PATH + " TEXT NOT NULL, " +
                MovieContract.MovieInfoEntry.COLUMN_POPULARITY + " REAL NOT NULL, " +
                MovieContract.MovieInfoEntry.COLUMN_VOTE_AVERAGE + " REAL NOT NULL, " +
                MovieContract.MovieInfoEntry.COLUMN_LIKE + " INTEGER DEFAULT 0 , " +
                " UNIQUE (" + MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID + ") ON CONFLICT REPLACE " +
                " );";

        final String SQL_CREATE_MOVIE_REVIEW_TABLE = "CREATE TABLE " + MovieContract.MovieReviewEntry.TABLE_NAME + " (" +
                MovieContract.MovieReviewEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieReviewEntry.COLUMN_REVIEW_ID + " TEXT NOT NULL , " +
                MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieReviewEntry.COLUMN_AUTHOR + " TEXT NOT NULL, " +
                MovieContract.MovieReviewEntry.COLUMN_CONTENT + " TEXT NOT NULL, " +
                MovieContract.MovieReviewEntry.COLUMN_URL + " TEXT NOT NULL, " +

                // set up the movie id column as a foreign key to movie info table.
                " FOREIGN KEY ( " + MovieContract.MovieReviewEntry._ID+ " ) REFERENCES " +
                MovieContract.MovieInfoEntry.TABLE_NAME + " (" + MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID + " ), " +
                " UNIQUE (" + MovieContract.MovieReviewEntry.COLUMN_REVIEW_ID + ") ON CONFLICT REPLACE " +
                " );";

        final String SQL_CREATE_MOVIE_VIDEO_TABLE = "CREATE TABLE " + MovieContract.MovieVideoEntry.TABLE_NAME + " ( " +
                MovieContract.MovieVideoEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                MovieContract.MovieVideoEntry.COLUMN_VIDEO_ID + " TEXT NOT NULL , " +
                MovieContract.MovieVideoEntry.COLUMN_MOVIE_ID + " INTEGER NOT NULL, " +
                MovieContract.MovieVideoEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MovieContract.MovieVideoEntry.COLUMN_ISO_639_1 + " TEXT NOT NULL, " +
                MovieContract.MovieVideoEntry.COLUMN_KEY + " TEXT NOT NULL, " +
                MovieContract.MovieVideoEntry.COLUMN_SITE + " TEXT NOT NULL, " +
                MovieContract.MovieVideoEntry.COLUMN_SIZE + " INTEGER NOT NULL, " +
                MovieContract.MovieVideoEntry.COLUMN_TYPE + " TEXT NOT NULL, " +
                "FOREIGN KEY ( " + MovieContract.MovieVideoEntry._ID + " ) REFERENCES " +
                MovieContract.MovieInfoEntry.TABLE_NAME + " ( " + MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID + " ), " +
                " UNIQUE (" + MovieContract.MovieVideoEntry.COLUMN_VIDEO_ID + ") ON CONFLICT REPLACE " +
                " ); ";


        Timber.d("SQLite : " + SQL_CREATE_MOVIE_INFO_TABLE);
        Timber.d("SQLite : " + SQL_CREATE_MOVIE_REVIEW_TABLE);
        Timber.d("SQLite : " + SQL_CREATE_MOVIE_VIDEO_TABLE);

        db.execSQL(SQL_CREATE_MOVIE_INFO_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_REVIEW_TABLE);
        db.execSQL(SQL_CREATE_MOVIE_VIDEO_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "  + MovieContract.MovieInfoEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieReviewEntry.TABLE_NAME);
        db.execSQL("DROP TABLE IF EXISTS " + MovieContract.MovieVideoEntry.TABLE_NAME);

        onCreate(db);
    }
}
