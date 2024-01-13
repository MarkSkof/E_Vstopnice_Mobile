package com.example.universityapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import android.content.Intent;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private RequestQueue requestQueue;
    private TextView osebe;
    private final String url = "https://is-eventim.azurewebsites.net/api/v1/event";
    public static final String EXTRA_MESSAGE = "com.example.universityapp.MESSAGE";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        requestQueue = Volley.newRequestQueue(getApplicationContext());
        osebe = (TextView) findViewById(R.id.osebe);
    }

    public void prikaziEvente(View view) {
        if (view != null) {
            JsonArrayRequest request = new JsonArrayRequest(url, jsonArrayListener, errorListener)
            {
                @Override
                public Map<String,String> getHeaders() throws AuthFailureError
                {
                    Map<String, String> params = new HashMap<String, String>();
                    params.put("ApiKey", "MarkZan123$");
                    return params;
                }
            };
            requestQueue.add(request);
        }
    }

    private final Response.Listener<JSONArray> jsonArrayListener = new Response.Listener<JSONArray>() {
        @Override
        public void onResponse(JSONArray response) {
            ArrayList<String> data = new ArrayList<>();

            for (int i = 0; i < response.length(); i++) {
                try {
                    JSONObject object = response.getJSONObject(i);
                    String name = object.getString("name");
                    String time = object.getString("time");
                    String numberOfSpots = object.getString("numberOfSpots");
                    String place = object.getString("placeId");

                    data.add(name + " " + time + " " + numberOfSpots + " " + place);

                } catch (JSONException e) {
                    e.printStackTrace();
                    return;

                }
            }

            osebe.setText("");


            for (String row : data) {
                String currentText = osebe.getText().toString();
                osebe.setText(currentText + "\n\n" + row);
            }

        }

    };

    private final Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError error) {
            Log.d("REST error", error.getMessage());
        }
    };

    public void addEventActivity(View view) {
        Intent intent = new Intent(this, AddEventActivity.class);
        String message = "Dodaj studenta v seznam.";
        intent.putExtra(EXTRA_MESSAGE, message);
        startActivity(intent);
    }


}