package com.example.clothshop;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.util.Log;
import android.view.Gravity;
import android.view.View;

import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.clothshop.databinding.ActivityMainBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{
    Button btnOpenDatabase, btnCart;
    DBHelper dbHelper;
    SQLiteDatabase database;
    int sum = 0;
    TextView sumInCart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sumInCart = findViewById(R.id.sumInCart);

        btnOpenDatabase = (Button) findViewById(R.id.btnOpenDatabase);
        btnOpenDatabase.setOnClickListener(this);

        btnCart = (Button) findViewById(R.id.btnCart);
        btnCart.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        database = dbHelper.getWritableDatabase();

        UpdateTable();

    }
    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_GOODS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idIndex = cursor.getColumnIndex(DBHelper.KEY_ID);
            int nameIndex = cursor.getColumnIndex(DBHelper.KEY_NAME);
            int priceIndex = cursor.getColumnIndex(DBHelper.KEY_PRICE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);

                dbOutputRow.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                TextView outputID = new TextView(this);
                params.weight = 1.0f;
                outputID.setLayoutParams(params);
                outputID.setText(cursor.getString(idIndex));
                dbOutputRow.addView(outputID);

                TextView outputName = new TextView(this);
                params.weight = 3.0f;
                outputName.setLayoutParams(params);
                outputName.setText(cursor.getString(nameIndex));
                dbOutputRow.addView(outputName);

                TextView outputPrice = new TextView(this);
                params.weight = 3.0f;
                outputPrice.setLayoutParams(params);
                outputPrice.setText(cursor.getString(priceIndex));
                dbOutputRow.addView(outputPrice);

                Button btnToCart = new Button(this);
                btnToCart.setOnClickListener(this);
                params.weight = 1.0f;
                btnToCart.setLayoutParams(params);
                btnToCart.setText("Добавить в корзину");
                btnToCart.setTag("cart");
                btnToCart.setId(cursor.getInt(idIndex));
                dbOutputRow.addView(btnToCart);


                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }
    public void ChangeSum(int sumToAdd)
    {
        sum = sum + sumToAdd;
        sumInCart.setText("Сумма заказа: "  + sum);
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnOpenDatabase:
                Intent intentDB = new Intent(this, AuthActivity.class);
                startActivity(intentDB);
                break;
            case R.id.btnCart:
                Intent intentCart = new Intent(this, CartActivity.class);
                startActivity(intentCart);
                break;
            default:
                Cursor cursorUpdater = database.query(DBHelper.TABLE_GOODS, null, null, null, null, null, null);
                cursorUpdater.move(v.getId());
                int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                String priceKek = cursorUpdater.getString(priceIndex);
                ChangeSum(Integer.parseInt(priceKek));
                break;
        }
    }
}