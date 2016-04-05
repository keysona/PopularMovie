package keysona.com.movie.data;

import android.annotation.TargetApi;
import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.support.annotation.Nullable;

/**
 * Created by key on 16-4-5.
 */
public class MovieProvider extends ContentProvider {

    private static final UriMatcher sUriMather = buildUriMather();

    private MovieDbHelper mOpenHelper;

    public static final int MOVIE = 100;

    public static final int MOVIE_WITH_ID = 101;

    public static final int REVIEW = 200;

    public static final int REVIEW_WITH_ID = 201;

    public static final int VIDEO = 300;

    public static final int VIDEO_WITH_ID = 301;

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDbHelper(getContext());
        return true;
    }

    static UriMatcher buildUriMather() {
        final UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        final String authority = MovieContract.CONTENT_AUTHORITY;

        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE, MOVIE);
        uriMatcher.addURI(authority, MovieContract.PATH_MOVIE + "/*", MOVIE_WITH_ID);
        uriMatcher.addURI(authority, MovieContract.PATH_REVIEW, REVIEW);
        uriMatcher.addURI(authority, MovieContract.PATH_REVIEW + "/*", REVIEW_WITH_ID);
        uriMatcher.addURI(authority, MovieContract.PATH_VIDEO, VIDEO);
        uriMatcher.addURI(authority, MovieContract.PATH_VIDEO + "/*", MOVIE_WITH_ID);

        return uriMatcher;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMather.match(uri);

        switch (match) {
            case MOVIE:
                return MovieContract.MovieInfoEntry.CONTENT_TYPE;
            case MOVIE_WITH_ID:
                return MovieContract.MovieInfoEntry.CONTENT_ITEM_TYPE;
            case REVIEW:
                return MovieContract.MovieReviewEntry.CONTENT_TYPE;
            case REVIEW_WITH_ID:
                return MovieContract.MovieReviewEntry.CONTENT_TYPE;
            case VIDEO:
                return MovieContract.MovieVideoEntry.CONTENT_TYPE;
            case VIDEO_WITH_ID:
                return MovieContract.MovieVideoEntry.CONTENT_TYPE;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {

        Cursor retCursor;

        long movieId;

        switch (sUriMather.match(uri)) {
            case MOVIE:
                retCursor = mOpenHelper.getWritableDatabase().query(MovieContract.MovieInfoEntry.TABLE_NAME,
                        projection,
                        selection,
                        selectionArgs,
                        null,
                        null,
                        sortOrder
                );
                break;

            case MOVIE_WITH_ID:
                movieId = ContentUris.parseId(uri);

                retCursor = mOpenHelper.getWritableDatabase().query(
                        MovieContract.MovieInfoEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieInfoEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{"" + movieId},
                        null,
                        null,
                        sortOrder
                );
                break;

            case REVIEW_WITH_ID:
                movieId = ContentUris.parseId(uri);

                retCursor = mOpenHelper.getWritableDatabase().query(
                        MovieContract.MovieReviewEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieReviewEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{"" + movieId},
                        null,
                        null,
                        sortOrder
                );
                break;

            case VIDEO_WITH_ID:
                movieId = ContentUris.parseId(uri);

                retCursor = mOpenHelper.getWritableDatabase().query(
                        MovieContract.MovieVideoEntry.TABLE_NAME,
                        projection,
                        MovieContract.MovieVideoEntry.COLUMN_MOVIE_ID + " = ?",
                        new String[]{"" + movieId},
                        null,
                        null,
                        sortOrder
                );
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        retCursor.setNotificationUri(getContext().getContentResolver(), uri);
        return retCursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMather.match(uri);

        Uri returnUri;

        switch (match) {
            case MOVIE: {
                long _id = db.insert(MovieContract.MovieInfoEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieInfoEntry.buildMovieInfoWithMovieIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case REVIEW: {
                long _id = db.insert(MovieContract.MovieReviewEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieReviewEntry.buildReviewWithMovieIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }

            case VIDEO: {
                long _id = db.insert(MovieContract.MovieVideoEntry.TABLE_NAME, null, values);
                if (_id > 0)
                    returnUri = MovieContract.MovieVideoEntry.buildVideoWithMovieIdUri(_id);
                else
                    throw new android.database.SQLException("Failed to insert row into " + uri);
                break;
            }
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return returnUri;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMather.match(uri);
        int rowUpdated = 0;

        switch (match) {
            case MOVIE:
                rowUpdated = db.update(MovieContract.MovieInfoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case REVIEW:
                rowUpdated = db.update(MovieContract.MovieReviewEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            case VIDEO:
                rowUpdated = db.update(MovieContract.MovieVideoEntry.TABLE_NAME, values, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknow uri : " + uri);
        }

        if (rowUpdated != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }

        return rowUpdated;

    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMather.match(uri);
        int rowDeleted = 0;

        //delete all
        if (null == selection) selection = "1";
        switch (match) {
            case MOVIE:
                rowDeleted = db.delete(MovieContract.MovieInfoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case REVIEW:
                rowDeleted = db.delete(MovieContract.MovieReviewEntry.TABLE_NAME, selection, selectionArgs);
                break;
            case VIDEO:
                rowDeleted = db.delete(MovieContract.MovieVideoEntry.TABLE_NAME, selection, selectionArgs);
                break;
            default:
                throw new UnsupportedOperationException("Unknown uri: " + uri);
        }

        if (rowDeleted != 0) {
            getContext().getContentResolver().notifyChange(uri, null);
        }
        return rowDeleted;
    }


    @Override
    public int bulkInsert(Uri uri, ContentValues[] values) {

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMather.match(uri);

        int returnCount = 0;
        switch (match) {
            case MOVIE:
                returnCount = multiInsert(db, MovieContract.MovieInfoEntry.TABLE_NAME,values,uri);
                break;
            case REVIEW:
                returnCount = multiInsert(db, MovieContract.MovieReviewEntry.TABLE_NAME,values,uri);
                break;
            case VIDEO:
                returnCount = multiInsert(db, MovieContract.MovieVideoEntry.TABLE_NAME,values,uri);
                break;
            default:
                return super.bulkInsert(uri, values);
        }
        return returnCount;
    }

    private int multiInsert(SQLiteDatabase db,String tableName,ContentValues[] values,Uri uri){
        int returnCount = 0;
        db.beginTransaction();
        try {
            for (ContentValues contentValues :values){
                long _id = db.insert(MovieContract.MovieVideoEntry.TABLE_NAME,null,contentValues);
                if(_id!=-1)
                    returnCount++;
            }
            db.setTransactionSuccessful();
        }finally {
            db.endTransaction();
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return returnCount;
    }

    @Override
    @TargetApi(11)
    public void shutdown() {
        mOpenHelper.close();
        super.shutdown();
    }
}
