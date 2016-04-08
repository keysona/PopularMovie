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

import java.util.List;

import keysona.com.movie.R;
import keysona.com.movie.data.MovieReview;
import timber.log.Timber;

/**
 * Created by key on 16-4-7.
 */
public class MovieReviewAdapter extends RecyclerView.Adapter<MovieReviewAdapter.MovieReviewViewHolder>  {

    private Context mContext;

    private List<MovieReview> movieReviewList;

    public MovieReviewAdapter(Context context, List<MovieReview> movieReviewList){
        mContext = context;
        this.movieReviewList = movieReviewList;
    }



    @Override
    public MovieReviewViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.review_list_item,parent,false);
        MovieReviewViewHolder viewHolder = new MovieReviewViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(MovieReviewViewHolder holder, int position) {
        final MovieReview movieReview = movieReviewList.get(position);
        holder.textView.setText(movieReview.getContent());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(movieReview.getUrl()));
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return movieReviewList.size();

    }

    public static class MovieReviewViewHolder extends RecyclerView.ViewHolder {

        ImageView review_image;

        TextView textView;

        public MovieReviewViewHolder(View itemView) {
            super(itemView);
            review_image = (ImageView) itemView.findViewById(R.id.review_image);
            textView = (TextView) itemView.findViewById(R.id.review_content);
        }
    }
}
