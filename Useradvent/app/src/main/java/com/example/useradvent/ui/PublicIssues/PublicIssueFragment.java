package com.example.useradvent.ui.PublicIssues;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.useradvent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


public class PublicIssueFragment extends Fragment {
    private RecyclerView allPIssuesView;
    private List<PIssuesData> list;
    private PIssuesAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Public Issues");
        View root =  inflater.inflate(R.layout.fragment_public_issue, container, false);

        allPIssuesView = root.findViewById(R.id.rv_allNews);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = reference.child("PublicIssues");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                if (!snapshot.exists()) {
                    Toast.makeText(getActivity(), "No any News", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        PIssuesData data = snapshot1.getValue(PIssuesData.class);
                        list.add(data);
                        Collections.reverse(list);
                        allPIssuesView.setHasFixedSize(true);
                        allPIssuesView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        adapter = new PIssuesAdapter(list, getActivity());
                        allPIssuesView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();

            }
        });
        return root;
    }
}