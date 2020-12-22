package com.example.atomikiergasia2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import static com.example.atomikiergasia2.MainActivity.SHARED_PREFS;

public class EditUser_Activity extends AppCompatActivity {

    EditText fullname;
    SQLiteDatabase db;
    EditText address;
    public String email;
    SharedPreferences preferences;
    public int result_code;
//initialize
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user__activirty);
        fullname = findViewById(R.id.editTextTextPersonName7);
        address = findViewById(R.id.editTextTextPersonName8);
        Bundle extras = getIntent().getExtras();//get the required variables from the prev activity
        fullname.setText(extras.getString("full_name"));
        address.setText(extras.getString("address"));
        email = extras.getString("email");
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //get shared prefs for the db info
        result_code = 1000; //init with 1000 to distinguish between a succesful edit or a back button click

    }

    public void edit(View view){
        //UPDATE the values based on the email
        db = openOrCreateDatabase("UserDB", Context.MODE_PRIVATE, null);
        ContentValues values = new ContentValues();
        values.put("full_name",fullname.getText().toString()); //These Fields should be your String values of actual column names
        values.put("address",address.getText().toString());
        db.update(preferences.getString("USER_TABLE", ""), values, "email = ?", new String[]{email});
        Toast.makeText(getApplicationContext(), "Your info has been edited succesfully!",Toast.LENGTH_LONG).show();
        result_code = 999; //SUCCESUL edit code
    }


    public void back(View view){
        setResult(result_code);//if res code is 1000 when we go back we dont query for the updated data
        finish();
    }




}