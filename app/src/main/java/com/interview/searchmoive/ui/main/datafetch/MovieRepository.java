package com.interview.searchmoive.ui.main.datafetch;

import android.content.Context;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.interview.searchmoive.ui.main.data.Movie;
import com.interview.searchmoive.ui.main.data.MovieDao;
import com.interview.searchmoive.ui.main.data.MovieDataBaseOperator;
import com.interview.searchmoive.ui.main.data.MovieDatabase;
import com.interview.searchmoive.ui.main.datafetch.network.MovieResponse;

import java.util.List;

/** centralized class managing livedata from both network and database */
public class MovieRepository {

    private MovieDao mMovieDao;
    private LiveData<Movie> mFullyFetchedMovie;
    private LiveData<List<Movie>> mFavoriteMovies;
    private LiveData<List<Movie>> mAllMovies;
    private MutableLiveData<MovieResponse> mSearchedMovieResponse;
    private SearchMovieQueryHelper mSearchMovieQueryHelper;

    public MovieRepository(Context context) {
        MovieDatabase movieDatabase = MovieDatabase.getInstance(context.getApplicationContext());
        mMovieDao = movieDatabase.movieDao();
        mFavoriteMovies = mMovieDao.getFavoriteMovies();
        mAllMovies = mMovieDao.getAllMovies();
        mSearchMovieQueryHelper = SearchMovieQueryHelper.getInstance(context);
        mSearchedMovieResponse = mSearchMovieQueryHelper.getMovieResponse();
        mFullyFetchedMovie = mSearchMovieQueryHelper.getFullyFetchedMovie();
    }

    /**
     * DB operations
     */
    public void insert(Movie movie) {
        new MovieDataBaseOperator.InsertMovieTask(mMovieDao).execute(movie);
    }

    public void delete(Movie movie) {
        new MovieDataBaseOperator.DeleteMovieTask(mMovieDao).execute(movie);
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

    public LiveData<MovieResponse> getSearchedMovieResponse() {
        return mSearchedMovieResponse;
    }
}
