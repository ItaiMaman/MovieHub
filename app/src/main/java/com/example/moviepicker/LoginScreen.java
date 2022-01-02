package com.example.moviepicker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class LoginScreen extends AppCompatActivity {
    EditText et_email, et_password;
    TextView tv_forgot, tv_donthaveaccount;
    Button btn_login;
    CheckBox cb_rememberme;
    SharedPreferences sp;

    //todo - login verification

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);

        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_forgot = findViewById(R.id.tv_forgot);
        tv_donthaveaccount = findViewById(R.id.tv_donthaveaccount);
        btn_login = findViewById(R.id.btn_login);
        cb_rememberme = findViewById(R.id.cb_rememberme);

        sp = getSharedPreferences("SavedUser",0);
        String email = sp.getString("email", null);
        Boolean rememberme = sp.getBoolean("rememberme", false);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("email",et_email.getText().toString());
                editor.putBoolean("rememberme",cb_rememberme.isChecked());
                editor.commit();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(intent);
            }
        });

        tv_donthaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);
                startActivity(intent);
            }
        });

    }
}