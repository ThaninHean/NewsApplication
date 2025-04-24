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
import androidx.room.Delete;

import com.bumptech.glide.Glide;
import com.example.news.NewsViewModel;
import com.example.news.R;
import com.example.news.WebViewActivity;
import com.example.news.database.NewsItem;

import java.util.ArrayList;
import java.util.List;

public class SavedNewsAdapter extends RecyclerView.Adapter<SavedNewsAdapter.SavedNewsViewHolder> {

    private Context context;
    private List<NewsItem> savedNewsList = new ArrayList<>();
    private NewsViewModel newsViewModel;

    public SavedNewsAdapter(Context context) {
        this.context = context;
    }

    public void setNewsList(List<NewsItem> newsList) {
        this.savedNewsList = newsList;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public SavedNewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.saved_news_item, parent, false);
        return new SavedNewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SavedNewsViewHolder holder, int position) {
        NewsItem item = savedNewsList.get(position);

        holder.titleTextView.setText(item.getTitle());
        holder.publishedAtTextView.setText(item.getPublishedAt());
        holder.descriptionTextView.setText(item.getDescription());

        if (item.getImageUrl() == null || item.getImageUrl().isEmpty()) {
            Glide.with(context)
                    .load(R.drawable.loading) // Default image
                    .into(holder.newsImageView);
        } else {
            Glide.with(context)
                    .load(item.getImageUrl())
                    .placeholder(R.drawable.loading)
                    .error(R.drawable.loading)
                    .into(holder.newsImageView);
        }


        holder.itemView.setOnClickListener(v -> {
            String url = item.getUrl();
            Intent intent = new Intent(context, WebViewActivity.class);
            intent.putExtra("url", item.getUrl());
            context.startActivity(intent);
        });

        // Handle the delete button click
        holder.btnDelete.setOnClickListener(v -> {
            int currentPosition = holder.getBindingAdapterPosition(); // use this instead of `position`

            if (currentPosition != RecyclerView.NO_POSITION && newsViewModel != null) {
                NewsItem itemToDelete = savedNewsList.get(currentPosition);

                // Delete from database
                newsViewModel.deleteNews(itemToDelete);

                // Remove from list and update UI
                savedNewsList.remove(currentPosition);
                notifyItemRemoved(currentPosition);
                notifyItemRangeChanged(currentPosition, savedNewsList.size());

                Log.d("SavedNewsAdapter", "Deleted item at position: " + currentPosition);
                Toast.makeText(context, "Deleted", Toast.LENGTH_SHORT).show();
            } else {
                Log.e("SavedNewsAdapter", "Invalid position or ViewModel is null.");
            }
        });




    }



    @Override
    public int getItemCount() {
        return savedNewsList.size();
    }

    public void setNewsViewModel(NewsViewModel newsViewModel) {
        this.newsViewModel = newsViewModel;
    }

    public static class SavedNewsViewHolder extends RecyclerView.ViewHolder {
        TextView titleTextView, descriptionTextView, publishedAtTextView;
        ImageView newsImageView,btnDelete;

        public SavedNewsViewHolder(@NonNull View itemView) {
            super(itemView);
            titleTextView = itemView.findViewById(R.id.titleTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            publishedAtTextView = itemView.findViewById(R.id.publishedAtTextView);
            newsImageView = itemView.findViewById(R.id.newsImageView);
            btnDelete = itemView.findViewById(R.id.btnDelete);
        }
    }
}