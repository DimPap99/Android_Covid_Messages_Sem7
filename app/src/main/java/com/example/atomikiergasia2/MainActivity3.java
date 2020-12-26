package com.example.atomikiergasia2;

import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;

import static com.example.atomikiergasia2.MainActivity.SHARED_PREFS;

public class MainActivity3 extends AppCompatActivity implements LocationListener {
    SQLiteDatabase db;
    public List<String> messages;
    public List<String> codes;
    public ListView listView;
    TextView full_name_txt;
    Cursor cursor;
    LocationManager locationManager;
    public String email;
    public String password;
    public String full_name ;
    public String address ;
    public static int SUCCESFUL_EDIT = 999;
    public static int EDIT_ACTIVITY = 998;
    public static int BACK = 1000;
    public static int CREATE_MESSAGE_ACTIVITY = 1001;
    public static int SUCCESFUL_MESSAGE_CREATION = 1002;
    public static String last_selected_code = "";

    SharedPreferences preferences;
    FirebaseDatabase database_fb;
    public static String user_uid;
    DatabaseReference myRef;
    public static double longtitude;
    public static double latitude;
    public static long timestamp;


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

public void populate_listview() {
    codes = new ArrayList<String>();
    messages = new ArrayList<String>();
    cursor = db.rawQuery("SELECT * FROM Messages", null);
    if (cursor.getCount() > 0) {
        while (cursor.moveToNext()) {
            messages.add(String.valueOf(cursor.getString(0)));
            codes.add(String.valueOf(cursor.getString(1)));

        }
        listView.setAdapter(new CovidMsgAdapter(getApplicationContext(), codes, messages));
    }
}
//public void getMessagesAndCodes(){
//
//}

    public void delete(View view){
        if(last_selected_code != ""){
            Toast.makeText(getApplicationContext(),last_selected_code,Toast.LENGTH_LONG).show();
            String table = "Messages";
            String whereClause = "code=?";
            String[] whereArgs = new String[] { last_selected_code };
            db.delete(table, whereClause, whereArgs);
            //db.delete("Messages", "code" + "=" + last_selected_code, null);
            startActivity(getIntent());
        }

    }

//initialize
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        Bundle extras = getIntent().getExtras();
        email = extras.getString("email");
        user_uid = extras.getString("user_id");
        Toast.makeText(getApplicationContext(), user_uid,Toast.LENGTH_LONG).show();
        full_name_txt = findViewById(R.id.textView5);
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE); //get shared prefs
        listView = findViewById(R.id.listview);
        database_fb = FirebaseDatabase.getInstance();
        myRef = database_fb.getReference("Users/" + user_uid);


        if(preferences.contains("DB_NAME")){
            //open db
        db = openOrCreateDatabase(preferences.getString("DB_NAME",""), Context.MODE_PRIVATE, null);
        db.execSQL("CREATE TABLE IF NOT EXISTS Messages(code INTEGER PRIMARY KEY ,message TEXT)");
        setInfo();
        String temp_txt = full_name_txt.getText().toString();
        full_name_txt.setText(temp_txt +  " " + full_name);}
        else{
            full_name_txt.setText("ERROR. No Shared Prefs");
        }
        populate_listview();
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                // selected item
                String code = ((TextView) view.findViewById(R.id.code)).getText().toString();
                last_selected_code =code;


            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //if the user succesfully edited something
        if(resultCode == SUCCESFUL_EDIT && requestCode == EDIT_ACTIVITY){
            setInfo();
            //change the name in case it's been edited
            full_name_txt.setText("Welcome Back: " + full_name);}
        else if(resultCode == SUCCESFUL_MESSAGE_CREATION && requestCode == CREATE_MESSAGE_ACTIVITY){
            populate_listview();
            //change the name in case it's been edited
            }


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

    public void create_new_msg(View view){
        Intent intent = new Intent(getApplicationContext(), CreateMessage_Activity.class);
        startActivityForResult(intent, CREATE_MESSAGE_ACTIVITY);
    }



    //the method that the activate button uses to activate the location manager
    public void gps(View view) {
//check the permissions we have written on the manifest xml that have to do with the Location Manager (ACCESS_FINE_LOCATION, etc...
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.
                    requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},234);
            return;
        }
        //activate the location manager. minTime,minDist = 0 to give updateds constantly
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0,  this);}

    //standard location manager methods
    @SuppressLint("ResourceAsColor")
    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onLocationChanged(Location location) {
        longtitude = location.getLongitude();
        latitude = location.getLatitude();
        timestamp = System.currentTimeMillis()/1000;
    }
    //standard location manager methods
    @Override
    public void onStatusChanged(String s, int i, Bundle bundle) {

    }

    @Override
    public void onProviderEnabled(String s) {

    }

    @Override
    public void onProviderDisabled(String s) {

    }

    public void sendMessage(){
        //send the message
        //then save it
    }

}