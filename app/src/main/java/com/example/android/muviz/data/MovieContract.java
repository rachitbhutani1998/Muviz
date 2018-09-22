package com.example.android.muviz.data;

import android.provider.BaseColumns;

/**
 * Created by rachit on 30/03/18.
 */

public class MovieContract {

    private MovieContract(){

    }

    public static class MovieEntry implements BaseColumns{
        static final String TABLE_NAME = "favourites";
        static String COL_ID = "movie_id";
        static String COL_TITLE = "title";
    }
}
