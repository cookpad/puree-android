package com.cookpad.android.loghouse.storage;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import org.json.JSONException;
import org.json.JSONObject;

public class LogHouseDbHelper extends SQLiteOpenHelper implements LogHouseStorage {
    private static final String TAG = LogHouseDbHelper.class.getSimpleName();
    private static final String DATABASE_NAME = "log_house";
    private static final String TABLE_NAME = "logs";
    private static final String COLUMN_NAME_TYPE = "type";
    private static final String COLUMN_NAME_LOG = "log";
    private static final int DATABASE_VERSION = 1;

    private SQLiteDatabase db;

    public LogHouseDbHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        db = getWritableDatabase();
    }

    public void insert(String type, JSONObject serializedLog) {
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_NAME_TYPE, type);
        contentValues.put(COLUMN_NAME_LOG, serializedLog.toString());
        db.insert(TABLE_NAME, null, contentValues);
    }

    public Records select(String type, int logsPerRequest) {
        String query = "SELECT * FROM " + TABLE_NAME +
                " WHERE " + COLUMN_NAME_TYPE + " = '" + type + "'" +
                " ORDER BY id ASC" +
                " LIMIT " + logsPerRequest;
        Cursor cursor = db.rawQuery(query, null);

        Records records = new Records();
        try {
            while (cursor.moveToNext()) {
                try {
                    Record record = new Record(cursor);
                    records.add(record);
                } catch (JSONException e) {
                    // continue
                }
            }
        } finally {
            cursor.close();
        }

        return records;
    }

    public void delete(Records records) {
        String query = "DELETE FROM " + TABLE_NAME +
                " WHERE id IN (" + records.getIdsAsString() + ")";
        db.execSQL(query);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT," +
                COLUMN_NAME_TYPE + " TEXT," +
                COLUMN_NAME_LOG + " TEXT" +
                ")";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
    }
}
