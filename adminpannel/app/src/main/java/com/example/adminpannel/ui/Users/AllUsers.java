package com.example.adminpannel.ui.Users;


import android.os.Bundle;


import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.adminpannel.R;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class AllUsers extends Fragment {
    private myAdapter myAdapter;


    public View onCreateView(@NotNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View root = inflater.inflate(R.layout.fragment_all_users, container, false);


        RecyclerView allUsersView = root.findViewById(R.id.rv_allUsers);
        allUsersView.setLayoutManager(new LinearLayoutManager(getActivity()));
        FirebaseRecyclerOptions<User> options = new FirebaseRecyclerOptions.Builder<User>()
                .setQuery(FirebaseDatabase.getInstance().getReference().child("Users").child("Users"),User.class)
                .build();
        myAdapter = new myAdapter(options);
        allUsersView.setAdapter(myAdapter);



        return root;
    }
    @Override
    public void onStart() {
        super.onStart();
        myAdapter.startListening();
    }
    @Override
    public void onStop() {
        super.onStop();
        myAdapter.stopListening();
    }
}