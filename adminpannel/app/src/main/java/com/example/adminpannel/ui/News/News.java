package com.example.adminpannel.ui.News;

import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminpannel.MainadminActivity;
import com.example.adminpannel.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.database.annotations.NotNull;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.List;

import static android.app.Activity.RESULT_OK;

public class News extends Fragment {


    private NewsViewModel newsViewModel;
    MaterialButton btupload;
    MaterialCardView cardimgup;
    private ImageView newsPrev;
    private final int REQ = 1;
    private Bitmap bitmap;
    private TextView titlenews, detailnews, showNewNews;
    private LinearLayout newsLayout;
    private DatabaseReference dbref;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;

    private RecyclerView allNewsView;
    private List<NewsData> list;
    private NewsAdapter adapter;
    private Spinner newsSpinner;
    private DatabaseReference reference;
    String news;




    public static News newInstance() {
        return new News();
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_news,
                container, false);

        btupload = (MaterialButton) view.findViewById(R.id.btuploadnews);
        cardimgup = (MaterialCardView) view.findViewById(R.id.cardImgNews);
        newsPrev = view.findViewById(R.id.newsImgprev);
        titlenews = view.findViewById(R.id.titleNews);
        detailnews = view.findViewById(R.id.detailNews);
        showNewNews = view.findViewById(R.id.txCreateNewNews);
        newsLayout = view.findViewById(R.id.creNewLayout);
        dbref = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        allNewsView = view.findViewById(R.id.rv_allNews);
        reference = FirebaseDatabase.getInstance().getReference();
        pd = new ProgressDialog(getActivity());

        cardimgup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (titlenews.getText().toString().isEmpty()){
                    titlenews.setError("Empty");
                    titlenews.requestFocus();
                }
                if (detailnews.getText().toString().isEmpty()){
                    detailnews.setError("Empty");
                    detailnews.requestFocus();
                }
                else if (bitmap == null){
                    uploadData();
                }
                else{
                    uploadImage();
                }
            }
        });
        showNewNews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cardimgup.getVisibility()==View.GONE){
                    cardimgup.setVisibility(View.VISIBLE);
                    newsLayout.setVisibility(View.VISIBLE);
                }else {cardimgup.setVisibility(View.GONE);
                    newsLayout.setVisibility(View.GONE);

                }
            }
        });
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
        return view;
    }

    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] finalImg = boas.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("news").child(finalImg+"jpeg");
        final UploadTask uploadTask = filePath.putBytes(finalImg);
        uploadTask.addOnCompleteListener(requireActivity(), new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                if(task.isSuccessful()){
                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            filePath.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    downloadUrl = String.valueOf(uri);
                                    uploadData();

                                }
                            });
                        }
                    });
                }
                else{
                    pd.dismiss();
                    Toast.makeText(getActivity(), "Something went Wrong Upload Image", Toast.LENGTH_LONG).show();
                }
            }
        });



    }

    private void uploadData() {
        dbref = dbref.child("news");
        final String uniqueKey = dbref.push().getKey();
        String title = titlenews.getText().toString();
        String detailNews = detailnews.getText().toString();

        Calendar calendarDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currDate = new SimpleDateFormat("dd-MM-yy");
        String date = currDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currTime = new SimpleDateFormat("hh:mm a");
        String time = currTime.format(calendarTime.getTime());

        NewsData newsData = new NewsData(title, detailNews ,downloadUrl, date, time, uniqueKey);

        assert uniqueKey != null;
        dbref.child(date+"_"+time).setValue(newsData).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                pd.dismiss();
                Toast.makeText(getActivity(), "NEWS Data Upload Successfully", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getActivity(), MainadminActivity.class));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Something went Wrong upload data", Toast.LENGTH_LONG).show();
            }
        });
    }

    private void openGallery() {
        Intent picImg = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(picImg,REQ);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== REQ && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            newsPrev.setImageBitmap(bitmap);
        }
    }
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        newsViewModel = new ViewModelProvider(this).get(NewsViewModel.class);
        // TODO: Use the ViewModel
    }


}