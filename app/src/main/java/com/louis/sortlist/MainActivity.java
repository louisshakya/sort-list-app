package com.louis.sortlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    private static final String URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    private ArrayList<Lists> lists = new ArrayList<>();
    private RecyclerView recyclerView;

    public ArrayList<Lists> getLists() {
        return lists;
    }

    public void setLists(ArrayList<Lists> list) {
        lists = list;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initialize();
        fetchData(URL);
    }


    public void initialize() {
        recyclerView = findViewById(R.id.recyclerViewId);
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
        ArrayList<Lists> list = new ArrayList<>();
        try {
            JSONArray mainArray = new JSONArray(response);
            for (int i = 0; i < mainArray.length(); i++) {
                JSONObject object = mainArray.getJSONObject(i);
                String id = object.getString("id");
                String listId = object.getString("listId");
                String name = object.getString("name");
                list.add(new Lists(id,listId,name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        removeEmptyNames(list);
        setLists(list);
        updateRecyclerView(list);
    }

    public void removeEmptyNames(ArrayList<Lists> name) {
        Iterator<Lists> itr = name.iterator();
        while (itr.hasNext()) {
            Lists l = itr.next();
            if (l.getName().equals("") || l.getName().equals("null")) {
                itr.remove();
            }
        }
    }

    public void updateRecyclerView(ArrayList<Lists> list) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }
}