package com.example.useradvent.ui.PublicIssues;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.TextView;

import com.example.useradvent.R;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

public class VideoPlayers extends AppCompatActivity {



    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_players);

        YouTubePlayerView ytPlayer = findViewById(R.id.ytPlayer);
        TextView vtitle = findViewById(R.id.videotitle);
        TextView vDis = findViewById(R.id.videoDiscription);


        getLifecycle().addObserver(ytPlayer);
        String video_url = getIntent().getStringExtra("videoid");
        String videoDis = getIntent().getStringExtra("videodis");
        String videotitle = getIntent().getStringExtra("videotitle");

        vtitle.setText(videotitle);
        vDis.setText(videoDis);

        ytPlayer.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {
            @Override
            public void onReady(@NonNull YouTubePlayer youTPlayer) {
                super.onReady(youTPlayer);
                youTPlayer.loadVideo(video_url,0);
            }
        });

    }
}