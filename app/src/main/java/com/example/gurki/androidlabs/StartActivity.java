package com.example.gurki.androidlabs;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class StartActivity extends Activity {

    protected static final String ACTIVITY_NAME = "StartActivity";
    private Button start_Chat_button;
    private Button start_Weather_Forecast_button;
    private Button chat_button;
    private static final int REQUEST_LISTITEMS=50;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.i(ACTIVITY_NAME, "In onCreate()");
        setContentView(R.layout.activity_start);

        start_Chat_button = (Button)findViewById(R.id.start_button);
        start_Weather_Forecast_button = (Button)findViewById(R.id.start_weather_forecast);

        chat_button = (Button) findViewById(R.id.start_chat);

        chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(ACTIVITY_NAME, "User clicked Start Chat");
                Intent intent = new Intent(StartActivity.this,ChatWindow.class);
                startActivity(intent);
            }
        });

        start_Chat_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this,ListItemsActivity.class);
                startActivityForResult(intent,REQUEST_LISTITEMS);
            }
        });

        start_Weather_Forecast_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(StartActivity.this,WeatherForecast.class);
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        CharSequence text="ListItemsActivity passed: My information to share";
       if(requestCode==REQUEST_LISTITEMS && resultCode==Activity.RESULT_OK){
           Log.i(ACTIVITY_NAME, "Returned to StartActivity.onActivityResult");
           String messagePassed = data.getStringExtra("Response");
           Toast.makeText(getApplicationContext(),text,Toast.LENGTH_LONG).show();
       }
    }

    void onActivityResult(){

    }
}
