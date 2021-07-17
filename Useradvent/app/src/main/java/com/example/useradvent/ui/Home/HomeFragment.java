package com.example.useradvent.ui.Home;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.widget.ViewPager2;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.useradvent.ui.Election.ElectionFragment;
import com.example.useradvent.ui.Evm.EvmFragment;
import com.example.useradvent.R;
import com.example.useradvent.ui.PublicIssues.PublicIssueFragment;
import com.example.useradvent.ui.Report.ReportFragment;
import com.example.useradvent.ui.Data.DataFragment;
import com.example.useradvent.ui.Gallery.GalleryFragment;
import com.example.useradvent.ui.News.NewsFragment;

import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.CompositePageTransformer;
import androidx.viewpager2.widget.MarginPageTransformer;

import java.util.ArrayList;
import java.util.List;


public class HomeFragment extends Fragment {
    private ViewPager2 partymemb;
    private final Handler sliderHandler = new Handler();
    private TextView latest_news;
    private CardView cardNews, cardEvm, cardForm, cardPublicIssue, cardCandidate, cardReport, cardElection, cardGallery, cardComplaint;


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        requireActivity().setTitle("Home");
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        partymemb = root.findViewById(R.id.partymembersslide);
        latest_news = root.findViewById(R.id.latestNews);
        latest_news.setSelected(true);

        cardNews = root.findViewById(R.id.cardNews);
        cardEvm = root.findViewById(R.id.cardEVM);
        cardForm = root.findViewById(R.id.cardForm);
        cardPublicIssue = root.findViewById(R.id.cardPublicIssue);
        cardCandidate = root.findViewById(R.id.cardCanidates);
        cardReport = root.findViewById(R.id.cardReport);
        cardElection = root.findViewById(R.id.cardElection);
        cardGallery = root.findViewById(R.id.cardGallery);
        cardComplaint = root.findViewById(R.id.cardComplaint);
        assert getFragmentManager() != null;
        cardNews.setOnClickListener(v -> {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new NewsFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();

        });
        cardForm.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new GalleryFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        cardEvm.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new EvmFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        cardPublicIssue.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new PublicIssueFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();

        });
        cardCandidate.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new DataFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        cardReport.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new ReportFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        cardElection.setOnClickListener(v -> {
            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new ElectionFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        cardGallery.setOnClickListener(v -> {

            FragmentTransaction transaction = getFragmentManager().beginTransaction();
            transaction.replace(R.id.frame_layout, new GalleryFragment()); // give your fragment container id in first parameter
            transaction.addToBackStack(null);  // if written, this transaction will be added to backstack
            transaction.commit();
        });
        cardComplaint.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_SEND);
            String[] recipients = {"recipient@gmail.com"};//Add multiple recipients here
            intent.putExtra(Intent.EXTRA_EMAIL, recipients);
            intent.putExtra(Intent.EXTRA_SUBJECT, "Mail Subject"); //Add Mail Subject
            intent.putExtra(Intent.EXTRA_TEXT, "Enter your mail body here...");//Add mail body
            intent.putExtra(Intent.EXTRA_CC, "mailcc@gmail.com");//Add CC emailid's if any
            intent.putExtra(Intent.EXTRA_BCC, "mailbcc@gmail.com");//Add BCC email id if any
            intent.setType("text/html");
            intent.setPackage("com.google.android.gm");//Added Gmail Package to forcefully open Gmail App
            startActivity(Intent.createChooser(intent, "Send mail"));
//            Uri uri = Uri.parse("http://www.google.com"); // missing 'http://' will cause crashed
//            Intent intent = new Intent(Intent.ACTION_VIEW, uri);
//            startActivity(intent);
        });


        List<SliderItems> sliderItems = new ArrayList<>();
        sliderItems.add(new SliderItems(R.drawable.slider1));
        sliderItems.add(new SliderItems(R.drawable.slider2));
        sliderItems.add(new SliderItems(R.drawable.slider3));
        sliderItems.add(new SliderItems(R.drawable.slider4));
        sliderItems.add(new SliderItems(R.drawable.slider5));

        partymemb.setAdapter(new SliderAdapter(sliderItems, partymemb));

        partymemb.setClipToPadding(false);
        partymemb.setClipChildren(false);
        partymemb.setOffscreenPageLimit(3);
        partymemb.getChildAt(0).setOverScrollMode(RecyclerView.OVER_SCROLL_NEVER);


        CompositePageTransformer compositePageTransformer = new CompositePageTransformer();
        compositePageTransformer.addTransformer(new MarginPageTransformer(40));
        compositePageTransformer.addTransformer((page, position) -> {
            float r = 1 - Math.abs(position);
            page.setScaleY(1.8f + r * 0.15f);
        });

        partymemb.setPageTransformer(compositePageTransformer);

        partymemb.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                sliderHandler.removeCallbacks(sliderRunnable);
                sliderHandler.postDelayed(sliderRunnable, 2000); // slide duration 2 seconds
            }
        });
        return root;
    }


    private final Runnable sliderRunnable = new Runnable() {
        @Override
        public void run() {
            partymemb.setCurrentItem(partymemb.getCurrentItem() + 1);
        }

    };


    @Override
    public void onPause() {
        super.onPause();
        sliderHandler.removeCallbacks(sliderRunnable);
    }

    @Override
    public void onResume() {
        super.onResume();
        sliderHandler.postDelayed(sliderRunnable, 2000);
    }

}