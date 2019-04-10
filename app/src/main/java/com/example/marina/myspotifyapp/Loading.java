package com.example.marina.myspotifyapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
// мы что то делаем
public class Loading extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private MyAdapterTrack myAdapterTrack;
    final String TAG = "Loading";
    List<MyTrack> favorite = new ArrayList<>();
    Map<MyTrack, Integer> listOfTrack = new TreeMap<>();
    FloatingActionButton creatList;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        mRecyclerView = findViewById(R.id.recyclerView);
        creatList=findViewById(R.id.newPlayList);
        creatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<MyTrack> createNewList = new ArrayList<>();
                Intent intent=new Intent(Loading.this,CreateNewPlayList.class);
                for(MyTrack temp:favorite){
                    if(temp.isCheked==true){
                       createNewList.add(temp);
                    }
                }
                intent.putExtra("ListOfTracks", (Serializable) createNewList);

                startActivity(intent);

            }
        });
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
//                        MyTrack track=temp.getValue(User.class).getId().;

//                     if(listOfTrack.get(track)==null){
//                         listOfTrack.put(track,1);
//                     }else {
//                       listOfTrack.put(track, listOfTrack.get(track)+1);
//                     }
                        favorite.add(track);


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



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.track_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.save:
                Toast.makeText(this, "Save", Toast.LENGTH_LONG).show();
                return true;

            default:
                Toast.makeText(this, "Other", Toast.LENGTH_LONG).show();
                return super.onOptionsItemSelected(item);
        }
    }

//    @Override
//    public void onCreateContextMenu(ContextMenu menu, View v,
//                                    ContextMenu.ContextMenuInfo menuInfo) {
//        super.onCreateContextMenu(menu, v, menuInfo);
//        MenuInflater inflater = getMenuInflater();
//        inflater.inflate(R.menu.menu_float, menu);
//    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        AdapterView.AdapterContextMenuInfo info = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        switch (item.getItemId()) {
            case R.id.add:
                Toast.makeText(this, "Add", Toast.LENGTH_LONG).show();
                return true;
            default:
                return super.onContextItemSelected(item);
        }
    }

}
