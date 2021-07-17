package com.example.useradvent.ui.Profile;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.useradvent.R;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.useradvent.FullImageView;
import com.example.useradvent.PublicData;
import com.example.useradvent.R;
import com.squareup.picasso.Picasso;

public class Profile extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
//        Designation Info
        TextView designationInfo = findViewById(R.id.userpro_post);
        TextView booth = findViewById(R.id.userpro_booth);
        TextView sector = findViewById(R.id.userpro_sector);
        TextView district = findViewById(R.id.userpro_district);
        TextView zone = findViewById(R.id.userpro_zone);
        TextView state = findViewById(R.id.userpro_state);

        try {
            Glide.with(this).load(PublicData.imagePub).into(profileImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        username.setText(PublicData.userNamePub);
        mobileNo.setText(PublicData.mobileNoPub);
        designation.setText(PublicData.designationPub);

        fullName.setText(PublicData.userNamePub);
        father.setText(PublicData.fatherNamePub);
        dob.setText(PublicData.agePub);
        gender.setText(PublicData.genderPub);
        caste.setText(PublicData.castePub);
        subCaste.setText(PublicData.subcastePub);

        mobileInfo.setText(PublicData.mobileNoPub);
        email.setText(PublicData.emailPub);
        aadhaar.setText(PublicData.adhaarNoPub);
        epic.setText(PublicData.epicNoPub);
        try {
            Glide.with(this).load(PublicData.aadhaarImgPub).into(aadhaarImg);
        } catch (Exception e) {
            e.printStackTrace();
        }

        address.setText(PublicData.addressPub);
        pin.setText(PublicData.pinPub);

        designationInfo.setText(PublicData.designationPub);
        booth.setText(PublicData.desigBoothPub);
        sector.setText(PublicData.desigSectorPub);
        district.setText(PublicData.desigDistrictPub);
        zone.setText(PublicData.desigZonePub);
        state.setText(PublicData.desigStatePub);

        aadhaarprev.setOnClickListener(v -> {
            if (aadhaarImg.getVisibility() == View.GONE)
                aadhaarImg.setVisibility(View.VISIBLE);
            else aadhaarImg.setVisibility(View.GONE);
        });
        aadhaarImg.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullImageView.class);
            intent.putExtra("image", PublicData.aadhaarImgPub);
            startActivity(intent);

        });
        profileImg.setOnClickListener(v -> {
            Intent intent = new Intent(this, FullImageView.class);
            intent.putExtra("image", PublicData.imagePub);
            startActivity(intent);
        });
    }
}