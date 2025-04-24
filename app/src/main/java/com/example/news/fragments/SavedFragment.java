package com.example.news.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import com.example.news.NewsViewModel;
import com.example.news.R;
import com.example.news.adapter.SavedNewsAdapter;

public class SavedFragment extends Fragment {

    private RecyclerView recyclerView;
    private SavedNewsAdapter adapter;
    private NewsViewModel newsViewModel;
    private TextView emptyTextView;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.saved_fragment, container, false);
        emptyTextView = view.findViewById(R.id.emptyTextView);

        // Initialize the ViewModel
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);

        recyclerView = view.findViewById(R.id.savedRecyclerView);
        recyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));

        adapter = new SavedNewsAdapter(getContext());
        recyclerView.setAdapter(adapter);

        // Pass the ViewModel to the adapter
        adapter.setNewsViewModel(newsViewModel);  // <-- Add this line

        // Observe saved news items and update the adapter
        newsViewModel.getSavedNews().observe(getViewLifecycleOwner(), newsItems -> {
            adapter.setNewsList(newsItems);

            // Handle empty view visibility
            if (newsItems.isEmpty()) {
                recyclerView.setVisibility(View.GONE);
                emptyTextView.setVisibility(View.VISIBLE);
            } else {
                recyclerView.setVisibility(View.VISIBLE);
                emptyTextView.setVisibility(View.GONE);
            }
        });

        return view;
    }

}
