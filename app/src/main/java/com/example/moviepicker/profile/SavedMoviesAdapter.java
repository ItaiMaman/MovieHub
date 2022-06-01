package com.example.moviepicker.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.moviepicker.Genres;
import com.example.moviepicker.Movies;
import com.example.moviepicker.R;
import com.example.moviepicker.Utils;

import java.util.List;

public class SavedMoviesAdapter extends RecyclerView.Adapter<SavedMoviesAdapter.ViewHolder> {

    private List<Movies.Movie> movies;
    private final Context context;

    public SavedMoviesAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_saved_movie, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Movies.Movie movie = movies.get(position);
            Utils.loadImage(context, Utils.IMAGE_URL + movie.getPosterPath(), holder.image);
            holder.title.setText(movie.getTitle());
            holder.review.setText(String.valueOf(movie.getVoteAverage()));
            holder.genre.setText(Genres.findByKey(movie.getGenreIds().get(0)));
    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;
        else return movies.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, genre, review;



        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.txt);
            genre = itemView.findViewById(R.id.genre);
            review = itemView.findViewById(R.id.review);
        }
    }

    public void setMovies(List<Movies.Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }
}
