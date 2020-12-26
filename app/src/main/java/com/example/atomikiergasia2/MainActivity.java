package com.example.atomikiergasia2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Parcelable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static boolean created_edittexts = false; //monitor whether we ve dynamically created edittexts on Login/Register
    Button register_b;
    Button login_b;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;
    SharedPreferences preferences;

    FirebaseUser currentUser;
    public static final String SHARED_PREFS = "db_info";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_b = findViewById(R.id.button);
        register_b = findViewById(R.id.button3);
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPersonName2);
        preferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString("MESSAGES_TABLE", "Messages" );
        editor.putString("DB_NAME", "UserDB" );
        editor.putString("USER_TABLE", "User" );

        editor.apply();
        mAuth = FirebaseAuth.getInstance();
    }


    public void register(View view){
        Intent intent = new Intent(view.getContext(), Register_Activity.class);

        startActivity(intent);
    }
//login with the firebase auth. The following code comes from the docs
    public void login(View view){
        mAuth.signInWithEmailAndPassword(
                email.getText().toString(),password.getText().toString())
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()){
                            currentUser = mAuth.getCurrentUser();
                            Toast.makeText(getApplicationContext(),"Login success!",Toast.LENGTH_LONG).show();
                            Intent intent = new Intent(getApplicationContext(),MainActivity3.class);
                            intent.putExtra("email",email.getText().toString());
                            intent.putExtra("user_id", currentUser.getUid().toString());
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }






}