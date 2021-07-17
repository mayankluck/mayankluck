package com.example.adminpannel.ui.PublicIssues;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminpannel.MainadminActivity;
import com.example.adminpannel.R;
import com.example.adminpannel.ui.News.NewsData;
import com.example.adminpannel.ui.News.NewsViewModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static android.app.Activity.RESULT_OK;


public class PublicIssue extends Fragment {
    MaterialButton btupload;
    private ImageView selectVidoImge;
    private TextView videoTitle,videoLink, detailVideo, videoImgHint;
    private final int REQ = 1;
    private Bitmap bitmap;
    private DatabaseReference dbref;
    private StorageReference storageReference;
    String downloadUrl = "";
    private ProgressDialog pd;
    

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_nav_public_issue, container, false);

        btupload = (MaterialButton) root.findViewById(R.id.btuploadvideo);
        selectVidoImge = root.findViewById(R.id.selectedVideoPrev);
        videoImgHint = root.findViewById(R.id.videoHint);
        videoTitle = root.findViewById(R.id.videotitle);
        videoLink = root.findViewById(R.id.videolink);
        detailVideo = root.findViewById(R.id.detailvideo);
        dbref = FirebaseDatabase.getInstance().getReference("PublicIssues");
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(getActivity());

        selectVidoImge.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openGallery();
            }
        });
        btupload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (videoTitle.getText().toString().isEmpty()){
                    videoTitle.setError("");
                    videoTitle.requestFocus();
                }
                if (detailVideo.getText().toString().isEmpty()){
                    detailVideo.setError("");
                    detailVideo.requestFocus();
                }
                if(videoLink.getText().toString().isEmpty()){
                    videoLink.setError("");
                    videoLink.requestFocus();
                }
                if (bitmap == null){
                    videoImgHint.setError("");
                    videoImgHint.requestFocus();
                }
                else{
                    uploadImage();
                }
            }
        });
        
        return root;
    }
    private void uploadImage() {
        pd.setMessage("Uploading...");
        pd.show();
        ByteArrayOutputStream boas = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 50, boas);
        byte[] finalImg = boas.toByteArray();
        final StorageReference filePath;
        filePath = storageReference.child("PublicIssues").child(finalImg+"jpeg");
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
        final String uniqueKey = dbref.push().getKey();
        String title = videoTitle.getText().toString().trim();
        String link = videoLink.getText().toString().trim();
        String videodetail = detailVideo.getText().toString().trim();

        Calendar calendarDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currDate = new SimpleDateFormat("dd-MM-yy");
        String date = currDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currTime = new SimpleDateFormat("hh:mm a");
        String time = currTime.format(calendarTime.getTime());

        PublicIssuesData publicIssuesData = new PublicIssuesData(title,link, videodetail ,downloadUrl, date, time, uniqueKey);
        assert uniqueKey != null;
        dbref.child(date+"_"+time).setValue(publicIssuesData).addOnSuccessListener(new OnSuccessListener<Void>() {
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
        startActivityForResult(picImg,10);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data)
    {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode== 10 && resultCode == RESULT_OK)
        {
            Uri imageUri = data.getData();
            try {
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), imageUri);
            } catch (IOException e) {
                e.printStackTrace();
            }
            selectVidoImge.setImageBitmap(bitmap);
        }
    }


}