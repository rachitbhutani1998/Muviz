package com.example.android.muviz;


import android.net.Uri;
import android.util.Log;

import com.example.android.muviz.data.Movies;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;

class NetworkUtils {

    private static final String BASE_URL = "https://api.themoviedb.org/3/discover/movie";

    private static final String BASE_DETAIL_URL = "https://api.themoviedb.org/3/movie/";

    private static final String SORT_PARAM = "sort_by";

    private static final String api_key = "api_key";

    private static final String API_KEY = "fb3d1cb6e2de1c138354e8f94a91e2c2";

    private static final String LOG_TAG = "Network Utils: ";

    static URL buildURL(String query) {
        Uri uri = Uri.parse(BASE_URL).buildUpon().appendQueryParameter(SORT_PARAM, query).appendQueryParameter(api_key, API_KEY).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    static URL buildDetailURL(String id) {
        Uri uri = Uri.parse(BASE_DETAIL_URL).buildUpon().appendPath(id).appendQueryParameter(api_key, API_KEY).build();
        URL url = null;
        try {
            url = new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }


    static String makeHttpRequest(URL url) throws IOException {
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
            BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream,Charset.forName("UTF-8")));
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }

    static ArrayList<Movies> extractMovieList(String movieJSON) {
        ArrayList<Movies> list = new ArrayList<>();
        try {
            JSONObject sortedJSON = new JSONObject(movieJSON);
            JSONArray resultArray = sortedJSON.optJSONArray("results");
            for (int i = 0; i < resultArray.length(); i++) {
                JSONObject iterator = resultArray.optJSONObject(i);
                String poster_path = iterator.optString("poster_path");
                String movie_id = iterator.optString("id");
                String movie_name = iterator.optString("title");
                list.add(new Movies(poster_path, movie_id, movie_name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return list;
    }

    static Movies extractMovie(String detailJSON) throws JSONException {
        JSONObject json = new JSONObject(detailJSON);
        String title = json.optString("title");
        String release_date = json.optString("release_date");
        String plot = json.optString("overview");
        String id=json.optString("id");
        String poster = json.optString("backdrop_path");
        String rating = json.optString("vote_average");
        return new Movies(poster, title, release_date, rating, plot,id);
    }

    static Movies mostPopular(String popularJSON) throws JSONException{
        JSONObject sortedJSON=new JSONObject(popularJSON);
        JSONArray resultArray=sortedJSON.optJSONArray("results");
        JSONObject json = resultArray.optJSONObject(0);
        String title = json.optString("title");
        String id=json.optString("id");
        String release_date = json.optString("release_date");
        String plot = json.optString("overview");
        String poster = json.optString("backdrop_path");
        String rating = json.optString("vote_average");
        return new Movies(poster, title, release_date, rating, plot,id);
    }


    static String getTrailer(String jsonVideoResponse) throws JSONException {
        JSONObject trailerObject=new JSONObject(jsonVideoResponse);
        JSONArray resultArray=trailerObject.optJSONArray("results");
        JSONObject officialTrailer=resultArray.optJSONObject(0);
        return officialTrailer.optString("key");

    }

    static URL buildExtraUrl(String movie_id,String query) {
        URL url=null;
        Uri uri=Uri.parse(BASE_DETAIL_URL).buildUpon().appendPath(movie_id).appendPath(query).appendQueryParameter(api_key,API_KEY).build();
        try {
            url=new URL(uri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        return url;
    }

    static HashMap<String,String> getReviews(String jsonReviewResponse) throws JSONException {
        HashMap<String,String> map=new HashMap<>();
        JSONObject resultObject=new JSONObject(jsonReviewResponse);
        JSONArray resultArray=resultObject.optJSONArray("results");
        for(int i=0;i<resultArray.length();i++){
            map.put(resultArray.optJSONObject(i).optString("author"),resultArray.optJSONObject(i).optString("content"));
        }
        return map;
    }
}
