package com.example.assignment2;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder> {

    private Context context;
    private ArrayList id, address, latitude, longitude;
    private ArrayList<Location> locations;
    private ArrayList<Location> allLocations;

    //Constructor for creating activity type object
    CustomAdapter(Activity activity, Context context, ArrayList locations) {
        this.context = context;
        this.id = id;
        this.address = address;
        this.latitude = latitude;
        this.longitude = longitude;
        this.locations = locations;
        allLocations = locations;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.card, parent, false);
        return new MyViewHolder(view);
    }

    //Pass data in intent to allow for update when entry is clicked on main page
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        holder.id_text.setText(String.valueOf(position + 1));
        holder.address_text.setText(String.valueOf((locations.get(position).getAddress())));
        holder.latitude_text.setText(String.valueOf((locations.get(position).getLatitude())));
        holder.longitude_text.setText(String.valueOf((locations.get(position).getLongitude())));
        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateActivity.class);
                intent.putExtra("id", String.valueOf((locations.get(position).getId())));
                intent.putExtra("address", String.valueOf((locations.get(position).getAddress())));
                intent.putExtra("latitude", String.valueOf((locations.get(position).getLatitude())));
                intent.putExtra("longitude", String.valueOf((locations.get(position).getLongitude())));

                context.startActivity(intent);
            }
        });
    }

    //Return total locations
    @Override
    public int getItemCount() {
        return locations.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        TextView id_text, address_text, latitude_text, longitude_text;
        LinearLayout mainLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            id_text = itemView.findViewById(R.id.location_id);
            address_text = itemView.findViewById(R.id.address_value);
            latitude_text = itemView.findViewById(R.id.latitude_value);
            longitude_text = itemView.findViewById(R.id.longitude_value);
            mainLayout = itemView.findViewById(R.id.mainLayout);
        }
    }

    //Search function to return best matched entries of text
    public void search(final String searchKeyword) {

        if (searchKeyword.trim().isEmpty() || searchKeyword.trim().equals("") || searchKeyword.trim().equals(" ")) {
            locations = allLocations;
        } else {
            ArrayList<Location> temp = new ArrayList<>();
            for (Location location : allLocations) {
                if (location.getAddress().toLowerCase().contains(searchKeyword.toLowerCase())) {
                    temp.add(location);
                }
            }
            locations = temp;
        }

        new Handler(Looper.getMainLooper()).post(() -> notifyDataSetChanged());

    }
}
