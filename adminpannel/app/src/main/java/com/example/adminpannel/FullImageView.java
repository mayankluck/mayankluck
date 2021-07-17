package com.example.adminpannel;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageView extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Image Preview");
        setContentView(R.layout.activity_full_image_view);

        PhotoView fullImage = findViewById(R.id.fullImg);
        TextView title = findViewById(R.id.title);
        TextView description = findViewById(R.id.description);

        String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(fullImage);
        title.setText(getIntent().getStringExtra("title"));
        description.setText(getIntent().getStringExtra("description"));
    }
}