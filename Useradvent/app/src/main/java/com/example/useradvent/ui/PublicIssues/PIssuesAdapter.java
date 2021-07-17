package com.example.useradvent.ui.PublicIssues;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useradvent.R;
import com.example.useradvent.ui.News.DetailedNews;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PIssuesAdapter extends RecyclerView.Adapter<PIssuesAdapter.NewsviewAdapter> {
    private final List<PIssuesData> list;
    private final Context context;


    public PIssuesAdapter(List<PIssuesData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    public NewsviewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new NewsviewAdapter(view);
    }

    public void onBindViewHolder(@NonNull @NotNull NewsviewAdapter holder, int position) {
        PIssuesData item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        try {
            Picasso.get().load(item.getImage()).into(holder.newsImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.newsImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), VideoPlayers.class);
                intent.putExtra("videotitle", item.getTitle());
                intent.putExtra("videodis", item.getDetail());

                String url = item.getLink();
                String videoid = extractYTId(url);
                intent.putExtra("videoid", videoid);
                v.getContext().startActivity(intent);
            }
        });
    }

    private String extractYTId(String url) {
//        String vId = null;
//        Pattern pattern = Pattern.compile(
//                "^https?://.*(?:youtu.be/|v/|u/\\w/|embed/|watch?v=)([^#&?]*).*$",
//
//                Pattern.CASE_INSENSITIVE);
//        Matcher matcher = pattern.matcher(url);
//        if (matcher.matches()){
//            vId = matcher.group(1);
//        }
//        return vId;

        String videoId = "";
        String regex = "http(?:s)?:\\/\\/(?:m.)?(?:www\\.)?youtu(?:\\.be\\/|be\\.com\\/(?:watch\\?(?:feature=youtu.be\\&)?v=|v\\/|embed\\/|user\\/(?:[\\w#]+\\/)+))([^&#?\\n]+)";
        Pattern pattern = Pattern.compile(regex, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(url);
        if(matcher.find()){
            videoId = matcher.group(1);
        }
        return videoId;
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsviewAdapter extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final ImageView newsImg;

        public NewsviewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tx_newsTitle);
            date = itemView.findViewById(R.id.tx_date);
            newsImg = itemView.findViewById(R.id.newsImg);

        }

    }

}
