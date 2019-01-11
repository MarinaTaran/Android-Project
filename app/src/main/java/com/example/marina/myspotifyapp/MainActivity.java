package com.example.marina.myspotifyapp;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.content.ContextCompat;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.marina.myspotifyapp.R;
import com.spotify.android.appremote.api.ConnectionParams;
import com.spotify.android.appremote.api.Connector;
import com.spotify.android.appremote.api.SpotifyAppRemote;
import com.spotify.sdk.android.authentication.AuthenticationClient;
import com.spotify.sdk.android.authentication.AuthenticationRequest;
import com.spotify.sdk.android.authentication.AuthenticationResponse;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class MainActivity extends Activity {
    TextView textTok;
    TextView textCode;
    TextView textProf;
    Call mcal;
    public static final String CLIENT_ID = "f41477fde1804374addbfa10184175c9";
    //    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
//    public static final int AUTH_CODE_REQUEST_CODE = 0x11;
    //private static final String REDIRECT_URI = "yourcustomprotocol://callback";
//    private static final String REDIRECT_URI = "yourcustomprotocol://callback";
    private static final String REDIRECT_URI ="test://callback";
    private static final int REQUEST_CODE = 1337;
    private final OkHttpClient mOkHttpClient = new OkHttpClient();
    private String mAccessToken;
    private String mAccessCode;
    private Call mCall;
    Button token;
    Button request;
    Button user;
    private SpotifyAppRemote mSpotifyAppRemote;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textTok = findViewById(R.id.textToken);
        textCode = findViewById(R.id.textCode);
        textProf = findViewById(R.id.textProf);
        token = findViewById(R.id.get_token);
        request = findViewById(R.id.get_request_code);
        user = findViewById(R.id.get_user);
        onToken();

        //https://clickmy.site/u/callback/
        //251871c2d42d4e7bb675409c05baf0b4
    }

    public void onProf(View view) {
        if (mAccessToken == null) {
//            final Snackbar snackbar = Snackbar.make(findViewById(R.id.activity_main), R.string.warning_need_token, Snackbar.LENGTH_SHORT);
//            snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorAccent));
//            snackbar.show();
            Toast.makeText(this, "Token null", Toast.LENGTH_LONG).show();
            return;
        }
        Toast.makeText(this, "!!!!!!!!!!!!!!!!!", Toast.LENGTH_LONG).show();
        final Request request = new Request.Builder()
                .url("https://api.spotify.com/v1/me")
                .addHeader("Authorization", "Bearer " + mAccessToken)
                .build();

        cancelCall();
        mCall = mOkHttpClient.newCall(request);
        mCall.enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                setResponse("Failed to fetch data: " + e);
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    final JSONObject jsonObject = new JSONObject(response.body().string());


                    setResponse(jsonObject.toString(3));
                } catch (JSONException e) {
                    setResponse("Failed to parse data: " + e);
                }
            }
        });
    }

    private void setResponse(final String text) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                //final TextView responseView = findViewById(R.id.text_prof);
                textProf.setText(text);
                Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG).show();
            }
        });
    }
    public void onToken() {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
        AuthenticationRequest request = builder.build();
        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
    }

    public void onToken(View view) {
        AuthenticationRequest.Builder builder =
                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
        builder.setScopes(new String[]{"streaming"});
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

    public void onRequestCode(View view) {
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
                    Toast.makeText(this, mAccessCode, Toast.LENGTH_LONG).show();
                    break;
                case TOKEN:
                    mAccessToken = response.getAccessToken();
                    Toast.makeText(this, mAccessToken, Toast.LENGTH_LONG).show();
                    // Handle successful response
                    break;
                    // Auth flow returned an error
                case ERROR:
                    Log.e("Problem","$$$$$$$$$$$$$$$$$$$$$$$$$$$");
                    Toast.makeText(this, "Error", Toast.LENGTH_LONG).show();
                    // Handle error response
                    break;
                    // Most likely auth flow was cancelled
                default:
                    Toast.makeText(this, "Default", Toast.LENGTH_LONG).show();
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
//
//    @Override
//    protected void onStart() {
//        super.onStart();
//        ConnectionParams connectionParams = new ConnectionParams.Builder(CLIENT_ID)
//                        .setRedirectUri(REDIRECT_URI)
//                        .showAuthView(true)
//                        .build();
//        SpotifyAppRemote.connect(this, connectionParams,
//                new Connector.ConnectionListener() {
//
//                    @Override
//                    public void onConnected(SpotifyAppRemote spotifyAppRemote) {
//                        mSpotifyAppRemote = spotifyAppRemote;
//                        Log.d("MainActivity", "Connected! Yay!");
//                        // Now you can start interacting with App Remote
//                        connected();
//                    }
//
//                    @Override
//                    public void onFailure(Throwable throwable) {
//                        Log.e("MainActivity", throwable.getMessage(), throwable);
//                        // Something went wrong when attempting to connect! Handle errors here
//                    }
//                });
//
//        // We will start writing our code here.
//    }
//
//    private void connected() {
//        mSpotifyAppRemote.getPlayerApi().play("spotify:playlist:37i9dQZF1DX2sUQwD7tbmL");

//        mSpotifyAppRemote.getPlayerApi()
//                .subscribeToPlayerState()
//                .setEventCallback(playerState -> {
//                    final MyTrack track = playerState.track;
//                    if (track != null) {
//                        Log.d("MainActivity", track.name + " by " + track.artist.name);
//                    }
//                });


//    }

}


//package com.example.marina.myspotifyapp;
//
//import android.app.Activity;
//import android.content.Intent;
//import android.net.Uri;
//import android.os.Bundle;
//import android.view.View;
//import android.widget.Button;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.spotify.sdk.android.authentication.AuthenticationClient;
//import com.spotify.sdk.android.authentication.AuthenticationRequest;
//import com.spotify.sdk.android.authentication.AuthenticationResponse;
//
//public class MainActivity extends Activity {
//    public static final String CLIENT_ID = "f41477fde1804374addbfa10184175c9";
//    public static final int AUTH_TOKEN_REQUEST_CODE = 0x10;
//    public static final int AUTH_CODE_REQUEST_CODE = 0x11;
////        private static final String REDIRECT_URI = "yourcustomprotocol://callback";
////    private static final String REDIRECT_URI = "comspotifytestsdk://callback";
//private static final String REDIRECT_URI ="yourcustomprotocol://callback";
////    Button token;
////    Button code;
////    Button getUser;
//    TextView textToken;
//    TextView textCode;
//
//    // Code called from an activity
//    private static final int REQUEST_CODE = 1337;
//    private String mAccessCode;
//    private String mAccessToken;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
////        token = findViewById(R.id.request_token);
////        code = findViewById(R.id.request_code);
////        getUser = findViewById(R.id.get_user);
//        textToken = findViewById(R.id.textToken);
//        textCode = findViewById(R.id.textCode);
//
//        AuthenticationRequest.Builder builder =
//                new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI);
//
//        builder.setScopes(new String[]{"streaming"});
//        AuthenticationRequest request = builder.build();
//
//        AuthenticationClient.openLoginActivity(this, REQUEST_CODE, request);
//
//
//
//
////
//    }
//    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
//        super.onActivityResult(requestCode, resultCode, intent);
//
//        // Check if result comes from the correct activity
//        if (requestCode == REQUEST_CODE) {
//            AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, intent);
//
//            switch (response.getType()) {
//                case CODE:
//                    mAccessCode=response.getCode();
//                    Toast.makeText(this,mAccessCode,Toast.LENGTH_LONG).show();
//                    break;
//                // Response was successful and contains auth token
//                case TOKEN:
//                    mAccessToken=response.getAccessToken();
//                   Toast.makeText(this,mAccessToken,Toast.LENGTH_LONG).show(); // Handle successful response
//                    break;
//
//                // Auth flow returned an error
//                case ERROR:
//                   Toast.makeText(this,"Error",Toast.LENGTH_LONG).show();; // Handle error response
//                    break;
//
//                // Most likely auth flow was cancelled
//                default:
//                    // Handle other cases
//            }
//        }
//    }
//
//
//}
// token.setOnClickListener(new View.OnClickListener() {
//
//            @Override
//            public void onClick(View view) {
////                final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.TOKEN);
////                AuthenticationClient.openLoginActivity(MainActivity.this, AUTH_TOKEN_REQUEST_CODE, request);
//
//                final AuthenticationRequest request = new AuthenticationRequest.Builder(CLIENT_ID, AuthenticationResponse.Type.TOKEN, REDIRECT_URI)
//                        .setScopes(new String[]{"user-read-private", "playlist-read", "playlist-read-private", "streaming"})
//                        .build();
//
//                AuthenticationClient.openLoginActivity(MainActivity.this, REQUEST_CODE, request);
//            }
//        });
//        code.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                final AuthenticationRequest request = getAuthenticationRequest(AuthenticationResponse.Type.CODE);
//                AuthenticationClient.openLoginActivity(MainActivity.this, AUTH_CODE_REQUEST_CODE, request);
//            }
//        });
//
//    }
//
//
//    private AuthenticationRequest getAuthenticationRequest(AuthenticationResponse.Type type) {
//        return new AuthenticationRequest.Builder(CLIENT_ID, type, getRedirectUri().toString())
//                .setShowDialog(false)
//                .setScopes(new String[]{"user-read-email"})
//                .setCampaign("your-campaign-token")
//                .build();
//    }
//
//    private Uri getRedirectUri() {
//        return new Uri.Builder()
//                .scheme(getString(R.string.com_spotify_sdk_redirect_scheme))
//                .authority(getString(R.string.com_spotify_sdk_redirect_host))
//                .build();
//    }
//
//
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        final AuthenticationResponse response = AuthenticationClient.getResponse(resultCode, data);
//
//        if (AUTH_TOKEN_REQUEST_CODE == requestCode) {
////            Toast.makeText(this, response.getAccessToken(), Toast.LENGTH_LONG).show();
//            textToken.setText(response.getAccessToken());
//        } else if (AUTH_CODE_REQUEST_CODE == requestCode) {
//            textCode.setText(response.getCode());
//
//        }