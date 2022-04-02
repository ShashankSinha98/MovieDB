package com.shashank.moviedb.ui;

import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.NavOptions;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;
import androidx.navigation.ui.AppBarConfiguration;

import com.google.android.material.navigation.NavigationView;
import com.shashank.moviedb.R;
import com.shashank.moviedb.data.remote.MovieRepository;

import java.util.HashSet;

import javax.inject.Inject;

import dagger.android.support.DaggerAppCompatActivity;

public class HomeActivity extends DaggerAppCompatActivity implements NavController.OnDestinationChangedListener {

    private static final String TAG = "HomeActivity";

    @Inject
    public MovieRepository movieRepository;


    private Toolbar toolbar;
    private TextView toolbarTextView;
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;

    private NavController navController;
    private AppBarConfiguration appBarConfiguration;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        toolbar = findViewById(R.id.toolbar);
        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        toolbarTextView = findViewById(R.id.tv_toolbar_title);


        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        initNavigation();
    }

    private void initNavigation() {
        navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        //appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        NavigationUI.setupActionBarWithNavController(this, navController, drawerLayout);
        NavigationUI.setupWithNavController(navigationView, navController);
//        navigationView.setNavigationItemSelectedListener(this);
        navController.addOnDestinationChangedListener(this);
    }

    @Override
    public boolean onSupportNavigateUp() {
        return NavigationUI.navigateUp(navController, drawerLayout);
    }

 /*   @Override
    public void onDestinationChanged(@NonNull NavController controller,
                                     @NonNull NavDestination destination,
                                     @Nullable Bundle arguments) {

        switch (destination.getId()) {
            case R.id.nav_trending:
                    toolbar.setVisibility(View.VISIBLE);
                    toolbarTextView.setText(destination.getLabel());
                    break;

        }
    }*/

   /* @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {

        switch (item.getItemId()) {
            case R.id.menu_trending_movie:
                if(isValidDestination(R.id.nav_trending)) {
                    NavOptions navOptions = new NavOptions.Builder().setPopUpTo(R.id.main_nav_graph, true).build();
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_trending, null, navOptions);
                }
                break;

            case R.id.menu_now_playing_movie:
                if(isValidDestination(R.id.nav_now_playing)) {
                    Navigation.findNavController(this, R.id.nav_host_fragment).navigate(R.id.nav_now_playing);
                }
                break;


        }
        item.setChecked(true);
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }*/

    private boolean isValidDestination(int destination) {
        return destination != Navigation.findNavController(this, R.id.nav_host_fragment).getCurrentDestination().getId();
    }

    @Override
    public void onBackPressed() {
        Log.d(TAG,"xlr8: onbackpressed");
        super.onBackPressed();
    }

    @Override
    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
        switch (destination.getId()) {
            case R.id.nav_trending:
            case R.id.nav_now_playing:
                toolbar.setVisibility(View.VISIBLE);
                toolbarTextView.setText(destination.getLabel());
                break;

        }
    }
}