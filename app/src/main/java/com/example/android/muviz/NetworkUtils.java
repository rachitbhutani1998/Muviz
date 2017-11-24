package com.example.android.muviz;


import android.net.ConnectivityManager;
import android.net.Uri;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

class NetworkUtils {

    private static final String BASE_URL= "https://api.themoviedb.org/3/discover/movie";

    private static final String BASE_POSTER_URL="http://image.tmdb.org/t/p/w500";

    private static final String SORT_PARAM="sort_by";

    private static final String API_KEY="api_key";

    private static final String apikey="fb3d1cb6e2de1c138354e8f94a91e2c2";

    private static final String LOG_TAG="Network Utils: ";

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



    public static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = null;

        if (url == null) {
            return null;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000);
            urlConnection.setConnectTimeout(1500);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            if (urlConnection.getResponseCode() == HttpURLConnection.HTTP_OK) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(LOG_TAG, "Problem retrieving the earthquake JSON results", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    public static ArrayList<Movies> extractMovieList(String movieJSON){
        ArrayList<Movies> list=new ArrayList<>();
        try {
            JSONObject sortedJSON=new JSONObject(movieJSON);
            JSONArray resultArray=sortedJSON.optJSONArray("results");
            for(int i=0;i<resultArray.length();i++){
                JSONObject iterator=resultArray.optJSONObject(i);
                String poster_path=iterator.optString("poster_path");
                String movie_id=iterator.optString("id");
                String movie_name=iterator.optString("title");
                list.add(new Movies(poster_path,movie_id,movie_name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }
}
