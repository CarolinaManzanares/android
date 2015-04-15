package com.carolinamanzanares.earthquakes.providers;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

import com.carolinamanzanares.earthquakes.Model.Coordinate;
import com.carolinamanzanares.earthquakes.Model.EarthQuakes;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by cursomovil on 27/03/15.
 */
public class EarthQuakeDB {

    Context context;

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
        this.context = context;

    }

    public void insert(EarthQuakes earthquake) {
        //Create a new row of values to insert
        ContentValues insert = new ContentValues();

        //Assign values to each column
        insert.put(EarthQuakesProvider.Columns.KEY_ID, earthquake.getId());
        insert.put(EarthQuakesProvider.Columns.KEY_PLACE, earthquake.getPlace());
        insert.put(EarthQuakesProvider.Columns.KEY_MAGNITUDE, earthquake.getMagnitude());
        Coordinate coords = earthquake.getCoords();
        insert.put(EarthQuakesProvider.Columns.KEY_LATITUDE, coords.getLatitude());
        insert.put(EarthQuakesProvider.Columns.KEY_LONGITUDE, coords.getLongitude());
        insert.put(EarthQuakesProvider.Columns.KEY_DEPTH, coords.getDeth());
        insert.put(EarthQuakesProvider.Columns.KEY_URL, earthquake.getURL());
        insert.put(EarthQuakesProvider.Columns.KEY_TIME, (earthquake.getTime()).getTime());

        ContentResolver cr = context.getContentResolver();
        cr.insert(EarthQuakesProvider.CONTENT_URI, insert);

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

        ContentResolver cr = context.getContentResolver();

        ArrayList<EarthQuakes> earthQuakes;

        String orderBy = EarthQuakesProvider.Columns.KEY_TIME + " DESC";

        Cursor cursor = cr.query(
                EarthQuakesProvider.CONTENT_URI,
                ALL_COLUMNS,
                where,
                whereArgs,
                orderBy);

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
