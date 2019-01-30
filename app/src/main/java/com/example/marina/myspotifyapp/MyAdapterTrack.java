package com.example.marina.myspotifyapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.List;

public class MyAdapterTrack extends RecyclerView.Adapter<MyAdapterTrack.TrackViewHolder> {
    private Context mCtx;
    List<MyTrack> favorite;

    public MyAdapterTrack(Context mCtx, List<MyTrack> favorite) {
        this.mCtx = mCtx;
        this.favorite = favorite;
    }

    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_track_best, null);
        TrackViewHolder trackViewHolder = new TrackViewHolder(view);
        return trackViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder trackViewHolder, int position) {
        MyTrack track = favorite.get(position);

        trackViewHolder.artist_name.setText(track.getArtist_name()==null?"111":track.getArtist_name());
        trackViewHolder.track_name.setText(track.getTrack_name()==null?"333":track.getTrack_name());
        trackViewHolder.external_urls.setText(track.getExternal_urls()==null?"222":track.getExternal_urls());
    }

    @Override
    public int getItemCount() {
        return favorite.size();
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {
        TextView artist_name;
        TextView track_name;
        TextView start_time;
        TextView external_urls;


        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            artist_name = itemView.findViewById(R.id.textViewArtistName);
            track_name = itemView.findViewById(R.id.textViewTrack);
            external_urls = itemView.findViewById(R.id.textViewUrlTrack);
        }
    }

}
