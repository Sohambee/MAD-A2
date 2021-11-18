package com.example.assignment2;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class DatabaseHelper extends SQLiteOpenHelper {

    private Context context;
    private static final String DATABASE_NAME = "LocationFinder.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME = "my_database";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_ADDRESS = "_address";
    private static final String COLUMN_LATITUDE = "_latitude";
    private static final String COLUMN_LONGITUDE = "_longitude";



    DatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context = context;
    }

    //Used to create a table with 4 columns and a table name
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                        " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                        COLUMN_ADDRESS + " TEXT, " +
                        COLUMN_LATITUDE + " REAL, " +
                        COLUMN_LONGITUDE + " REAL);";

        db.execSQL(query);

    }

    //Drop table on relaunch of application
    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    //Add a location to the database using query and content values
    void addLocation(String address, Float latitude, Float longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_ADDRESS, address);
        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_LONGITUDE, longitude);

        long result = db.insert(TABLE_NAME, null, cv);
        if (result == -1) {
            Toast.makeText(context, "Failed data entry", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successful data entry", Toast.LENGTH_SHORT).show();
        }
    }

    //Cursor reading data for fetching database entries
    Cursor readData() {
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;
        if (db != null) {
            cursor = db.rawQuery(query, null);

        }
        return cursor;
    }

    //Update a tuple with new information from the front-end
    void updateData(String row_id, String address, Float latitude, Float longitude) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_LATITUDE, latitude);
        cv.put(COLUMN_LONGITUDE, longitude);
        cv.put(COLUMN_ADDRESS, address);

        long result = db.update(TABLE_NAME, cv, "_id=?", new String []{row_id});
        if (result == -1) {
            Toast.makeText(context, "Failed to fetch", Toast.LENGTH_SHORT).show();
        }
    }

    //Delete a row in the database
    void deleteOne(String row_id) {
        SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?", new String[]{row_id});
        if (result == -1) {
            Toast.makeText(context, "Could not delete", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "Successfully deleted", Toast.LENGTH_SHORT).show();
        }
    }
}
