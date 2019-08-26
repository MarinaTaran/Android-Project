package com.example.marina.myspotifyapp;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;
//import android.util.Log;
//
//import java.security.Provider;
//
//public class BackgroundService extends Service {
//    private boolean isRunning;
//    private Context context;
//    Thread backgroundTread;
//    private static final String TAG = "BackgroundService";
//
//
//    @Override
//    public IBinder onBind(Intent intent) {
//        return null;
//    }
//
//    @Override
//    public void onCreate() {
//        this.context = this;
//        this.isRunning = true;
//        this.backgroundTread = new Thread(myTask);
//    }
//
//    private Runnable myTask = new Runnable() {
//        @Override
//        public void run() {
//            Log.d(TAG, "run: @@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@@");
//            System.out.println("You have new 50 tracks");
//            stopSelf();
//        }
//    };
//
//    @Override
//    public void onDestroy() {
//
//    }
//
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        if (!this.isRunning) {
//            this.isRunning = true;
//            this.backgroundTread.start();
//        }
//        return START_STICKY;
//    }
//}
