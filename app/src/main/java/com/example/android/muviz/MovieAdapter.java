package com.example.android.muviz;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * Created by rachit on 23/11/17.
 */

public class MovieAdapter extends ArrayAdapter<Movies> {

    MovieAdapter(@NonNull Context context, ArrayList<Movies> list) {
        super(context, 0,list);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View listItemView=convertView;
        if (listItemView==null){
            listItemView= LayoutInflater.from(getContext()).inflate(R.layout.movie_item,parent,false);
        }
        Movies thisMovie=getItem(position);
        ImageView imageView=listItemView.findViewById(R.id.movie_image);
        if (thisMovie != null) {
            Glide.with(getContext()).load(thisMovie.getPoster()).into(imageView);
            Log.i("Adapter: ", "getView: "+thisMovie.getPoster());
        }
        return listItemView;
    }
}
