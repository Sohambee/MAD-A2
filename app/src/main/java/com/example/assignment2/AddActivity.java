package com.example.assignment2;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

public class AddActivity extends AppCompatActivity {

    TextView address_input;
    EditText latitude_input, longitude_input;
    Button set_button, add_button;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);
        //Initialize geocoder and all variables for database entry
        Geocoder geocoder = new Geocoder(this);

        latitude_input = findViewById(R.id.latitude_input);
        longitude_input = findViewById(R.id.longitude_input);
        address_input = findViewById(R.id.address_box);
        set_button = findViewById(R.id.set);
        set_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Convert lat and lng to string from EditText
                String latInput = latitude_input.getText().toString();
                String lngInput = longitude_input.getText().toString();

                //Check if empty
                if (latInput.isEmpty()) {
                    latitude_input.setError("Please enter latitude.");
                } else if (lngInput.isEmpty()) {
                    longitude_input.setError("Please enter longitude.");
                } else {
                    //Try and fetch location, set address box with location
                    try {
                        Float latitude = Float.parseFloat(latInput);
                        Float longitude = Float.parseFloat(lngInput);

                        List<Address> addressList = geocoder.getFromLocation(latitude, longitude, 1);

                        String addressLine = addressList.get(0).getAddressLine(0);
                        String city = addressList.get(0).getLocality();
                        String provState = addressList.get(0).getAdminArea();
                        String country = addressList.get(0).getCountryName();
                        String fullAddress = addressLine + ", " + city + ", " + provState + ", " + country;

                        address_input.setText(fullAddress);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(getApplicationContext(), "No address found!", Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getApplicationContext(), "Invalid coordinates!", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
        add_button = findViewById(R.id.add);
        //Add to database if pressed
        add_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper dbHelper = new DatabaseHelper(AddActivity.this);
                dbHelper.addLocation(address_input.getText().toString().trim(), Float.valueOf(latitude_input.getText().toString().trim()), Float.valueOf(longitude_input.getText().toString().trim()));
                Intent intent = new Intent(AddActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
    }
}