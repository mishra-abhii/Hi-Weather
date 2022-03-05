package com.abhishek.hiweather;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class MainActivity extends AppCompatActivity {

    EditText et_UserInput;
    Button btnGet_CityID, btnWeatherBy_CityName, btnWeatherBy_CityID;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        et_UserInput = findViewById(R.id.etUser_Input);
        btnGet_CityID = findViewById(R.id.btnGetCity_ID);
        btnWeatherBy_CityName = findViewById(R.id.btnGetWeatherby_CityName);
        btnWeatherBy_CityID = findViewById(R.id.btnGetWeatherby_CityID);

        Weather_Data weather_data = new Weather_Data(MainActivity.this);

        btnGet_CityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                weather_data.getCityID(et_UserInput.getText().toString() , new Weather_Data.VolleyResponseListener() {
                    @Override
                    public void onError(String message) {
                        Toast.makeText(MainActivity.this, "Error occurred", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onResponse(String cityID) {
                        Toast.makeText(MainActivity.this, "Returned an ID of : "+cityID, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        btnWeatherBy_CityName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnWeatherBy_CityID.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }
}