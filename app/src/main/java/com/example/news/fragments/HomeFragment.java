package com.example.news.fragments;

import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.R;
import com.example.news.adapter.NewsAdapter;
import com.example.news.api.NewsApiService;
import com.example.news.api.RetrofitClient;
import com.example.news.model.NewsArticle;
import com.example.news.model.NewsResponse;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeFragment extends Fragment {

    private RecyclerView recyclerView;
    private ConstraintLayout searchContainer;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private TextView noResultFound;
    private NewsAdapter newsAdapter;
    private final List<NewsArticle> newsList = new ArrayList<>();
    private static final String API_KEY = "3ca0ba20aeea428881d1be0af67d8f11";
    private NewsApiService apiService;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);

        // Initialize all views
        initViews(view);
        setupRecyclerView();
        setupScrollAnimation(); // moved here after recyclerView is initialized
        fetchNews();

        // Add search listener
        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void afterTextChanged(Editable s) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    searchNews(s.toString());
                } else {
                    fetchNews();
                }
            }
        });

        return view;
    }

    private void initViews(View view) {
        searchContainer = view.findViewById(R.id.searchContainer);
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        noResultFound = view.findViewById(R.id.noResults);
        apiService = RetrofitClient.getRetrofitInstance().create(NewsApiService.class);
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(getContext(), newsList);
        recyclerView.setAdapter(newsAdapter);
    }

    private void setupScrollAnimation() {
        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            boolean isSearchBarVisible = true;

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                if (dy > 10 && isSearchBarVisible) {
                    // Scroll down - hide search bar smoothly
                    searchContainer.animate()
                            .translationY(-searchContainer.getHeight())
                            .alpha(0f)
                            .setDuration(300)
                            .start();
                    isSearchBarVisible = false;
                    // Reset background to transparent when hidden
                    searchContainer.setBackgroundColor(Color.TRANSPARENT);
                } else if (dy < -10 && !isSearchBarVisible) {
                    // Scroll up - show search bar smoothly
                    searchContainer.animate()
                            .translationY(0)
                            .alpha(1f)
                            .setDuration(300)
                            .start();
                    isSearchBarVisible = true;
                    // Change background color when visible
                    searchContainer.setBackgroundColor(getResources().getColor(R.color.background_light));
                }
            }
        });
    }



    private void fetchNews() {
        toggleLoading(true);
        apiService.getEverything("everything", API_KEY).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                handleApiResponse(response);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                handleApiFailure();
            }
        });
    }

    private void searchNews(String query) {
        toggleLoading(true);
        apiService.getEverything(query, API_KEY).enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, Response<NewsResponse> response) {
                handleApiResponse(response);
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                handleApiFailure();
            }
        });
    }

    private void handleApiResponse(Response<NewsResponse> response) {
        toggleLoading(false);
        if (response.isSuccessful() && response.body() != null && !response.body().getArticles().isEmpty()) {
            newsList.clear();
            newsList.addAll(response.body().getArticles());
            newsAdapter.notifyDataSetChanged();
            toggleResults(true);
        } else {
            toggleResults(false);
        }
    }

    private void handleApiFailure() {
        toggleLoading(false);
        toggleResults(false);
    }

    private void toggleLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        noResultFound.setVisibility(View.GONE);
    }

    private void toggleResults(boolean hasResults) {
        recyclerView.setVisibility(hasResults ? View.VISIBLE : View.GONE);
        noResultFound.setVisibility(hasResults ? View.GONE : View.VISIBLE);
    }
}
