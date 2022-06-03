package com.example.moviehub.startScreens;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.example.moviehub.FirebaseSource;
import com.example.moviehub.R;
import com.example.moviehub.home.MainActivity;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpScreen extends AppCompatActivity {
    EditText et_username, et_email, et_password, et_confirm;
    Button btn_create;
    FirebaseAuth firebaseAuth;
    ProgressDialog progressDialog;
    String email, password, confirm, username;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up_screen);
        firebaseAuth = FirebaseAuth.getInstance();

        et_username = findViewById(R.id.et_username);
        et_email = findViewById(R.id.et_email);
        et_password = findViewById(R.id.et_password);
        et_confirm = findViewById(R.id.et_confirm);
        btn_create = findViewById(R.id.btn_create);
        progressDialog = new ProgressDialog(this);

        btn_create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                username = et_username.getText().toString();
                email = et_email.getText().toString();
                password = et_password.getText().toString();
                confirm = et_confirm.getText().toString();

                if (validEmail(email) && validPassword(password) && passwordsMatch(password, confirm)) {
                    registerUserToFirebase(email, password);
                }
                else if (!validEmail(email)) {
                    mySnackBar("Enter a valid email address");
                }
                else if (!validPassword(password)) {
                    mySnackBar("Enter a valid password. Password must be more than 8 characters");
                }
                else if (!passwordsMatch(password, confirm)) {
                    mySnackBar("Passwords do not match");
                }
            }
        });
    }

    private void registerUserToFirebase(String email, String password) {
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(SignUpScreen.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){

                    new FirebaseSource().setUsername(username).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.putExtra("snackbar", "User created successfully");
                            startActivity(intent);

                        }
                    });


                }
                else{
                    mySnackBar(task.getException().getMessage());
                }
            }
        });


    }

    private boolean validEmail(String email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    private boolean validPassword(@NonNull String password) {
        return !password.isEmpty() && password.length() >= 8;
    }

    private boolean passwordsMatch(String password, @NonNull String confirm) {
        return confirm.equals(password);
    }

    public void mySnackBar(String message) {
        Snackbar snackbar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG);
        snackbar.show();
    }


}