package com.example.news;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.news.adapter.NewsAdapter;
import com.example.news.api.NewsApiService;
import com.example.news.api.RetrofitClient;
import com.example.news.model.NewsArticle;
import com.example.news.model.NewsResponse;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> newsList = new ArrayList<>();
    private EditText searchEditText;
    private ProgressBar progressBar;
    private TextView noResultFound;

    private static final String BASE_URL = "https://newsapi.org/v2/";
    private static final String API_KEY = "3ca0ba20aeea428881d1be0af67d8f11";

    private NewsApiService apiService;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();
        setupRecyclerView();
        fetchNews();

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() >= 1) {
                    searchNews(s.toString());
                } else {
                    fetchNews(); // Fetch general news if the search query is empty
                }
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    // Initialize UI components
    private void initViews() {
        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        progressBar = findViewById(R.id.progressBar);
        noResultFound = findViewById(R.id.noResults);
    }

    // Setup RecyclerView
    private void setupRecyclerView() {
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(newsAdapter);
    }

    // Fetch news from the API
    private void fetchNews() {
        toggleLoading(true);
        NewsApiService apiService = RetrofitClient.getRetrofitInstance().create(NewsApiService.class);
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

    // Search for news based on the provided query
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

    // Handle API response
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

    // Handle API failure
    private void handleApiFailure() {
        toggleLoading(false);
        toggleResults(false);
    }

    // Show/hide loading indicators
    private void toggleLoading(boolean isLoading) {
        progressBar.setVisibility(isLoading ? View.VISIBLE : View.GONE);
        recyclerView.setVisibility(isLoading ? View.GONE : View.VISIBLE);
        noResultFound.setVisibility(View.GONE);
    }

    // Show/hide results
    private void toggleResults(boolean hasResults) {
        recyclerView.setVisibility(hasResults ? View.VISIBLE : View.GONE);
        noResultFound.setVisibility(hasResults ? View.GONE : View.VISIBLE);
    }
}
