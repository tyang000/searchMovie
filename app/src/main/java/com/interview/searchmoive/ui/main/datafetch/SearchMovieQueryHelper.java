package com.interview.searchmoive.ui.main.datafetch;

import android.content.Context;

import androidx.lifecycle.MutableLiveData;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.interview.searchmoive.ui.main.data.Movie;
import com.interview.searchmoive.ui.main.datafetch.network.MovieResponse;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import static com.interview.searchmoive.ui.main.datafetch.network.FetchState.FAILURE;
import static com.interview.searchmoive.ui.main.datafetch.network.FetchState.LOADING;
import static com.interview.searchmoive.ui.main.datafetch.network.FetchState.SUCCESS;

/**
 * Singleton class to issue search movie request
 */
public class SearchMovieQueryHelper {

    // Seems we still need an API key to perform the get method.
    private static final String API_KEY = "ddec0d4a";
    private static final String REQUEST_URL_HEADER = "http://omdbapi.com/?";
    private static final String SEARCH_TAG = "search_query_tag";

    private static SearchMovieQueryHelper mQueryHelperInstance;
    private Context mContext;
    private RequestQueue mRequestQueue;
    private MutableLiveData<MovieResponse> mMovieResponse = new MutableLiveData<>();
    private MutableLiveData<Movie> mFullyFetchedMovie = new MutableLiveData<>();

    public static synchronized SearchMovieQueryHelper getInstance(Context context) {
        if (mQueryHelperInstance == null) {
            mQueryHelperInstance = new SearchMovieQueryHelper(context.getApplicationContext());
        }
        return mQueryHelperInstance;
    }

    private SearchMovieQueryHelper(Context context) {
        mContext = context;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            mRequestQueue = Volley.newRequestQueue(mContext.getApplicationContext());
        }
        return mRequestQueue;
    }

    public void searchResult(final String searchTerm, final int page) {
        String url = buildSearchRequestUrl(searchTerm, page);
        final boolean isSameQuery =
                mMovieResponse.getValue() != null && searchTerm.equals(mMovieResponse.getValue().getQueryKey());

        // Cancel all search queries, if search term has changed, clear the request queue.
        getRequestQueue().cancelAll(new RequestQueue.RequestFilter() {
            @Override
            public boolean apply(Request<?> request) {
                return isSameQuery ? SEARCH_TAG.equals(request.getTag()) : true;
            }
        });

        mMovieResponse.postValue(new MovieResponse(isSameQuery ? getPreviousMovieList() : null,
                LOADING, searchTerm, page, false));

        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processParsedResult(response, searchTerm);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                mMovieResponse.postValue(new MovieResponse(null, FAILURE, searchTerm, 0, false));
            }
        });
        stringRequest.setTag(SEARCH_TAG);
        getRequestQueue().add(stringRequest);
    }

    public void getMoreInfoByPostId(final String imdbId) {
        JsonObjectRequest stringRequest = new JsonObjectRequest(Request.Method.GET,
                buildGetOneIdUrl(imdbId), null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        processSingleParsedResult(response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        getRequestQueue().add(stringRequest);
    }

    private static String buildSearchRequestUrl(final String searchTerm, final int page) {
        String processedSearchTerm = searchTerm.replace(" ", "+");
        return REQUEST_URL_HEADER + "apikey=" + API_KEY + "&s=" + processedSearchTerm + "&page=" + page;
    }

    private static String buildGetOneIdUrl(final String imdbId) {
        return REQUEST_URL_HEADER + "apikey=" + API_KEY + "&i=" + imdbId;
    }

    private void processParsedResult(JSONObject response, String searchTerm) {
        if (response == null) {
            return;
        }

        ResponseParser.ParsedSearchingResult result =
                ResponseParser.parseMovieSearchResponse(response);
        if (result == null || result.movieList == null) {
            return;
        }
        for (Movie movie : result.movieList) {
            getMoreInfoByPostId(movie.getId());
        }
        List<Movie> previousList = getPreviousMovieList();
        previousList.addAll(result.movieList);
        mMovieResponse.postValue(new MovieResponse(previousList, SUCCESS, searchTerm,
                previousList.size() / 10 + 1, previousList.size() < result.totalCount));
    }

    private void processSingleParsedResult(JSONObject response) {
        if (response == null) {
            return;
        }

        Movie result = ResponseParser.parseSingleMovieInfo(response);
        if (result == null) {
            return;
        }
        mFullyFetchedMovie.postValue(result);

    }

    private List<Movie> getPreviousMovieList() {
        return mMovieResponse != null && mMovieResponse.getValue() != null && mMovieResponse.getValue().getMovieList() != null ?
                mMovieResponse.getValue().getMovieList() : new ArrayList<Movie>();
    }

    public MutableLiveData<MovieResponse> getMovieResponse() {
        return mMovieResponse;
    }

    public MutableLiveData<Movie> getFullyFetchedMovie() {
        return mFullyFetchedMovie;
    }
}
