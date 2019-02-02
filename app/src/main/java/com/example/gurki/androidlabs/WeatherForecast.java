package com.example.gurki.androidlabs;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.util.Log;
import android.util.Xml;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

import static org.xmlpull.v1.XmlPullParser.*;

public class WeatherForecast extends Activity {

    private ProgressBar mProgressBar;
    private TextView mMinTextView;
    private TextView mMaxTextView;
    private TextView mWindSpeed;
    private TextView mCurrentTemperatureTextView;
    private ImageView mCurrentWeather;
    protected static final String URL_STRING = "http://api.openweathermap.org/data/2.5/weather?q=ottawa,ca&APPID=d99666875e0e51521f0040a3d97d0f6a&mode=xml&units=metric";
    protected static final String URL_IMAGE = "http://openweathermap.org/img/w/";

    protected static final String ACTIVITY_NAME = "WeatherForecast";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_forecast);
        mWindSpeed = (TextView) findViewById(R.id.wind_speed_textview);
        mCurrentTemperatureTextView = (TextView) findViewById(R.id.current_temperature_textview);
        mMaxTextView = (TextView) findViewById(R.id.max_temperature_textview);
        mMinTextView = (TextView) findViewById(R.id.min_temperature_textview);
        mCurrentWeather = (ImageView) findViewById(R.id.imageView);
        mProgressBar = (ProgressBar) findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.VISIBLE);
        new ForeCastQuery().execute(null, null, null);
    }

    private class ForeCastQuery extends AsyncTask<String, Integer, String> {
        InputStream stream;
        private String mMin;
        private String mMax;
        private String mWind;
        private String mCurrentTemperature;
        private Bitmap mImage;
        private String iconFile;

        @Override
        protected String doInBackground(String... strings) {
            URL url = null;
            try {
                url = new URL(URL_STRING);
            } catch (MalformedURLException e) {
                e.printStackTrace();
            }
            HttpURLConnection conn = null;
            try {
                conn = (HttpURLConnection) url.openConnection();
            } catch (IOException e) {
                e.printStackTrace();
            }
            conn.setReadTimeout(10000 /* milliseconds */);
            conn.setConnectTimeout(15000 /* milliseconds */);
            try {
                conn.setRequestMethod("GET");
            } catch (ProtocolException e) {
                e.printStackTrace();
            }
            conn.setDoInput(true);
            // Starts the query
            try {
                conn.connect();
            } catch (IOException e) {
                e.printStackTrace();
            }
            try {
                XmlPullParser parser = Xml.newPullParser();
                parser.setFeature(FEATURE_PROCESS_NAMESPACES, false);
                parser.setInput(conn.getInputStream(), null);
                int event = parser.getEventType();
                boolean inside;
                while (event != END_DOCUMENT) {
                    String name = parser.getName();

                    switch (event) {
                        case START_TAG:

                            if (name.equals("Route")) {
                               inside=true;

                            } else if (name.equals("speed")) {
                                mWind = parser.getAttributeValue(null, "value") + "";

                            } else if (parser.getName().equals("weather")) {
                                iconFile = parser.getAttributeValue(null, "icon");
                            }


                        case END_TAG:

                            break;

                        default:
                            break;
                    }
                    event = parser.next();
                }

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    conn.getInputStream().close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            if (fileExist(iconFile + ".png")) {
                Log.i(ACTIVITY_NAME, "Weather image exists, read file");
                FileInputStream fis = null;
                try {
                    fis = openFileInput(iconFile + ".png");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mImage = BitmapFactory.decodeStream(fis);

            } else {
                Log.i(ACTIVITY_NAME, "Weather image does not exist, download URL");

                URL imageUrl = null;
                try {
                    imageUrl = new URL(URL_IMAGE + iconFile + ".png");
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                }
                try {
                    conn = (HttpURLConnection) imageUrl.openConnection();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    conn.connect();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    stream = conn.getInputStream();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                mImage = BitmapFactory.decodeStream(stream);

                FileOutputStream fos = null;
                try {
                    fos = openFileOutput(iconFile + ".png", Context.MODE_PRIVATE);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }
                mImage.compress(Bitmap.CompressFormat.PNG, 80, fos);
                try {
                    fos.flush();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                try {
                    fos.close();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                conn.disconnect();
            }
            publishProgress(100);

        return null;}


    protected void onProgressUpdate(Integer... mInt) {
        mProgressBar.setVisibility(View.VISIBLE);
        mProgressBar.setProgress(mInt[0]);
    }

    protected void onPostExecute(String result) {
        mMinTextView.setText("Minimunm Temperature" + mMin);
        mMaxTextView.setText("Maximum Temperature:" + mMax);
        mCurrentTemperatureTextView.setText("Current " + mCurrentTemperature);
        mWindSpeed.setText("Speed" + mWind);
        mProgressBar.setVisibility(View.INVISIBLE);
        mCurrentWeather.setImageBitmap(mImage);
    }


        public boolean fileExist(String name){
            File file = getBaseContext().getFileStreamPath(name);
            return file.exists();
        }

    }

}
