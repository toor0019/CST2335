package com.example.gurki.androidlabs;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

public class LoginActivity extends Activity {

    protected static final String ACTIVITY_NAME = "LoginActivity";
    private static final String   DEFAULT_EMAIL_KEY="DEFAULT_EMAIL_KEY";
    private static final String   EMAIL_KEY="EMAIL_KEY";
    private static final String MY_PREF="my_pref";

    private SharedPreferences sharePref;
    private Button loginButton;
    private EditText emailEditText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_login);

        loginButton= findViewById(R.id.login_button);
        emailEditText=findViewById(R.id.email_editText);
        sharePref=getApplicationContext().getSharedPreferences(MY_PREF,getApplicationContext().MODE_PRIVATE);
        emailEditText.setText(sharePref.getString(EMAIL_KEY,"email@domain.com"));

        final SharedPreferences.Editor editor = sharePref.edit();

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editor.putString(EMAIL_KEY,emailEditText.getText().toString());
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, StartActivity.class);

                startActivity(intent);
            }
        });
    }

    public void onStart(){
        super.onStart();
        Log.i(ACTIVITY_NAME, "In onStart()");
    }

    public void onResume(){
        super.onResume();
        Log.i(ACTIVITY_NAME, "In onResume()");
    }

    public void onPause(){
        super.onPause();
        Log.i(ACTIVITY_NAME, "In onPause()");
    }

    public void onStop(){
        super.onStop();
        Log.i(ACTIVITY_NAME, "In onStop()");
    }

    public void onDestroy(){
        super.onDestroy();
        Log.i(ACTIVITY_NAME, "In onDestroy()");
    }
}
