package com.example.android.muviz;

class Movies{
    private String mPosterUrl,mId,mTitle,mRelease,mRating,mPlot,mBackdrop;
    public static final String POSTER_BASE_URL="http://image.tmdb.org/t/p/w500";

    Movies(String posterUrl, String id,String title){
        mPosterUrl=POSTER_BASE_URL+posterUrl;
        mId=id;
        mTitle=title;
    }

    Movies(String posterUrl,String title,String release_date,String rating,String plot){
        mBackdrop=posterUrl;
        mTitle=title;
        mRelease=release_date;
        mRating=rating;
        mPlot=plot;
    }

    String getReleaseDate(){
        return mRelease;
    }

    String getRating(){
        return mRating;
    }

    String getPlot(){
        return mPlot;
    }

    String getBackdrop(){
        return mBackdrop;
    }

    String getPoster(){
        return mPosterUrl;
    }

    String getMovieTitle(){
        return mTitle;
    }

    String getMovieId(){
        return mId;
    }
}
