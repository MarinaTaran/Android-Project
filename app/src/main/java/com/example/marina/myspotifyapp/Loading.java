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
    FloatingActionButton creatList;
    User user;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);
        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        mRecyclerView = findViewById(R.id.recyclerView);
        creatList = findViewById(R.id.newPlayList);
        Intent intent = this.getIntent();
        user = (User) intent.getSerializableExtra("CurrentUser");
        Log.d(TAG, "onCreate: 222222" + user.tracks);

        myAdapterTrack=new MyAdapterTrack(this,user.tracks);
        mRecyclerView.setAdapter(myAdapterTrack);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        creatList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ArrayList<MyTrack> createNewList = new ArrayList<>();
                Intent intent = new Intent(Loading.this, CreateNewPlayList.class);

                for (MyTrack temp : user.tracks) {
                    if (temp.isCheked == true) {
                        createNewList.add(temp);
                    }
                }
                intent.putExtra("ListOfTracks", createNewList);
                Log.d(TAG, "onClick: 2222221" + createNewList);
                startActivity(intent);

            }
        });

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
