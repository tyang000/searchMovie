package com.interview.searchmoive.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.interview.searchmoive.R;
import com.interview.searchmoive.ui.main.components.ComponentEventHandler;
import com.interview.searchmoive.ui.main.components.ComponentEventHandlerImpl;
import com.interview.searchmoive.ui.main.components.MovieCardAdapter;
import com.interview.searchmoive.ui.main.data.Movie;
import com.interview.searchmoive.ui.main.data.MovieViewModel;
import com.interview.searchmoive.ui.main.datafetch.SearchMovieQueryHelper;
import com.interview.searchmoive.ui.main.datafetch.network.FetchState;
import com.interview.searchmoive.ui.main.datafetch.network.MovieResponse;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SearchMovieFragment extends Fragment {

    private MovieViewModel mMovieViewModel;
    private SearchMovieQueryHelper mSearchMovieQueryHelper;
    private ComponentEventHandler mComponentEventHandler;
    private String mSearchTerm;
    private boolean mHasNextPage;
    private int mCurrentPage;

    public static SearchMovieFragment newInstance() {
        SearchMovieFragment fragment = new SearchMovieFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mSearchMovieQueryHelper = SearchMovieQueryHelper.getInstance(getContext());
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mMovieViewModel.getFullyFetchedMovie().observe(this, new Observer<Movie>() {
            @Override
            public void onChanged(Movie movie) {
                boolean isAlreadyLiked = false;
                if (mMovieViewModel.getFavoriteMovies() != null && mMovieViewModel.getFavoriteMovies().getValue() != null) {
                    for (Movie favoriteMovie : mMovieViewModel.getFavoriteMovies().getValue()) {
                        if (favoriteMovie.getId() == movie.getId()) {
                            isAlreadyLiked = true;
                        }
                    }
                }
                if (isAlreadyLiked) {
                    movie.setIsFavorite(1);
                }
                mMovieViewModel.insertMovie(movie);
            }
        });
        mComponentEventHandler = new ComponentEventHandlerImpl(mMovieViewModel);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.search_movie_fragment, container, false);
        final SearchView searchView = root.findViewById(R.id.search_input);
        searchView.setQueryHint(getContext().getString(R.string.search_edit_text_hint));
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                mSearchTerm = query;
                mSearchMovieQueryHelper.searchResult(query, 1);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return false;
            }
        });

        final RecyclerView searchResultRecyclerView = root.findViewById(R.id.search_result);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultRecyclerView.setHasFixedSize(true);
        final MovieCardAdapter adapter = new MovieCardAdapter();
        adapter.setComponentClickHandler(mComponentEventHandler);
        searchResultRecyclerView.setAdapter(adapter);

        searchResultRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                if (!recyclerView.canScrollVertically(1)) {
                    mSearchMovieQueryHelper.searchResult(mSearchTerm, mCurrentPage + 1);
                }
            }
        });

        final TextView networkStateInfoView = root.findViewById(R.id.network_state_info);

        mMovieViewModel.getSearchedMovies().observe(this, new Observer<MovieResponse>() {
            @Override
            public void onChanged(MovieResponse movieResponse) {
                if (movieResponse != null) {
                    adapter.setMovieSearchResults(movieResponse.getMovieList(),
                            movieResponse.getQueryKey());
                    if (mMovieViewModel.getAllMovies() != null && mMovieViewModel.getAllMovies().getValue() != null) {
                        updateAdapterListWithMovieDB(adapter,
                                mMovieViewModel.getAllMovies().getValue());
                    }
                    updateNetworkInfo(movieResponse.getStatus(), networkStateInfoView);
                    mHasNextPage = movieResponse.isHasNextPage();
                    mCurrentPage = movieResponse.getPage();
                }
            }
        });

        mMovieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> allMoviesFromDb) {
                updateAdapterListWithMovieDB(adapter, allMoviesFromDb);
            }
        });
        return root;
    }

    private static void updateAdapterListWithMovieDB(final MovieCardAdapter adapter,
                                                     final List<Movie> allMoviesFromDb) {
        Map<String, Movie> movieMap = new HashMap<>();
        for (Movie movie : allMoviesFromDb) {
            movieMap.put(movie.getId(), movie);
        }
        adapter.updateListWithDataFromDB(movieMap);
    }

    private void updateNetworkInfo(FetchState state, TextView networkStateInfoView) {
        switch (state) {
            case LOADING:
                networkStateInfoView.setText(R.string.network_loading);
                break;
            case FAILURE:
                networkStateInfoView.setText(R.string.network_failure);
                break;
            case SUCCESS:
                networkStateInfoView.setText("");
                networkStateInfoView.setVisibility(View.GONE);
                break;
        }
    }
}
