package com.interview.searchmoive.ui.main.datafetch;

import android.util.Log;

import androidx.annotation.Nullable;

import com.interview.searchmoive.ui.main.data.Movie;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Hardcoded JSON parser logic
 */
public class ResponseParser {

    public static class ParsedSearchingResult {
        List<Movie> movieList;
        int totalCount;

        public ParsedSearchingResult(List<Movie> movieList, int totalCount) {
            this.movieList = movieList;
            this.totalCount = totalCount;
        }
    }

    public static @Nullable
    ParsedSearchingResult parseMovieSearchResponse(JSONObject baseJsonResponse) {
        if (baseJsonResponse == null) return null;
        List<Movie> movieList = new ArrayList<>();
        int totalCount = 0;

        try {
            JSONArray movieArray = baseJsonResponse.getJSONArray("Search");
            totalCount = baseJsonResponse.getInt("totalResults");
            for (int i = 0; i < movieArray.length(); i++) {
                JSONObject movieData = movieArray.getJSONObject(i);

                String imdbIDString = movieData.getString("imdbID");
                String movieName = movieData.getString("Title");
                int year = movieData.getInt("Year");
                String poster = movieData.getString("Poster");
                String director = movieData.optString("Director");
                String description = movieData.optString("Plot");
                String actors = movieData.optString("Actors");
                String awards = movieData.optString("Awards");

                movieList.add(new Movie(imdbIDString, movieName, director, year, description, poster,
                        actors, awards,
                        0));

            }
        } catch (JSONException e) {
            Log.e("Json Parse", "Fail to parse json response");
        }
        return new ParsedSearchingResult(movieList, totalCount);
    }

    public static @Nullable
    Movie parseSingleMovieInfo(JSONObject movieData) {
        if (movieData == null) return null;
        Movie movie = null;
        try {
            String imdbIDString = movieData.getString("imdbID");;
            String movieName = movieData.getString("Title");
            int year = movieData.getInt("Year");
            String poster = movieData.getString("Poster");
            String director = movieData.optString("Director");
            String description = movieData.optString("Plot");
            String actors = movieData.optString("Actors");
            String awards = movieData.optString("Awards");

            movie = new Movie(imdbIDString, movieName, director, year, description, poster,
                    actors, awards,
                    0);

        } catch (JSONException e) {
            Log.e("Json Parse", "Fail to parse json response");
        }
        return movie;
    }
}


