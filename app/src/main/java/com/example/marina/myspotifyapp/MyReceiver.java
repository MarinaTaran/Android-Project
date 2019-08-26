//package com.example.marina.myspotifyapp;
//
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//import android.content.IntentFilter;
//import android.net.ConnectivityManager;
//import android.util.Log;
//import android.widget.Toast;
//
//public class MyReceiver extends BroadcastReceiver {
//    private static final String TAG = "MyReceiver";
//
//    @Override
//    public void onReceive(Context context, Intent intent) {
//
//        Intent background = new Intent(context,BackgroundService.class);
//        context.startService(background);
//        Log.d(TAG, "onReceive: !!!!!!!!!!!!!!!!!!!!!!!!!!");
////        StringBuilder sb = new StringBuilder();
////        sb.append("Action: " + intent.getAction() + "\n");
////        sb.append("URI: " + intent.toUri(Intent.URI_INTENT_SCHEME).toString() + "\n");
////        String log = sb.toString();
////        Log.d(TAG, log);
////        Toast.makeText(context, log, Toast.LENGTH_LONG).show();
////
////        BroadcastReceiver br = new MyReceiver();
////        IntentFilter filter = new IntentFilter(ConnectivityManager.CONNECTIVITY_ACTION);
////        filter.addAction(Intent.ACTION_GET_CONTENT);
////        this.registerReceiver(br, filter);
////        // TODO: This method is called when the BroadcastReceiver is receiving
////        // an Intent broadcast.
////        throw new UnsupportedOperationException("Not yet implemented");
//    }
//
////    private void registerReceiver(BroadcastReceiver br, IntentFilter filter) {
////    }
//}
