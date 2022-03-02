package com.example.clothshop;

import android.content.Intent;
import android.database.Cursor;
import android.view.Gravity;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class CartActivity extends AppCompatActivity implements View.OnClickListener{

    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToMain:
                Intent intent = new Intent(this, CartActivity.class);
                startActivity(intent);
                break;
            case R.id.btnOrder:
                break;
        }
    }
}
