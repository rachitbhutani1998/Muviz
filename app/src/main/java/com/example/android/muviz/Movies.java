package com.example.android.muviz;

class Movies {
    private String mPosterUrl,mId,mTitle;
    private static final String POSTER_BASE_URL="http://image.tmdb.org/t/p/w500";

    Movies(String posterUrl, String id,String title){
        mPosterUrl=POSTER_BASE_URL+posterUrl;
        mId=id;
        mTitle=title;
    }

    String getPoster(){
        return mPosterUrl;
    }
    public String getMovieTitle(){
        return mTitle;
    }

    String getMovieId(){
        return mId;
    }
}
