package com.example.userlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class Registration extends AppCompatActivity {

    private EditText eRegName;
    private EditText eRegPassword;
    private Button eRegister;

    public Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.userlogin.R.layout.activity_registration);

        eRegName = findViewById(com.example.userlogin.R.id.etRegName);
        eRegPassword = findViewById(com.example.userlogin.R.id.etRegPassword);
        eRegister = findViewById(com.example.userlogin.R.id.btnRegister);

        credentials = new Credentials();

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences != null){

            java.util.Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if(preferencesMap.size() != 0){
                credentials.loadCredentials(preferencesMap);
            }
        }

        eRegister.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                String regUsername = eRegName.getText().toString();
                String regPassword = eRegPassword.getText().toString();

                if(validate(regUsername, regPassword)) {

                    if(credentials.checkUsername(regUsername)){
                        android.widget.Toast.makeText(com.example.userlogin.Registration.this, "Username already taken!", android.widget.Toast.LENGTH_SHORT).show();
                    }else{

                        credentials.addCredentials(regUsername, regPassword);

                        /* Store the credentials */
                        sharedPreferencesEditor.putString(regUsername, regPassword);
                        sharedPreferencesEditor.putString("LastSavedUsername", "");
                        sharedPreferencesEditor.putString("LastSavedPassword", "");

                        /* Commits the changes and adds them to the file */
                        sharedPreferencesEditor.apply();

                        startActivity(new android.content.Intent(com.example.userlogin.Registration.this, com.example.userlogin.MainActivity.class));
                    }
                }
            }
        });

    }

    private boolean validate(String username, String password){

        if(username.isEmpty() || password.length() < 8){
            Toast.makeText(this, "Please enter all the details, password should be atleast 8 characters!", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }
}