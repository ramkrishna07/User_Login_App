package com.example.userlogin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.SharedPreferences;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private EditText eName;
    private EditText ePassword;
    private Button eLogin;
    private TextView eAttemptsInfo;
    private TextView eRegister;
    private CheckBox eRememberMe;

    boolean isValid = false;
    private int counter = 5;

    public Credentials credentials;

    SharedPreferences sharedPreferences;
    SharedPreferences.Editor sharedPreferencesEditor;

    @Override
    protected void onCreate(android.os.Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(com.example.userlogin.R.layout.activity_main);

        eName = findViewById(com.example.userlogin.R.id.etName);
        ePassword = findViewById(com.example.userlogin.R.id.etPassword);
        eLogin = findViewById(com.example.userlogin.R.id.btnLogin);
        eAttemptsInfo = findViewById(com.example.userlogin.R.id.tvAttemptsInfo);
        eRegister = findViewById(com.example.userlogin.R.id.tvRegister);
        eRememberMe = findViewById(com.example.userlogin.R.id.cbRememberMe);

        credentials = new Credentials();

        sharedPreferences = getApplicationContext().getSharedPreferences("CredentialsDB", MODE_PRIVATE);
        sharedPreferencesEditor = sharedPreferences.edit();

        if(sharedPreferences != null){

            java.util.Map<String, ?> preferencesMap = sharedPreferences.getAll();

            if(preferencesMap.size() != 0){
                credentials.loadCredentials(preferencesMap);
            }

            String savedUsername = sharedPreferences.getString("LastSavedUsername", "");
            String savedPassword = sharedPreferences.getString("LastSavedPassword", "");

            if(sharedPreferences.getBoolean("RememberMeCheckbox", false)){
                eName.setText(savedUsername);
                ePassword.setText(savedPassword);
                eRememberMe.setChecked(true);
            }
        }

        eRememberMe.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                sharedPreferencesEditor.putBoolean("RememberMeCheckbox", eRememberMe.isChecked());
                sharedPreferencesEditor.apply();
            }
        });

        eRegister.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                startActivity(new android.content.Intent(com.example.userlogin.MainActivity.this, com.example.userlogin.Registration.class));
            }
        });

        eLogin.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {

                String inputName = eName.getText().toString();
                String inputPassword = ePassword.getText().toString();

                if(inputName.isEmpty() || inputPassword.isEmpty())
                {
                    android.widget.Toast.makeText(com.example.userlogin.MainActivity.this, "Please enter all the details correctly!", android.widget.Toast.LENGTH_SHORT).show();
                }else{

                    isValid = validate(inputName, inputPassword);

                    if(!isValid){

                        counter--;

                        android.widget.Toast.makeText(com.example.userlogin.MainActivity.this, "Incorrect credentials entered!", android.widget.Toast.LENGTH_SHORT).show();

                        eAttemptsInfo.setText("No. of attempts remaining: " + counter);

                        if(counter == 0){
                            eLogin.setEnabled(false);
                        }

                    }else{

                        android.widget.Toast.makeText(com.example.userlogin.MainActivity.this, "Login successful!", android.widget.Toast.LENGTH_SHORT).show();

                        sharedPreferencesEditor.putString("LastSavedUsername", inputName);
                        sharedPreferencesEditor.putString("LastSavedPassword", inputPassword);

                        sharedPreferencesEditor.apply();

                        // Add the code to go to new activity
                        android.content.Intent intent = new android.content.Intent(MainActivity.this, HomeScreenActivity.class);
                        startActivity(intent);
                    }

                }

            }
        });
    }

    private boolean validate(String name, String password){
        return credentials.checkCredentials(name, password);
    }
}