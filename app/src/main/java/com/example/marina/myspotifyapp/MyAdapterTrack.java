package com.example.marina.myspotifyapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.TextureView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.neovisionaries.i18n.LanguageCode;
import com.spotify.protocol.types.Track;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class MyAdapterTrack extends RecyclerView.Adapter<MyAdapterTrack.TrackViewHolder> {
    private Context mCtx;
    List<MyTrack> favorite = new ArrayList<>();
    final String TAG = "MyAdapterTrack";


    public MyAdapterTrack(Context mCtx, List<MyTrack> favorite1) {
        this.mCtx = mCtx;
        Set<MyTrack> temp = new TreeSet<>();
        temp.addAll(favorite1);
        this.favorite.addAll(temp);
        Collections.sort(this.favorite, new Comparator<MyTrack>() {
            @Override
            public int compare(MyTrack o1, MyTrack o2) {
                return o1.getArtist_name().compareTo(o2.getArtist_name());
            }
        });
    }


    @NonNull
    @Override
    public TrackViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        LayoutInflater inflater = LayoutInflater.from(mCtx);
        View view = inflater.inflate(R.layout.list_of_track, null);
        TrackViewHolder trackViewHolder = new TrackViewHolder(view);
        return trackViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull TrackViewHolder trackViewHolder, int position) {
        MyTrack track = favorite.get(position);
        trackViewHolder.artist_name.setText(track.getArtist_name());
        trackViewHolder.track_name.setText(track.getTrack_name());
//        trackViewHolder.external_urls.setText(track.getExternal_urls()==null?"222":track.getExternal_urls());
//        trackViewHolder.checkBox.setText("Checkbox " + position);
        trackViewHolder.numberTrack.setText(String.valueOf(position));
        trackViewHolder.checkBox.setChecked(favorite.get(position).isCheked());
        trackViewHolder.checkBox.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                favorite.get(position).setCheked(trackViewHolder.checkBox.isChecked());
                Toast.makeText(mCtx, String.valueOf(position), Toast.LENGTH_LONG).show();
            }
        });
    }

    @Override
    public int getItemCount() {
        return favorite.size();
    }

    class TrackViewHolder extends RecyclerView.ViewHolder {
        protected CheckBox checkBox;
        TextView artist_name;
        TextView track_name;
        TextView start_time;
        //        TextView external_urls;
        TextView numberTrack;


        public TrackViewHolder(@NonNull View itemView) {
            super(itemView);
            artist_name = itemView.findViewById(R.id.textViewArtistName);
            track_name = itemView.findViewById(R.id.textViewTrack);
//            external_urls = itemView.findViewById(R.id.textViewUrlTrack);
            checkBox = (CheckBox) itemView.findViewById(R.id.cb);
            numberTrack = itemView.findViewById(R.id.number);
//            itemView.setOnCreateContextMenuListener((View.OnCreateContextMenuListener)this);
        }

//        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
////            menu.setHeaderTitle("Select The Action");
//            menu.add(Menu.NONE, R.id.edit,
//                    Menu.NONE, "Edit");
//            menu.add(Menu.NONE, R.id.save,
//                    Menu.NONE, "Save to new list");
//
//        }
    }
}
