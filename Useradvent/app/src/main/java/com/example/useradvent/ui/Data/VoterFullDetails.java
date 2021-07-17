package com.example.useradvent.ui.Data;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.pdf.PdfDocument;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.useradvent.R;
import com.github.chrisbanes.photoview.PhotoView;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;

public class VoterFullDetails extends AppCompatActivity {

    private Bitmap bmp, scaledbmp;



    @SuppressLint("SetTextI18n")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Voter Detail");
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
        TextView f_subCaste = findViewById(R.id.fsubCaste);
        TextView f_mobile = findViewById(R.id.fmobileNo);
        TextView f_adhar = findViewById(R.id.fadhar);
        TextView f_occupation = findViewById(R.id.foccupation);
        TextView f_adult = findViewById(R.id.fadult);
        TextView f_kids = findViewById(R.id.fkids);
        TextView f_address = findViewById(R.id.faddress);
        TextView f_city = findViewById(R.id.fcityandstate);
        TextView f_state = findViewById(R.id.fstate);
        TextView print = findViewById(R.id.print);


        f_epic.setText(" EPIC NO "+getIntent().getStringExtra("epic"));
        f_Name.setText(getIntent().getStringExtra("name"));
        f_father.setText(getIntent().getStringExtra("father"));
        f_dob.setText(getIntent().getStringExtra("age"));
        f_gender.setText(getIntent().getStringExtra("gender"));
        f_caste.setText(getIntent().getStringExtra("caste"));
        f_subCaste.setText(getIntent().getStringExtra("subCaste"));
        f_mobile.setText(getIntent().getStringExtra("mobile"));
        f_adhar.setText(getIntent().getStringExtra("adhar"));
        f_occupation.setText(getIntent().getStringExtra("occupation"));
        f_address.setText(getIntent().getStringExtra("address1")+" "+getIntent().getStringExtra("address2"));
        f_city.setText(getIntent().getStringExtra("city")+" "+getIntent().getStringExtra("district"));
        f_state.setText(getIntent().getStringExtra("state"));

        int totalAdult = Integer.parseInt(getIntent().getStringExtra("male18p"))+Integer.parseInt(getIntent().getStringExtra("female18p"));
        int totalKids = Integer.parseInt(getIntent().getStringExtra("male18m"))+Integer.parseInt(getIntent().getStringExtra("female18m"));
        f_adult.setText(Integer.toString(totalAdult));
        f_kids.setText(Integer.toString(totalKids));


        print.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                printPdf();
            }
        });


    }

    private void printPdf() {
        PdfDocument pdfDocument = new PdfDocument();
        Paint paint = new Paint();
        Paint forLinePaint = new Paint();
        PdfDocument.PageInfo pageInfo = new PdfDocument.PageInfo
                .Builder(270, 400, 1 )
                .create();
        PdfDocument.Page page = pdfDocument.startPage(pageInfo);
        Canvas canvas = page.getCanvas();


        paint.setTextSize(15.5f);
        paint.setColor(Color.BLACK);
        canvas.drawText("Voter Full Detail", 50, 20, paint);

        paint.setTextSize(12.0f);
        paint.setColor(Color.BLACK);
        canvas.drawText("EPIC No.",40, 40, paint);
        canvas.drawText(getIntent().getStringExtra("epic"),100, 40, paint);

        canvas.drawText("Voter Name ",20, 60, paint);
        canvas.drawText(getIntent().getStringExtra("name"),120, 60, paint);

        canvas.drawText("Father Name",20, 80, paint);
        canvas.drawText(getIntent().getStringExtra("father"),120, 80, paint);

        canvas.drawText("Gender",20, 100, paint);
        canvas.drawText(getIntent().getStringExtra("gender"),120, 100, paint);

        canvas.drawText("DOB",20, 120, paint);
        canvas.drawText(getIntent().getStringExtra("age"),120, 120, paint);

        canvas.drawText("Caste",20, 140, paint);
        canvas.drawText(getIntent().getStringExtra("caste"),120, 140, paint);

        canvas.drawText("SubCaste",20, 160, paint);
        canvas.drawText(getIntent().getStringExtra("subCaste"),120, 160, paint);

        canvas.drawText("Mobile No.",20, 180, paint);
        canvas.drawText(getIntent().getStringExtra("mobile"),120, 180, paint);

        canvas.drawText("Aadhaar No.",20, 200, paint);
        canvas.drawText(getIntent().getStringExtra("adhar"),120, 200, paint);

        canvas.drawText("Occupation",20, 220, paint);
        canvas.drawText(getIntent().getStringExtra("occupation"),120, 220, paint);

        canvas.drawText("Adult Member ",20, 240, paint);
        canvas.drawText(""+Integer.parseInt(getIntent().getStringExtra("male18p"))+Integer.parseInt(getIntent().getStringExtra("female18p")),120, 240, paint);

        canvas.drawText("Kids/teen",20, 260, paint);
        canvas.drawText(""+Integer.parseInt(getIntent().getStringExtra("male18m"))+Integer.parseInt(getIntent().getStringExtra("female18m")),120, 260, paint);

        canvas.drawText("Address",20, 280, paint);
        canvas.drawText(getIntent().getStringExtra("address1")+" "+getIntent().getStringExtra("address2"),120, 280, paint);
        canvas.drawText(getIntent().getStringExtra("city")+" "+getIntent().getStringExtra("district"),120, 300, paint);
        canvas.drawText(getIntent().getStringExtra("state"),120, 320, paint);

//        canvas.drawBitmap(scaledbmp, 20, 340, paint);

//        canvas.drawLine(15,190, 230, 310, forLinePaint);
        pdfDocument.finishPage(page);

        File file = new File(this.getExternalFilesDir("/"),getIntent().getStringExtra("name")+".pdf");
        try {
            pdfDocument.writeTo(new FileOutputStream(file));
        } catch (IOException e) {
            e.printStackTrace();
        }

        pdfDocument.close();
        Toast.makeText(this, "Pdf Created", Toast.LENGTH_SHORT).show();

    }
}