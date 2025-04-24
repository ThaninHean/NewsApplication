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

import com.example.news.NewsViewModel;
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

import androidx.lifecycle.ViewModelProvider;
public class HomeFragment extends Fragment {
    private ConstraintLayout searchContainer;
    private RecyclerView recyclerView;
    private EditText searchEditText;
    private ProgressBar progressBar;
    private TextView noResultFound;
    private NewsAdapter newsAdapter;
    private final List<NewsArticle> newsList = new ArrayList<>();
    private static final String API_KEY = "41ae75be90bc42878fc1d3225383ba9a";
    private NewsApiService apiService;
    private NewsViewModel newsViewModel;
    private String savedQuery = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_fragment, container, false);
        searchContainer = view.findViewById(R.id.searchContainer);
        initViews(view);
        setupViewModel();          // ✅ Add this
        setupRecyclerView();
        fetchNews();
        setupScrollAnimation();

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
                } else if (dy < -10 && !isSearchBarVisible) {
                    // Scroll up - show search bar smoothly
                    searchContainer.animate()
                            .translationY(0)
                            .alpha(1f)
                            .setDuration(300)
                            .start();
                    isSearchBarVisible = true;
                }
            }
        });
    }


    private void initViews(View view) {
        searchEditText = view.findViewById(R.id.searchEditText);
        recyclerView = view.findViewById(R.id.recyclerView);
        progressBar = view.findViewById(R.id.progressBar);
        noResultFound = view.findViewById(R.id.noResults);
        apiService = RetrofitClient.getRetrofitInstance().create(NewsApiService.class);
    }

    private void setupViewModel() {
        newsViewModel = new ViewModelProvider(requireActivity()).get(NewsViewModel.class); // ✅ Properly get ViewModel
    }

    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newsAdapter = new NewsAdapter(getContext(), newsList, newsViewModel); // ✅ Pass ViewModel to adapter
        recyclerView.setAdapter(newsAdapter);
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

    @Override
    public void onPause() {
        super.onPause();
        savedQuery = searchEditText.getText().toString();
    }

    @Override
    public void onResume() {
        super.onResume();
        if (savedQuery != null && !savedQuery.equals("null")) {
            searchEditText.setText(savedQuery);
        }
    }
}
