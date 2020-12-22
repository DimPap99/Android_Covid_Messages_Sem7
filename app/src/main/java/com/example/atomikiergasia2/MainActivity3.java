package com.example.atomikiergasia2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import static com.example.atomikiergasia2.MainActivity.SHARED_PREFS;

public class MainActivity3 extends AppCompatActivity {
    SQLiteDatabase db;
    public List<String> messages;
    public List<String> codes;
    public ListView listView;
    TextView full_name_txt;
    Cursor cursor;
    public String email;
    public String password;
    public String full_name ;
    public String address ;
    public static int SUCCESFUL_EDIT = 999;
    public static int EDIT_ACTIVITY = 998;
    public static int BACK = 1000;
    SharedPreferences preferences;

    public Button go_edit;
//a function that queries the db in order to assign to email/pass/... vars
public void setInfo(){
    cursor = db.rawQuery("SELECT * FROM "+preferences.getString("USER_TABLE","")+"  WHERE  email='"+email+"'", null);

    if (cursor.getCount() > 0) {
        while (cursor.moveToNext()) {
            email = String.valueOf(cursor.getString(0));
            password = String.valueOf(cursor.getString(1));
            full_name = String.valueOf(cursor.getString(2));
            address = String.valueOf(cursor.getString(3));
        }}
}

//initialize
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        full_name_txt = findViewById(R.id.textView5);
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //get shared prefs

        if(preferences.contains("DB_NAME")){
            //open db
        db = openOrCreateDatabase(preferences.getString("DB_NAME",""), Context.MODE_PRIVATE, null);
        setInfo();
        String temp_txt = full_name_txt.getText().toString();
        full_name_txt.setText(temp_txt +  " " + full_name);}
        else{
            full_name_txt.setText("ERROR. No Shared Prefs");
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the user succesfully edited something
        if(resultCode == SUCCESFUL_EDIT && requestCode == EDIT_ACTIVITY){
            setInfo();
            //change the name in case it's been edited
            full_name_txt.setText("Welcome Back: " + full_name);}


    }
// go to edit activity
    public void go_edit(View view){
        Intent intent = new Intent(getApplicationContext(), EditUser_Activity.class);
        //get the required variables into the intent for the edit activity
        intent.putExtra("full_name", full_name);
        intent.putExtra("address", address);
        intent.putExtra("email", email);

        startActivityForResult(intent, EDIT_ACTIVITY);
    }
}