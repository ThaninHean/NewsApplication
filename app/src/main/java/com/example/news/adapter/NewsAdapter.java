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

    private List<NewsArticle> newsList;
    private List<NewsArticle> newsListFull;
    private Context context;
    private NewsViewModel newsViewModel;


    // Updated constructor with ViewModel
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

        holder.titleTextView.setText(article.getTitle());
        holder.publishedAtTextView.setText(article.getPublishedAt());
        holder.descriptionTextView.setText(article.getDescription());

        Glide.with(context)
                .load(article.getUrlToImage())
                .placeholder(R.drawable.loading)
                .error(R.drawable.loading)
                .into(holder.newsImageView);

        // Open news in WebView
        holder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", article.getUrl());
            context.startActivity(intent);
        });

        // Save news to Room when btnSaved is clicked
        holder.btnSaved.setOnClickListener(v -> {
            NewsArticle saveItem = newsList.get(holder.getBindingAdapterPosition());

            // Null check to avoid crashing
            if (article.getUrl() == null || article.getUrlToImage() == null) {
                Toast.makeText(context, "This item cannot be saved (missing URL or image)", Toast.LENGTH_SHORT).show();
                return;
            }

            // Map API model to Room model
            NewsItem newsItem = new NewsItem(
                    article.getUrl(), // Use URL as primary key
                    article.getTitle(),
                    article.getDescription(),
                    article.getUrlToImage(),
                    article.getPublishedAt()
            );

            // Log for debugging
            Log.d("SaveNews", "Saving item: \nTitle: " + article.getTitle() +
                    "\nURL: " + article.getUrl() +
                    "\nImage URL: " + article.getUrlToImage());

            // Save to DB
            newsViewModel.insert(newsItem);
            Toast.makeText(context, "Saved to favorites", Toast.LENGTH_SHORT).show();
        });


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

