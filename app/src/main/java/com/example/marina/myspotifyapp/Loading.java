package com.example.marina.myspotifyapp;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class Loading extends Activity {
//    private Context mCtx;
//    private List<MyTrack> myTrackList;
//
//    public Loading(Context mCtx, List<MyTrack> myTrackList) {
//        this.mCtx = mCtx;
//        this.myTrackList = myTrackList;
//
//        @NonNull
//        @Override
//    }public MyTrackViewHolder(@NonNull ViewGroup parent,int)

    TextView idName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
//        idName=findViewById(R.id.textView_id_name);

        Intent intent = getIntent();
        String track = intent.getStringExtra("track");
//        String Name = intent.getStringExtra("lname");

//        idName.setText("Your favorite track: " + track );
    }
}
