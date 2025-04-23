package com.example.news.api;

import com.example.news.model.NewsResponse;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;


//Interface for the News API service
public interface NewsApiService {
    @GET("everything")// Endpoint for retrieving
    Call<NewsResponse> getEverything(
            @Query("q") String query,
            @Query("apiKey") String apiKey
    );

}


