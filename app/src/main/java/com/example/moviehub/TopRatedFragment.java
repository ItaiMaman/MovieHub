package com.example.moviehub;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.moviehub.api.TopRatedViewModel;
import com.yuyakaido.android.cardstackview.CardStackLayoutManager;
import com.yuyakaido.android.cardstackview.CardStackListener;
import com.yuyakaido.android.cardstackview.CardStackView;
import com.yuyakaido.android.cardstackview.Direction;


public class TopRatedFragment extends Fragment implements CardStackListener {

    TopRatedViewModel viewModel;
    CardStackView cardStackView;
    CardStackLayoutManager layoutManager;
    CardsAdapter adapter;

    public TopRatedFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_top_rated, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        viewModel = ViewModelProviders.of(requireActivity()).get(TopRatedViewModel.class);
        cardStackView = view.findViewById(R.id.csv);

        adapter = new CardsAdapter(getContext());
        layoutManager = new CardStackLayoutManager(requireContext(), this);
        layoutManager.setDirections(Direction.HORIZONTAL);
        layoutManager.setCanScrollVertical(false);
        cardStackView.setAdapter(adapter);
        cardStackView.setLayoutManager(layoutManager);



        viewModel.getMovies().observe(getViewLifecycleOwner(), new Observer<Movies>() {
            @Override
            public void onChanged(Movies movies) {
                if(movies != null){
                    adapter.setMovies(movies.getMovies());
                }
            }
        });



    }

    @Override
    public void onCardDragging(Direction direction, float ratio) {

    }

    @Override
    public void onCardSwiped(Direction direction) {
        if (layoutManager.getTopPosition() % 20 == 18)
            viewModel.newRequest();
        if(direction == Direction.Right){
            viewModel.saveMovie(adapter.getMovie(layoutManager.getTopPosition()-1));
        }
        else if(direction == Direction.Left){
            viewModel.deleteMovie(adapter.getMovie(layoutManager.getTopPosition()-1).getId());
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

    public void rewind(){
        cardStackView.rewind();
    }
}