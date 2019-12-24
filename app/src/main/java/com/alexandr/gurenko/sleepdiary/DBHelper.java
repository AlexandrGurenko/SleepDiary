package com.alexandr.gurenko.sleepdiary;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DBHelper extends SQLiteOpenHelper {

    public static final String DB_NAME = "SleepDiaryDB";
    public static final String TABLE_NAME = "SleepDiary";
    public static final String ID = "ID";
    public static final String START_DATE_TIME = "StartDateTime";
    public static final String END_DATE_TIME = "EndDateTime";
    public static final String DURATION = "Duration";

    private static final String CREATE_TABLE = "CREATE TABLE " + TABLE_NAME +
            " (" + ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
            START_DATE_TIME + " INTEGER, " +
            END_DATE_TIME + " INTEGER, " +
            DURATION + " INTEGER)";

    public DBHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    long getState() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{END_DATE_TIME}, null, null, null, null, null);
        if (cursor.moveToLast())
            return cursor.getLong(cursor.getColumnIndex(END_DATE_TIME));
        else
            return -1;
    }

    long action(long currentTime, boolean sleep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0, duration = 0;
        if (sleep) {
            contentValues.put(START_DATE_TIME, currentTime);
            contentValues.put(END_DATE_TIME, 0);
            db.insert(TABLE_NAME, null, contentValues);
        } else {
            Cursor cursor = db.query(TABLE_NAME, new String[]{ID, START_DATE_TIME}, null, null, null, null, null);
            if (cursor.moveToLast()) {
                id = cursor.getLong(cursor.getColumnIndex(ID));
                duration = currentTime - cursor.getLong(cursor.getColumnIndex(START_DATE_TIME));
            }
            contentValues.put(END_DATE_TIME, currentTime);
            contentValues.put(DURATION, duration);
            db.update(TABLE_NAME, contentValues, "id=?", new String[]{String.valueOf(id)});
        }
        db.close();
        return duration;
    }

    void updateTime(long time, boolean sleep) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        long id = 0, start = 0;
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID, START_DATE_TIME}, null, null, null, null, null);
        if (cursor.moveToLast()) {
            id = cursor.getLong(cursor.getColumnIndex(ID));
            start = cursor.getLong(cursor.getColumnIndex(START_DATE_TIME));
        }
        if (sleep) {
            contentValues.put(START_DATE_TIME, time);
            db.update(TABLE_NAME, contentValues, "id=?", new String[]{String.valueOf(id)});
        } else {
            contentValues.put(END_DATE_TIME, time);
            contentValues.put(DURATION, time - start);
            db.update(TABLE_NAME, contentValues, "id=?", new String[]{String.valueOf(id)});
        }
        db.close();
    }

    List<DreamList> getAllData() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{START_DATE_TIME, END_DATE_TIME, DURATION}, null, null, null, null, null);
        List<DreamList> result = new ArrayList<>();
        if (cursor.moveToFirst()) {
            do {
                DreamList dl = new DreamList();
                dl.setStartSleep(cursor.getLong(cursor.getColumnIndex(START_DATE_TIME)));
                dl.setEndSleep(cursor.getLong(cursor.getColumnIndex(END_DATE_TIME)));
                dl.setDuration(cursor.getLong(cursor.getColumnIndex(DURATION)));
                result.add(dl);
            } while (cursor.moveToNext());
        }
        db.close();
        return result;
    }

}
