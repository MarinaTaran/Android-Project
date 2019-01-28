package com.example.marina.myspotifyapp;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

public class Loading extends Activity {
    private RecyclerView mRecyclerView;
    private MyAdapterTrack myAdapterTrack;
    final String TAG = "Loading";
    List<MyTrack> favorite = new ArrayList<>();
    Map<MyTrack, Integer> listOfTrack = new TreeMap<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        mRecyclerView = findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("tracks");


        Query query1 = myRef.getDatabase().getReference("tracks");
        query1.addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                favorite.clear();
                listOfTrack.clear();

                if (dataSnapshot.exists()) {
                    for (DataSnapshot temp : dataSnapshot.getChildren()) {
                        MyTrack track = temp.getValue(MyTrack.class);
                     if(listOfTrack.get(track)==null){
                         listOfTrack.put(track,1);
                     }else {
                       listOfTrack.put(track, listOfTrack.get(track)+1);
                     }
                       // favorite.add(track);


                    }
                   // favorite.addAll(listOfTrack.keySet());
                   for(Map.Entry<MyTrack,Integer> temp: listOfTrack.entrySet()){
                        if(temp.getValue()>3){
                            favorite.add(temp.getKey());
                        }
                   }
                    Log.d(TAG, listOfTrack.toString());
                    Log.d(TAG, favorite.toString());
                    myAdapterTrack = new MyAdapterTrack(Loading.this, favorite);
                    mRecyclerView.setAdapter(myAdapterTrack);
                    myAdapterTrack.notifyDataSetChanged();
                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        //        myAdapterTrack.notifyDataSetChanged();
    }
}
