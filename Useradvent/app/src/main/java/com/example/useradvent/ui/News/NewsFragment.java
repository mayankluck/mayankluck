package com.example.useradvent.ui.News;

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


public class NewsFragment extends Fragment {
    private RecyclerView allNewsView;
    private List<NewsData> list;
    private NewsAdapter adapter;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("News ");
        View root = inflater.inflate(R.layout.fragment_news, container, false);

        allNewsView = root.findViewById(R.id.rv_allNews);
        DatabaseReference reference = FirebaseDatabase.getInstance().getReference();
        DatabaseReference dbRef = reference.child("news");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {

                list = new ArrayList<>();
                if (!snapshot.exists()) {
                    Toast.makeText(getActivity(), "No any News", Toast.LENGTH_SHORT).show();
                } else {
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        NewsData data = snapshot1.getValue(NewsData.class);
                        list.add(data);
                        Collections.reverse(list);
                        allNewsView.setHasFixedSize(true);
                        allNewsView.setLayoutManager(new LinearLayoutManager(getActivity()));

                        adapter = new NewsAdapter(list, getActivity());
                        allNewsView.setAdapter(adapter);
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