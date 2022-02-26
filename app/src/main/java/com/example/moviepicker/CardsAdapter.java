package com.example.moviepicker;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.google.android.material.button.MaterialButton;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class CardsAdapter extends RecyclerView.Adapter<CardsAdapter.ViewHolder > {

    private List<Movies.Movie> movies;
    private Context context;

    public CardsAdapter(Context context) {
        this.context = context;
    }

    @NonNull
    @Override
    public CardsAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.item_card, parent, false));
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull CardsAdapter.ViewHolder holder, int position) {
        if(movies != null){
            Movies.Movie movie = movies.get(position);
            holder.title.setText(movie.getTitle());
            SimpleDateFormat date = new SimpleDateFormat("yyyy", Locale.ENGLISH);

            @SuppressLint("SimpleDateFormat") SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

            try {
                Date theDate = sdf.parse(movie.getReleaseDate());
                String release_date = date.format(theDate);
                holder.releaseDate.setText(release_date);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            holder.genre.setText(Genres.findByKey(movie.getGenreIds().get(0)));

            holder.review.setText(movie.getVoteAverage().toString());
            Glide.with(context).load(Constants.IMAGE_URL + movie.getPosterPath()).centerCrop().into(holder.img);
        }

    }

    @Override
    public int getItemCount() {
        if(movies != null)
            return movies.size();
        else
            return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder{

        TextView title, genre, releaseDate, review;
        ImageView img;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.title);
            genre = itemView.findViewById(R.id.genre);
            releaseDate = itemView.findViewById(R.id.release_date);
            img = itemView.findViewById(R.id.image);
            review = itemView.findViewById(R.id.review);
        }
    }

    public void setMovies(List<Movies.Movie> movies){
        int size = getItemCount();
        this.movies = movies;

        notifyItemRangeInserted(size, getItemCount()-1);
    }
}
