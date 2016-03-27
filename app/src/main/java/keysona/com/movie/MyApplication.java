package keysona.com.movie;

import android.app.Application;

import timber.log.Timber;

/**
 * Created by key on 16-3-27.
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }
    }
}