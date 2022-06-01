package com.example.moviehub.Rooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.moviehub.AddEventActivity;
import com.example.moviehub.CardsAdapter;
import com.example.moviehub.CustomProgressDialog;
import com.example.moviehub.Movies;
import com.example.moviehub.MyViewModelFactory;
import com.example.moviehub.R;
import com.example.moviehub.Utils;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.HashMap;

public class SwipeMatchActivity extends AppCompatActivity implements CardStackListener {

    CardStackView cardStackView;
    CardStackLayoutManager layoutManager;
    SwipeMatchViewModel viewModel;
    CardsAdapter adapter;
    Toolbar toolbar;
    String id;
    boolean host;
    MaterialAlertDialogBuilder leaveDialog, roomClosedDialog;
    HashMap<String, HashMap<String, Long>> swiped;
    int users;
    FloatingActionButton fab;
    Dialog matchDialog;
    boolean closeRoom;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_match);

        closeRoom = true;
        id = getIntent().getStringExtra("id");
        host = getIntent().getBooleanExtra("host", false);
        viewModel = ViewModelProviders.of(this, new MyViewModelFactory(getApplication(), id, 0)).get(SwipeMatchViewModel.class);
        adapter = new CardsAdapter(this);
        cardStackView = findViewById(R.id.csv);
        toolbar = findViewById(R.id.toolbar);
        fab = findViewById(R.id.fab);
        layoutManager = new CardStackLayoutManager(this, this);

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> this.onBackPressed());

        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setAdapter(adapter);

        leaveDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you want to leave the room?")
                .setPositiveButton("Leave", (dialog, which) -> {
                    if (host) viewModel.closeRoom();
                    else viewModel.leaveRoom();
                    finish();
                })
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                .setCancelable(true);

        roomClosedDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Session ended")
                .setNeutralButton("Ok", (dialog, which) -> finish())
                .setCancelable(false);

        fab.setOnClickListener(v -> cardStackView.rewind());

        viewModel.getMovies().observe(this, movies -> adapter.setMovies(movies.getMovies()));

        viewModel.getUsers().observe(this, i -> users = i + 1);

        viewModel.getSwiped().observe(this, map -> {
            if (map != null) {
                swiped = map;
                if (host) {
                    for (HashMap<String, Long> movie : map.values()) {
                        if (movie.size() == users)
                            viewModel.match(((Long) movie.values().toArray()[0]).intValue());
                    }
                }

            }
        });
        CustomProgressDialog dialog = new CustomProgressDialog(this, "Waiting for host to set event date");

        if (!host)
            viewModel.getActive().observe(this, active -> {
                if (active == null) roomClosedDialog.show();
                else if(!active){
                    dialog.show();
                }
                else {
                    dialog.dismiss();
                    if(!host){
                        Toast.makeText(this, "Host chose to keep swiping", Toast.LENGTH_SHORT).show();
                    }
                }
            });


        matchDialog = new Dialog(this, R.style.PauseDialog);
        matchDialog.setContentView(R.layout.dialog_match);
        matchDialog.setCancelable(false);
        TextView title = matchDialog.findViewById(R.id.title);
        TextView keepSwiping = matchDialog.findViewById(R.id.keep_swiping);
        ImageView img = matchDialog.findViewById(R.id.img);
        MaterialButton setEvent = matchDialog.findViewById(R.id.add_event);

        if(!host){
            keepSwiping.setVisibility(View.GONE);
            setEvent.setVisibility(View.GONE);
        }

        keepSwiping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.clearMatch();
            }
        });

        setEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                viewModel.setActive(false);
                Intent intent = new Intent(SwipeMatchActivity.this, AddEventActivity.class);
                intent.putExtra("roomId", id).putExtra("movieName", title.getText().toString());
                closeRoom = false;
                startActivity(intent);
            }
        });

        matchDialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        viewModel.getMatch().observe(this, new Observer<Integer>() {
            @Override
            public void onChanged(Integer integer) {
                if (integer != null && integer != 0) {
                    Movies.Movie movie = null;

                    for (Movies.Movie m : adapter.getMovies()) {
                        if (m.getId().equals(integer)) {
                            movie = m;
                            break;
                        }
                    }

                    Glide.with(SwipeMatchActivity.this).load(Utils.IMAGE_URL + movie.getPosterPath()).centerCrop().into(img);
                    title.setText(movie.getTitle());
                    matchDialog.show();

                }
                else
                    matchDialog.dismiss();
            }
        });



    }

    private void handleLeave() {


    }


    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (layoutManager.getTopPosition() == adapter.getMovies().size() - 3) {
            viewModel.newRequest();
        }

        int id = adapter.getMovie(layoutManager.getTopPosition() - 1).getId();

        if (direction == Direction.Right) {
            viewModel.swipeRight(id);

        } else if (direction == Direction.Left)
            viewModel.swipeLeft(id);
    }

    @Override
    public void onCardRewound() {

    }

    @Override
    public void onCardCanceled() {

    }

    @Override
    public void onCardAppeared(View view, int position) {

    }

    @Override
    public void onCardDisappeared(View view, int position) {

    }

    @Override
    public void onBackPressed() {
        leaveDialog.setMessage(host ? "All participants will be kicked from the session" : null);
        leaveDialog.show();
    }

    @Override
    protected void onResume() {
        closeRoom = true;
        viewModel.setActive(true);
        super.onResume();
    }

    @Override
    protected void onStop() {
        if(host && closeRoom)
            viewModel.deleteRoom();
        super.onStop();
    }
}