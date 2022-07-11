package com.example.musix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class RegisterActivity extends AppCompatActivity {

    private EditText eRegUserName;
    private EditText eRegPassword;
    private Button eRegister;

    public static Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        eRegUserName = findViewById(R.id.etRegUserName);
        eRegPassword = findViewById(R.id.etRegPassword);
        eRegister = findViewById(R.id.btnRegister);
        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String regUsername = eRegUserName.getText().toString();
                String regPassword = eRegPassword.getText().toString();

                if (validate(regUsername, regPassword)) {
                    credentials = new Credentials(regUsername, regPassword);

                    sharedPreferencesEditor.putString("Username", regUsername);
                    sharedPreferencesEditor.putString("Password", regPassword);

                    sharedPreferencesEditor.apply();

                    startActivity(new Intent(RegisterActivity.this, MainActivity.class));
                    Toast.makeText(RegisterActivity.this, "Registration Succesful!!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate(String username, String password) {
        if (username.isEmpty() || password.length() < 8) {
            Toast.makeText(this, "Please enter all the details. Password should be atleast 8 characters.", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}