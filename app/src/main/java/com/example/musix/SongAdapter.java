package com.example.musix;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<MyView> implements Filterable {

    private List<Song> songs;
    private List<Song> songsFiltered;

    SongCollection songCollection = new SongCollection();

    public SongAdapter(List<Song> songs) {
        this.songs = songs;
        this.songsFiltered = songs;
    }

    Context context;
    @NonNull
    @Override
    public MyView onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        View songView = inflater.inflate(R.layout.item_song, parent, false);
        MyView viewHolder = new MyView(songView);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, final int position) {
        Song contact = songsFiltered.get(position);
        TextView artist = holder.artistTxt;
        artist.setText(contact.getArtist());
        TextView title = holder.titleTxt;
        title.setText(contact.getTitle());
        holder.image.setImageResource(contact.getDrawable());
        holder.image.setContentDescription(contact.getId());

        holder.image.setOnClickListener((View v) ->{
            String songID = holder.image.getContentDescription().toString();
            int index = songCollection.searchSongById(songID);
            Intent intent= new Intent(v.getContext(), PlaySongActivity.class);
            intent.putExtra("index", index);
            v.getContext().startActivity(intent);
        });
        holder.removeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.favList.remove(position);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return songsFiltered.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                String charString = constraint.toString();
                if (charString.isEmpty()) {
                    songsFiltered = songs;
                } else {
                    List<Song> filteredList = new ArrayList<Song>();
                    for (int i = 0; i < songs.size(); i++) {
                        if (songs.get(i).getTitle().toLowerCase().contains(charString.toLowerCase())){
                            filteredList.add(songs.get(i));
                        }
                    }
                    songsFiltered = filteredList;
                }

                FilterResults filterResults = new FilterResults();
                filterResults.values = songsFiltered;
                return filterResults;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults results) {
                songsFiltered = (List<Song>) results.values;
                notifyDataSetChanged();
            }
        };
    }
}
