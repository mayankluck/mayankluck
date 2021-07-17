package com.example.useradvent;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.github.chrisbanes.photoview.PhotoView;

public class FullImageView extends AppCompatActivity {
    private PhotoView fullImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("Image Preview");
        setContentView(R.layout.activity_full_image_view);

        fullImage = findViewById(R.id.fullImg);

        String image = getIntent().getStringExtra("image");
        Glide.with(this).load(image).into(fullImage);
    }

}