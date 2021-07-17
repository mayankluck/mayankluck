package com.example.useradvent.ui.Profile;

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


public class ProfileFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        getActivity().setTitle("Profile");
        View root = inflater.inflate(R.layout.fragment_profile, container, false);
//        Header
        ImageView profileImg = root.findViewById(R.id.userpro_Img);
        TextView username = root.findViewById(R.id.userpro_name);
        TextView mobileNo = root.findViewById(R.id.userpro_mobile);
        TextView designation = root.findViewById(R.id.userpro_designation);
//        Personal info
        TextView fullName = root.findViewById(R.id.userpro_fullname);
        TextView father = root.findViewById(R.id.userpro_father);
        TextView dob = root.findViewById(R.id.userpro_dob);
        TextView gender = root.findViewById(R.id.userpro_gender);
        TextView caste = root.findViewById(R.id.userpro_caste);
        TextView subCaste = root.findViewById(R.id.userpro_subcaste);
//        Contact Info
        TextView mobileInfo = root.findViewById(R.id.userpro_mobileinfo);
        TextView email = root.findViewById(R.id.userpro_email);
        TextView aadhaar = root.findViewById(R.id.userpro_adhar);
        TextView epic = root.findViewById(R.id.userpro_epic);
        TextView aadhaarprev =root.findViewById(R.id.userpro_aadhaarImgprev);
        ImageView aadhaarImg = root.findViewById(R.id.userpro_aadhaarImg);
//        Address Info
        TextView address = root.findViewById(R.id.userpro_address);
        TextView pin = root.findViewById(R.id.userpro_pin);
//        Designation Info
        TextView designationInfo = root.findViewById(R.id.userpro_post);
        TextView booth = root.findViewById(R.id.userpro_booth);
        TextView sector = root.findViewById(R.id.userpro_sector);
        TextView district = root.findViewById(R.id.userpro_district);
        TextView zone = root.findViewById(R.id.userpro_zone);
        TextView state = root.findViewById(R.id.userpro_state);

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
            Intent intent = new Intent(getContext(), FullImageView.class);
            intent.putExtra("image", PublicData.aadhaarImgPub);
            startActivity(intent);

        });
        profileImg.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), FullImageView.class);
            intent.putExtra("image", PublicData.imagePub);
            startActivity(intent);
        });

        return root;
    }
}