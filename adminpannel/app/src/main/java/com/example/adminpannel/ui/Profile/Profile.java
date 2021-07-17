package com.example.adminpannel.ui.Profile;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.adminpannel.FullImageView;
import com.example.adminpannel.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.jetbrains.annotations.NotNull;

public class Profile extends AppCompatActivity {
    String image, adharImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        setTitle("Profile");
        setContentView(R.layout.activity_profile);
        ImageView profileImg = findViewById(R.id.userpro_Img);
        TextView username = findViewById(R.id.userpro_name);
        TextView mobileNo = findViewById(R.id.userpro_mobile);
        TextView designation = findViewById(R.id.userpro_designation);
//        Personal info
        TextView fullName = findViewById(R.id.userpro_fullname);
        TextView father = findViewById(R.id.userpro_father);
        TextView dob = findViewById(R.id.userpro_dob);
        TextView gender = findViewById(R.id.userpro_gender);
        TextView caste = findViewById(R.id.userpro_caste);
        TextView subCaste = findViewById(R.id.userpro_subcaste);
//        Contact Info
        TextView mobileInfo = findViewById(R.id.userpro_mobileinfo);
        TextView email = findViewById(R.id.userpro_email);
        TextView aadhaar = findViewById(R.id.userpro_adhar);
        TextView epic = findViewById(R.id.userpro_epic);
        TextView aadhaarprev =findViewById(R.id.userpro_aadhaarImgprev);
        ImageView aadhaarImg = findViewById(R.id.userpro_aadhaarImg);
//        Address Info
        TextView address = findViewById(R.id.userpro_address);
        TextView pin = findViewById(R.id.userpro_pin);

        FirebaseDatabase.getInstance().getReference("Admin").child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (snapshot.exists()){
                            try {
                                Glide.with(Profile.this).load(snapshot.child("image").getValue()).into(profileImg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            username.setText(snapshot.child("userName").getValue().toString());
                            mobileNo.setText(snapshot.child("mobileNo").getValue().toString());
                            designation.setText("Admin");

                            fullName.setText(snapshot.child("userName").getValue().toString());
                            father.setText(snapshot.child("fatherName").getValue().toString());
                            dob.setText(snapshot.child("age").getValue().toString());
                            gender.setText(snapshot.child("gender").getValue().toString());
                            caste.setText(snapshot.child("caste").getValue().toString());
                            subCaste.setText(snapshot.child("subcaste").getValue().toString());

                            mobileInfo.setText(snapshot.child("mobileNo").getValue().toString());
                            email.setText(snapshot.child("email").getValue().toString());
                            aadhaar.setText(snapshot.child("adhaarNo").getValue().toString());
                            epic.setText(snapshot.child("epicNo").getValue().toString());
                            try {
                                Glide.with(Profile.this).load(snapshot.child("aadhaarImg")).into(aadhaarImg);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            address.setText(snapshot.child("address").getValue().toString());
                            pin.setText(snapshot.child("pin").getValue().toString());
                            image = snapshot.child("image").getValue().toString();
                            adharImage = snapshot.child("aadhaarImg").getValue().toString();
                        }else
                            Toast.makeText(Profile.this, "Profile Not Exist", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {

                    }
                });
        aadhaarprev.setOnClickListener(v -> {
            if (aadhaarImg.getVisibility() == View.GONE)
                aadhaarImg.setVisibility(View.VISIBLE);
            else aadhaarImg.setVisibility(View.GONE);
        });
        aadhaarImg.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullImageView.class);
            intent.putExtra("image", adharImage);
            startActivity(intent);
        });
        profileImg.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullImageView.class);
            intent.putExtra("image", image);
            startActivity(intent);
        });
    }
}