package com.example.moviehub.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.selection.SelectionTracker;
import androidx.recyclerview.widget.RecyclerView;


import com.example.moviehub.models.Genres;
import com.example.moviehub.models.Movies;
import com.example.moviehub.R;
import com.example.moviehub.utils.MyItemDetail;
import com.example.moviehub.utils.Utils;

import java.util.List;

public class SavedMoviesAdapter extends RecyclerView.Adapter<SavedMoviesAdapter.ViewHolder> {

    private List<Movies.Movie> movies;
    private final Context context;
    private SelectionTracker<Long> selectionTracker;

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

            if(selectionTracker != null){
                boolean visible = selectionTracker.hasSelection();

                holder.genre.setVisibility(visible? View.INVISIBLE : View.VISIBLE);
                holder.review.setVisibility(visible? View.INVISIBLE : View.VISIBLE);
                holder.checkBox.setVisibility(visible? View.VISIBLE : View.GONE);
                holder.checkBox.setChecked(selectionTracker.isSelected(holder.getItemDetails().getSelectionKey()));
            }
    }

    @Override
    public int getItemCount() {
        if(movies == null)
            return 0;
        else return movies.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView image;
        TextView title, genre, review;
        CheckBox checkBox;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.img);
            title = itemView.findViewById(R.id.txt);
            genre = itemView.findViewById(R.id.genre);
            review = itemView.findViewById(R.id.review);
            checkBox = itemView.findViewById(R.id.checkbox);
        }

        public MyItemDetail getItemDetails(){
            return new MyItemDetail(getAdapterPosition(), movies.get(getAdapterPosition()).getId());
        }
    }

    public void setMovies(List<Movies.Movie> movies){
        this.movies = movies;
        notifyDataSetChanged();
    }

    public List<Movies.Movie> getMovies() {
        return movies;
    }

    public void setSelectionTracker(SelectionTracker<Long> selectionTracker) {
        this.selectionTracker = selectionTracker;
    }
}
