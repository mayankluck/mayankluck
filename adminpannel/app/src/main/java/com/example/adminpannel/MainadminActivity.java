package com.example.adminpannel;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.Menu;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.adminpannel.ui.AdminData.NewAdmin;
import com.example.adminpannel.ui.Profile.Profile;
import com.example.adminpannel.ui.Users.CreateUser;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import androidx.annotation.NonNull;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import org.jetbrains.annotations.NotNull;

import de.hdodenhof.circleimageview.CircleImageView;

public class MainadminActivity extends AppCompatActivity {

    private AppBarConfiguration mAppBarConfiguration;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mainadmin);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        mAppBarConfiguration = new AppBarConfiguration.Builder(
                R.id.nav_home, R.id.nav_gallery, R.id.nav_News,R.id.nav_publicIssue,R.id.nav_allUsers,R.id.nav_dataview,R.id.nav_media,
                R.id.nav_crtVoter)
                .setDrawerLayout(drawer)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, mAppBarConfiguration);
        NavigationUI.setupWithNavController(navigationView, navController);

        View headerview = navigationView.getHeaderView(0);
        TextView hName = headerview.findViewById(R.id.headerName);
        TextView hMobile = headerview.findViewById(R.id.headerMobile);
        TextView hEmail = headerview.findViewById(R.id.headerEmail);
        CircleImageView hImage = headerview.findViewById(R.id.headerImage);
        FirebaseDatabase.getInstance().getReference().child("Admin").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            hName.setText(snapshot.child("userName").getValue().toString().toUpperCase());
                            hMobile.setText(snapshot.child("mobileNo").getValue().toString());
                            hEmail.setText(snapshot.child("email").getValue().toString().toLowerCase());
                            Glide.with(MainadminActivity.this).load(snapshot.child("image").getValue()).into(hImage);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.mainadmin, menu);
        return true;
    }

    @Override
    public boolean onSupportNavigateUp() {
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        return NavigationUI.navigateUp(navController, mAppBarConfiguration)
                || super.onSupportNavigateUp();
    }

    public void exit(MenuItem item) {
        MainadminActivity.this.finish();
        System.exit(0);
    }

    public void logout(MenuItem item) {

    }

    public void newmem(MenuItem item) {
        Intent intent = new Intent(this, CreateUser.class);
        startActivity(intent);
    }
    @Override
    protected void onPause() {
        super.onPause();
    }

    public void newAdmin(View view) {
        Intent intent = new Intent(this, NewAdmin.class);
        startActivity(intent);
    }

    public void profile(View view) {
        startActivity(new Intent(MainadminActivity.this, Profile.class));
    }
}