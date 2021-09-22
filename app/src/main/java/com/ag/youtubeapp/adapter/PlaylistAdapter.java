package com.ag.youtubeapp.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.youtubeapp.R;
import com.ag.youtubeapp.db.DatabaseHandler;
import com.ag.youtubeapp.model.YouTubeVideo;
import com.squareup.picasso.Picasso;

import java.util.List;

public class PlaylistAdapter extends RecyclerView.Adapter<PlaylistAdapter.MyViewHolder1>  {

    private List<YouTubeVideo.VideoItem> mVideoList;

    private TextView titleTV1;
    private TextView descriptionTV1;
    private ImageView imageIV1;
    private ImageButton deleteBtn;
    private Context mContext;
    private CardView videoCard1;



    public class MyViewHolder1 extends RecyclerView.ViewHolder{


        public MyViewHolder1(View view) {

            super(view);

            titleTV1 = (TextView) view.findViewById(R.id.titleTV1);
            descriptionTV1 = (TextView) view.findViewById(R.id.descriptionTV1);
            imageIV1 = (ImageView) view.findViewById(R.id.imageIV1);
            deleteBtn = (ImageButton) view.findViewById(R.id.deleteBtn);
            videoCard1 = (CardView) view.findViewById(R.id.videoCard1);
        }
    }

    public PlaylistAdapter(Context mContext, List<YouTubeVideo.VideoItem> mVideoList) {
        this.mContext = mContext;
        this.mVideoList = mVideoList;
    }

    @NonNull
    @Override
    public PlaylistAdapter.MyViewHolder1 onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        // create a new view
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.playlist_video_card, parent, false);

        return new MyViewHolder1(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistAdapter.MyViewHolder1 holder, int position) {
        final YouTubeVideo.VideoItem singleVideo = mVideoList.get(position);
        Log.i("SQLLL", "video: "+ mVideoList.get(position));

        titleTV1.setText(singleVideo.getTitle());

        descriptionTV1.setText(singleVideo.getDescription());

        Picasso.get().load(singleVideo.getThumbnailURL())
                .fit().into(imageIV1);

        videoCard1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(mContext, titleTV1.getText(), Toast.LENGTH_LONG).show();
            }
        });

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHandler db = new DatabaseHandler(mContext);
                try {
                    db.deleteItem(mVideoList.get(position).getId());
                    mVideoList.remove(position);
                    notifyItemRemoved(position);
                }catch (Exception ignored){}
            }
        });

    }

    @Override
    public int getItemCount() {
        return mVideoList.size();
    }
}
