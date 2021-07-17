package com.example.useradvent.ui.Gallery;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.example.useradvent.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Objects;


public class GalleryFragment extends Fragment implements AdapterView.OnItemSelectedListener {
    GalleryAdapter adapterParty;
    DatabaseReference reference;
    Spinner categorySpinner;
    private ArrayList<String> allCategory;
    private ArrayList<GalleryData> partyImg;
    private ArrayAdapter<String> catAdapter;
    ValueEventListener catListener;


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Gallery ");
        View root = inflater.inflate(R.layout.fragment_gallery, container, false);
        allCategory = new ArrayList<>();
        partyImg = new ArrayList<>();

        RecyclerView party = root.findViewById(R.id.galView);
        categorySpinner = root.findViewById(R.id.imgcategory);


        reference = FirebaseDatabase.getInstance().getReference().child("Gallery");
        reference.keepSynced(true);
        adapterParty = new GalleryAdapter(getContext(), partyImg);
        party.setLayoutManager(new GridLayoutManager(getContext(), 2));
        party.setAdapter(adapterParty);

        categorySpinner.setOnItemSelectedListener(this);

        catAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, allCategory);
        categorySpinner.setAdapter(catAdapter);
        catListener = reference.child("CategoryModel").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                allCategory.clear();
                for (DataSnapshot data : snapshot.getChildren())
                    allCategory.add(Objects.requireNonNull(data.getValue()).toString());
                catAdapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        return root;
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        String cate = parent.getItemAtPosition(position).toString();
        reference.child(cate).addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot datasnapshot) {
                partyImg.clear();
                for (DataSnapshot partySnapshot : datasnapshot.getChildren())
                    partyImg.add(partySnapshot.getValue(GalleryData.class));
                adapterParty.notifyDataSetChanged();
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getActivity(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }
}
