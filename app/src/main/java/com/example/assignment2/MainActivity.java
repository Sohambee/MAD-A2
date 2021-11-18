package com.example.assignment2;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    //Initialize variables
    RecyclerView recyclerView;
    FloatingActionButton add_button;
    SearchView searchBox;

    DatabaseHelper helper;
    ArrayList<String> id, address, latitude, longitude;
    ArrayList<Location> location;
    CustomAdapter customAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Bind and initialize elements of the interface
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchBox = findViewById(R.id.search_box);
        recyclerView = findViewById(R.id.recyclerView);
        add_button = findViewById(R.id.add_button);
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, AddActivity.class);
                startActivity(intent);
            }
        });

        //Declare and set array/variables
        helper = new DatabaseHelper(MainActivity.this);
        id = new ArrayList<>();
        address = new ArrayList<>();
        latitude = new ArrayList<>();
        longitude = new ArrayList<>();
        location = new ArrayList<Location>();

        displayData();

        customAdapter = new CustomAdapter(MainActivity.this, this, location);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(MainActivity.this));

        //Listener for search box
        searchBox.setOnQueryTextListener(
                new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (location.size() != 0) {
                    customAdapter.search(s.toString());
                }
                return false;
            }
        });
    }

    //Refresh for page
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1) {
            recreate();
        }
    }

    //Show the data by reading the database
    void displayData() {
        Cursor cursor = helper.readData();
        if (cursor.getCount() == 0) {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        } else {
            while (cursor.moveToNext()) {
                location.add(new Location(cursor.getInt(0), cursor.getString(1), cursor.getFloat(2), cursor.getFloat(3)));
            }
        }
    }
}