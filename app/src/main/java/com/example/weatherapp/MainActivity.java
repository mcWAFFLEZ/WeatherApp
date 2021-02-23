package com.example.weatherapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity {

    TextView cityEditText;
    TextView weatherDescTextView;
    Button tellWeatherButton;
    String city = "";
    /*"https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=ae303bac1fa6c1bd5fdfcddd27bf5f32"*/

    public void tellWeather(View view){

        Log.i("tellWeatherButton","Pressed.");
        Log.i("cityEditText",cityEditText.getText().toString());

        city = cityEditText.getText().toString();

        API_Task task = new API_Task();

        task.execute("https://api.openweathermap.org/data/2.5/weather?q="+city+"&appid=ae303bac1fa6c1bd5fdfcddd27bf5f32");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        cityEditText = findViewById(R.id.cityEditText);
        tellWeatherButton = findViewById(R.id.tellWeatherButton);
        weatherDescTextView = findViewById(R.id.weatherDescTextView);
    }

    public class API_Task extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... urls) {

            String result = "";
            URL url;
            HttpURLConnection connection;

            try {

                url = new URL(urls[0]);
                connection = (HttpURLConnection) url.openConnection();
                connection.connect();
                InputStream in = connection.getInputStream();
                InputStreamReader reader = new InputStreamReader(in);
                int res = reader.read();

                while(res != -1){

                    char ch = (char) res;
                    result += ch;
                    res = reader.read();
                }

                return result;

            } catch (Exception e){

                e.printStackTrace();
                return "Failed";
            }
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            try {

                JSONObject jsonObject = new JSONObject(s);

                String mainTemp = jsonObject.optJSONObject("main").getString("temp");
                int tempInCelcius = (int) (Float.valueOf(mainTemp)- 273);

                String weatherInfo = jsonObject.getString("weather");
                JSONArray arr = new JSONArray(weatherInfo);

                String mainWeather = "";
                String mainWeatherDescription = "";

                for (int i = 0; i < arr.length(); i++){
                    JSONObject jsonPart = arr.getJSONObject(i);

                    mainWeather = jsonPart.getString("main");
                    mainWeatherDescription = jsonPart.getString("description");
                }

                Log.i("Temper",String.valueOf(tempInCelcius));
                Log.i("mainWeather",mainWeather);
                Log.i("mainWeatherDescription",mainWeatherDescription);

                weatherDescTextView.setText(mainWeather+": "+mainWeatherDescription+", Celsius: "+tempInCelcius);

            } catch (Exception e){

                e.printStackTrace();
            }

        }
    }
}
