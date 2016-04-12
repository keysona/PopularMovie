package keysona.com.movie.adapter;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import keysona.com.movie.R;
import keysona.com.movie.data.MovieInfo;
import keysona.com.movie.ui.MainActivity;

/**
 * Created by key on 16-3-25.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private Context mContext;

    private List<MovieInfo> movies;

    public MovieAdapter(Context context, List<MovieInfo> movieInfoList) {
        this.mContext = context;
        this.movies = movieInfoList;
    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.thumb_view, parent, false);
        MovieViewHolder viewHolder = new MovieViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, final int position) {
        final MovieInfo movieInfo = movies.get(position);
        Picasso.with(mContext).load(movieInfo.getPosterPath()).into(holder.posterImageView);

        holder.posterImageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ((MainActivity) mContext).onItemSelected(movieInfo);
                }
        });

    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {
        ImageView posterImageView;
        CardView cardView;

        public MovieViewHolder(View v) {
            super(v);
            cardView = (CardView) v.findViewById(R.id.card_view);
            posterImageView = (ImageView) cardView.findViewById(R.id.image_view);
        }
    }

}
