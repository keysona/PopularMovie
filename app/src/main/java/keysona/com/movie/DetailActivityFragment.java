package keysona.com.movie;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import keysona.com.movie.data.Movie;

/**
 * A placeholder fragment containing a simple view.
 */
public class DetailActivityFragment extends Fragment {

    private static final String TAG = "DetailActivityFragment";

    private Movie movie;

    public DetailActivityFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Intent intent = getActivity().getIntent();
        if(intent != null){
            movie = intent.getParcelableExtra("movie");
            Log.d(TAG,movie.toString());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        TextView titleTextView = (TextView) view.findViewById(R.id.title);
        TextView dateTextView = (TextView) view.findViewById(R.id.date);
        TextView overviewTextView = (TextView) view.findViewById(R.id.overview);
        TextView voteTextView = (TextView) view.findViewById(R.id.vote);
        ImageView posterImageView = (ImageView) view.findViewById(R.id.poster);

        titleTextView.setText(movie.getOriginalTitle());
        dateTextView.setText(movie.getReleaseDate());
        overviewTextView.setText(movie.getOverview());
        voteTextView.setText(""+movie.getVoteCount());

        Picasso.with(getActivity()).load(movie.buildImageUrl()).into(posterImageView);
        return view;
    }
}
