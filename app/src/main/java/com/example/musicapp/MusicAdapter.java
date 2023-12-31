package com.example.musicapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;
import java.util.concurrent.TimeUnit;

public class MusicAdapter extends RecyclerView.Adapter<MusicAdapter.MyViewHolder>{
    private  List<MusicList>list;
    private final Context context;
    private int playingPos = 0;
    private final songChangeListener songChangeListener;

    public MusicAdapter(List<MusicList> list, Context context) {
        this.list = list;
        this.context = context;
        this.songChangeListener = ((com.example.musicapp.songChangeListener)context);
    }
    @SuppressLint("InflateParams")
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.music_adapter_layout,null));
    }

    @Override
    public void onBindViewHolder( MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
        MusicList list2 = list.get(position);
        if (list2.isPlaying()){
            playingPos = position;
            holder.rootLayout.setBackgroundResource(R.drawable.round_back_blue_10);
        }
        else holder.rootLayout.setBackgroundResource(R.drawable.round_back_10);

       /* String generateDuration = String.format(Locale.getDefault(),"%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getDuration())),
                TimeUnit.MILLISECONDS.toSeconds(Long.parseLong(list2.getDuration())) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(Long.parseLong(list2.getDuration()))));
*/
        holder.songTitle.setText(list2.getTitle());
        holder.songArtist.setText(list2.getArtist());
        //holder.songDuration.setText(generateDuration);

        holder.rootLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                list.get(playingPos).setPlaying(false);
                list2.setPlaying(true);
                songChangeListener.onChange(position);
                notifyDataSetChanged();
            }
        });

    }
    public void updateList(List<MusicList>list){
        this.list = list;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
    static class MyViewHolder extends RecyclerView.ViewHolder{
        private final RelativeLayout rootLayout;
        private final TextView songTitle;
        private final TextView songArtist;
        private final TextView songDuration;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            rootLayout = itemView.findViewById(R.id.rootLayout);
            songTitle  = itemView.findViewById(R.id.songTitle);
            songArtist  = itemView.findViewById(R.id.songArtist);
            songDuration = itemView.findViewById(R.id.songDuration);

        }
    }

    public static String convertToMMSS(String duration){
        Long millis = Long.parseLong(duration);
        return String.format("%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(millis) % TimeUnit.HOURS.toMinutes(1),
                TimeUnit.MILLISECONDS.toSeconds(millis) % TimeUnit.MINUTES.toSeconds(1));
    }
}
