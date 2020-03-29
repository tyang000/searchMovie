package com.interview.searchmoive.ui.main.components;

import android.content.res.Resources;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.GuardedBy;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.interview.searchmoive.R;
import com.interview.searchmoive.ui.main.data.Movie;

import java.util.List;
import java.util.Map;

public class MovieCardAdapter extends RecyclerView.Adapter<MovieCardAdapter.MovieCardHolder> {

    @GuardedBy("this")
    private List<Movie> mMovieCardList;

    private String mPreviousSearchKey;
    private ComponentEventHandler mHandler;

    public void setComponentClickHandler(ComponentEventHandler handler) {
        mHandler = handler;
    }

    public synchronized void setMovieSearchResults(List<Movie> searchResults, String searchKey) {
        int previousSize = mMovieCardList != null ? mMovieCardList.size() : 0;
        mMovieCardList = searchResults;
        if (mPreviousSearchKey != null && mPreviousSearchKey.equals(searchKey)) {
            notifyItemInserted(previousSize);
        } else {
            notifyDataSetChanged();
        }
        mPreviousSearchKey = searchKey;
    }

    public synchronized void updateListWithDataFromDB(Map<String, Movie> movieMap) {
        if (mMovieCardList == null || mMovieCardList.size() == 0) return;
        for (int i = 0; i < mMovieCardList.size(); ++i) {
            String movieId = mMovieCardList.get(i).getId();
            if (movieMap.containsKey(movieId)) {
                mMovieCardList.set(i, movieMap.get(movieId));
            }
        }
        notifyDataSetChanged();
    }

    public synchronized void setFavoriteMovieLists(List<Movie> favoriteMovieLists) {
        mMovieCardList = favoriteMovieLists;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MovieCardHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View movieCardView = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_card
                , parent, false);
        return new MovieCardHolder(movieCardView);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieCardHolder holder, int position) {
        Resources res = holder.itemView.getResources();
        final Movie movieData = mMovieCardList.get(position);
        holder.movieNameLabel.setText(movieData.getName());
        holder.directorNameLabel.setText(res.getString(R.string.director_label,
                movieData.getDirector()));
        holder.yearLabel.setText(res.getString(R.string.year_label, movieData.getYear()));
        holder.descriptionLabel.setText(res.getString(R.string.description_label,
                movieData.getDescription()));
        holder.moviePosterView.setImageURI(movieData.getPosterUrl());
        holder.button.setText(res.getString(movieData.getIsFavorite() == 1 ?
                R.string.liked_label : R.string.like_it_label));
        holder.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mHandler != null) {
                    mHandler.onLikeButtonClicked(movieData);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mMovieCardList != null ? mMovieCardList.size() : 0;
    }

    class MovieCardHolder extends RecyclerView.ViewHolder {
        private TextView movieNameLabel;
        private TextView directorNameLabel;
        private TextView yearLabel;
        private TextView descriptionLabel;
        private SimpleDraweeView moviePosterView;
        private Button button;

        public MovieCardHolder(@NonNull View itemView) {
            super(itemView);
            this.movieNameLabel = itemView.findViewById(R.id.movie_name);
            this.directorNameLabel = itemView.findViewById(R.id.director_name);
            this.yearLabel = itemView.findViewById(R.id.year);
            this.descriptionLabel = itemView.findViewById(R.id.description);
            this.moviePosterView = itemView.findViewById(R.id.movie_poster);
            this.button = itemView.findViewById(R.id.like_button);
        }
    }
}
