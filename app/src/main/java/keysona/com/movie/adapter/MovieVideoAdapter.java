package keysona.com.movie.adapter;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.List;

import keysona.com.movie.R;
import keysona.com.movie.data.MovieVideo;
import timber.log.Timber;

/**
 * Created by key on 16-4-7.
 */
public class MovieVideoAdapter extends RecyclerView.Adapter<MovieVideoAdapter.MovieVideoViewHolder> {

    private Context mContext;

    private List<MovieVideo> movieVideoList;

    public MovieVideoAdapter(Context mContext, List<MovieVideo> movieVideoList) {
        this.mContext = mContext;
        this.movieVideoList = movieVideoList;
    }

    @Override
    public MovieVideoViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.video_list_item, parent, false);
        MovieVideoViewHolder videoViewHolder = new MovieVideoViewHolder(view);
        return videoViewHolder;
    }

    @Override
    public void onBindViewHolder(MovieVideoViewHolder holder, int position) {
        final MovieVideo movieVideo = movieVideoList.get(position);
        final String videoImage = movieVideo.buildVideoUrl();
        Timber.d("movieVideo : " + videoImage);
        Picasso.with(mContext).load(videoImage).into(holder.videoImageView);
        holder.videoNameTextView.setText(movieVideo.getName());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse("https://youtu.be/" + movieVideo.getKey()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieVideoList.size();
    }

    public static class MovieVideoViewHolder extends RecyclerView.ViewHolder {
        ImageView videoImageView;
        TextView videoNameTextView;
        public MovieVideoViewHolder(View itemView) {
            super(itemView);
            videoImageView = (ImageView) itemView.findViewById(R.id.video_image);
            videoNameTextView = (TextView) itemView.findViewById(R.id.video_name);
        }
    }
}
