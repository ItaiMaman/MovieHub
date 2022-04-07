package com.example.moviepicker.Rooms;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;

import android.os.Bundle;
import android.view.View;

import com.example.moviepicker.CardsAdapter;
import com.example.moviepicker.Movies;
import com.example.moviepicker.R;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;

public class SwipeMatchActivity extends AppCompatActivity implements CardStackListener {

    CardStackView cardStackView;
    CardStackLayoutManager layoutManager;
    SwipeMatchViewModel viewModel;
    CardsAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_swipe_match);

        String id = getIntent().getStringExtra("id");
        viewModel = new SwipeMatchViewModel(getApplication(), id);
        adapter = new CardsAdapter(this);
        cardStackView = findViewById(R.id.csv);
        layoutManager = new CardStackLayoutManager(this, this);

        cardStackView.setLayoutManager(layoutManager);
        cardStackView.setAdapter(adapter);

        viewModel.getMovies().observe(this, new Observer<Movies>() {
            @Override
            public void onChanged(Movies movies) {
                adapter.setMovies(movies.getMovies());
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
        if(layoutManager.getTopPosition() == adapter.getMovies().size() -3 ){
            viewModel.newRequest();
        }
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
}