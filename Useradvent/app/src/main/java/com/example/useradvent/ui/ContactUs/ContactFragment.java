package com.example.useradvent.ui.ContactUs;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.useradvent.R;

public class ContactFragment extends Fragment {

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Contact US");
        return inflater.inflate(R.layout.fragment_contact, container, false);
    }
}