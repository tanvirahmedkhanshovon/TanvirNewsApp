package com.tanvir.newsapp.ui;

import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import com.tanvir.newsapp.R;
import com.tanvir.newsapp.databinding.MainActivityBinding;
import com.tanvir.newsapp.view.NewsDetailsFragment;

public class MainActivity extends AppCompatActivity implements NewsDetailsFragment.OnFragmentInteractionListener {

    private MainActivityBinding binding;
    private Toolbar toolbar;
    private NavController navController;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.main_activity);
        toolbar = binding.toolbar;
        setSupportActionBar(toolbar);
        setNavigation();


    }

    private void setNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController);


    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {

            navController.popBackStack();


        }
        return super.onOptionsItemSelected(item);
    }
}
