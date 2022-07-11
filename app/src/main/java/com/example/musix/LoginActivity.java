package com.example.musix;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.w3c.dom.Text;

public class LoginActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;
    private TextView eRegister;
    private CheckBox eRememberMe;

    boolean isValid = false;
    private int counter = 5;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        eName = findViewById(R.id.etUserName);
        ePassword = findViewById(R.id.etPassword);
        eLogin = findViewById(R.id.btnLogin);
        eAttemptsInfo = findViewById(R.id.tvAttemptsInfo);
        eRegister = findViewById(R.id.tvRegister);
        eRememberMe = findViewById(R.id.cbRememberMe);

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if (sharedPreferences != null){
            String savedUsername = sharedPreferences.getString("Username", "");
            String savedPassword = sharedPreferences.getString("Password", "");

            RegisterActivity.credentials = new Credentials(savedUsername, savedPassword);

            if (sharedPreferences.getBoolean("RememberMeCheckbox", false)) {
                eName.setText(savedUsername);
                ePassword.setText(savedPassword);
                eRememberMe.setChecked(true);
            }
        }

        eRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        eLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String inputName = eName.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if (inputName.isEmpty() || inputPassword.isEmpty()) {
                    Toast.makeText(LoginActivity.this, "Please enter all details correctly!", Toast.LENGTH_SHORT).show();
                } else {
                    isValid = validate(inputName, inputPassword);

                    if (!isValid) {

                        counter--;

                        Toast.makeText(LoginActivity.this, "Incorrect credentials entered!", Toast.LENGTH_SHORT).show();

                        eAttemptsInfo.setText("Number of attempts remaining: " + counter);

                        if (counter == 0) {
                            eLogin.setEnabled(false);
                        }
                    } else {

                        Toast.makeText(LoginActivity.this, "Login Successful!", Toast.LENGTH_SHORT).show();

                        sharedPreferencesEditor.putBoolean("RememberMeCheckbox", eRememberMe.isChecked());
                        sharedPreferencesEditor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);
                    }
                }
            }
        });
    }

    private boolean validate(String name, String password) {

        if (RegisterActivity.credentials != null) {
            if (name.equals(RegisterActivity.credentials.getUsername()) && password.equals(RegisterActivity.credentials.getPassword())) {
                return true;
            }
        }
        return false;
    }
}