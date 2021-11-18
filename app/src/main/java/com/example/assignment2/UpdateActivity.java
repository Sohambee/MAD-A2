package com.example.assignment2;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
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

public class UpdateActivity extends AppCompatActivity {

    //Initialize geocoder and all variables for database entry
    EditText latitude, longitude;
    TextView address;
    Button delete_button, update_button;

    String id_txt, address_txt, latitude_txt, longitude_txt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);
        Geocoder geocoder = new Geocoder(this);

        latitude = findViewById(R.id.latitude_update);
        longitude = findViewById(R.id.longitude_update);
        address = findViewById(R.id.address_box_update);
        update_button = findViewById(R.id.update);
        delete_button = findViewById(R.id.delete);

        //Update entry in database if pressed
        update_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DatabaseHelper helper = new DatabaseHelper(UpdateActivity.this);
                //Convert lat and lng to string from EditText
                String latInput = latitude.getText().toString();
                String lngInput = longitude.getText().toString();

                //Check if empty
                if (latInput.isEmpty()) {
                    latitude.setError("Please enter latitude.");
                } else if (lngInput.isEmpty()) {
                    longitude.setError("Please enter longitude.");
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

                        address.setText(fullAddress);
                        helper.updateData(id_txt, fullAddress, latitude, longitude);
                    } catch (IOException e) {
                        e.printStackTrace();
                    } catch (IndexOutOfBoundsException e) {
                        Toast.makeText(getApplicationContext(), "No address found!", Toast.LENGTH_SHORT).show();
                    } catch (IllegalArgumentException e) {
                        Toast.makeText(getApplicationContext(), "Invalid coordinates!", Toast.LENGTH_SHORT).show();
                    }
                    Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                    startActivity(intent);
                }
            }
        });

        delete_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d("test", "hi");
                confirmDialog();
            }
        });

        getAndSetIntentData();
    }

    //Get intent data and set it to page to fill information of pressed entry
    void getAndSetIntentData() {
        if (getIntent().hasExtra("id") && getIntent().hasExtra("latitude") && getIntent().hasExtra("longitude") && getIntent().hasExtra("address")) {
            id_txt = getIntent().getStringExtra("id");
            address_txt = getIntent().getStringExtra("address");
            latitude_txt = getIntent().getStringExtra("latitude");
            longitude_txt = getIntent().getStringExtra("longitude");

            latitude.setText(latitude_txt);
            longitude.setText(longitude_txt);
            address.setText(address_txt);

        } else {
            Toast.makeText(this, "No data found", Toast.LENGTH_SHORT).show();
        }
    }

    //Confirm deletion and delete one entry
    void confirmDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete location " + id_txt + "?");
        builder.setMessage("Are you sure you want to delete this entry?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                DatabaseHelper helper = new DatabaseHelper(UpdateActivity.this);
                helper.deleteOne(id_txt);
                Intent intent = new Intent(UpdateActivity.this, MainActivity.class);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.create().show();
    }
}