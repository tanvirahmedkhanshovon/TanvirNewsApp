package com.tanvir.newsapp.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.tanvir.newsapp.R;
import com.tanvir.newsapp.databinding.NewsItemBinding;
import com.tanvir.newsapp.model.Article;

import java.util.ArrayList;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {
    private ArrayList<Article> articleArrayList = new ArrayList<>();
    private OnItemClickListener listener;

    public void setListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        NewsItemBinding binding = DataBindingUtil.inflate(LayoutInflater.from(parent.getContext()), R.layout.news_item, parent, false);

        return new NewsViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        Article article = articleArrayList.get(position);
        holder.binding.setNewsArticle(article);


    }

    public void clearNewsList() {

        articleArrayList.clear();
    }

    public void setNewsList(ArrayList<Article> articleArrayList) {

        this.articleArrayList.addAll(articleArrayList);

    }

    @Override
    public int getItemCount() {
        return articleArrayList.size();
    }

    public interface OnItemClickListener {

        void onItemClick(Article article, View view);
    }

    public class NewsViewHolder extends RecyclerView.ViewHolder {

        private NewsItemBinding binding;

        public NewsViewHolder(@NonNull NewsItemBinding itemView) {
            super(itemView.getRoot());
            binding = itemView;
            binding.getRoot().setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int clickedPosition = getAdapterPosition();
                    if (listener != null && clickedPosition != RecyclerView.NO_POSITION) {
                        listener.onItemClick(articleArrayList.get(clickedPosition), view);
                    }
                }
            });


        }
    }
}
