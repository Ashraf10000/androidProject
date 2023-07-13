package com.example.musicapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegiserActivity extends AppCompatActivity {

    EditText username,password,repassword;
    Button btnsignup,btnlogin;
    DBHelper mydb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_regiser);
        username = findViewById(R.id.username);
        password = findViewById(R.id.password);
        repassword = findViewById(R.id.repassword);
        btnlogin = findViewById(R.id.btnlogin);
        btnsignup = findViewById(R.id.btnsignup);

        mydb = new DBHelper(this);

        btnsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String user = username.getText().toString();
                String pass = password.getText().toString();
                String repass = repassword.getText().toString();
                if(user.equals("") || pass.equals("") ||repass.equals(""))
                {
                    Toast.makeText(RegiserActivity.this, "fill all fields", Toast.LENGTH_SHORT).show();
                }else
                {
                    if(pass.equals(repass))
                    {
                        Boolean usercheckresult = mydb.checkusers(user);
                        if(usercheckresult == false)
                        {
                            Boolean regResult = mydb.insertData(user,pass);
                            if(regResult == true)
                            {
                                Toast.makeText(RegiserActivity.this, "Registration successful", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                                startActivity(intent);
                            }
                            else
                            {
                                Toast.makeText(RegiserActivity.this, "Registration failed", Toast.LENGTH_SHORT).show();
                            }
                        }
                        else
                        {
                            Toast.makeText(RegiserActivity.this, "User already exists", Toast.LENGTH_SHORT).show();
                        }
                    }
                    else
                    {
                        Toast.makeText(RegiserActivity.this, "password not matching", Toast.LENGTH_SHORT).show();
                    }

                }
            }
        });
        btnlogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),LoginActivity.class);
                startActivity(intent);
            }
        });

    }
}