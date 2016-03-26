package keysona.com.movie.net;

import android.util.Log;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by key on 16-3-25.
 */
public class NetworkConnector {

    private static OkHttpClient client = new OkHttpClient();

    private static String run(String url) throws IOException {
        Request request = new Request.Builder()
                .url(url)
                .build();

        Response response = client.newCall(request).execute();
        return response.body().string();
    }

    /**
     * @param url
     * @return data string or empty string.
     */
    public static String get(String url) {
        try {
            String data = run(url);
            data = (data != null) ? data : "";
            Log.d("Test", data);
            return data;
        } catch (Exception e) {
            Log.d("Test", e.toString());
        }
        return "";
    }

}
