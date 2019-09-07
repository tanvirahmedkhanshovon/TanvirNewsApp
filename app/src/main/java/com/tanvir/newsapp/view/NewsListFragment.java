package com.tanvir.newsapp.view;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import com.google.android.material.snackbar.Snackbar;
import com.tanvir.newsapp.R;
import com.tanvir.newsapp.adapter.NewsAdapter;
import com.tanvir.newsapp.databinding.NewsListBinding;
import com.tanvir.newsapp.model.Article;
import com.tanvir.newsapp.model.NewsApp;
import com.tanvir.newsapp.viewmodel.NewsViewModel;
import com.tanvir.newsapp.viewmodel.NewsViewModelFactory;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

public class NewsListFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {

    @Inject
    public NewsViewModelFactory factory;
    private NewsListBinding binding;
    private RecyclerView recyclerView;
    private SwipeRefreshLayout swipeRefreshLayout;
    private NewsViewModel viewModel;
    private NewsAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(LayoutInflater.from(container.getContext()), R.layout.news_list, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        recyclerView = binding.newsList;
        swipeRefreshLayout = binding.swipeRefreshLayout;
        NewsApp.getApp().getAppServiceComponent().inject(this);

        viewModel = ViewModelProviders.of(this, factory).get(NewsViewModel.class);
        adapter = new NewsAdapter();
        swipeRefreshLayout.setOnRefreshListener(NewsListFragment.this);


        viewModel.hasInternetConnection().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {

                    swipeRefreshLayout.setRefreshing(true);
                    loadDataFromWeb();


                } else {


                    swipeRefreshLayout.setRefreshing(true);

                    viewModel.getNewsCount().observe(getActivity(), new Observer<Integer>() {
                        @Override
                        public void onChanged(Integer integer) {
                            if (integer == 0) {

                                swipeRefreshLayout.setRefreshing(false);
                                Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "You have to connect to internet at least once and load data to proceed!!", Snackbar.LENGTH_LONG);
                                snackbar.show();
                            } else {
                                loadDataFromDataBase();

                            }
                        }
                    });


                }
                adapter.setListener(new NewsAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(Article article, View view) {

                        Navigation.findNavController(view).navigate(NewsListFragmentDirections.newsToDetails(article).setNewsDetailsContent(article));

                    }
                });
            }
        });


    }


    public void loadRecyclerView() {

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setHasFixedSize(true);


    }


    @Override
    public void onRefresh() {
        viewModel.hasInternetConnection().observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if (aBoolean) {
                    viewModel.clear();
                    viewModel.deleteAll();
                    adapter.clearNewsList();
                    adapter.notifyDataSetChanged();
                    swipeRefreshLayout.setRefreshing(true);
                    loadDataFromWeb();


                } else {

                    swipeRefreshLayout.setRefreshing(false);
                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Connect internet to proceed!!", Snackbar.LENGTH_LONG);
                    snackbar.show();

                }
            }
        });


    }

    public void loadDataFromWeb() {

        viewModel.getNewsFromInternetAndSaveToDataBase("us").observe(getActivity(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                swipeRefreshLayout.setRefreshing(false);
                if (articles != null && !articles.isEmpty()) {

                    if (articles.get(0).getMessage() == null) {
                        viewModel.clear();
                        loadDataFromDataBase();
                    } else {
                        Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), articles.get(0).getMessage(), Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } else {

                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Something went wrong!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }

            }
        });
    }

    public void loadDataFromDataBase() {

        viewModel.getNewsFromDataBaseInOffLine().observe(getActivity(), new Observer<List<Article>>() {
            @Override
            public void onChanged(List<Article> articles) {
                swipeRefreshLayout.setRefreshing(false);
                if (articles != null && !articles.isEmpty()) {

                    loadRecyclerView();
                    adapter.setNewsList((ArrayList<Article>) articles);
                    recyclerView.setAdapter(adapter);

                } else {

                    Snackbar snackbar = Snackbar.make(getActivity().findViewById(android.R.id.content), "Please Connect internet to proceed!!", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        viewModel.clear();
    }
}
