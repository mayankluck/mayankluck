package com.example.useradvent.ui.News;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.useradvent.R;
import com.google.firebase.database.annotations.NotNull;
import com.squareup.picasso.Picasso;

import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsviewAdapter> {
    private final List<NewsData> list;
    private final Context context;


    public NewsAdapter(List<NewsData> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    public NewsAdapter.NewsviewAdapter onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.news_item_layout, parent, false);
        return new NewsviewAdapter(view);
    }

    public void onBindViewHolder(@NonNull @NotNull NewsviewAdapter holder, int position) {
        NewsData item = list.get(position);
        holder.title.setText(item.getTitle());
        holder.date.setText(item.getDate());
        try {
            Picasso.get().load(item.getImage()).into(holder.newsImg);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), DetailedNews.class);
                intent.putExtra("image", item.getImage());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("newsDate", item.getDate());
                intent.putExtra("fullNews", item.getDetailNews());
                v.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class NewsviewAdapter extends RecyclerView.ViewHolder {
        private final TextView title;
        private final TextView date;
        private final ImageView newsImg;
        private final CardView cardView;

        public NewsviewAdapter(@NonNull @NotNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tx_newsTitle);
            date = itemView.findViewById(R.id.tx_date);
            newsImg = itemView.findViewById(R.id.newsImg);
            cardView = itemView.findViewById(R.id.finalData);

        }

    }

}
