package keysona.com.movie.ui;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;

import keysona.com.movie.R;
import timber.log.Timber;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //get screen information
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);

        int width = displayMetrics.widthPixels;
        int height = displayMetrics.heightPixels;

        float density = displayMetrics.density;
        int densityDpi = displayMetrics.densityDpi;

        float xdpi = displayMetrics.xdpi;
        float ydpi = displayMetrics.ydpi;

        String tag = "Screen info";

        Timber.tag(tag).d("width :" + width + " height : " + height);
        Timber.tag(tag).d("density : " + density + " densityDpi : " + height);
        Timber.tag(tag).d("xdp : " + xdpi + " dpi : " + ydpi);

    }

}
