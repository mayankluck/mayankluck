package com.example.useradvent.ui.Evm;

import android.os.Bundle;

import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.useradvent.R;


public class EvmFragment extends Fragment {


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        requireActivity().setTitle("EVM");
        View root = inflater.inflate(R.layout.fragment_evm, container, false);
        CardView history = root.findViewById(R.id.cardEvmHistory);
        CardView security = (CardView) root.findViewById(R.id.cardEvmSecurity);
        CardView training = (CardView) root.findViewById(R.id.cardEvmTraining);
        CardView benefits = (CardView) root.findViewById(R.id.cardEvmBenefits);
        CardView faqs = (CardView) root.findViewById(R.id.cardEvmFaqs);


        TextView txHistory = root.findViewById(R.id.evmHistory);
        TextView txSecurity = root.findViewById(R.id.evmSecurity);
        TextView txTraining = root.findViewById(R.id.evmTraining);
        TextView txBenefit = root.findViewById(R.id.evmBenefits);
        TextView txFaqs = root.findViewById(R.id.evmFaqs);

        history.setOnClickListener(v ->{
            if(txHistory.getVisibility()==View.GONE) {
                txHistory.requestFocus();
                txHistory.setVisibility(View.VISIBLE);
                txSecurity.setVisibility(View.GONE);
                txTraining.setVisibility(View.GONE);
                txBenefit.setVisibility(View.GONE);
                txFaqs.setVisibility(View.GONE);

            }
            else txHistory.setVisibility(View.GONE);
        });

        security.setOnClickListener(v ->{
            if(txSecurity.getVisibility()==View.GONE) {
                txHistory.requestFocus();
                txSecurity.setVisibility(View.VISIBLE);
                txHistory.setVisibility(View.GONE);
                txTraining.setVisibility(View.GONE);
                txBenefit.setVisibility(View.GONE);
                txFaqs.setVisibility(View.GONE);

            }
            else txSecurity.setVisibility(View.GONE);

        });

        training.setOnClickListener(v ->{
            if(txTraining.getVisibility()==View.GONE) {
                txHistory.requestFocus();
                txTraining.setVisibility(View.VISIBLE);
                txSecurity.setVisibility(View.GONE);
                txHistory.setVisibility(View.GONE);
                txBenefit.setVisibility(View.GONE);
                txFaqs.setVisibility(View.GONE);
            }
            else txTraining.setVisibility(View.GONE);
        });
        benefits.setOnClickListener(v ->{
            if(txBenefit.getVisibility()==View.GONE) {
                txHistory.requestFocus();
                txHistory.setVisibility(View.GONE);
                txSecurity.setVisibility(View.GONE);
                txTraining.setVisibility(View.GONE);
                txBenefit.setVisibility(View.VISIBLE);
                txFaqs.setVisibility(View.GONE);

            }
            else txBenefit.setVisibility(View.GONE);


        });
        faqs.setOnClickListener(v ->{
            if(txFaqs.getVisibility()==View.GONE) {
                txHistory.requestFocus();
                txHistory.setVisibility(View.GONE);
                txSecurity.setVisibility(View.GONE);
                txTraining.setVisibility(View.GONE);
                txBenefit.setVisibility(View.GONE);
                txFaqs.setVisibility(View.VISIBLE);

            }
            else txFaqs.setVisibility(View.GONE);

        });

        return root;
    }
}