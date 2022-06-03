package com.example.moviehub.profile;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;

import com.example.moviehub.MyBroadcastReceiver;
import com.example.moviehub.R;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

public class AddEventActivity extends AppCompatActivity {

    Toolbar toolbar;
    TextView movieNameTv, friends;
    TextInputEditText time, date;
    String roomId, movieName, timeString, dateString;
    AddEventViewModel viewModel;
    TimePickerDialog timePicker;
    DatePickerDialog datePicker;
    MaterialButton saveEvent;

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
        saveEvent = findViewById(R.id.save_event);

        roomId = getIntent().getStringExtra("roomId");
        movieName = getIntent().getStringExtra("movieName");

        toolbar.setNavigationOnClickListener(v -> onBackPressed());

        movieNameTv.setText(movieName);

        viewModel.getUserNames(roomId).observe(this, new Observer<List<String>>() {
            @Override
            public void onChanged(List<String> strings) {
                String tmp = strings.size() > 1 ? getString(R.string.friends) : getString(R.string.friends).substring(0, getString(R.string.friends).length() - 1);
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
                dateString = dayOfMonth + "/" + (month + 1) + "/" + year;
                date.setText(dateString);
            }
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));


        time.setOnClickListener(v -> timePicker.show());

        date.setOnClickListener(v -> datePicker.show());

        saveEvent.setOnClickListener(v -> {
            if(!time.getText().toString().isEmpty() && !date.getText().toString().isEmpty()){
                String stime = time.getText().toString() + " " + date.getText().toString();
                try {
                    onBackPressed();
                    SimpleDateFormat parser = new SimpleDateFormat("HH:mm dd/MM/yyyy");
                    parser.setTimeZone(TimeZone.getTimeZone("GMT"));
                    Date date = parser.parse(stime);

                    viewModel.setDate(roomId, date);
                } catch (ParseException e) {

                    e.printStackTrace();
                }
            }
        });

    }

    private String prettyPrint(List<String> strings) {
        String tmp = "";
        for (int i = 0; i < strings.size(); i++) {
            tmp += strings.get(i);
            if (strings.size() - i > 2)
                tmp += ", ";
            else if (i < strings.size() - 1)
                tmp += " and ";
        }
        return tmp;
    }


}