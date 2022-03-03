package com.example.clothshop;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import java.util.Optional;

public class CartActivity extends AppCompatActivity implements View.OnClickListener{
    EditText etName, etAddress, etPhone;
    Button btnBackToMain, btnOrder;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        etName = findViewById(R.id.editTextTextPersonName);
        etAddress = findViewById(R.id.editTextTextPostalAddress);
        etPhone = findViewById(R.id.editTextPhone);

        btnBackToMain = (Button) findViewById(R.id.btnBackToMain);
        btnBackToMain.setOnClickListener(this);

        btnOrder = (Button) findViewById(R.id.btnOrder);
        btnOrder.setOnClickListener(this);

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
                String id = cursor.getString(idIndex);
                if(ShopItems.goodsInCart.contains(id))
                {
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

                    Button buttonDelete = new Button(this);
                    buttonDelete.setOnClickListener(this);
                    params.weight = 1.0f;
                    buttonDelete.setLayoutParams(params);
                    buttonDelete.setText("Убрать");
                    buttonDelete.setTag(cursor.getString(idIndex));
                    buttonDelete.setId(cursor.getInt(idIndex));
                    dbOutputRow.addView(buttonDelete);

                    dbOutput.addView(dbOutputRow);
                }

            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnBackToMain:
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
                break;
            case R.id.btnOrder:
                contentValues = new ContentValues();
                String name = etName.getText().toString();
                String address = etAddress.getText().toString();
                String phone = etPhone.getText().toString();
                contentValues.put(DBHelper.KEY_USERNAME, name);
                contentValues.put(DBHelper.KEY_USERADDRESS, address);
                contentValues.put(DBHelper.KEY_USERPHONE, phone);
                long addedID = database.insert(DBHelper.TABLE_ORDERS, null, contentValues);
                for(int i = 1; i <= ShopItems.goodsInCart.size(); i++)
                {
                    contentValues = new ContentValues();
                    String order = Long.toString(addedID);
                    String good = ShopItems.goodsInCart.get(i);
                    contentValues.put(DBHelper.KEY_OORDERID, order);
                    contentValues.put(DBHelper.KEY_GOODID, good);
                    database.insert(DBHelper.TABLE_ORDERGOODS, null, contentValues);
                    ShopItems.goodsInCart.remove(good);
                }
                UpdateTable();
                etName.setText("");
                etAddress.setText("");
                etPhone.setText("");
                break;
            default:
                String id = v.getTag().toString();
                ShopItems.goodsInCart.remove(id);
                UpdateTable();
                break;
        }
    }
}
