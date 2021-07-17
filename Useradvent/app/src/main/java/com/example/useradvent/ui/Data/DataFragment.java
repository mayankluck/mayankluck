package com.example.useradvent.ui.Data;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.useradvent.PublicData;
import com.example.useradvent.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.view.inputmethod.EditorInfo;
import android.widget.SearchView;
import android.widget.Toast;

import java.util.ArrayList;

public class DataFragment extends Fragment {
    private VoterAdapter adapter;
    private RecyclerView allVoterView;
    FloatingActionButton fab;
    SearchView searchItem;
    private ArrayList<VoterData> list;
    DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference().child("VoterData");
    SwipeRefreshLayout refresh;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("All Voters List");
        View root = inflater.inflate(R.layout.fragment_data, container, false);

        refresh = root.findViewById(R.id.refresh);
        list = new ArrayList<>();
        fab = root.findViewById(R.id.floatbutton);
        allVoterView = root.findViewById(R.id.rv_allVoters);
        searchItem = root.findViewById(R.id.searchbaritem);
        dbRef.keepSynced(true);

        allVoterView.setLayoutManager(new LinearLayoutManager(getActivity()));
        adapter = new VoterAdapter(list, getActivity());
        allVoterView.setAdapter(adapter);



        allVoterList();
        refresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                allVoterList();
                adapter.notifyDataSetChanged();
                refresh.setRefreshing(false);
            }
        });


        fab.setOnClickListener(v -> startActivity(new Intent(getActivity(), VoterDetails.class)));

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
        switch (PublicData.designationPub) {
            case "Booth Coordinator":
                dbRef.orderByChild("booth").equalTo(PublicData.desigBoothPub).addValueEventListener(new ValueEventListener() {
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
                break;
            case "Sector Coordinator":
                dbRef.orderByChild("sector").equalTo(PublicData.desigSectorPub).addValueEventListener(new ValueEventListener() {
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
                break;
            case "District Coordinator":
                dbRef.orderByChild("district").equalTo(PublicData.desigDistrictPub).addValueEventListener(new ValueEventListener() {
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
                break;
            case "Zone Coordinator":
                dbRef.orderByChild("zone").equalTo(PublicData.desigZonePub).addValueEventListener(new ValueEventListener() {
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
                break;
            case "State Coordinator":
                dbRef.orderByChild("state").equalTo(PublicData.desigStatePub).addValueEventListener(new ValueEventListener() {
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
                break;
        }
    }
}
