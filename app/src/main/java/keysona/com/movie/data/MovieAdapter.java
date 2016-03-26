package keysona.com.movie.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by key on 16-3-25.
 */
public class MovieAdapter extends ArrayAdapter<Movie> {

    private Context mContext;
    private int resourceId;

    public MovieAdapter(Context context, int resource, List<Movie> objects) {
        super(context, resource, objects);
        mContext = context;
        resourceId = resource;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Movie movie = getItem(position);
        ImageView view;
        if (convertView == null) {
            view = (ImageView) LayoutInflater.from(mContext).inflate(resourceId, parent, false);
        } else {
            view = (ImageView) convertView;
        }
        Picasso.with(mContext).load(movie.buildImageUrl()).into(view);
        return view;
    }
}
