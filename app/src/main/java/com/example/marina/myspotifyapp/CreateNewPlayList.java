package com.example.marina.myspotifyapp;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;
import com.wrapper.spotify.SpotifyApi;
import com.wrapper.spotify.model_objects.specification.Playlist;
import com.wrapper.spotify.requests.data.playlists.CreatePlaylistRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CreateNewPlayList extends Activity {
    final String TAG ="CreateNewPlaylist";
   private final OkHttpClient mOkHttpClient = new OkHttpClient();
   private  String mAccessToken=StartActivity.mAccessToken;
   private Call mCall;
   EditText nameOfList;
   Button createList;
// final String idUser = "zpd66efn0du6f5qn1hit9bdyb";
public static final MediaType JSON
        = MediaType.parse("application/json; charset=utf-8");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_of_playlists);
//        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
        Intent intent = this.getIntent();
        ArrayList<MyTrack> tracks = (ArrayList<MyTrack>) intent.getSerializableExtra("ListOfTracks");
        Log.d(TAG, "onCreate: " + tracks);
        nameOfList = findViewById(R.id.namePlayList);
        createList = findViewById(R.id.create);


    createList.setOnClickListener(new View.OnClickListener(){
        @Override
            public void onClick(View view) {
                onProf();
            }
        });
    }



    public void onProf() {
        if (mAccessToken == null) {
            Toast.makeText(this, "Token null", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject jsonObj = new JSONObject();
        try {
            jsonObj.accumulate("name", nameOfList.getText().toString() ).
                    accumulate("description","new play list").
                    accumulate("public","false");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody formBody=RequestBody.create(JSON,jsonObj.toString());
           final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/users/zpd66efn0du6f5qn1hit9bdyb/playlists" )
                   .post(formBody)
                   .addHeader("Authorization","Bearer " + mAccessToken)
                   .addHeader("Content-Type","application/json")
                .build();
               cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setResponse("Failed to fetch data: " + e);
            }


            //user-read-currently-playing user-modify-playback-state user-read-recently-played
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String qwe = response.body().string();
                Log.d(TAG, "onResponse: "+mAccessToken);
                //final org.json.JSONObject jsonObject = new org.json.JSONObject(response.body().string());
//                JsonParser parser = new JsonParser();
//                JsonElement element = parser.parse(qwe);
                Log.d(TAG, "onResponse: "+qwe);

                }


        });
    }
//    https://open.spotify.com/user/zpd66efn0du6f5qn1hit9bdyb
//spotify:track:4UZuoEj2hJeyZpnLb68fg7
    private void cancelCall() {
        if (mCall != null) {
            mCall.cancel();
        }
        //AuthenticationClient.clearCookies
    }


    private Uri getRedirectUri() {
        return new Uri.Builder()
                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
                .authority("https://clickmy.site/u/callback/")
                .build();
    }

    private void setResponse(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //final TextView responseView = findViewById(R.id.text_prof);
//                textProf.setText(text);
                //   Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }

    public void addTarck(View view) {
        if (mAccessToken == null) {
            Toast.makeText(this, "Token null", Toast.LENGTH_LONG).show();
            return;
        }
        JSONObject jsonObj = new JSONObject();
        JSONArray  jsonArr=new JSONArray();
        try {
            jsonArr.put(0,"spotify:track:4UZuoEj2hJeyZpnLb68fg7");
            jsonObj.accumulate("uris",jsonArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }


        RequestBody formBody=RequestBody.create(JSON,jsonObj.toString());
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/playlists/4VSQDJmirtssghdTZmFWxP/tracks" )
                .post(formBody)
                .addHeader("Authorization","Bearer " + mAccessToken)
                .addHeader("Content-Type","application/json")
                .build();
        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setResponse("Failed to fetch data: " + e);
            }


            //user-read-currently-playing user-modify-playback-state user-read-recently-played
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String qwe = response.body().string();
                Log.d(TAG, "onResponse: "+mAccessToken);
                //final org.json.JSONObject jsonObject = new org.json.JSONObject(response.body().string());
//                JsonParser parser = new JsonParser();
//                JsonElement element = parser.parse(qwe);
                Log.d(TAG, "onResponse: "+qwe);

            }


        });


    }
//    final String TAG = "CreateNewPlayList";
//    EditText nameOfList;
//    Button createList;
//    final String idUser = "zpd66efn0du6f5qn1hit9bdyb";
//    private Call mCall;
//    private final OkHttpClient mOkHttpClient = new OkHttpClient();
//
//
//    private static final String accessToken = StartActivity.mAccessToken;
//    private static final String userId = "zpd66efn0du6f5qn1hit9bdyb";
////    private static final String name = "Abba";
//
//    private static final SpotifyApi spotifyApi = new SpotifyApi.Builder()
//            .setAccessToken(accessToken)
//            .build();
//    private static CreatePlaylistRequest createPlaylistRequest;
//
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_list_of_playlists);
//        getWindow().getDecorView().setBackgroundColor(Color.BLACK);
//        Intent intent = this.getIntent();
//        ArrayList<MyTrack> tracks = (ArrayList<MyTrack>) intent.getSerializableExtra("ListOfTracks");
//        Log.d(TAG, "onCreate: " + StartActivity.mAccessToken);
//        nameOfList = findViewById(R.id.namePlayList);
//        createList = findViewById(R.id.create);
//    }
//
//
//    public void createPlaylist(View view) {
//        String name = nameOfList.getText().toString();
//        createPlaylistRequest = spotifyApi.createPlaylist(userId, name)
//                .collaborative(false)
//                .public_(false)
//                .description("New playlist description")
//                .build();
//
//        try {
//            final Future<Playlist> playlistFuture = createPlaylistRequest.executeAsync();
//
//            // ...
//
//            final Playlist playlist = playlistFuture.get();
//
//            System.out.println("Name: " + playlist.getName());
//        } catch (InterruptedException | ExecutionException e) {
//            System.out.println("Error: " + e.getCause().getMessage());
//        }
//   }
}