package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {
    Button lunch;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lunch = findViewById(R.id.lunch);
        lunch.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.lunch:
                loginActivity();
                Toast.makeText(this, "opening MusicApp!", Toast.LENGTH_SHORT).show();
        }
    }
    private void loginActivity(){
        Intent i  = new Intent(this,RegiserActivity.class);
        startActivity(i);
    }
}


/*

 */