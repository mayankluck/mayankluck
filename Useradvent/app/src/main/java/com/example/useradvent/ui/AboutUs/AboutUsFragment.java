package com.example.useradvent.ui.AboutUs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.useradvent.R;


public class AboutUsFragment extends Fragment {


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("About Us ");

        return inflater.inflate(R.layout.fragment_about_us, container, false);
    }
}