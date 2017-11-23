package com.example.android.muviz;

public class Movies {
    private String mPosterUrl,mId;

    public Movies(String posterUrl,String id){
        mPosterUrl=posterUrl;
        mId=id;
    }

    public String getPoster(){
        return mPosterUrl;
    }

    public String getMovieId(){
        return mId;
    }
}
