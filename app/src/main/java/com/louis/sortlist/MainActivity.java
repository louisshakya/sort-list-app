package com.louis.sortlist;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity implements View.OnClickListener{

    private static final String URL = "https://fetch-hiring.s3.amazonaws.com/hiring.json";
    private ArrayList<Lists> lists = new ArrayList<>();
    private RecyclerView recyclerView;
    private Button sortByListButton, sortByNameButton, sortByBothButton;
    private boolean isSortByListIDPressed, isSortByNamePressed, isSortByBothPressed = false;

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
        updateRecyclerView(lists);
        fetchData(URL);
    }

    //Initializing components
    public void initialize() {
        recyclerView = findViewById(R.id.recyclerViewId);

        sortByListButton = findViewById(R.id.listIdButtonId);
        sortByNameButton = findViewById(R.id.nameButtonId);
        sortByBothButton = findViewById(R.id.bothButtonId);

        sortByListButton.setOnClickListener(this);
        sortByNameButton.setOnClickListener(this);
        sortByBothButton.setOnClickListener(this);
    }


    //Logic for button clicks
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.listIdButtonId) {
            isSortByNamePressed = false;
            isSortByBothPressed = false;
            sortByNameButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            sortByBothButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            ArrayList<Lists> newList = new ArrayList<>(getLists());
            if (isSortByListIDPressed) {
                isSortByListIDPressed = false;
                updateRecyclerView(newList);
                sortByListButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            } else {
                isSortByListIDPressed = true;
                sortByListId(newList);
                updateRecyclerView(newList);
                sortByListButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200));
            }
        }
        if (v.getId() == R.id.nameButtonId) {
            isSortByListIDPressed = false;
            isSortByBothPressed = false;
            sortByListButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            sortByBothButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            ArrayList<Lists> newList = new ArrayList<>(getLists());
            if (isSortByNamePressed) {
                isSortByNamePressed = false;
                updateRecyclerView(newList);
                sortByNameButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            } else {
                isSortByNamePressed = true;
                sortByName(newList);
                updateRecyclerView(newList);
                sortByNameButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200));
            }
        }
        if (v.getId() == R.id.bothButtonId) {
            isSortByNamePressed = false;
            isSortByListIDPressed = false;
            sortByNameButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            sortByListButton.setBackgroundColor(ContextCompat.getColor(this,R.color.white));
            ArrayList<Lists> newList = new ArrayList<>(getLists());
            if (isSortByBothPressed) {
                isSortByBothPressed = false;
                updateRecyclerView(newList);
                sortByBothButton.setBackgroundColor(ContextCompat.getColor(this, R.color.white));
            } else {
                isSortByBothPressed = true;
                ArrayList<Lists> sortedList = sortByBoth(newList);
                updateRecyclerView(sortedList);
                sortByBothButton.setBackgroundColor(ContextCompat.getColor(this, R.color.purple_200));
            }
        }
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

            //Creating JSON array
            JSONArray mainArray = new JSONArray(response);

            for (int i = 0; i < mainArray.length(); i++) {

                //Creating JSON object
                JSONObject object = mainArray.getJSONObject(i);

                //Parsing data
                String id = object.getString("id");
                String listId = object.getString("listId");
                String name = object.getString("name");
                list.add(new Lists(id,listId,name));
            }
        } catch (JSONException e) {
            e.printStackTrace();
            System.out.println("An error occurred while parsing data");
        }
        removeEmptyNames(list);
        setLists(list);
        updateRecyclerView(list);
    }

    //Removes null values for names
    public void removeEmptyNames(ArrayList<Lists> name) {
        Iterator<Lists> itr = name.iterator();
        while (itr.hasNext()) {
            Lists l = itr.next();
            if (l.getName().equals("") || l.getName().equals("null")) {
                itr.remove();
            }
        }
    }

    //Updates RecyclerView to display the list
    public void updateRecyclerView(ArrayList<Lists> list) {
        RecyclerViewAdapter adapter = new RecyclerViewAdapter(this);
        adapter.setList(list);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false));
    }

    //Sorts the list by list id in ascending order
    public void sortByListId(ArrayList<Lists> list) {
        Collections.sort(list, new Comparator<Lists>() {
            @Override
            public int compare(Lists o1, Lists o2) {
                return o1.getListId().compareToIgnoreCase(o2.getListId());
            }
        });
    }

    //Sorts the list by name in ascending order
    public void sortByName(ArrayList<Lists> list) {
        Collections.sort(list, new Comparator<Lists>() {
            @Override
            public int compare(Lists o1, Lists o2) {
                return o1.getName().compareToIgnoreCase(o2.getName());
            }
        });
    }

    //Sorts the list first by list id and then by name in ascending order
    public ArrayList<Lists> sortByBoth(ArrayList<Lists> list) {
        sortByListId(list);
        int listIDValue = Integer.parseInt(list.get(list.size() - 1).getListId());
        ArrayList<Lists> newSortedArray = new ArrayList<>();
        for (int i = 0; i <= listIDValue; i++) {
            ArrayList<Lists> tempArray = new ArrayList<>();
            for (Lists l: list) {
                if (Integer.parseInt(l.getListId()) == i) {
                    tempArray.add(l);
                }
            }
            sortByName(tempArray);
            newSortedArray.addAll(tempArray);
        }
        return newSortedArray;
    }
}