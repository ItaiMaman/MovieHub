package com.example.moviepicker.Rooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.example.moviepicker.CardsAdapter;
import com.example.moviepicker.Movies;
import com.example.moviepicker.MyViewModelFactory;
import com.example.moviepicker.R;
import com.example.moviepicker.Room;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SwipeMatchActivity extends AppCompatActivity implements CardStackListener {

    CardStackView cardStackView;
    CardStackLayoutManager layoutManager;
    SwipeMatchViewModel viewModel;
    CardsAdapter adapter;
    Toolbar toolbar;
    String id;
    boolean host;
    MaterialAlertDialogBuilder leaveDialog;
    List<Integer> right, left;
    HashMap<String, Integer> swiped;
    int users;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_match);

        id = getIntent().getStringExtra("id");
        host = getIntent().getBooleanExtra("host", false);
        viewModel = ViewModelProviders.of(this, new MyViewModelFactory(getApplication(), id, 0)).get(SwipeMatchViewModel.class);
        adapter = new CardsAdapter(this);
        cardStackView = findViewById(R.id.csv);
        toolbar = findViewById(R.id.toolbar);
        layoutManager = new CardStackLayoutManager(this, this);
        right = new ArrayList<>();
        left = new ArrayList<>();

        toolbar.setNavigationIcon(R.drawable.ic_arrow_back);
        toolbar.setNavigationOnClickListener(v -> this.onBackPressed());

        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setAdapter(adapter);

        leaveDialog = new MaterialAlertDialogBuilder(this)
                .setTitle("Are you sure you want to leave the room?")
                .setPositiveButton("Leave", (dialog, which) -> finish())
                .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss()).setCancelable(true);

        viewModel.getMovies().observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(Movies movies) {
                adapter.setMovies(movies.getMovies());
            }
        });

        viewModel.getUsers().observe(this, i -> users = i);

        viewModel.getSwiped().observe(this, new Observer<HashMap<String, Integer>>() {
            @Override
            public void onChanged(HashMap<String, Integer> map) {
                if (map != null) {
                    swiped = map;
                    if (host) {
                        for (String key : map.keySet()) {
                            if(map.get(key) == users)
                                Log.d("tag", "maaaaaaaaaaaaaaaaatch");
                        }
                    }

                }
            }
        });


        //todo - observe room state
        //todo - make list of swiped movies -> hosts check for match, everyone checks for swiped movies -> notify viewmodel accordingly
        //todo - observe user list in case user leaves and match occurs


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

            if (!right.contains(id)) {
                viewModel.swipeRight(id);
            }
        }
        else if (direction == Direction.Left)
            left.add(id);
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
}