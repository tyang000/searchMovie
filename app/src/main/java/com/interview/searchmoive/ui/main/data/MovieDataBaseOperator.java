package com.interview.searchmoive.ui.main.data;

import android.os.AsyncTask;

/**
 * Helper class processing Async database job
 */
public class MovieDataBaseOperator {

    public static class InsertMovieTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mMovieDao;

        public InsertMovieTask(MovieDao movieDao) {
            mMovieDao = movieDao;
        }

        @Override
        public Void doInBackground(Movie... movies) {
            mMovieDao.insert(movies[0]);
            return null;
        }
    }

    public static class DeleteMovieTask extends AsyncTask<Movie, Void, Void> {
        private MovieDao mMovieDao;

        public DeleteMovieTask(MovieDao movieDao) {
            mMovieDao = movieDao;
        }

        @Override
        public Void doInBackground(Movie... movies) {
            mMovieDao.delete(movies[0]);
            return null;
        }
    }
}
