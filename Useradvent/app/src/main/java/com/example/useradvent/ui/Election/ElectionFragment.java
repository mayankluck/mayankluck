package com.example.useradvent.ui.Election;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useradvent.R;


public class ElectionFragment extends Fragment implements View.OnClickListener {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("Election");
        View root =  inflater.inflate(R.layout.fragment_election, container, false);

        TextView nsvp = root.findViewById(R.id.nvsp);
        TextView officialwebsite = root.findViewById(R.id.officialwebsite);
        TextView searchname = root.findViewById(R.id.searchname);
        TextView registervoter = root.findViewById(R.id.registervoter);
        TextView voterservice = root.findViewById(R.id.voterservice);
        TextView policparty = root.findViewById(R.id.policparty);
        TextView downloadelectrol = root.findViewById(R.id.downloadelectrol);
        TextView election = root.findViewById(R.id.election);
        TextView publication = root.findViewById(R.id.publication);
        TextView currentissues = root.findViewById(R.id.currentissues);

        nsvp.setOnClickListener(this);
        officialwebsite.setOnClickListener(this);
        searchname.setOnClickListener(this);
        registervoter.setOnClickListener(this);
        voterservice.setOnClickListener(this);
        policparty.setOnClickListener(this);
        downloadelectrol.setOnClickListener(this);
        election.setOnClickListener(this);
        publication.setOnClickListener(this);
        currentissues.setOnClickListener(this);

        return root;
    }

    @Override
    public void onClick(View v) {
        Intent intent = new Intent(getActivity(), OpenElectionUrl.class);
        switch (v.getId()){
            case R.id.nvsp:
                intent.putExtra("url","https://www.nvsp.in");
                startActivity(intent);
                break;
            case R.id.officialwebsite:
                intent.putExtra("url","https://eci.gov.in");
                startActivity(intent);
                break;
            case R.id.searchname:
                intent.putExtra("url","https://electoralsearch.in");
                startActivity(intent);
                break;
            case R.id.registervoter:
                intent.putExtra("url","https://www.nvsp.in");
                startActivity(intent);
                break;
            case R.id.voterservice:
                intent.putExtra("url","http://servicevoter.nic.in");
                startActivity(intent);
                break;
            case R.id.policparty:
                intent.putExtra("url","https://eci.gov.in/candidate-political-parties/candidate-politicalparties");
                startActivity(intent);
                break;
            case R.id.downloadelectrol:
                intent.putExtra("url","https://www.nvsp.in/Home/DownloadPdf");
                startActivity(intent);
                break;
            case R.id.election:
                intent.putExtra("url","https://eci.gov.in/elections/election");
                startActivity(intent);
                break;
            case R.id.publication:
                intent.putExtra("url","https://eci.gov.in/media-publication/media-publication");
                startActivity(intent);
                break;
            case R.id.currentissues:
                intent.putExtra("url","https://eci.gov.in");
                startActivity(intent);
                break;
        }

    }
}