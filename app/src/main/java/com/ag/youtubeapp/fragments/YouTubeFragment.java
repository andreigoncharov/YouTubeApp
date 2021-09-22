package com.ag.youtubeapp.fragments;

import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.youtubeapp.R;
import com.ag.youtubeapp.adapter.YouTubeAdapter;
import com.ag.youtubeapp.db.DatabaseHandler;
import com.ag.youtubeapp.model.YouTubeVideo;
import com.ag.youtubeapp.network.YouTubeAPI;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputEditText;

import java.util.List;

public class YouTubeFragment extends Fragment implements View.OnClickListener {

    private ImageButton searchButton;
    private TextInputEditText textInput;
    private FloatingActionButton addBtn;

    private YouTubeAdapter youtubeAdapter;
    private RecyclerView mRecyclerView;
    private Handler handler;
    private List<YouTubeVideo.VideoItem> searchResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView =
                inflater.inflate(R.layout.youtube_fragment, container, false);

        searchButton = (ImageButton) rootView.findViewById(R.id.searchButton);
        textInput = (TextInputEditText) rootView.findViewById(R.id.textInputTI);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.videosRV);
        addBtn = (FloatingActionButton) rootView.findViewById(R.id.addBtn);
        addBtn.setOnClickListener(this);

        searchButton.setOnClickListener(this);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        handler = new Handler();

        return rootView;

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.searchButton:
                searchOnYoutube(textInput.getText().toString());
                break;
            case R.id.addBtn:
                DatabaseHandler db = new DatabaseHandler(getContext());
                try{
                db.addItem(youtubeAdapter.getmVideoList());
                }catch (Exception ignored){

                }

        }

    }


    private void searchOnYoutube(final String keywords){

        new Thread(){
            public void run(){
                YouTubeAPI yc = new YouTubeAPI(getContext());

                searchResults = yc.search(keywords);

                handler.post(new Runnable(){
                    public void run(){
                        fillYoutubeVideos();
                    }
                });
            }
        }.start();
    }

    private void fillYoutubeVideos(){
        youtubeAdapter = new YouTubeAdapter(getContext(),searchResults);
        mRecyclerView.setAdapter(youtubeAdapter);
        youtubeAdapter.notifyDataSetChanged();
    }

}
