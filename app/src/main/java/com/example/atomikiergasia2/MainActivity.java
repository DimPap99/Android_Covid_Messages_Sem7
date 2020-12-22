package com.example.atomikiergasia2;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserInfo;

public class MainActivity extends AppCompatActivity {

    public static boolean created_edittexts = false; //monitor whether we ve dynamically created edittexts on Login/Register
    Button register_b;
    Button login_b;
    EditText email;
    EditText password;
    private FirebaseAuth mAuth;
    FirebaseUser currentUser;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        login_b = findViewById(R.id.button);
        register_b = findViewById(R.id.button3);
        email = findViewById(R.id.editTextTextPersonName);
        password = findViewById(R.id.editTextTextPersonName2);
        mAuth = FirebaseAuth.getInstance();
    }


    public void register(View view){
        Intent intent = new Intent(view.getContext(), MainActivity2.class);
        startActivity(intent);
    }

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
                            //Toast.makeText(getApplicationContext(),email.getText().toString(),Toast.LENGTH_LONG).show();
                            startActivity(intent);
                        }else {
                            Toast.makeText(getApplicationContext(),
                                    task.getException().getMessage(),Toast.LENGTH_LONG).show();
                        }
                    }
                });


    }






}