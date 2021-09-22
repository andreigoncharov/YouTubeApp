package com.ag.youtubeapp.db;

import java.util.ArrayList;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import com.ag.youtubeapp.model.YouTubeVideo;

public class DatabaseHandler extends SQLiteOpenHelper {

    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "YouTubeAppDB";

    // Database table name
    private static final String TABLE_LIST = "playlist";

    // Table Columns names
    static final String COLUMN_ID = "id";
    static final String COLUMN_TITLE = "title";
    static final String COLUMN_DESCRIPTION = "description";
    static final String COLUMN_THUMBNAI_URL = "thumbnaiURL";

    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_LIST_TABLE = "CREATE TABLE " + TABLE_LIST
                + "(" + COLUMN_ID + " TEXT,"
                + COLUMN_TITLE + " TEXT,"
                + COLUMN_DESCRIPTION + " TEXT,"
                + COLUMN_THUMBNAI_URL + " TEXT" + ")";

        db.execSQL(CREATE_LIST_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_LIST);

        // Create tables again
        onCreate(db);
    }



    public void addItem(List<YouTubeVideo.VideoItem> video) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        for (int i=0; i<video.size();i++){
            if(video.get(i).getChecked())
                if (!isExist(video.get(i).getId())) {
                    cv.put(COLUMN_ID, video.get(i).getId());
                    cv.put(COLUMN_TITLE, video.get(i).getTitle());
                    cv.put(COLUMN_DESCRIPTION, video.get(i).getDescription());
                    cv.put(COLUMN_THUMBNAI_URL, video.get(i).getThumbnailURL());
                    db.insert(TABLE_LIST, null, cv);
                }
        }
        getListItem();

        db.close(); // Closing database connection
    }

    public boolean isExist(String id){
        try {
            SQLiteDatabase db = this.getReadableDatabase();
            char sk = '"';
            String selectQuery = "SELECT  * FROM " + TABLE_LIST + " WHERE " +
                    COLUMN_THUMBNAI_URL + " = " +
                    sk + id + sk + ";";

            Cursor cursor = db.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                return true;
            }
        } catch (Exception e) {
            Log.i("SQLLL", "Error:"+e.getMessage());
        }
        return false;
    }

    public void deleteItem(String id){
        SQLiteDatabase db = this.getWritableDatabase();
        char sk = '"';
        db.delete(TABLE_LIST, COLUMN_ID + "="+sk+id+sk, null);
    }

    public Cursor getListItem() {
        String selectQuery = "SELECT  * FROM " + TABLE_LIST;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        Log.i("SQLLL", "Data: "+cursor.getCount());

        return cursor;
    }

}
