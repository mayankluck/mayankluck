package com.example.adminpannel.ui.VoterData;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.adminpannel.R;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;

public class VoterFullDetails extends AppCompatActivity {
    private LinearLayout linearLayout;


    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_voter_full_details);

        TextView f_epic = findViewById(R.id.fepic);
        ImageView f_img = findViewById(R.id.fImage);
        String img = getIntent().getStringExtra("image");
        Picasso.get().load(img).into(f_img);

        TextView f_Name = findViewById(R.id.fname);
        TextView f_father = findViewById(R.id.ffather);
        TextView f_gender = findViewById(R.id.fgender);
        TextView f_dob = findViewById(R.id.fage);
        TextView f_caste = findViewById(R.id.fcaste);
        TextView f_mobile = findViewById(R.id.fmobileNo);
        TextView f_adhar = findViewById(R.id.fadhar);
        TextView f_occupation = findViewById(R.id.foccupation);
        TextView f_adult = findViewById(R.id.fadult);
        TextView f_kids = findViewById(R.id.fkids);
        TextView f_address = findViewById(R.id.faddress);
        TextView f_city = findViewById(R.id.fcityandstate);
        TextView f_state = findViewById(R.id.fstate);
        TextView print = findViewById(R.id.print);
        linearLayout = (LinearLayout) findViewById(R.id.linearLayout);

        f_epic.setText(" EPIC NO " + getIntent().getStringExtra("epic"));
        f_Name.setText(getIntent().getStringExtra("name"));
        f_father.setText(getIntent().getStringExtra("father"));
        f_dob.setText(getIntent().getStringExtra("age"));
        f_gender.setText(getIntent().getStringExtra("gender"));
        f_caste.setText(getIntent().getStringExtra("caste"));
        f_mobile.setText(getIntent().getStringExtra("mobile"));
        f_adhar.setText(getIntent().getStringExtra("adhar"));
        f_occupation.setText(getIntent().getStringExtra("occupation"));
        f_address.setText(getIntent().getStringExtra("address1") + " " + getIntent().getStringExtra("address2"));
        f_city.setText(getIntent().getStringExtra("city") + " " + getIntent().getStringExtra("district"));
        f_state.setText(getIntent().getStringExtra("state"));

        int totalAdult = Integer.parseInt(getIntent().getStringExtra("male18p")) + Integer.parseInt(getIntent().getStringExtra("female18p"));
        int totalKids = Integer.parseInt(getIntent().getStringExtra("male18m")) + Integer.parseInt(getIntent().getStringExtra("female18m"));
        f_adult.setText(Integer.toString(totalAdult));
        f_kids.setText(Integer.toString(totalKids));

        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takeScreenshot();
            }
        });


    }

    private void takeScreenshot() {
        Date now = new Date();
        android.text.format.DateFormat.format("yyyy-MM-dd_hh:mm:ss", now);

        try {
            // create bitmap screen capture
            View v1 = linearLayout;
//            View v1 = getWindow().getDecorView().getRootView();
            v1.setDrawingCacheEnabled(true);
            Bitmap bitmap = Bitmap.createBitmap(v1.getDrawingCache());
            createPdf(bitmap);
            v1.setDrawingCacheEnabled(false);

            File imageFile = new File(this.getExternalFilesDir("/"),getIntent().getStringExtra("name")+".jpg");

            FileOutputStream outputStream = new FileOutputStream(imageFile);
            int quality = 100;
            bitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.flush();
            outputStream.close();

            openScreenshot(imageFile);
        } catch (Throwable e) {
            // Several error may come out with file handling or DOM
            e.printStackTrace();
        }
    }

    private void createPdf(Bitmap bitmap) {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                .Builder(270, 500, 1 )
                .create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();

        Bitmap scaledbmp = Bitmap.createScaledBitmap(bitmap, 270, 450, false);
        canvas.drawBitmap(scaledbmp, 0, 0, paint);

        pdfDocument.finishPage(page);

        File file = new File(this.getExternalFilesDir("/"),"new"+getIntent().getStringExtra("name")+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show();
    }

    private void openScreenshot(File imageFile) {
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_VIEW);
        Uri uri = Uri.fromFile(imageFile);
        intent.setDataAndType(uri, "image/*");
        startActivity(intent);
    }
}