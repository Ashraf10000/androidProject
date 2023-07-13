package com.example.musicapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.ContentUris;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

public class song_playing extends AppCompatActivity implements songChangeListener {
    private final List<MusicList>songsList = new ArrayList<>();
    private RecyclerView songsRecyclerview;
    private MediaPlayer mediaPlayer;
    private TextView endTime,startTime;
    private boolean isPlaying = false;
    private SeekBar playerSeekBar;
    private ImageView playPauseBtn;
    private Timer timer;
    private int  currentPos = 0;
    private MusicAdapter  musicAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decodeView = getWindow().getDecorView();
        decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        setContentView(R.layout.activity_song_playing);
        final LinearLayout searchBtn = findViewById(R.id.search_btn);
        final LinearLayout menuBtn = findViewById(R.id.menu_btn);
        songsRecyclerview = findViewById(R.id.songsRecyclerview);
        final CardView playPauseCard = findViewById(R.id.playPauseCard);
        final ImageView prevBtn = findViewById(R.id.previousBtn);
        playPauseBtn = findViewById(R.id.playPauseImg);
        final ImageView nextBtn = findViewById(R.id.nextBtn);
        startTime = findViewById(R.id.startTime);
        endTime = findViewById(R.id.endTime);
        playerSeekBar = findViewById(R.id.playerSeekBar);
        songsRecyclerview.setHasFixedSize(true);
        songsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        mediaPlayer = new MediaPlayer();
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            getMusicFiles();
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M)
                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, 11);
             else getMusicFiles();
        }

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int nextSongPos = currentPos+1;
                if(nextSongPos >= songsList.size())
                    nextSongPos = 0;
                songsList.get(currentPos).setPlaying(false);
                songsList.get(nextSongPos).setPlaying(true);
                musicAdapter.updateList(songsList);
                songsRecyclerview.scrollToPosition(nextSongPos);
                onChange(nextSongPos);
            }
        });
        prevBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int prevSongPos = currentPos-1;
                if(prevSongPos < 0)
                    prevSongPos = songsList.size()-1;
                songsList.get(currentPos).setPlaying(false);
                songsList.get(prevSongPos).setPlaying(true);
                musicAdapter.updateList(songsList);
                songsRecyclerview.scrollToPosition(prevSongPos);
                onChange(prevSongPos);
            }
        });

        playPauseCard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isPlaying){
                    isPlaying = false;
                    mediaPlayer.pause();
                    playPauseBtn.setImageResource(R.drawable.play_icon);
                }
                else {
                    isPlaying = true;
                    mediaPlayer.start();
                    playPauseBtn.setImageResource(R.drawable.pause_icon);

                }
            }
        });

        playerSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                    if(fromUser){
                        if(isPlaying)
                            mediaPlayer.seekTo(progress);
                        else
                            mediaPlayer.seekTo(0);
                    }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("Range")
    private void getMusicFiles() {
        ContentResolver contentResolver = getContentResolver();
        Uri uri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
        Cursor cursor = contentResolver.query(uri,null,MediaStore.Audio.Media.DATA+" LIKE?",new String[]{"%.mp3%"},null);
        if(!cursor.moveToNext())
            Toast.makeText(this, "NO SONGS FOUND!", Toast.LENGTH_SHORT).show();
        else {
            while (cursor.moveToNext()){
                @SuppressLint("Range")
                final String getMusicFileName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.TITLE));
                final String getArtistName = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST));
                @SuppressLint("Range")
                long cursorId = cursor.getLong(cursor.getColumnIndex(MediaStore.Audio.Media._ID));
                Uri musicFileUri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,cursorId);
                String getDuration = "00:00";
                if(Build.VERSION.SDK_INT>=Build.VERSION_CODES.Q){
                    getDuration = cursor.getString(cursor.getColumnIndex(MediaStore.Audio.AudioColumns.DURATION));
                }
                final MusicList musicList = new MusicList(getMusicFileName,getArtistName,getDuration,false,musicFileUri);
                songsList.add(musicList);
            }
            musicAdapter = new MusicAdapter(songsList,song_playing.this);
            songsRecyclerview.setAdapter(musicAdapter);
        }
        cursor.close();
    }

    @SuppressLint("MissingSuperCall")
    @Override
    public void onRequestPermissionsResult(int requestCode,  String[] permissions,int[] grantResults) {
       // super.onRequestPermissionsResult(requestCode, permissions, grantResults);
      //  super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            getMusicFiles();
        } else {
            Toast.makeText(this, "Permission Denied!!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        if (hasFocus){
            View decodeView = getWindow().getDecorView();
            decodeView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_FULLSCREEN|View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION);
        }
    }

    @Override
    public void onChange(int pos) {
        currentPos = pos;
        if(mediaPlayer.isPlaying()){
            mediaPlayer.pause();
            mediaPlayer.reset();
        }

        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    mediaPlayer.setDataSource(song_playing.this,songsList.get(currentPos).getMusicFile());
                    mediaPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(song_playing.this, "unable to play song!", Toast.LENGTH_SHORT).show();
                }
            }
        }).start();
        mediaPlayer.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                final int getTotalDuration = mp.getDuration();
                String generateDuration = String.format(Locale.getDefault(),"%02d:%02d",
                TimeUnit.MILLISECONDS.toMinutes(getTotalDuration),
                TimeUnit.MILLISECONDS.toSeconds(getTotalDuration) -
                TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getTotalDuration)));
                endTime.setText(generateDuration);
                isPlaying = true;
                mp.start();
                playerSeekBar.setMax(getTotalDuration);
                playPauseBtn.setImageResource(R.drawable.pause_icon);
            }
        });

        timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        final int getCurrentDuration = mediaPlayer.getCurrentPosition();
                        String generateDuration = String.format(Locale.getDefault(),"%02d:%02d",
                                TimeUnit.MILLISECONDS.toMinutes(getCurrentDuration),
                                TimeUnit.MILLISECONDS.toSeconds(getCurrentDuration) -
                                        TimeUnit.MINUTES.toSeconds(TimeUnit.MILLISECONDS.toMinutes(getCurrentDuration)));
                        playerSeekBar.setProgress(getCurrentDuration);
                        startTime.setText(generateDuration);
                    }
                });

            }

        },1000,1000);
        mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(song_playing.this, "end of surrah!", Toast.LENGTH_SHORT).show();
                mediaPlayer.reset();
                timer.purge();
                timer.cancel();
                isPlaying = false;
                playPauseBtn.setImageResource(R.drawable.play_icon);
                playerSeekBar.setProgress(0);
            }
        });

    }
}