package com.example.musicapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {
    EditText username , password;
    Button btnLogin;
    TextView txt;
    DBHelper mydb;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        username = findViewById(R.id.usernameform);
        password = findViewById(R.id.passwordform);
        btnLogin = findViewById(R.id.btnloginform);
        mydb = new DBHelper(this);
        btnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            // loginButton
            case R.id.btnloginform:
                String user = username.getText().toString();
                String pass = password.getText().toString();
                if (user.equals("") || pass.equals("")) {
                    Toast.makeText(this, " Pls fill all fields", Toast.LENGTH_SHORT).show();
                } else {
                    Boolean result = mydb.checkuserspassword(user, pass);
                    if (result == true) {
                        Toast.makeText(this, "Welcome " + user, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(getApplicationContext(), song_playing.class);
                        startActivity(intent);
                    } else {
                        Toast.makeText(this, "Invalid userName OR password!!", Toast.LENGTH_SHORT).show();
                    }
                 }
        }
    }
}