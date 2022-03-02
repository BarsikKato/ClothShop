package com.example.clothshop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnSignin;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        btnSignin = (Button) findViewById(R.id.buttSignIn);
        btnSignin.setOnClickListener(this);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttSignIn:
                Intent intent = new Intent(this, AdminActivity.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }
}
