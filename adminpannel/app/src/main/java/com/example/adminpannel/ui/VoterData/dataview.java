package com.example.adminpannel.ui.VoterData;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import com.example.adminpannel.R;
import com.example.adminpannel.ui.Users.User;
import com.example.adminpannel.ui.Users.myAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;


public class dataview extends Fragment {

    private VoterAdapter adapter;
    private RecyclerView allVoterView;
    FloatingActionButton fab;
    SearchView searchItem;
    private ArrayList<VoterData> list;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("VoterData");


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Voter List");

        View root = inflater.inflate(R.layout.fragment_dataview, container, false);

        list = new ArrayList<>();
        fab = root.findViewById(R.id.floatbutton);
        allVoterView = root.findViewById(R.id.rv_Voters);
        searchItem = root.findViewById(R.id.searchbaritem);

        allVoterView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VoterAdapter(list, getActivity());
        allVoterView.setAdapter(adapter);

        allVoterList();


//        fab.setOnClickListener(v -> startActivity(new Intent(getActivity(), VoterDetails.class)));

        searchItem.setImeOptions(EditorInfo.IME_ACTION_DONE);
        searchItem.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                searchList(newText);
                return false;
            }
        });

        return root;
    }

    private void searchList(String s) {
        ArrayList<VoterData> searchInList = new ArrayList<>();
        for (VoterData data : list) {
            if (data.getFullname().toLowerCase().contains(s.toLowerCase())
                    || data.getCaste().toLowerCase().contains(s.toLowerCase())
                    || data.getSubcaste().toLowerCase().contains(s.toLowerCase())
                    || data.getBooth().toLowerCase().contains(s.toLowerCase())
                    || data.getDistrict().toLowerCase().contains(s.toLowerCase())
                    || data.getSector().toLowerCase().contains(s.toLowerCase())
                    || data.getZone().toLowerCase().contains(s.toLowerCase())
                    || data.getState().toLowerCase().contains(s.toLowerCase()))
                searchInList.add(data);
        }
        adapter = new VoterAdapter(searchInList, getActivity());
        allVoterView.setAdapter(adapter);
    }

    private void allVoterList() {
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                if (!snapshot.exists()) {
                    Toast.makeText(getActivity(), "No any users", Toast.LENGTH_SHORT).show();
                } else {
                    list.clear();
                    for (DataSnapshot data : snapshot.getChildren())
                        list.add(data.getValue(VoterData.class));
                    allVoterView.setHasFixedSize(true);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull @NotNull DatabaseError error) {
                Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
            }
        });
    }

}