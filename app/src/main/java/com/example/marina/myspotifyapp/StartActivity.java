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


import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class StartActivity extends Activity {


    TextView idName;
    Call mcal;
    public static final String CLIENT_ID = "f41477fde1804374addbfa10184175c9";
    private static final String REDIRECT_URI = "com.example.marina.myspotifyapp://callback";
    private static final int REQUEST_CODE = 1337;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private String mAccessCode;
    private Call mCall;
    private SpotifyAppRemote mSpotifyAppRemote;
    Button login;
    String id;
    String name;


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
                startActivity(intent);
            }
        });
    }


    public void onProf() {
        if (mAccessToken == null) {
//            final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), R.string.warning_need_token, Snackbar.LENGTH_SHORT);
//            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
//            snackbar.show();
            Toast.makeText(this, "Token null", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me/player/recently-played?limit=50")
                .addHeader("Authorization", "Bearer " + mAccessToken)
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
                //final org.json.JSONObject jsonObject = new org.json.JSONObject(response.body().string());
                JsonParser parser = new JsonParser();
                JsonElement element = parser.parse(qwe);
                JsonObject root = element.getAsJsonObject();
                JsonArray items = root.getAsJsonArray("items");
                Iterator it1 = items.iterator();

                List<MyTrack> allTracks = new ArrayList();
                while (it1.hasNext()) {
                    JsonObject item = (JsonObject) it1.next();
                    String dataPlayed = item.get("played_at").toString().replace("\"", "");
                    JsonObject track = (JsonObject) item.get("track");
                    JsonObject alb = (JsonObject) track.get("album");
                    String nameTrack = alb.get("name").toString().replace("\"", "");

                    // System.out.println(nameTrack);
                    JsonArray artists = (JsonArray) alb.get("artists");
                    JsonObject temp1 = (JsonObject) artists.get(0);
                    JsonObject urlTrack = (JsonObject) temp1.get("external_urls");
                    String urlSpot = urlTrack.get("spotify").toString().replace("\"", "");
                    String nameArtist = ((JsonObject) artists.get(0)).get("name").toString().replace("\"", "");
//                    System.out.println(nameArtist);
                    MyTrack temp = new MyTrack(nameArtist, nameTrack, dataPlayed, urlSpot);
                    allTracks.add(temp);
                    // System.out.println(favorite + " ****************");
                    setResponse(root.toString());

                }


                FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference myRef = database.getReference("tracks");
                Log.d("qaz", allTracks.toString() + " 111111111111111111111");

                for (MyTrack temp : allTracks) {
                    String key = myRef.push().getKey();
                    Query query = myRef.orderByChild("start_time")
                            .equalTo(temp.getStart_time());

                    query.addListenerForSingleValueEvent(new ValueEventListener() {
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
        builder.setScopes(new String[]{"streaming", "user-read-recently-played", "user-read-currently-playing ", "user-modify-playback-state"});
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
}
