package com.ag.youtubeapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.youtubeapp.R;
import com.ag.youtubeapp.model.YouTubeVideo;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.squareup.picasso.Picasso;

import java.util.List;

public class YouTubeAdapter extends RecyclerView.Adapter<YouTubeAdapter.MyViewHolder> {

    private Context mContext;
    private List<YouTubeVideo.VideoItem> mVideoList;

    private TextView titleTV;
    private TextView descriptionTV;
    private ImageView imageIV;
    private CheckBox checkbox;

    public class MyViewHolder extends RecyclerView.ViewHolder{


        public MyViewHolder(View view) {

            super(view);

            titleTV = (TextView) view.findViewById(R.id.titleTV);
            descriptionTV = (TextView) view.findViewById(R.id.descriptionTV);
            imageIV = (ImageView) view.findViewById(R.id.imageIV);
            checkbox = (CheckBox) view.findViewById(R.id.checkbox);
        }
    }


    public YouTubeAdapter(Context mContext, List<YouTubeVideo.VideoItem> mVideoList) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.video_card, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull YouTubeAdapter.MyViewHolder holder, int position) {
        final YouTubeVideo.VideoItem singleVideo = mVideoList.get(position);

        titleTV.setText(singleVideo.getTitle());

        descriptionTV.setText(singleVideo.getDescription());

        Picasso.get().load(singleVideo.getThumbnailURL())
                .fit().into(imageIV);

        checkbox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(compoundButton.isChecked()){
                    compoundButton.setChecked(true);
                    mVideoList.get(position).setChecked(true);

                }
                else{
                    compoundButton.setChecked(false);
                    mVideoList.get(position).setChecked(false);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }

    public List<YouTubeVideo.VideoItem> getmVideoList(){
        return mVideoList;
    }
}
