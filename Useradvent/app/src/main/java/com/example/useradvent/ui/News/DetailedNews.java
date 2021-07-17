package com.example.useradvent.ui.News;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.useradvent.R;
import com.squareup.picasso.Picasso;

public class DetailedNews extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detailed_news);
        ImageView newsImg = findViewById(R.id.imgDetailednews);
        String img = getIntent().getStringExtra("image");
        Picasso.get().load(img).into(newsImg);

        TextView title = findViewById(R.id.newTitle);
        TextView date = findViewById(R.id.newsDate);
        TextView detail = findViewById(R.id.newsDescription);

        title.setText(getIntent().getStringExtra("title"));
        date.setText(getIntent().getStringExtra("newsDate"));
        detail.setText(getIntent().getStringExtra("fullNews"));


    }
}