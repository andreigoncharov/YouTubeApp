package com.ag.youtubeapp.fragments;

import android.database.Cursor;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.ag.youtubeapp.R;
import com.ag.youtubeapp.adapter.PlaylistAdapter;
import com.ag.youtubeapp.adapter.YouTubeAdapter;
import com.ag.youtubeapp.db.DatabaseHandler;
import com.ag.youtubeapp.model.YouTubeVideo;

import java.util.ArrayList;
import java.util.List;

public class PlaylistFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private PlaylistAdapter playlistAdapter;
    private List<YouTubeVideo.VideoItem> searchResults;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView =
                inflater.inflate(R.layout.playlist_fragment, container, false);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.videosRV1);

        mRecyclerView.setHasFixedSize(true);

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        DatabaseHandler db = new DatabaseHandler(getContext());
        Cursor cursor = db.getListItem();

        searchResults = new ArrayList<>();

        while (cursor.moveToNext()) {
            Log.i("SQLLL", "cursor: "+cursor.getString(cursor.getColumnIndex("title")));
            YouTubeVideo.VideoItem video = new YouTubeVideo.VideoItem();
            video.setId(cursor.getString(cursor.getColumnIndex("id")));
            video.setDescription(cursor.getString(cursor.getColumnIndex("description")));
            video.setTitle(cursor.getString(cursor.getColumnIndex("title")));
            video.setThumbnailURL(cursor.getString(cursor.getColumnIndex("thumbnaiURL")));
            Log.i("SQLLL", "cursor1: "+video.getTitle());
            searchResults.add(video);
        }

        cursor.close();
        fillPlaylist(searchResults);
        return rootView;
    }

    private void fillPlaylist(List<YouTubeVideo.VideoItem> list){
        playlistAdapter = new PlaylistAdapter(getContext(), list);
        mRecyclerView.setAdapter(playlistAdapter);
        playlistAdapter.notifyDataSetChanged();
    }

}
