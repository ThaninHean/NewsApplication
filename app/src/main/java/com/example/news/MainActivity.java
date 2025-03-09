package com.example.news;

import android.content.Context;
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
import com.example.news.model.NewsArticle;
import com.example.news.model.NewsResponse;
import com.example.news.retrofit.NewsApiService;

import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    private RecyclerView recyclerView;
    private NewsAdapter newsAdapter;
    private List<NewsArticle> newsList = new ArrayList<>();
    private EditText searchEditText;
    private ProgressBar progressBar;
    private TextView noResultFound;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        recyclerView = findViewById(R.id.recyclerView);
        searchEditText = findViewById(R.id.searchEditText);
        progressBar = findViewById(R.id.progressBar);
        noResultFound = findViewById(R.id.noResults);

        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        newsAdapter = new NewsAdapter(this, newsList);
        recyclerView.setAdapter(newsAdapter);

        fetchNews(); // Initial fetch of general news

        // Set up a text watcher to trigger search as the user types
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

    // Fetch news from the API
    private void fetchNews() {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        noResultFound.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        // Create an instance of the NewsApiService
        NewsApiService apiService = retrofit.create(NewsApiService.class);
        String apiKey = "3ca0ba20aeea428881d1be0af67d8f11";

        // Make the API call to get top headlines
        Call<NewsResponse> call = apiService.getTopHeadlines("everything", apiKey);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, retrofit2.Response<NewsResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && !response.body().getArticles().isEmpty()) {
                    newsList.clear();
                    newsList.addAll(response.body().getArticles());
                    newsAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    noResultFound.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noResultFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {// Handle API call failure
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                noResultFound.setVisibility(View.VISIBLE);
            }
        });
    }

    // Search for news based on the provided query
    private void searchNews(String query) {
        progressBar.setVisibility(View.VISIBLE);
        recyclerView.setVisibility(View.GONE);
        noResultFound.setVisibility(View.GONE);

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://newsapi.org/v2/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        NewsApiService apiService = retrofit.create(NewsApiService.class);
        String apiKey = "41ae75be90bc42878fc1d3225383ba9a"; // Replace with your API key

        Call<NewsResponse> call = apiService.getEverything(query, apiKey);
        call.enqueue(new Callback<NewsResponse>() {
            @Override
            public void onResponse(Call<NewsResponse> call, retrofit2.Response<NewsResponse> response) {
                progressBar.setVisibility(View.GONE);

                if (response.isSuccessful() && response.body() != null && !response.body().getArticles().isEmpty()) {
                    newsList.clear();
                    newsList.addAll(response.body().getArticles());
                    newsAdapter.notifyDataSetChanged();
                    recyclerView.setVisibility(View.VISIBLE);
                    noResultFound.setVisibility(View.GONE);
                } else {
                    recyclerView.setVisibility(View.GONE);
                    noResultFound.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<NewsResponse> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                recyclerView.setVisibility(View.GONE);
                noResultFound.setVisibility(View.VISIBLE);
            }
        });
    }

}