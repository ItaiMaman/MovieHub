package com.example.moviepicker;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginScreen extends AppCompatActivity {
    EditText et_email, et_password;
    TextView tv_forgot, tv_donthaveaccount;
    Button btn_login;
    FirebaseAuth firebaseAuth;
    Dialog d;
    EditText et_dialogemail;
    Button btn_dialogsend;
    ProgressDialog progressDialog;
    String email, password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_screen);
        firebaseAuth = FirebaseAuth.getInstance();

        progressDialog = new ProgressDialog(this);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        tv_forgot = findViewById(R.id.tv_forgot);
        tv_donthaveaccount = findViewById(R.id.tv_donthaveaccount);
        btn_login = findViewById(R.id.btn_login);

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                email = et_email.getText().toString();
                password = et_password.getText().toString();

                if(validEmail(email) && validPassword(password)){
                    progressDialog.setMessage("Logging in...");
                    progressDialog.show();
                    firebaseAuth.signInWithEmailAndPassword(email, password)
                            .addOnCompleteListener(LoginScreen.this, new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    progressDialog.dismiss();
                                    if (task.isSuccessful()){
                                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                        startActivity(intent);
                                    }
                                    else{
                                        mySnackbar(task.getException().getMessage());
                                    }
                                }
                            });
                }
                else if(!validEmail(email)){
                    progressDialog.dismiss();
                    mySnackbar("Invalid email");
                }
                else if(!validPassword(password)){
                    progressDialog.dismiss();
                    mySnackbar("Invalid password");
                }
            }
        });

        tv_donthaveaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), SignUpScreen.class);
                startActivity(intent);
            }
        });

        tv_forgot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                createForgotDialog();
            }
        });

    }

    private void createForgotDialog() {
        d = new Dialog(this);
        d.setContentView(R.layout.forgot_dialog);
        d.setTitle("Reset Password");
        d.setCancelable(true);
        et_dialogemail = d.findViewById(R.id.et_dialogemail);
        btn_dialogsend = d.findViewById(R.id.btn_dialogsend);
        d.show();

        btn_dialogsend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.setMessage("Sending...");
                progressDialog.show();
                email = et_dialogemail.getText().toString();

                if(validEmail(email)){
                    firebaseAuth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            progressDialog.dismiss();
                            if(task.isSuccessful()){
                                d.dismiss();
                                mySnackbar("Email sent");
                            }
                            else{
                                mySnackbar(task.getException().getMessage());
                            }
                        }
                    });

                }
                else{
                    mySnackbar("Enter a valid email address");
                }
            }
        });
    }


    private boolean validEmail(String email){
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validPassword(String password){
        boolean valid = true;
        if(password.isEmpty() || password.length()<8) valid = false;
        return valid;
    }

    public void mySnackbar(String message){
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }
}