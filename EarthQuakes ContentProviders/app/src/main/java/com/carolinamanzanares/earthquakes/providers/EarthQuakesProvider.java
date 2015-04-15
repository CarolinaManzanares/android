package com.carolinamanzanares.earthquakes.providers;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;

import com.carolinamanzanares.earthquakes.Fragments.EarthQuakeListFragment;

import java.sql.SQLException;

public class EarthQuakesProvider extends ContentProvider {

    private static final String AUTHORITY = "com.carolinamanzanares.provider.earthquakes";
    public static final	Uri CONTENT_URI	= Uri.parse("content://" + AUTHORITY + "/earthquakes");

    private static final int ALLROWS = 1;
    private static final int SINGLE_ROW	= 2;

    //Column names. Se acostumbra a meter en una clase, Columns que extiende BaseColumns (que implementa _id entre otras cosas)
    public static class Columns implements BaseColumns {
        public static final String KEY_ID = "_id";
        public static final String KEY_PLACE = "place";
        public static final String KEY_MAGNITUDE = "magnitude";
        public static final String KEY_LATITUDE = "lat";
        public static final String KEY_LONGITUDE = "long";
        public static final String KEY_DEPTH = "depth";
        public static final String KEY_URL = "url";
        public static final String KEY_TIME = "time";
    }

    private	static final UriMatcher uriMatcher;
    private EarthQuakeOpenHelper earthQuakeOpenHelper;

    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY, "earthquakes", ALLROWS);
        uriMatcher.addURI(AUTHORITY, "earthquakes/#", SINGLE_ROW);
    }

    public EarthQuakesProvider() {
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        // Implement this to handle requests to delete one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public String getType(Uri uri) {
        //Return a string that identifies the MIME type
        // for a Content Provider URI

        switch (uriMatcher.match(uri))	{
            case ALLROWS:
                return	"vnd.android.cursor.dir/" + AUTHORITY;
            case SINGLE_ROW:
                return "vnd.android.cursor.item/" + AUTHORITY;
            default:
                throw new IllegalArgumentException("Unsupported	URI: " + uri);

        }
    }

    @Override
    public Uri insert(Uri uri, ContentValues values) {

        SQLiteDatabase db = earthQuakeOpenHelper.getWritableDatabase();

          //si hubiera más de una tabla
/*        switch ( uriMatcher.match(uri)) {
            case ALL_ROWS :
                String table = EarthQuakeOpenHelper.DATABASE_TABLE;
            default:break;
        }*/

        long id = db.insert(EarthQuakeOpenHelper.DATABASE_TABLE, null, values);

        if (id > -1) {
            Uri insertedId = ContentUris.withAppendedId(CONTENT_URI, id);
            //Esta línea es la que permite que se actualice la vista automáticamente al insertar en la DB
            getContext().getContentResolver().notifyChange(insertedId, null);

            return insertedId;
        }
        else{
            return null;
        }
    }

    @Override
    public boolean onCreate() {

        earthQuakeOpenHelper = new EarthQuakeOpenHelper(getContext(), earthQuakeOpenHelper.DATABASE_NAME, null, earthQuakeOpenHelper.DATABASE_VERSION);

        return false;
    }

    @Override
    public Cursor query(Uri uri, String[] projection, String selection,
                        String[] selectionArgs, String sortOrder) {

        SQLiteDatabase db;

        try{
            db = earthQuakeOpenHelper.getWritableDatabase();
        }catch(SQLiteException ex){
            db = earthQuakeOpenHelper.getWritableDatabase();
        }

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)) {
            case SINGLE_ROW:
                String rowID =  uri.getPathSegments().get(1);
                queryBuilder.appendWhere(Columns._ID + " = ?");
                selectionArgs = new String[]{ rowID };
            default: break;
        }

        queryBuilder.setTables(earthQuakeOpenHelper.DATABASE_TABLE);

        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);

        return cursor;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection,
                      String[] selectionArgs) {
        // TODO: Implement this to handle requests to update one or more rows.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    //Clase para gestionar la base de datos SQLite
    private static class EarthQuakeOpenHelper extends SQLiteOpenHelper {

        private static final String DATABASE_NAME = "earthquakes.db";
        private static final String DATABASE_TABLE = "EARTHQUAKES";
        private static final int DATABASE_VERSION = 1;

        private static final String DATABASE_CREATE = "CREATE table " + DATABASE_TABLE + " (" +
                Columns.KEY_ID + " TEXT PRIMARY KEY," +
                Columns.KEY_PLACE + " TEXT," +
                Columns.KEY_MAGNITUDE + " REAL," +
                Columns.KEY_LATITUDE + " REAL, " +
                Columns.KEY_LONGITUDE + " REAL, " +
                Columns.KEY_DEPTH + " REAL, " +
                Columns.KEY_URL + " TEXT, " +
                Columns.KEY_TIME + " INTEGER)";

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
