    package com.example.news.adapter;


    import android.content.Context;
    import android.content.Intent;
    import android.view.LayoutInflater;
    import android.view.View;
    import android.view.ViewGroup;
    import android.widget.ImageView;
    import android.widget.TextView;
    import androidx.annotation.NonNull;
    import androidx.recyclerview.widget.RecyclerView;
    import com.bumptech.glide.Glide;
    import com.example.news.R;
    import com.example.news.WebViewActivity;
    import com.example.news.model.NewsArticle;

    import java.util.ArrayList;
    import java.util.List;

    public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
        private List<NewsArticle> newsList;// List of news articles
        private List<NewsArticle> newsListFull;
        private Context context;// Context of the activity that uses this adapter


        // Constructor that takes a context and a list of news articles
        public NewsAdapter(Context context, List<NewsArticle> newsList) {
            this.context = context;
            this.newsList = newsList;
            this.newsListFull = new ArrayList<>(newsList);
        }


        @NonNull
        @Override
        // Called when the RecyclerView needs a new ViewHolder to represent an item in the list
        public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(context).inflate(R.layout.news_item, parent, false);
            return new NewsViewHolder(view);
        }

        @Override
        // Called to bind the data at the specified position to the ViewHolder
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

            holder.itemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, WebViewActivity.class);
                intent.putExtra("url", article.getUrl());
                context.startActivity(intent);
            });
        }

        @Override
        // Returns the total number of items in the data set held by the adapter
        public int getItemCount() {
            return newsList.size();
        }


        // ViewHolder class that holds the views for each item in the RecyclerView
        static class NewsViewHolder extends RecyclerView.ViewHolder {
            TextView titleTextView, descriptionTextView, publishedAtTextView;
            ImageView newsImageView;

            public NewsViewHolder(@NonNull View itemView) {
                super(itemView);
                titleTextView = itemView.findViewById(R.id.titleTextView);
                descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
                publishedAtTextView = itemView.findViewById(R.id.publishedAtTextView);
                newsImageView = itemView.findViewById(R.id.newsImageView);
            }
        }
    }
