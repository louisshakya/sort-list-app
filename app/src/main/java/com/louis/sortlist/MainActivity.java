package com.louis.sortlist;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fetchData(URL);
    }


    public void fetchData(String URL) {

        //Creating a request queue
        RequestQueue queue = Volley.newRequestQueue(this);

        //Request a string response from the provided url
        StringRequest stringRequest = new StringRequest(Request.Method.GET, URL, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                parseData(response);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                System.out.println("An error occurred while fetching data");
            }
        });

        //Adding the request to the RequestQueue
        queue.add(stringRequest);

    }

    public void parseData(String response) {
        
    }
}