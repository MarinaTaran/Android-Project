package com.example.marina.myspotifyapp;

import java.io.Serializable;

public class MyTrack implements Serializable {
    private String artist_name;
    private String track_name;
    private String start_time;
    private String external_urls;

    public MyTrack(String artist_name, String track_name, String start_time, String external_urls) {
        this.artist_name = artist_name;
        this.track_name = track_name;
        this.start_time = start_time;
        this.external_urls = external_urls;
    }

    public MyTrack() {
    }

    public String getArtist_name() {
        return artist_name;
    }

    public String getTrack_name() {
        return track_name;
    }

    public String getStart_time() {
        return start_time;
    }

    public String getExternal_urls() {
        return external_urls;
    }

    @Override
    public String toString() {
        return artist_name + " " + track_name + " " + start_time;
    }
}
