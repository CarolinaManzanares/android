package com.carolinamanzanares.earthquakes.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;
import android.util.Log;

import com.carolinamanzanares.earthquakes.Model.Coordinate;
import com.carolinamanzanares.earthquakes.Model.EarthQuakes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cursomovil on 27/03/15.
 */
public class EarthQuakeDB {

    private EarthQuakeOpenHelper helper;
    private SQLiteDatabase db;

    //Column names
    public static final String KEY_ID = "_id";
    public static final String KEY_PLACE = "place";
    public static final String KEY_MAGNITUDE = "magnitude";
    public static final String KEY_LATITUDE = "lat";
    public static final String KEY_LONGITUDE = "long";
    public static final String KEY_DEPTH = "depth";
    public static final String KEY_URL = "url";
    public static final String KEY_TIME = "time";

    public static class Columns implements BaseColumns {

    }

    public static final String[] ALL_COLUMNS = {
            KEY_ID,
            KEY_PLACE,
            KEY_MAGNITUDE,
            KEY_LATITUDE,
            KEY_LONGITUDE,
            KEY_DEPTH,
            KEY_URL,
            KEY_TIME
    };

    public EarthQuakeDB(Context context) {
        //Aunque DATABASE_NAME es private de otra clase al estar dentro de MI clase puedo "verla"
        this.helper = new EarthQuakeOpenHelper(context, EarthQuakeOpenHelper.DATABASE_NAME, null, EarthQuakeOpenHelper.DATABASE_VERSION);
        this.db = helper.getWritableDatabase();
        Log.d("EARTHQUAKE", "DB writable");
    }

    public void insert(EarthQuakes earthquake) {
        //Create a new row of values to insert
        ContentValues newValues = new ContentValues();

        //Assign values to each column
        newValues.put(KEY_ID, earthquake.getId());
        newValues.put(KEY_PLACE, earthquake.getPlace());
        newValues.put(KEY_MAGNITUDE, earthquake.getMagnitude());
        Coordinate coords = earthquake.getCoords();
        newValues.put(KEY_LATITUDE, coords.getLatitude());
        newValues.put(KEY_LONGITUDE, coords.getLongitude());
        newValues.put(KEY_DEPTH, coords.getDeth());
        newValues.put(KEY_URL, earthquake.getURL());
        newValues.put(KEY_TIME, (earthquake.getTime()).getTime());

        //insert the row into the table
        try {
            db.insertOrThrow(EarthQuakeOpenHelper.DATABASE_TABLE, null, newValues);
        }catch (SQLException e) {
            Log.d("EARTHQUAKE", "Insert error: " + e.getMessage());
        }

    }

    //para hacer más general esta función podríamos devolver un List en lugar de un ArrayList
    public List<EarthQuakes> getAll(){

        return query(null, null);
    }

    public List<EarthQuakes> getAllByMagnitude(int magnitude){
        String where = KEY_MAGNITUDE + " >= ?";
        String[] whereArgs = {String.valueOf(magnitude)};

        return query(where, whereArgs);
    }

    public EarthQuakes getAllById(String id){
        String where = KEY_ID + " = ?";
        String[] whereArgs = {id};

        ArrayList<EarthQuakes> earthQuakes;
        earthQuakes = new ArrayList<>();
        earthQuakes.addAll(query(where, whereArgs));

        EarthQuakes earthquake;
        earthquake = new EarthQuakes();
        if(earthQuakes.size() == 0){
            earthquake = null;
        }
        else{
            earthquake = earthQuakes.get(0);
        }

        //return earthQuakes.size() : earthQuakes.get(0) ? null;
        return earthquake;
    }


    public List<EarthQuakes> query(String where, String[] whereArgs) {

        ArrayList<EarthQuakes> earthQuakes;

        Cursor cursor = db.query(
                EarthQuakeOpenHelper.DATABASE_TABLE,
                ALL_COLUMNS,
                where,
                whereArgs,
                null,
                null,
                KEY_TIME + " DESC");

        //Crear tabla hash que relaciona index con nombre columna
        HashMap<String, Integer> indexes = new HashMap<>(); //String: nombre column, Integer: index
        for (int i = 0 ; i < ALL_COLUMNS.length; i++){
            indexes.put(ALL_COLUMNS[i], cursor.getColumnIndex(ALL_COLUMNS[i]));
        }

        earthQuakes = new ArrayList<>();
        while (cursor.moveToNext()) {
            EarthQuakes earthQuake = new EarthQuakes(); //creamos un objeto para cada terremoto, sino solo tendríamos uno
            //Log.d("EARTHQUAKE", "String: " + cursor.getString(cursor.getColumnIndex("place")));

            earthQuake.setId(cursor.getString(indexes.get(KEY_ID)));
            earthQuake.setPlace(cursor.getString(indexes.get(KEY_PLACE)));
            earthQuake.setMagnitude(cursor.getDouble(indexes.get(KEY_MAGNITUDE)));
            earthQuake.setCoords(new Coordinate(cursor.getDouble(indexes.get(KEY_LATITUDE)), cursor.getDouble(indexes.get(KEY_LONGITUDE)), cursor.getDouble(indexes.get(KEY_DEPTH))));
            earthQuake.setURL(cursor.getString(indexes.get(KEY_URL)));
            earthQuake.setTime(cursor.getLong(indexes.get(KEY_TIME)));

            earthQuakes.add(earthQuake);
        }

        cursor.close();
        return earthQuakes;

    }
    private static class EarthQuakeOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "earthquakes.db";
        private static final String DATABASE_TABLE = "EARTHQUAKES";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "CREATE table " + DATABASE_TABLE + " (" +
                KEY_ID + " TEXT PRIMARY KEY," +
                KEY_PLACE + " TEXT," +
                KEY_MAGNITUDE + " REAL," +
                KEY_LATITUDE + " REAL, " +
                KEY_LONGITUDE + " REAL, " +
                KEY_DEPTH + " REAL, " +
                KEY_URL + " TEXT, " +
                KEY_TIME + " INTEGER)";

        private EarthQuakeOpenHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL(DATABASE_CREATE);

        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
            db.execSQL("DROP TABLE IF EXISTS" + DATABASE_TABLE);
            onCreate(db);
        }
    }


}
