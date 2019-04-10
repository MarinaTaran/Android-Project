package com.example.marina.myspotifyapp;

import android.app.Activity;
import android.os.Bundle;

import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

//import org.json.simple.JSONArray;
//import org.json.JSONException;
//import org.json.simple.JSONObject;


import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StartActivity extends Activity {

    final String TAG = "StartActivity";
    TextView idName;
    Call mcal;
    public static final String CLIENT_ID = "f41477fde1804374addbfa10184175c9";
    //public static final String CLIENT_ID="zpd66efn0du6f5qn1hit9bdyb";
    private static final String REDIRECT_URI = "com.example.marina.myspotifyapp://callback";
    private static final int REQUEST_CODE = 1337;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    public static String mAccessToken;
    private String mAccessCode;
    private Call mCall, mCall2;

    String userId;

    private SpotifyAppRemote mSpotifyAppRemote;
    Button login;
    String id;
    String name;
    List<MyTrack> allTracks;
//Branch SpotifyTop

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        onToken();

        login = findViewById(R.id.login_button);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onProf();

                Intent intent = new Intent(StartActivity.this, Loading.class);

                intent.putExtra("ListOfTracks", (Serializable) allTracks);
                startActivity(intent);
            }
        });
    }


    public void onProf() {
        if (mAccessToken == null) {
            Toast.makeText(this, "Token null", Toast.LENGTH_LONG).show();
            return;
        }
        userId();
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/top/tracks?time_range=short_term")

//                .url("https://api.spotify.com/v1/me/player/recently-played?limit=50")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();
        Log.d(TAG, request.toString());


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
                //final org.json.JSONObject jsonObject = new org.json.JSONObject(response.body().string());
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(qwe);
                Log.d(TAG, "onResponse: qwe " + qwe);
                JsonObject root = element.getAsJsonObject();
                JsonArray items = root.getAsJsonArray("items");
                Iterator it1 = items.iterator();

//                List<MyTrack> allTracks = new ArrayList();
                allTracks = new ArrayList();
                while (it1.hasNext()) {
                    JsonObject item = (JsonObject) it1.next();
                    JsonElement track = (JsonElement) item.get("name");
                    String nameTrack = track.getAsString().replace("\"", "");
                    JsonArray artists = (JsonArray) item.get("artists");
                    JsonObject temp1 = (JsonObject) artists.get(0);
                    String artistName = temp1.get("name").toString();
//                   JsonObject urlTrack = (JsonObject) temp1.get("external_urls");
                    JsonElement extrUrl1 = item.get("uri");
                    String extrUrl = extrUrl1.getAsString().replace("\"", "");
                    Date data = new Date();
                    String time = String.valueOf(data.getTime());

                    MyTrack temp = new MyTrack(artistName, nameTrack, time, extrUrl);
                    Log.d(TAG, "onResponse:MyTrack " + temp);
                    allTracks.add(temp);
                    // System.out.println(favorite + " ****************");
                    setResponse(root.toString());

                }


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference iD=database.getReference("Users");
                Query queryUsers=iD.getDatabase().getReference("Users");
              queryUsers.addListenerForSingleValueEvent(new ValueEventListener() {
                  @Override
                  public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                      if(dataSnapshot.exists()){
                       User user = new User(userId,allTracks);
                       database.getReference("Users").child(userId).setValue(user);//dobavlenie usera
                      }else{

                      }
                  }

                  @Override
                  public void onCancelled(@NonNull DatabaseError databaseError) {

                  }
              });


                DatabaseReference myRef = database.getReference("tracks");
                Log.d(TAG, allTracks.toString() + " 111111111111111111111");
                Query query = myRef.getDatabase().getReference("tracks");
                query.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });
                for (MyTrack temp : allTracks) {
                    String key = myRef.push().getKey();
                    Query query1 = myRef.orderByChild("start_time")
//                    Query query1 = myRef.getDatabase().getReference("tracks");
                            .equalTo(temp.getStart_time());

                    query1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            if (!dataSnapshot.exists()) {
                                myRef.child(key).setValue(temp);
                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError databaseError) {
                        }
                    });

                }
            }
        });
    }

    public void onToken() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming", "user-read-recently-played",
                "user-read-currently-playing ", "user-modify-playback-state",
                "user-top-read", "playlist-modify-private", "playlist-modify-public"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    private AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type) {
        return new AuthenticationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
                .setShowDialog(false)
                .setScopes(new String[]{"user-read-email"})
                .setCampaign("your-campaign-token")
                .build();
    }

    public void onRequestCode() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.CODE, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);

    }

    @Override
    protected void onDestroy() {
        cancelCall();
        super.onDestroy();
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == REQUEST_CODE) {
            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);

            switch (response.getType()) {
                // Response was successful and contains auth token
                case CODE:
                    mAccessCode = response.getCode();
//                    Toast.makeText(this, mAccessCode, Toast.LENGTH_LONG).show();
                    break;
                case TOKEN:
                    mAccessToken = response.getAccessToken();
                    Log.d(TAG, "onActivityResu " + mAccessToken);
//                    Toast.makeText(this, mAccessToken, Toast.LENGTH_LONG).show();
                    // Handle successful response
                    break;
                // Auth flow returned an error
                case ERROR:
                    Log.e("Problem", "$$$$$$$$$$$$$$$$$$$$$$$$$$$");
//                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                    // Handle error response
                    break;
                // Most likely auth flow was cancelled
                default:
                    // Handle other cases
            }
        }
    }


    private void cancelCall() {
        if (mcal != null) {
            mcal.cancel();
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

    public void userId() {
        if (mAccessToken == null) {
            Toast.makeText(this, "Token null", Toast.LENGTH_LONG).show();
            return;
        }

        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        //cancelCall();
        mCall2 = mOkHttpClient.newCall(request);
        mCall2.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setResponse1("Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                //                    final JSONObject jsonObject = new JSONObject(response.body().string());
                // Log.d(TAG, "onResponse: jsonObject " + jsonObject);
                String qwe = response.body().string();
                JsonParser parser = new JsonParser();
                JsonObject root = (JsonObject) parser.parse(qwe);
//                    JsonObject  = (JsonObject) root.get("external_urls");
                userId = root.get("id").toString();
                Log.d(TAG, "onResponse: USERID " + userId);
                // setResponse(jsonObject.toString(3));
            }
        });
    }

    private void setResponse1(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //final TextView responseView = findViewById(R.id.text_prof);
//                textProf.setText(text);
//                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }
}
