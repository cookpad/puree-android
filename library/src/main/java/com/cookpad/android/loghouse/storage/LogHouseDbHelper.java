package com.cookpad.android.loghouse.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class LogHouseDbHelper extends SQLiteOpenHelper implements LogHouseStorage {
    private static final String TAG = LogHouseDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "log_house";
    private static final String TABLE_NAME = "logs";
    private static final String COLUMN_NAME_LOG = "log";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public LogHouseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public void insert(JSONObject serializedLog) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_LOG, serializedLog.toString());
        db.insert(TABLE_NAME, null, contentValues);
    }

    public List<JSONObject> select() {
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_NAME, null);
        List<JSONObject> serializedLogs = new ArrayList<JSONObject>();
        try {
            while (cursor.moveToNext()) {
                try {
                    JSONObject serializedLog = new JSONObject(cursor.getString(1));
                    serializedLogs.add(serializedLog);
                } catch (JSONException e) {
                    // continue
                }
            }
        } finally {
            cursor.close();
        }
        return serializedLogs;
    }

    public void delete() {
        db.delete(TABLE_NAME, null, null);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_LOG + " TEXT" +
                ")");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
