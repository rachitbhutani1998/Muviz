package com.example.android.muviz;


import android.net.Uri;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;

class NetworkUtils {

    private static final String BASE_URL= "https://api.themoviedb.org/3/discover/movie";

    private static final String BASE_POSTER_URL="http://image.tmdb.org/t/p/w500";

    private static final String SORT_PARAM="sort_by";

    private static final String API_KEY="api_key";

    private static final String apikey="fb3d1cb6e2de1c138354e8f94a91e2c2";

    static URL buildURL(String query){
        Uri uri=Uri.parse(BASE_URL).buildUpon().appendQueryParameter(SORT_PARAM,query).appendQueryParameter(API_KEY,apikey).build();
        URL url=null;
        try {
             url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }
}
