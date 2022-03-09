package com.example.moviepicker;

import android.content.Context;
import android.provider.SyncStateContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import java.util.List;

public class SavedMoviesAdapter extends RecyclerView.Adapter<SavedMoviesAdapter.ViewHolder> {

    private List<Movies.Movie> movies;
    private Context context;

    public SavedMoviesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return  new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_saved_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movies.Movie movie = movies.get(position);
            Utils.loadImage(context, Utils.IMAGE_URL + movie.getBackdropPath(), holder.img);
            holder.txt.setText(movie.getTitle());
    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;
        else return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView img;
        TextView txt;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            img = itemView.findViewById(R.id.img);
            txt = itemView.findViewById(R.id.txt);
        }
    }

    public void setMovies(List<Movies.Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
}
