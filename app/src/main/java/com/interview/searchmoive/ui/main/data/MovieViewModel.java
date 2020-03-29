package com.interview.searchmoive.ui.main.data;

import android.app.Application;

import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import com.interview.searchmoive.ui.main.datafetch.MovieRepository;
import com.interview.searchmoive.ui.main.datafetch.network.MovieResponse;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {

    private MovieRepository mMovieRepository;
    private LiveData<List<Movie>> mFavoriteMovies;
    private LiveData<List<Movie>> mAllMovies;
    private LiveData<Movie> mFullyFetchedMovie;
    private LiveData<MovieResponse> mMovieResponse;

    public MovieViewModel(Application application) {
        super(application);
        mMovieRepository = new MovieRepository(application);
        mFavoriteMovies = mMovieRepository.getFavoriteMovies();
        mAllMovies = mMovieRepository.getAllMovies();
        mMovieResponse = mMovieRepository.getSearchedMovieResponse();
        mFullyFetchedMovie = mMovieRepository.getFullyFetchedMovie();
    }

    public void insertMovie(Movie movie) {
        mMovieRepository.insert(movie);
    }
    public void likeMovie(Movie movie) {
        movie.setIsFavorite(1);
        mMovieRepository.insert(movie);
    }

    public void unlikeMovie(Movie movie) {
        movie.setIsFavorite(0);
        mMovieRepository.insert(movie);
    }

    public LiveData<List<Movie>> getFavoriteMovies() {
        return mFavoriteMovies;
    }

    public LiveData<List<Movie>> getAllMovies() {
        return mAllMovies;
    }
    public LiveData<Movie> getFullyFetchedMovie() {
        return mFullyFetchedMovie;
    }

    public LiveData<MovieResponse> getSearchedMovies() {
        return mMovieResponse;
    }
}
