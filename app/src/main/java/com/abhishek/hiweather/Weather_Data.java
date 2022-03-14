package com.abhishek.hiweather;

import android.content.Context;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.JsonObjectRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.BreakIterator;
import java.util.ArrayList;
import java.util.ConcurrentModificationException;
import java.util.List;

public class Weather_Data {

    public static final String QUERY_FOR_CITY_ID = "https://www.metaweather.com/api/location/search/?query=";
    public static final String QUERY_FOR_CITY_WEATHER_BY_ID = "https://www.metaweather.com/api/location/";

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
                cityID = "No ID is allotted";
                try {
                    // we can't directly use (JSONArray response) to get String like we did in above code for Objects only
                    // so we have to create locally JSONObject
                    JSONObject cityInfo = response.getJSONObject(0);
                    cityID = cityInfo.getString("woeid");

                }
                catch (JSONException e){
                    e.printStackTrace();
                }

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


    public interface ForecastByID_Response {
        void onError(String message);

        void onResponse(List<WeatherReport_Modal> weatherReport_modal);
    }

    public void getCityForecastByID(String cityID , ForecastByID_Response forecastByID_response){

        List<WeatherReport_Modal> report_list = new ArrayList<>();

        // get the json object
        String url = QUERY_FOR_CITY_WEATHER_BY_ID + cityID;
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
//                    Toast.makeText(context, response.toString() , Toast.LENGTH_SHORT).show();
                    try {
                        JSONArray consolidated_weather_list = response.getJSONArray("consolidated_weather");


                        for (int i = 0; i <consolidated_weather_list.length() ; i++) {
                            
                            WeatherReport_Modal one_day_report = new WeatherReport_Modal();
                            
                            JSONObject first_day_from_api = (JSONObject) consolidated_weather_list.get(i);
                            
                            one_day_report.setId(first_day_from_api.getInt("id"));
                            one_day_report.setWeather_state_name(first_day_from_api.getString("weather_state_name"));
                            one_day_report.setWeather_state_abbr(first_day_from_api.getString("weather_state_abbr"));
                            one_day_report.setWind_direction_compass(first_day_from_api.getString("wind_direction_compass"));
                            one_day_report.setCreated(first_day_from_api.getString("created"));
                            one_day_report.setApplicable_date(first_day_from_api.getString("applicable_date"));
                            one_day_report.setMin_temp(first_day_from_api.getLong("min_temp"));
                            one_day_report.setMax_temp(first_day_from_api.getLong("max_temp"));
                            one_day_report.setThe_temp(first_day_from_api.getLong("the_temp"));
                            one_day_report.setWind_speed(first_day_from_api.getLong("wind_speed"));
                            one_day_report.setWind_direction(first_day_from_api.getLong("wind_direction"));
                            one_day_report.setAir_pressure(first_day_from_api.getInt("air_pressure"));
                            one_day_report.setHumidity(first_day_from_api.getInt("humidity"));
                            one_day_report.setVisibility(first_day_from_api.getLong("visibility"));
                            one_day_report.setPredictability(first_day_from_api.getInt("predictability"));

                            report_list.add(one_day_report);
                        }
                        forecastByID_response.onResponse(report_list);


                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

            },  new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {

                }
        });

        // Adding a request to my RequestQueue.
        MySingleton.getInstance(context).addToRequestQueue(jsonObjectRequest);

    }
    

    public interface GetCityForecastByName{
        void onError(String message);
        void onResponse(List<WeatherReport_Modal> weatherReportModals);
    }

// combining above two methods to get the below result
    public void getCityForecastByName(String cityName , GetCityForecastByName getCityForecastByName){

        // fetching cityID, given to us cityName
        getCityID(cityName, new VolleyResponseListener() {
            @Override
            public void onError(String message) {

            }

            @Override
            public void onResponse(String cityID) {
                // now we have the cityID
                getCityForecastByID(cityID , new ForecastByID_Response() {
                    @Override
                    public void onError(String message) {

                    }

                    @Override
                    public void onResponse(List<WeatherReport_Modal> weatherReport_modal) {
                        // we have the weather report now
                        getCityForecastByName.onResponse(weatherReport_modal);
                    }
                });
            }
        });
    }
}
