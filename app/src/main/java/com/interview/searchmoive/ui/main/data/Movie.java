package com.interview.searchmoive.ui.main.data;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "movie_table")
public class Movie {

    @PrimaryKey
    @NonNull
    private String id;

    @ColumnInfo(name = "name")
    private String name;

    @ColumnInfo(name = "director")
    private String director;

    @ColumnInfo(name = "year")
    private int year;

    @ColumnInfo(name = "description")
    private String description;

    @ColumnInfo(name = "poster_url")
    private String posterUrl;

    @ColumnInfo(name = "actors")
    private String actors;

    @ColumnInfo(name = "awards")
    private String awards;

    @ColumnInfo(name = "is_favorite")
    private int isFavorite;

    public Movie(String id, String name, String director, int year, String description,
                 String posterUrl, String actors, String awards, int isFavorite) {
        this.id = id;
        this.name = name;
        this.director = director;
        this.year = year;
        this.description = description;
        this.posterUrl = posterUrl;
        this.isFavorite = isFavorite;
        this.awards = awards;
        this.actors = actors;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setPosterUrl(String posterUrl) {
        this.posterUrl = posterUrl;
    }

    public void setActors(String actors) {
        this.actors = actors;
    }

    public void setAwards(String awards) {
        this.awards = awards;
    }

    public void setIsFavorite(int isFavorite) {
        this.isFavorite = isFavorite;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDirector() {
        return director;
    }

    public int getYear() {
        return year;
    }

    public String getDescription() {
        return description;
    }

    public String getPosterUrl() {
        return posterUrl;
    }

    public String getActors() {
        return actors;
    }

    public String getAwards() {
        return awards;
    }

    public int getIsFavorite() {
        return isFavorite;
    }
}
