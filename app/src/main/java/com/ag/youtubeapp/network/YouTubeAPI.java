package com.ag.youtubeapp.network;

import android.content.Context;
import android.util.Log;

import com.ag.youtubeapp.model.YouTubeVideo;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.ResourceId;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class YouTubeAPI {


    //public static final String KEY = "AIzaSyDliAERoT2mukIo6AY-sUF7EfEIsVsSQzM";
    public static final String KEY = "AIzaSyBok8YCYJXpxWnfwJqhUqc_vXmZmW0KRVI";

    private YouTube youtube;

    private YouTube.Search.List query;

    public static final String PACKAGENAME = "com.ag.youtubeapp.network";

    public static final String SHA1 = "45:87:6D:AD:34:5B:15:94:82:53:CA:F8:AE:98:CE:A3:C0:89:E7:B5";

    private static final long MAXRESULTS = 75;


    public  YouTubeAPI(Context context) {

        youtube = new YouTube.Builder(new NetHttpTransport(), new JacksonFactory(), new HttpRequestInitializer() {

            @Override
            public void initialize(HttpRequest request) throws IOException {

                request.getHeaders().set("X-Android-Package", PACKAGENAME);
                request.getHeaders().set("X-Android-Cert",SHA1);
            }
        }).setApplicationName("SearchYoutube").build();

        try {

            query = youtube.search().list("id,snippet");
            query.setKey(KEY);
            query.setType("video");
            query.setFields("items(id/kind,id/videoId,snippet/title,snippet/description,snippet/thumbnails/high/url)");

        } catch (IOException e) {

        }
    }

    public List<YouTubeVideo.VideoItem> search(String keywords) {

        query.setQ(keywords);
        query.setMaxResults(MAXRESULTS);
        List<YouTubeVideo.VideoItem> tempSetItems = new ArrayList<>();
        try {
            SearchListResponse response = query.execute();
            List<SearchResult> results = response.getItems();
            List<YouTubeVideo.VideoItem> items = new ArrayList<>();
            Log.i("YouTubeAPI", "results"+results);
            if (results != null) {

                items = setItemsList(results.iterator());
            }
            return items;

        } catch (IOException e) {
            return null;
        }
    }

    private static List<YouTubeVideo.VideoItem> setItemsList(Iterator<SearchResult> iteratorSearchResults) {

        List<YouTubeVideo.VideoItem> tempSetItems = new ArrayList<>();
        if (!iteratorSearchResults.hasNext()) {
        }
        while (iteratorSearchResults.hasNext()) {

            SearchResult singleVideo = iteratorSearchResults.next();
            ResourceId rId = singleVideo.getId();
                YouTubeVideo.VideoItem item = new YouTubeVideo.VideoItem();

                Thumbnail thumbnail = singleVideo.getSnippet().getThumbnails().getHigh();
                item.setId(singleVideo.getId().getVideoId());
                item.setTitle(singleVideo.getSnippet().getTitle());
                item.setDescription(singleVideo.getSnippet().getDescription());
                item.setThumbnailURL(thumbnail.getUrl());
                item.setChecked(false);
                tempSetItems.add(item);
        }
        return tempSetItems;
    }

}
