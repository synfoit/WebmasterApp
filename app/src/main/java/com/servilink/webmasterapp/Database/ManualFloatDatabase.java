package com.servilink.webmasterapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.servilink.webmasterapp.Model.ManualFloatData;

import java.util.ArrayList;
import java.util.List;

public class ManualFloatDatabase extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MANUALFlOAT.db";
    //table name
    private static final String TABLE_Name = "ManualFloatDatabase";
    private static final String Id = "Id";
    private static final String actualDateTime = "actualDateTime";
    private static final String dateTime = "dateTime";
    private static final String tagId = "tagId";
    private static final String value = "value";
    private static final String syncWithServer = "syncWithServer";


    // create table sql query
    private String CREATE_TABLE = "CREATE TABLE " + TABLE_Name + "("
            + Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + actualDateTime + " TEXT," + dateTime + " TEXT," + tagId + " INTEGER," + value + " REAL," + syncWithServer + " TEXT"
            + ")";

    // drop table sql query
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_Name;

    public ManualFloatDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);
    }


    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //Drop User Table if exist
        db.execSQL(DROP_TABLE);

        // Create tables again
        onCreate(db);

    }

    public int getcount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select count(*) From " + TABLE_Name, null);
        if (c.moveToFirst()) {
            return c.getInt(0);
        }
        return 0;

    }

    public void addManualFloatEntry(String actualDateTimeValue, String dateTimeValue, Long tagIdValue, Float entryValue, String syncstatus) {

        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(actualDateTime, actualDateTimeValue);
        values.put(dateTime, dateTimeValue);
        values.put(tagId, tagIdValue);
        values.put(value, entryValue);
        values.put(syncWithServer, syncstatus);
        // Inserting Row
        db.insert(TABLE_Name, null, values);
        db.close();

    }

    public int checkDataEntry(Long tagIdValue, String dateTimeValue) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * From " + TABLE_Name + " WHERE " + dateTime + "='" + dateTimeValue + "' AND " + tagId + "=" + tagIdValue, null);
        if (c.moveToFirst()) {
            return 1;
        }
        return 0;
    }

    @SuppressLint("Range")
    public List<ManualFloatData> getFloatEntry() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * From " + TABLE_Name + " WHERE " + syncWithServer + "='No'", null);
        List<ManualFloatData> requests = new ArrayList<>();

        while (c.moveToNext()) {

            String actualtime = c.getString(c.getColumnIndex(actualDateTime));
            String datetime = c.getString(c.getColumnIndex(dateTime));
            Long tagid = Long.valueOf(c.getInt(c.getColumnIndex(tagId)));
            Float valueString = c.getFloat(c.getColumnIndex(value));

            ManualFloatData request = new ManualFloatData(actualtime, datetime, tagid, valueString);
            requests.add(request);
        }
        return requests;
    }

    public void updateData(String actualDateTimeValue, String dateTimeValue, Long tagIdValue, Float entryValue, String syncstatus) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_Name + " SET " +
                value + "=" + entryValue + " WHERE " + tagId + "=" + tagIdValue + " AND " + dateTime + "='" + dateTimeValue + "'");
    }

    public void updateFlag(String status, Long tagIdvalue, String dateTimevalue) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_Name + " SET " +
                syncWithServer + "='" + status + "' WHERE " + tagId + "=" + tagIdvalue + " AND " + dateTime + "='" + dateTimevalue + "'");
    }

}
