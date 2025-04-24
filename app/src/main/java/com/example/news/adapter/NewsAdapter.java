package com.example.news.adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.news.NewsViewModel;
import com.example.news.R;
import com.example.news.WebViewActivity;
import com.example.news.database.NewsItem;
import com.example.news.model.NewsArticle;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<NewsArticle> newsList;
    private final List<NewsArticle> newsListFull;
    private final Context context;
    private final NewsViewModel newsViewModel;

    // Constructor
    public NewsAdapter(Context context, List<NewsArticle> newsList, NewsViewModel newsViewModel) {
        this.context = context;
        this.newsList = newsList;
        this.newsViewModel = newsViewModel;
        this.newsListFull = new ArrayList<>(newsList);
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        NewsArticle article = newsList.get(position);

        // Bind data to the views
        holder.titleTextView.setText(article.getTitle());
        holder.publishedAtTextView.setText(article.getPublishedAt());
        holder.descriptionTextView.setText(article.getDescription());

        // Load image with Glide
        Glide.with(context)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.newsImageView);

        // Set item click listener to open article in WebView
        holder.itemView.setOnClickListener(v -> openWebView(article));

        // Set save button click listener
        holder.btnSaved.setOnClickListener(v -> saveToFavorites(holder, article));
    }

    private void openWebView(NewsArticle article) {
        Intent intent = new Intent(context, WebViewActivity.class);
        intent.putExtra("url", article.getUrl());
        context.startActivity(intent);
    }

    private void saveToFavorites(NewsViewHolder holder, NewsArticle article) {
        if (article.getUrl() == null || article.getUrlToImage() == null) {
            Toast.makeText(context, "This item cannot be saved (missing URL or image)", Toast.LENGTH_SHORT).show();
            return;
        }

        NewsItem newsItem = new NewsItem(
                article.getUrl(),
                article.getTitle(),
                article.getDescription(),
                article.getUrlToImage(),
                article.getPublishedAt()
        );

        Log.d("SaveNews", "Saving item: \nTitle: " + article.getTitle() +
                "\nURL: " + article.getUrl() +
                "\nImage URL: " + article.getUrlToImage());

        // Save to database
        newsViewModel.insert(newsItem);
        Toast.makeText(context, "Saved to favorites", Toast.LENGTH_SHORT).show();
    }

    @Override
    public int getItemCount() {
        return newsList.size();
    }

    static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, publishedAtTextView;
        ImageView newsImageView, btnSaved;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            publishedAtTextView = itemView.findViewById(R.id.publishedAtTextView);
            newsImageView = itemView.findViewById(R.id.newsImageView);
            btnSaved = itemView.findViewById(R.id.btnSaved);
        }
    }
}
