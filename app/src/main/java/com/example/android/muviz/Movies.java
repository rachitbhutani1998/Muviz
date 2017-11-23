package com.example.android.muviz;

class Movies {
    private String mPosterUrl,mId;
    private static final String POSTER_BASE_URL="http://image.tmdb.org/t/p/w500";

    Movies(String posterUrl, String id){
        mPosterUrl=POSTER_BASE_URL+posterUrl;
        mId=id;
    }

    String getPoster(){
        return mPosterUrl;
    }

    String getMovieId(){
        return mId;
    }
}
