package com.example.musix;

import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class PlaySongActivity extends AppCompatActivity {

    private String title = "";
    private String artist = "";
    private String fileLink = "";
    private int drawable;
    private int currentIndex = -1;

    private MediaPlayer player = new MediaPlayer();
    private Button btnPlayPause = null;
    private SongCollection songCollection = new SongCollection();
    SeekBar seekbar;
    Handler handler = new Handler();
    private SongCollection originalSongCollection = new SongCollection();
    List<Song> shuffleList = Arrays.asList(songCollection.songs);

    ImageButton repeatBtn;
    Boolean repeatFlag = false;

    ImageButton shuffleBtn;
    Boolean shuffleFlag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play_song);
        btnPlayPause = findViewById(R.id.btnPlayPause);
        Bundle songData = this.getIntent().getExtras();
        currentIndex = songData.getInt("index");
        Log.d("temasek", "Retrieved position is: " + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        seekbar = findViewById(R.id.seekBar);
        seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                if (player != null && player.isPlaying()) {
                    player.seekTo(seekBar.getProgress());
                }
            }
        });
        repeatBtn = findViewById(R.id.repeatBtn);
        shuffleBtn = findViewById(R.id.shuffleBtn);
        playSong(fileLink);
    }

    Runnable p_bar = new Runnable() {
        @Override
        public void run() {
            if (player != null && player.isPlaying()) {
                seekbar.setProgress(player.getCurrentPosition());
            }
            handler.postDelayed(this,1000);
        }
    };

    public void displaySongBasedOnIndex(int selectedIndex) {
        Song song = songCollection.getCurrentSong(currentIndex);
        title = song.getTitle();
        artist = song.getArtist();
        fileLink = song.getFileLink();
        drawable = song.getDrawable();
        TextView txtTitle = findViewById(R.id.txtSongTitle);
        txtTitle.setText(title);
        TextView txtArtist = findViewById(R.id.txtArtist);
        txtArtist.setText(artist);
        ImageView iCoverArt = findViewById(R.id.imgCoverArt);
        iCoverArt.setImageResource(drawable);
    }

    public void playSong(String songUrl) {
        try {
            player.reset();
            player.setDataSource(songUrl);
            player.prepare();
            player.start();
            seekbar.setMax(player.getDuration());
            handler.removeCallbacks(p_bar);
            handler.postDelayed(p_bar, 1000);
            gracefullyStopsWhenMusicEnds();

            btnPlayPause.setText("PAUSE");
            setTitle(title);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void playOrPauseMusic(View view) {
        if (player.isPlaying()) {
            player.pause();
            btnPlayPause.setText("PLAY");
        } else {
            player.start();
            btnPlayPause.setText("PAUSE");
        }
    }

    public void gracefullyStopsWhenMusicEnds() {
        player.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                if (repeatFlag){
                    playOrPauseMusic(null);
                }
                else {
                    playNext(null);
                    btnPlayPause.setText("PAUSE");
                }
            }
        });
    }

    public void playNext(View view) {
        currentIndex = songCollection.getNextSong(currentIndex);
        Toast.makeText(this, "After clicking playNext, \nthe current index of this song\n" + "in the songCollection array is now:" + currentIndex, Toast.LENGTH_LONG).show();
        Log.d("temasek", "After playNext, the index is now :" + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(fileLink);
    }

    public void playPrevious(View view) {
        currentIndex = songCollection.getPrevSong(currentIndex);
        Toast.makeText(this, "After clicking playPrevious, \nthe current index of this song\n" + "in the songCollection is nowS" + currentIndex, Toast.LENGTH_LONG).show();
        Log.d("temasek", "After playPrevious, the index is now :" + currentIndex);
        displaySongBasedOnIndex(currentIndex);
        playSong(fileLink);
    }



    public void repeatSong(View view) {
        if (repeatFlag) {
            repeatBtn.setImageResource(R.drawable.repeat);
        }
        else {
            repeatBtn.setImageResource(R.drawable.repeat_on);
            Log.d("temasek", "here in repeat song");
        }
        repeatFlag = !repeatFlag;
    }

    public void shuffleSong(View view) {
        if (shuffleFlag) {
            shuffleBtn.setImageResource(R.drawable.shuffle);
        }
        else {
            shuffleBtn.setImageResource(R.drawable.shuffle_on);
            Collections.shuffle(shuffleList);
            for (int i = 0; i < shuffleList.size(); i++) {
                Log.d("shuffle", shuffleList.get(i).getTitle());
            }
        }
        shuffleFlag = !shuffleFlag;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        handler.removeCallbacks(p_bar);
        player.stop();
        player.release();
        player = null;
    }
}