package com.example.useradvent;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.NavigationUI;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.example.useradvent.ui.Profile.Profile;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Objects;

public class userNavigation extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ActionBarDrawerToggle toggle;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_navigation);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);
        NavController navController = Navigation.findNavController(this, R.id.frame_layout);
        DrawerLayout drawerLayout = findViewById(R.id.drawerLayout);
        NavigationView navigationView = findViewById(R.id.navigationView);

        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.start, R.string.stop);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);

        navigationView.setNavigationItemSelectedListener(this);
        NavigationUI.setupWithNavController(bottomNavigationView, navController);



    }

    @Override
    public boolean onSupportNavigateUp() {
        return super.onSupportNavigateUp();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(toggle.onOptionsItemSelected(item))
            return true;
        return true;

    }


    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onNavigationItemSelected(@NonNull  MenuItem item) {
        switch (item.getItemId()){
            case R.id.nav_videos:

                Toast.makeText(this, "Video", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_downloads:
                Toast.makeText(this, "downloads", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_profile:
                startActivity(new Intent(this, Profile.class));
//                FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
//                transaction.add(R.id.frame_layout, new ProfileFragment()); // give your fragment container id in first parameter
//                transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
//                transaction.commit();
                Toast.makeText(this, "profile", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_website:
                Toast.makeText(this, "website", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_rateus:
                Toast.makeText(this, "rateUs", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_contactus:
                Toast.makeText(this, "contact us", Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_exit:
//                FirebaseAuth.getInstance().signOut();
                Toast.makeText(this, "exit", Toast.LENGTH_SHORT).show();
                finish();
                System.exit(0);
                break;
            case R.id.nav_logout:
                FirebaseAuth.getInstance().signOut();
                finish();
                startActivity(new Intent(userNavigation.this, MainActivity.class));
                break;
            default:
                Toast.makeText(this, "ready to exit", Toast.LENGTH_SHORT).show();

        }
        return true;
    }

    @Override
    protected void onPostResume() {
        super.onPostResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
    }

    public void himg(View view) {

    }
}