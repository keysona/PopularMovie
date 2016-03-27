package keysona.com.movie.data;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

import keysona.com.movie.ui.DetailActivity;
import keysona.com.movie.R;

/**
 * Created by key on 16-3-25.
 */
public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private List<Movie> movies;

    private Context mContext;


    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.thumb_view, parent, false);
        MovieViewHolder movieViewHolder = new MovieViewHolder(view);
        return movieViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        final Movie movie = movies.get(position);
        String imageUrl = movie.buildImageUrl();
        Picasso.with(mContext).load(imageUrl).into(holder.imageView);
        holder.imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, DetailActivity.class);
                intent.putExtra("movie", movie);
                mContext.startActivity(intent);
            }
        });
    }

    public MovieAdapter(Context context, List<Movie> movies) {
        this.movies = movies;
        this.mContext = context;
    }

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
    }

    public static class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView imageView;

        MovieViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.image_view);

        }
    }

    @Override
    public int getItemCount() {
        return movies.size();
    }


}
