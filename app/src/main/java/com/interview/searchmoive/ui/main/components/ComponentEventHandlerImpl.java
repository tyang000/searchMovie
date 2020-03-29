package com.interview.searchmoive.ui.main.components;

import com.interview.searchmoive.ui.main.data.Movie;
import com.interview.searchmoive.ui.main.data.MovieViewModel;

public class ComponentEventHandlerImpl implements ComponentEventHandler {

    private MovieViewModel mMovieViewModel;

    public ComponentEventHandlerImpl(MovieViewModel movieViewModel) {
        mMovieViewModel = movieViewModel;
    }

    @Override
    public void onLikeButtonClicked(Movie movie) {
        if (movie.getIsFavorite() == 0) {
            mMovieViewModel.likeMovie(movie);
        } else {
            mMovieViewModel.unlikeMovie(movie);
        }
    }
}
