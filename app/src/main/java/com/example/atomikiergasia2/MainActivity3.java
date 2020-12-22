package com.example.atomikiergasia2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class MainActivity3 extends AppCompatActivity {
    SQLiteDatabase db;
    public List<String> messages;
    public List<String> codes;
    public ListView listView;
    TextView full_name_txt;
    Cursor cursor;
    public String email;
    public String password;
    public String username ;
    public String address ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        full_name_txt = findViewById(R.id.textView5);


        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
        cursor = db.rawQuery("SELECT * FROM User  WHERE  email='"+email+"'", null);
        if (cursor.getCount() > 0) {
            while (cursor.moveToNext()) {
                email = String.valueOf(cursor.getString(0));
                password = String.valueOf(cursor.getString(1));
                username = String.valueOf(cursor.getString(2));
                address = String.valueOf(cursor.getString(3));
            }}
        String temp_txt = full_name_txt.getText().toString();
        full_name_txt.setText(temp_txt +  " " + username);

    }
}