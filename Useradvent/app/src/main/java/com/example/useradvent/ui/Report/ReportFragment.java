package com.example.useradvent.ui.Report;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useradvent.PublicData;
import com.example.useradvent.R;
import com.example.useradvent.ui.Data.VoterData;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.data.PieEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;


public class ReportFragment extends Fragment {
    private PieChart pieChartCaste;
    private PieChart pieChartSubCaste;

    private ArrayList<VoterData> allList;
    private ArrayList<String> casteList;
    private ArrayList<String> subCasteList;
    private final DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("VoterData");
    TextView demo;


    @SuppressLint("SetTextI18n")
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("All Data Reports");

        View root = inflater.inflate(R.layout.fragment_report, container, false);

        pieChartCaste = root.findViewById(R.id.piechartCaste);
        pieChartSubCaste = root.findViewById(R.id.piechartSubcaste);

        demo = root.findViewById(R.id.demo);
        allList = new ArrayList<>();
        casteList = new ArrayList<>();
        subCasteList = new ArrayList<>();
        refList();


        return root;
    }

    private void refList() {
        switch (PublicData.designationPub) {
            case "Booth Coordinator":
                reference.orderByChild("booth").equalTo(PublicData.desigBoothPub).addValueEventListener(new ValueEventListener() {
                    @SuppressLint("SetTextI18n")
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(getActivity(), "No any users", Toast.LENGTH_SHORT).show();
                        } else {
                            allList.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allList.add(data.getValue(VoterData.class));
                            for (VoterData data : allList){
                                if (!data.getCaste().toLowerCase().contains("maya")) {
                                    casteList.add(data.getCaste());
                                }
                                if (!data.getSubcaste().toLowerCase().contains("maya")) {
                                    subCasteList.add(data.getSubcaste());
                                }
                                else demo.setText("not general");
                            }
                            casteGraph(casteList);
                            subCasteGraph(subCasteList);
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "Sector Coordinator":
                reference.orderByChild("sector").equalTo(PublicData.desigSectorPub).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(getActivity(), "No any users", Toast.LENGTH_SHORT).show();
                        } else {
                            allList.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allList.add(data.getValue(VoterData.class));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "District Coordinator":
                reference.orderByChild("district").equalTo(PublicData.desigDistrictPub).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(getActivity(), "No any users", Toast.LENGTH_SHORT).show();
                        } else {
                            allList.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allList.add(data.getValue(VoterData.class));

                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "Zone Coordinator":
                reference.orderByChild("zone").equalTo(PublicData.desigZonePub).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(getActivity(), "No any users", Toast.LENGTH_SHORT).show();
                        } else {
                            allList.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allList.add(data.getValue(VoterData.class));
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull @NotNull DatabaseError error) {
                        Toast.makeText(getActivity(), "Database error", Toast.LENGTH_SHORT).show();
                    }
                });
                break;
            case "State Coordinator":
                reference.orderByChild("state").equalTo(PublicData.desigStatePub).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull @NotNull DataSnapshot snapshot) {
                        if (!snapshot.exists()) {
                            Toast.makeText(getActivity(), "No any users", Toast.LENGTH_SHORT).show();
                        } else {
                            allList.clear();
                            for (DataSnapshot data : snapshot.getChildren())
                                allList.add(data.getValue(VoterData.class));

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

    @SuppressLint("SetTextI18n")
    private void subCasteGraph(ArrayList<String> subCasteList) {

        ArrayList<PieEntry> subcasteEntry = new ArrayList<>();
        Log.i("newlist", subCasteList.toString());
        Map<String, Integer> hm = new HashMap<String, Integer>();
        int totalSubcaste = subCasteList.size();
        demo.setText(""+totalSubcaste);

        for (String i : subCasteList) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        // displaying the occurrence of elements in the arraylist
        for (Map.Entry<String, Integer> val : hm.entrySet()) {
            Log.i(" occurance", val.getKey() + " " + val.getValue());
            subcasteEntry.add(new PieEntry(val.getValue(), val.getKey()));
        }

        PieDataSet pieDataSet = new PieDataSet(subcasteEntry, "Subaste Report");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(20f);

        PieData pieData = new PieData(pieDataSet);

        pieChartSubCaste.setData(pieData);
        pieChartSubCaste.getDescription().setEnabled(true);
        pieChartSubCaste.setCenterText("SubCast Report");
        pieChartSubCaste.animate();

    }

    private void casteGraph(ArrayList<String> casteList) {
        ArrayList<PieEntry> casteEntry = new ArrayList<>();
        Log.i("newlist", casteList.toString());
        Map<String, Integer> hm = new HashMap<String, Integer>();

        for (String i : casteList) {
            Integer j = hm.get(i);
            hm.put(i, (j == null) ? 1 : j + 1);
        }

        // displaying the occurrence of elements in the arraylist
        for (Map.Entry<String, Integer> val : hm.entrySet()) {
            Log.i(" occurance", val.getKey() + " " + val.getValue());
            casteEntry.add(new PieEntry(val.getValue(), val.getKey()));
        }

        PieDataSet pieDataSet = new PieDataSet(casteEntry, "caste Report");
        pieDataSet.setColors(ColorTemplate.COLORFUL_COLORS);
        pieDataSet.setValueTextColor(Color.BLACK);
        pieDataSet.setValueTextSize(20f);

        PieData pieData = new PieData(pieDataSet);

        pieChartCaste.setData(pieData);
        pieChartCaste.getDescription().setEnabled(true);
        pieChartCaste.setCenterText("Cast Report");
        pieChartCaste.animate();
    }
}