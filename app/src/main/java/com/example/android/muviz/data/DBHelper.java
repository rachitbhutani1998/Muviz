package com.example.android.muviz.data;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.android.muviz.data.MovieContract.MovieEntry;


public class DBHelper extends SQLiteOpenHelper {

    private static String DATABASE_NAME="favourites.db";
    private static int DATABASE_VERSION=1;

    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        String createTable = "CREATE TABLE "
                + MovieEntry.TABLE_NAME + "("
                + MovieEntry._ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieEntry.COL_ID + " INTEGER,"
                + MovieEntry.COL_TITLE + "TEXT  );";
        sqLiteDatabase.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL("DROP TABLE IF EXISTS " + MovieEntry.TABLE_NAME);
        onCreate(sqLiteDatabase);
    }

    public boolean addMovies(String id, String title) {
        SQLiteDatabase sqLiteDatabase = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(MovieEntry.COL_ID, id);
        values.put( MovieEntry.COL_TITLE , title);

        long result = sqLiteDatabase.insert(MovieEntry.TABLE_NAME, null, values);

        return result != -1;
    }

    public Cursor getMovies() {
        SQLiteDatabase database = this.getReadableDatabase();
        return database.rawQuery("SELECT * FROM " + MovieEntry.TABLE_NAME, null);

    }
}
