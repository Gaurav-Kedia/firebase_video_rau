package com.gaurav.firebase_video_rau;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.VideoView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {

    RecyclerView recycler;
    Context context;
    ArrayList<String> items = new ArrayList<>();
    ArrayList<String> urls = new ArrayList<>();

    public void update(String filename, String url) {
        items.add(filename);
        urls.add(url);
        notifyDataSetChanged();
    }

    public MyAdapter(RecyclerView recycler, Context context, ArrayList<String> items, ArrayList<String> urls) {
        this.recycler = recycler;
        this.context = context;
        this.items = items;
        this.urls = urls;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_row, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.text.setText(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
        VideoView video;
        TextView text;

        public ViewHolder(View ItemView){
            super(ItemView);
            text = (TextView) ItemView.findViewById(R.id.text);
            video = (VideoView) ItemView.findViewById(R.id.video);

            ItemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = recycler.getChildLayoutPosition(v);
                    //Intent intent = new Intent();
                    //intent.setType(Intent.ACTION_VIEW);
                    video.setVideoURI(Uri.parse(urls.get(position)));
                    video.start();
                    video.setBackgroundResource(0);
                    //context.startActivity(intent);
                }
            });
        }

    }

}
