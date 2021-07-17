package com.example.adminpannel.ui.gallery;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.adminpannel.FullImageView;
import com.example.adminpannel.R;

import java.util.List;

public class GalleryAdapter extends RecyclerView.Adapter<GalleryAdapter.GalleryviewAdapter> {
    private Context context;
    private List<GalleryData> list;

    public GalleryAdapter(Context context, List<GalleryData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public GalleryviewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_item_layout,parent, false);

        return new GalleryviewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull GalleryviewAdapter holder, int position) {
        GalleryData item = list.get(position);
        Glide.with(context).load(item.getImage()).into(holder.imgView);

        holder.imgView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, FullImageView.class);
                intent.putExtra("image", item.getImage());
                intent.putExtra("title", item.getTitle());
                intent.putExtra("description", item.getDescription());
                context.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class GalleryviewAdapter extends RecyclerView.ViewHolder {
        ImageView imgView;

        public GalleryviewAdapter(@NonNull View itemView) {
            super(itemView);
            imgView = itemView.findViewById(R.id.galImg);

        }
    }
}
