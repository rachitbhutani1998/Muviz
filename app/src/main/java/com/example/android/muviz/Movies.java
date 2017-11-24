package com.example.android.muviz;

import android.os.Parcel;
import android.os.Parcelable;

class Movies implements Parcelable{
    private String mPosterUrl,mId,mTitle;
    private static final String POSTER_BASE_URL="http://image.tmdb.org/t/p/w500";

    Movies(String posterUrl, String id,String title){
        mPosterUrl=POSTER_BASE_URL+posterUrl;
        mId=id;
        mTitle=title;
    }

    protected Movies(Parcel in) {
        mPosterUrl = in.readString();
        mId = in.readString();
        mTitle = in.readString();
    }

    public static final Creator<Movies> CREATOR = new Creator<Movies>() {
        @Override
        public Movies createFromParcel(Parcel in) {
            return new Movies(in);
        }

        @Override
        public Movies[] newArray(int size) {
            return new Movies[size];
        }
    };

    String getPoster(){
        return mPosterUrl;
    }
    String getMovieTitle(){
        return mTitle;
    }

    String getMovieId(){
        return mId;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mPosterUrl);
        parcel.writeString(mId);
        parcel.writeString(mTitle);
    }
}
