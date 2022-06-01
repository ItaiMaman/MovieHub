package com.example.moviepicker;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.moviepicker.Rooms.SwipeMatchActivity;
import com.example.moviepicker.Rooms.SwipeMatchViewModel;
import com.google.android.material.textfield.TextInputEditText;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class AddEventActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView movieNameTv, friends;
    TextInputEditText time, date;
    String roomId, movieName, timeString, dateString;
    AddEventViewModel viewModel;
    TimePickerDialog timePicker;
    DatePickerDialog datePicker;

    public AddEventActivity() {
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_event);

        viewModel = ViewModelProviders.of(this).get(AddEventViewModel.class);
        movieNameTv = findViewById(R.id.movie_name);
        friends = findViewById(R.id.friends);
        toolbar = findViewById(R.id.toolbar);
        time = findViewById(R.id.time);
        date = findViewById(R.id.date);

        roomId = getIntent().getStringExtra("roomId");
        movieName = getIntent().getStringExtra("movieName");

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        movieNameTv.setText(movieName);

        viewModel.getUserNames(roomId).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                String tmp = strings.size() > 1? getString(R.string.friends) : getString(R.string.friends).substring(0, getString(R.string.friends).length()-1);
                tmp += " " + prettyPrint(strings);

                friends.setText(tmp);
            }
        });

        Calendar c = Calendar.getInstance();

        timePicker = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                timeString = hourOfDay + ":" + minute;
                time.setText(timeString);
            }
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), true);

        datePicker = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                dateString = dayOfMonth + "/" + (month+1) + "/" + year;
                date.setText(dateString);
            }
        },c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


        time.setOnClickListener(v -> timePicker.show());

        date.setOnClickListener(v -> datePicker.show());

    }

    private String prettyPrint(List<String> strings) {
        String tmp = "";
        for(int i = 0; i < strings.size(); i++){
            tmp += strings.get(i);
            if(strings.size() - i > 2)
                tmp += ", ";
            else if(i< strings.size() -1)
                tmp += " and ";
        }
        return tmp;
    }
}