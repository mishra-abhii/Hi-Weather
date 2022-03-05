package com.abhishek.hiweather;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Weather_Data {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    Context context;
    String cityID;

    public Weather_Data(Context context) {
        this.context = context;
    }

    //Used for volley callback purpose to handle delay in return value
    public interface VolleyResponseListener {
        void onError(String message);

        void onResponse(String cityID);
    }

    public void getCityID(String cityName , final VolleyResponseListener volleyResponseListener){

        String url = QUERY_FOR_CITY_ID + cityName;

        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url,
                null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                cityID = "";
                try {
                    // we can't directly use (JSONArray response) to get String like we did in above code for Objects only
                    // so we have to create locally JSONObject
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

                Toast.makeText(context, "City Id : " + cityID, Toast.LENGTH_SHORT).show();
                volleyResponseListener.onResponse(cityID);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
//                Toast.makeText(context, "Something went wrong", Toast.LENGTH_SHORT).show();
                volleyResponseListener.onError("Something went wrong...");
            }
        });

        // Adding a request to my RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonArrayRequest);

    }

//    public List<WeatherReport_Modal> getCityForecastByID(String cityID){
//
//    }
//
//    public List<WeatherReport_Modal> getCityForecastByName(String cityName){
//
//    }
}
