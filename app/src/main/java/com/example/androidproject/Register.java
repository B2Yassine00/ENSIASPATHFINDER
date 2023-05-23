package com.example.androidproject;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.HashMap;
import java.util.Objects;

public class Register extends AppCompatActivity {

    private EditText textusername,textemail,textpassword,textrepassword;
    private Button sign_up;
    private FirebaseAuth mAuth;

    private DatabaseReference mRootRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        Button returnbutton = findViewById(R.id.returnbutton);
        textusername = findViewById(R.id.username);
        textemail = findViewById(R.id.email);
        textpassword = findViewById(R.id.password);
        textrepassword = findViewById(R.id.repassword);
        sign_up = findViewById(R.id.sign_up);

        mRootRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        //En cliquant sur le boutton, en renvoie le client vers la page de connection
        returnbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Register.this,MainActivity.class);
                startActivity(intent);
            }
        });

        sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String username = textusername.getText().toString().trim();
                String email = textemail.getText().toString().trim();
                String password = textpassword.getText().toString().trim();
                String repassword = textrepassword.getText().toString().trim();

                if(username.isEmpty()){
                    textusername.setError("Username is required");
                    textusername.requestFocus();
                }
                if(email.isEmpty()){
                    textemail.setError("Email is required");
                    textemail.requestFocus();
                    return;
                }
                if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
                    textemail.setError("Please enter a valid email");
                    textemail.requestFocus();
                    return;
                }
                if(password.isEmpty()){
                    textpassword.setError("Password is required !");
                    textpassword.requestFocus();
                    return;
                }
                if (password.length() < 8){
                    textpassword.setError("Min password length is 8 characters !");
                    textpassword.requestFocus();
                    return;
                }
                if(!repassword.equals(password)){
                    textrepassword.setError("Password is incorrect");
                    textrepassword.requestFocus();
                }else{
                    mAuth.createUserWithEmailAndPassword(email , password).addOnSuccessListener(authResult -> {
                        HashMap<String , Object> map = new HashMap<>();
                        map.put("Username" , username);
                        map.put("Email", email);
                        map.put("id" , Objects.requireNonNull(mAuth.getCurrentUser()).getUid());
                        map.put("imageurl" , "default");
                        mRootRef.child("Users").child(mAuth.getCurrentUser().getUid()).setValue(map).addOnCompleteListener(task -> {
                            if (task.isSuccessful()){

                            }

                        });
                        Intent intent = new Intent(Register.this , HomePage.class);
                        startActivity(intent);
                        finish();
                    }).addOnFailureListener(e -> {
                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    });
                }
            }
        });

    }
}