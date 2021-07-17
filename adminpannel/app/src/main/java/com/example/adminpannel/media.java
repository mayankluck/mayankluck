package com.example.adminpannel;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TaskInfo;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.OpenableColumns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

import static android.app.Activity.RESULT_OK;

public class media extends Fragment {

    MaterialButton btupload;
    MaterialCardView cardPdfUp;
    private final int REQ = 1;
    private Uri pdfData;
    private TextView titlePdf, pdfFileName;
    private DatabaseReference dbRef;
    private StorageReference storageReference;
    String downloadUrl = "", pdfName, pdftitle;
    private ProgressDialog pd;


    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View root = inflater.inflate(R.layout.fragment_media, container, false);

        btupload = (MaterialButton) root.findViewById(R.id.btuploadnews);
        cardPdfUp = (MaterialCardView) root.findViewById(R.id.ebook);
        titlePdf = root.findViewById(R.id.titlePdf);
        pdfFileName = root.findViewById(R.id.pdfFile);
        dbRef = FirebaseDatabase.getInstance().getReference();
        storageReference = FirebaseStorage.getInstance().getReference();
        pd = new ProgressDialog(getActivity());
        cardPdfUp.setOnClickListener(v -> openGallery());
         btupload.setOnClickListener(new View.OnClickListener() {
             @Override
             public void onClick(View v) {
                 pdftitle = titlePdf.getText().toString().trim();
                 if( pdftitle.isEmpty()){
                     titlePdf.setError("not empty");
                     titlePdf.requestFocus();
                 }else if(pdfData == null){
                     pdfFileName.setError("Select pdf file");
                     pdfFileName.requestFocus();
                 }else uploadPdf();
             }
         });

        return root;
    }

    private void uploadPdf() {
        pd.setMessage("Uploading...");
        pd.setTitle("Please wait");;
        pd.show();
        StorageReference reference = storageReference.child("PDFs/"+pdfName+"_"+System.currentTimeMillis()+".pdf");
        reference.putFile(pdfData)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();
                        uploaddata(String.valueOf(uri));

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Something goes wrong", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploaddata(String downloadUrl) {
        String uniqueKey = dbRef.child("PDFs").push().getKey();
        HashMap data = new HashMap();
        data.put("pdfTitle", pdftitle);
        data.put("pdfUrl", downloadUrl);

        Calendar calendarDate = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currDate = new SimpleDateFormat("dd-MM-yy");
        String date = currDate.format(calendarDate.getTime());

        Calendar calendarTime = Calendar.getInstance();
        @SuppressLint("SimpleDateFormat") SimpleDateFormat currTime = new SimpleDateFormat("hh:mm a");
        String time = currTime.format(calendarTime.getTime());

        assert uniqueKey != null;
        dbRef.child("PDFs").child(date+"_"+time).setValue(data).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull @NotNull Task<Void> task) {
                pd.dismiss();
                pdfFileName.setText("Select New pdf");
                titlePdf.setText("");
                Toast.makeText(getActivity(), "Pdf Uploaded successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull @NotNull Exception e) {
                pd.dismiss();
                Toast.makeText(getActivity(), "Something went wrong on server", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void openGallery() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("application/pdf");
        startActivityForResult(Intent.createChooser(intent, "Select Pdf File"), REQ);
    }
    @SuppressLint({"SetTextI18n", "Recycle"})
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ && resultCode == RESULT_OK) {
            assert data != null;
            pdfData = data.getData();
            if(pdfData.toString().startsWith("content://")){
                Cursor cursor = null;
                try {
                    cursor = getActivity().getContentResolver().query(pdfData, null, null, null, null);
                    if (cursor != null && cursor.moveToFirst()) {
                        pdfName = cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }else if(pdfData.toString().startsWith("file://")){
                pdfName = new File(pdfData.toString()).getName();
            }
            pdfFileName.setText(pdfName);
        }
    }
}