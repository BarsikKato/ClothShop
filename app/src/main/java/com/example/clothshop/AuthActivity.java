package com.example.clothshop;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class AuthActivity extends AppCompatActivity implements View.OnClickListener{
    TextView password;
    Button btnSignin, btnBack;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        password = findViewById(R.id.txtPassword);

        btnSignin = (Button) findViewById(R.id.buttSignIn);
        btnSignin.setOnClickListener(this);

        btnBack = (Button) findViewById(R.id.buttBack);
        btnBack.setOnClickListener(this);

    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.buttSignIn:
                if(password.getText().toString().equals("0000"))
                {
                    Intent intent = new Intent(this, AdminActivity.class);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = new Toast(getApplicationContext());
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.setDuration(Toast.LENGTH_LONG);
                    toast.setText("Неверный пароль.");
                    toast.show();
                }
                break;
            case R.id.buttBack:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            default:

                break;
        }
    }
}
