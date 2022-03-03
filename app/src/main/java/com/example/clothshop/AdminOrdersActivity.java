package com.example.clothshop;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class AdminOrdersActivity extends AppCompatActivity implements View.OnClickListener {
    Button btnAdd, btnClear, btnShop, btnOrders;
    DBHelper dbHelper;
    SQLiteDatabase database;
    ContentValues contentValues;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_adminorders);

        btnClear = (Button) findViewById(R.id.btnClear);
        btnClear.setOnClickListener(this);

        btnShop = (Button) findViewById(R.id.btnShop);
        btnShop.setOnClickListener(this);

        btnOrders = (Button) findViewById(R.id.btnOrders);
        btnOrders.setOnClickListener(this);

        dbHelper = new DBHelper(this);

        database = dbHelper.getWritableDatabase();
        UpdateTable();
    }

    public void UpdateTable()
    {
        Cursor cursor = database.query(DBHelper.TABLE_ORDERS, null, null, null, null, null, null);

        if (cursor.moveToFirst()) {
            int idOrder = cursor.getColumnIndex(DBHelper.KEY_ORDERID);
            int idUsername = cursor.getColumnIndex(DBHelper.KEY_USERNAME);
            int idAddress = cursor.getColumnIndex(DBHelper.KEY_USERADDRESS);
            int idPhone = cursor.getColumnIndex(DBHelper.KEY_USERPHONE);
            TableLayout dbOutput = findViewById(R.id.dbOutput);
            dbOutput.removeAllViews();
            do{
                TableRow dbOutputRow = new TableRow(this);

                dbOutputRow.setLayoutParams(new ViewGroup.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT));
                TableRow.LayoutParams params = new TableRow.LayoutParams(TableRow.LayoutParams.WRAP_CONTENT, TableLayout.LayoutParams.WRAP_CONTENT);

                TextView outputOrderId = new TextView(this);
                params.weight = 1.0f;
                outputOrderId.setLayoutParams(params);
                outputOrderId.setText(cursor.getString(idOrder));
                dbOutputRow.addView(outputOrderId);

                TextView outputUsername = new TextView(this);
                params.weight = 3.0f;
                outputUsername.setLayoutParams(params);
                outputUsername.setText(cursor.getString(idUsername));
                dbOutputRow.addView(outputUsername);

                TextView outputAddress = new TextView(this);
                params.weight = 3.0f;
                outputAddress.setLayoutParams(params);
                outputAddress.setText(cursor.getString(idAddress));
                dbOutputRow.addView(outputAddress);

                TextView outputPhone = new TextView(this);
                params.weight = 3.0f;
                outputPhone.setLayoutParams(params);
                outputPhone.setText(cursor.getString(idPhone));
                dbOutputRow.addView(outputPhone);

                TextView outputList = new TextView(this);
                params.weight = 3.0f;
                outputList.setLayoutParams(params);
                outputList.setText(GetOrderGoods(cursor.getString(idOrder)));
                dbOutputRow.addView(outputList);

                Button buttonDelete = new Button(this);
                buttonDelete.setOnClickListener(this);
                params.weight = 1.0f;
                buttonDelete.setLayoutParams(params);
                buttonDelete.setText("Отменить");
                buttonDelete.setTag("delete");
                buttonDelete.setId(cursor.getInt(idOrder));
                dbOutputRow.addView(buttonDelete);

                dbOutput.addView(dbOutputRow);
            }
            while (cursor.moveToNext());
        } else
            Log.d("mLog","0 rows");

        cursor.close();
    }

    public String GetOrderGoods(String orderId)
    {
        String orderedGoodsList = "";
        Cursor c = database.rawQuery("SELECT goodid FROM ordergoods WHERE orderid = " + orderId, null);
        if (c.moveToFirst())
        {
            do
            {
                String goodId = c.getString(0);
                Cursor c2= database.rawQuery("SELECT name FROM goods WHERE _id = " + 5, null);
                if (c2.moveToFirst())
                {
                    do
                    {
                        String goodName = c.getString(0);
                        orderedGoodsList += goodName + "; ";
                    }
                    while(c2.moveToNext());
                }
                c2.close();
            }
            while(c.moveToNext());
        }
        c.close();
        return orderedGoodsList;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId())
        {
            default:
                if(v.getTag() == "delete") {
                    View outputDBRow = (View) v.getParent();
                    ViewGroup outputDB = (ViewGroup) outputDBRow.getParent();
                    outputDB.removeView(outputDBRow);
                    outputDB.invalidate();
                    database.delete(dbHelper.TABLE_GOODS, dbHelper.KEY_ID + " = ?", new String[]{String.valueOf(v.getId())});

                    contentValues = new ContentValues();
                    Cursor cursorUpdater = database.query(DBHelper.TABLE_GOODS, null, null, null, null, null, null);
                    if (cursorUpdater.moveToFirst()) {
                        int idIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_ID);
                        int nameIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_NAME);
                        int priceIndex = cursorUpdater.getColumnIndex(DBHelper.KEY_PRICE);
                        int realID = 1;
                        do {
                            if (cursorUpdater.getInt(idIndex) > realID) {
                                contentValues.put(dbHelper.KEY_ID, realID);
                                contentValues.put(dbHelper.KEY_NAME, cursorUpdater.getString(nameIndex));
                                contentValues.put(dbHelper.KEY_PRICE, cursorUpdater.getString(priceIndex));
                                database.replace(dbHelper.TABLE_GOODS, null, contentValues);
                            }
                            realID++;
                        }
                        while (cursorUpdater.moveToNext());
                        if (cursorUpdater.moveToLast()) {
                            if (cursorUpdater.moveToLast() && v.getId() != realID) {
                                database.delete(dbHelper.TABLE_GOODS, dbHelper.KEY_ID + " = ?", new String[]{cursorUpdater.getString(idIndex)});
                            }
                        }
                        UpdateTable();
                    }
                }

                break;
        }
    }
}
