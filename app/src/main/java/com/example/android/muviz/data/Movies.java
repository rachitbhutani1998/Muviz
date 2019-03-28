package com.example.android.muviz.data;

public class Movies {
    private String mPosterUrl, mId, mTitle, mRelease, mRating, mPlot, mBackdrop;
    public static final String POSTER_BASE_URL = "http://image.tmdb.org/t/p/w500";

    public Movies(String posterUrl, String id, String title) {
        mPosterUrl = POSTER_BASE_URL + posterUrl;
        mId = id;
        mTitle = title;
    }

    public Movies(String posterUrl, String title, String release_date, String rating, String plot, String id) {
        mBackdrop = posterUrl;
        mTitle = title;
        mRelease = release_date;
        mRating = rating;
        mPlot = plot;
        mId = id;
    }

    public String getReleaseDate() {
        return mRelease;
    }

    public String getRating() {
        return mRating;
    }

    public String getPlot() {
        return mPlot;
    }

    public String getBackdrop() {
        return mBackdrop;
    }

    public String getPoster() {
        return mPosterUrl;
    }

    public String getMovieTitle() {
        return mTitle;
    }

    public String getMovieId() {
        return mId;
    }
}
