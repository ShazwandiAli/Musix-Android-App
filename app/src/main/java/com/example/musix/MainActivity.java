package com.example.musix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private SongCollection songCollection = new SongCollection();
    static ArrayList<Song> favList = new ArrayList<Song>();
    SharedPreferences sharedPreferences;

    public void sendDataToActivity(int index) {
        Intent intent = new Intent(this, PlaySongActivity.class);
        intent.putExtra("index", index);
        startActivity(intent);
    }

    public void handleSelection(View myView) {
        String resourceId = getResources().getResourceEntryName(myView.getId());
        int currentArrayIndex = songCollection.searchSongById(resourceId);
        Log.d("temasek", "The index in the array for this song is : " + currentArrayIndex);
        sendDataToActivity(currentArrayIndex);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        sharedPreferences = getSharedPreferences("playList", MODE_PRIVATE);
        String albums = sharedPreferences.getString("list", "");
        if (!albums.equals("")) {
            TypeToken<ArrayList<Song>> token = new TypeToken<ArrayList<Song>>( ){ };
            Gson gson = new Gson();
            favList = gson.fromJson(albums, token.getType());
        }
    }


    public void addToFavourite(View view) {
        String songId = view.getContentDescription().toString();
        Log.d("temasek", "addToFavourite: " + songId);
        int songIndex = songCollection.searchSongById(songId);
        favList.add(songCollection.getCurrentSong(songIndex));
        Gson gson = new Gson();
        String json = gson.toJson(favList);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putString("list", json);
        editor.apply();
        Log.d("gson", json);
        Log.d("temasek", "addToFavourite: " + favList);
        Toast.makeText(this, "button is clicked", Toast.LENGTH_SHORT).show();
    }

    public void goToFavourite(View view) {
        Intent intent = new Intent(this, FavouriteActivity.class);
        startActivity(intent);
    }
}