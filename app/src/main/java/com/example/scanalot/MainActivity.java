package com.example.scanalot;

import android.os.Bundle;


// Bottom Nav Imports
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;
// End of Bottom Nav Imports

import com.google.android.material.navigation.NavigationBarView;
import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.scanalot.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends AppCompatActivity implements NavigationBarView.OnItemSelectedListener{
    BottomNavigationView bottomNavigationView;

    private AppBarConfiguration appBarConfiguration;
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();

        fragmentTransaction.replace(R.id.nav_host_fragment_content_main,new FirstFragment()).commit();


        bottomNavigationView = findViewById(R.id.bottom_nav);
        bottomNavigationView.setOnItemSelectedListener(this);
        loadFragment(new scan_fragment());


        //binding = ActivityMainBinding.inflate(getLayoutInflater());
        // setContentView(binding.getRoot());

        // setSupportActionBar(binding.toolbar);

        // NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        // appBarConfiguration = new AppBarConfiguration.Builder(navController.getGraph()).build();
        // NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);

      /*  binding.fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });*/
    }


    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Fragment fragment = null;
        switch (item.getItemId()) {
            case R.id.scan:
                fragment = new scan_fragment();
                break;
            case R.id.edit_ticket:
                fragment = new edit_ticket_fragment();
                break;
            case R.id.select_lot:
                fragment = new select_lot_fragment();
                break;
            case R.id.log_out:
                fragment = new log_out_fragment();
                break;
        }
        if (fragment != null) {
            loadFragment(fragment);
        }
        return true;
    }
    void loadFragment(Fragment fragment) {
        //to attach fragment
        getSupportFragmentManager().beginTransaction().replace(R.id.relativelayout, fragment).commit();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment_content_main);
        return NavigationUI.navigateUp(navController, appBarConfiguration)
                || super.onSupportNavigateUp();
    }
}








