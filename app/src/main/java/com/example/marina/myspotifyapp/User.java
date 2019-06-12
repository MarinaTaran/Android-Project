package com.example.marina.myspotifyapp;

import com.wrapper.spotify.model_objects.specification.Track;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

public class User implements Serializable {//sinxronizaciya bazi dannix s prilozheniem
    String id;
   List<MyTrack> tracks = new ArrayList<>();

    public User(String id, List<MyTrack> tracks) {
        this.id = id;
        this.tracks = tracks;
        System.out.println("");
    }

    public User() {
    }



    public String getId() {//chtob rabotal Firebase
        return id;
    }

    public List<MyTrack> getTracks() {
        return tracks;
    }
}
