package com.example.atomikiergasia2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import static com.example.atomikiergasia2.MainActivity.SHARED_PREFS;

public class CreateMessage_Activity extends AppCompatActivity {

    TextView codetxt;
    TextView msgtxt;
    SharedPreferences preferences;
    SQLiteDatabase db;
    public int result_code;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_message_2);
        codetxt = findViewById(R.id.editTextTextPersonName9);
        msgtxt = findViewById(R.id.editTextTextPersonName10);
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //get shared prefs for the db info
        db = openOrCreateDatabase(preferences.getString("DB_NAME",""), Context.MODE_PRIVATE, null);
        result_code = 1000;


    }



    public void create_message(View view){
        boolean success = false;
        if( checkValues() == true) {
            try{
            int code = Integer.parseInt(codetxt.getText().toString());
            String message = msgtxt.getText().toString();
            ContentValues insertValues = new ContentValues();
            insertValues.put("code", code);
            insertValues.put("message", message);
            db.insert(preferences.getString("MESSAGES_TABLE",""), null, insertValues);
            success = true;
            }
            catch (NumberFormatException | SQLException e){ //catch sql exception in case the user gives a code that is already registered in the db
                Toast.makeText(getApplicationContext(),e.toString(),Toast.LENGTH_LONG).show();
                success = false;
                if( e instanceof SQLException ){
                    Toast.makeText(getApplicationContext(),"The message code you provided already exists!",Toast.LENGTH_LONG).show();
                }
            }

        }
        if(success == true){
            Toast.makeText(getApplicationContext(),"The message was created succesfully!",Toast.LENGTH_LONG).show();
            result_code = 1002;
        }
    }

    public void back(View view){
        setResult(result_code);//if res code is 1000 when we go back we dont query for the updated data
        finish();
    }


    public boolean checkValues(){
        if(codetxt.getText().toString().equals("") || msgtxt.getText().toString().equals("")){
            return false;
        }else return true;

    }
}