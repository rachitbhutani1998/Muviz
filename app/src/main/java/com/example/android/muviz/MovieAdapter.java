package com.example.android.muviz;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;


public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieViewHolder> {

    private ArrayList<Movies> mMovies;
    private Context mContext;

    MovieAdapter(@NonNull Context context, ArrayList<Movies> list) {
        this.mContext = context;
        this.mMovies = list;
    }

//    @NonNull
//    @Override
//    public View getView(int position, @Nullable View convertView, @NonNull final ViewGroup parent) {
//        View listItemView = convertView;
//        if (listItemView == null) {
//            listItemView = LayoutInflater.from(getContext()).inflate(R.layout.movie_item, parent, false);
//        }
//        final Movies thisMovie = getItem(position);
//        ImageView imageView = listItemView.findViewById(R.id.movie_image);
//
//        return listItemView;
//    }

    @Override
    public MovieViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MovieViewHolder(LayoutInflater.from(mContext).inflate(R.layout.movie_item, parent, false));
    }

    @Override
    public void onBindViewHolder(MovieViewHolder holder, int position) {
        Movies movie = mMovies.get(position);
        if (movie != null) {
            Glide.with(mContext).load(movie.getPoster()).into(holder.movieImage);
        }
    }

    @Override
    public int getItemCount() {
        return mMovies.size();
    }

    void setMovies(ArrayList<Movies> moviesArrayList) {
        this.mMovies = moviesArrayList;
    }

    class MovieViewHolder extends RecyclerView.ViewHolder {

        ImageView movieImage;

        MovieViewHolder(View itemView) {
            super(itemView);
            movieImage = itemView.findViewById(R.id.movie_image);
            movieImage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent toDetailIntent = new Intent(mContext, DetailActivity.class);
                    toDetailIntent.putExtra("movie_id", mMovies.get(getAdapterPosition()).getMovieId());
                    toDetailIntent.putExtra("movie_title", mMovies.get(getAdapterPosition()).getMovieTitle());
                    ActivityOptionsCompat optionsCompat = ActivityOptionsCompat.makeSceneTransitionAnimation((GridActivity) mContext,
                            movieImage, mContext.getString(R.string.image_transition));
                    mContext.startActivity(toDetailIntent,optionsCompat.toBundle());
                }
            });
            movieImage.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    Snackbar snackbar = Snackbar.make(view, mMovies.get(getAdapterPosition()).getMovieTitle(), Snackbar.LENGTH_SHORT)
                            .setAction("Open", new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    Intent goToDetail = new Intent(mContext, DetailActivity.class);
                                    goToDetail.putExtra("movie_id", mMovies.get(getAdapterPosition()).getMovieId());
                                    goToDetail.putExtra("movie_title", mMovies.get(getAdapterPosition()).getMovieTitle());
                                    mContext.startActivity(goToDetail);
                                }
                            });
                    snackbar.show();
                    return true;
                }
            });
        }
    }
}
