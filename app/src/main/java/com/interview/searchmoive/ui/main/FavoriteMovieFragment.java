package com.interview.searchmoive.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
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

import java.util.List;

public class FavoriteMovieFragment extends Fragment {

    private MovieViewModel mMovieViewModel;
    private ComponentEventHandler mComponentEventHandler;

    public static FavoriteMovieFragment newInstance() {
        FavoriteMovieFragment fragment = new FavoriteMovieFragment();
        Bundle bundle = new Bundle();
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mMovieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);
        mComponentEventHandler = new ComponentEventHandlerImpl(mMovieViewModel);
    }

    @Override
    public View onCreateView(
            @NonNull LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.favorite_movie_fragment, container, false);
        final RecyclerView searchResultRecyclerView = root.findViewById(R.id.favorite_movie_list);
        searchResultRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        searchResultRecyclerView.setHasFixedSize(true);
        final MovieCardAdapter adapter = new MovieCardAdapter();
        adapter.setComponentClickHandler(mComponentEventHandler);
        searchResultRecyclerView.setAdapter(adapter);

        mMovieViewModel.getFavoriteMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(List<Movie> favoriteMovies) {
                if (favoriteMovies != null) {
                    adapter.setFavoriteMovieLists(favoriteMovies);
                }
            }
        });
        return root;
    }
}
