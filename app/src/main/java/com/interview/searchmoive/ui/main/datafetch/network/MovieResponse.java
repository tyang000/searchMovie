package com.interview.searchmoive.ui.main.datafetch.network;

import com.interview.searchmoive.ui.main.data.Movie;

import java.util.List;

public class MovieResponse {

    private List<Movie> movieList;
    private FetchState status;
    private String queryKey;
    private int page;
    private boolean hasNextPage;

    public MovieResponse(List<Movie> movieList, FetchState status, String queryKey, int page, boolean hasNextPage) {
        this.movieList = movieList;
        this.status = status;
        this.queryKey = queryKey;
        this.page = page;
        this.hasNextPage = hasNextPage;
    }

    public List<Movie> getMovieList() {
        return movieList;
    }

    public FetchState getStatus() {
        return status;
    }

    public String getQueryKey() {
        return queryKey;
    }

    public int getPage() {
        return page;
    }

    public boolean isHasNextPage() {
        return hasNextPage;
    }
}
