package com.interview.searchmoive.ui.main.data;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

@Dao
public interface MovieDao {
    @Query("SELECT * FROM movie_table WHERE is_favorite = 1")
    LiveData<List<Movie>> getFavoriteMovies();

    @Query("SELECT * FROM movie_table")
    LiveData<List<Movie>> getAllMovies();

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(Movie movies);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertAll(Movie... movies);

    @Delete
    void delete(Movie movie);

    @Query("UPDATE movie_table SET is_favorite = :isFavorite WHERE id = :id")
    void updateIsFavorite(int id, int isFavorite);

    @Query("UPDATE movie_table SET director = :director WHERE id = :id")
    void updateDirectorInfo(int id, String director);

    @Query("UPDATE movie_table SET description = :description WHERE id = :id")
    void updateDescriptionInfo(int id, String description);

    @Query("UPDATE movie_table SET actors = :actors WHERE id = :id")
    void updateActorsInfo(int id, String actors);
}
