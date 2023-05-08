package com.servilink.webmasterapp.Database;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.servilink.webmasterapp.Model.ManualEntry;

import java.util.ArrayList;
import java.util.List;

public class ManualDataEntry extends SQLiteOpenHelper {
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "MANUALSTRING.db";
    //table name
    private static final String TABLE_Name = "ManualDataEntry";
    private static final String Id = "Id";
    private static final String actualDateTime = "actualDateTime";
    private static final String dateTime = "dateTime";
    private static final String tagId = "tagId";
    private static final String value = "value";
    private static final String syncWithServer = "syncWithServer";

    // create table sql query
    private String CREATE_TABLE = "CREATE TABLE " + TABLE_Name + "("
            + Id + " INTEGER PRIMARY KEY AUTOINCREMENT," + actualDateTime + " TEXT," + dateTime + " TEXT," + tagId + " INTEGER," + value + " TEXT," + syncWithServer + " TEXT"
            + ")";

    // drop table sql query
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_Name;

    public ManualDataEntry(Context context) {
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

    public void addManualStringEntry(String actualDateTimeValue, String dateTimeValue, Long tagIdValue, String entryValue, String syncstatus) {
        SQLiteDatabase db = this.getWritableDatabase();

        db.execSQL("Insert into " + TABLE_Name + " (" + actualDateTime + "," + dateTime + "," + tagId + "," + value + "," + syncWithServer + ") " +
                "Values ('" + actualDateTimeValue + "','"
                + dateTimeValue + "', "
                + tagIdValue + " ,'" +
                entryValue + "','"
                + syncstatus + "')");
        db.close();
    }

    public int checkDataEntry(Long tagIdValue, String dateTimeValue){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * From " + TABLE_Name + " WHERE " + dateTime + "='"+dateTimeValue +"' AND "+tagId+"="+tagIdValue, null);
        if (c.moveToFirst()) {
            return 1;
        }
        return 0;
    }
    @SuppressLint("Range")
    public List<ManualEntry> getStringEntry() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor c = db.rawQuery("Select * From " + TABLE_Name + " WHERE " + syncWithServer + "='No'", null);
        List<ManualEntry> requests = new ArrayList<>();

        while (c.moveToNext()) {

            String actualtime = c.getString(c.getColumnIndex(actualDateTime));
            String datetime = c.getString(c.getColumnIndex(dateTime));
            Long tagid = (long) c.getInt(c.getColumnIndex(tagId));
            String valueString = c.getString(c.getColumnIndex(value));

            ManualEntry request = new ManualEntry(actualtime, datetime, tagid, valueString);
            requests.add(request);
        }
        return requests;
    }
    public void updateData(String actualDateTimeValue, String dateTimeValue, Long tagIdValue, String entryValue, String syncstatus){
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_Name + " SET " +
                value + "='" + entryValue + "' WHERE " + tagId + "=" + tagIdValue + " AND "+dateTime+"='"+dateTimeValue+"'");
    }
    public void updateStringFlag(String status,Long tagIdvalue,String dateTimevalue) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL("UPDATE " + TABLE_Name + " SET " +
                syncWithServer + "='" + status + "' WHERE " + tagId + "=" + tagIdvalue + " AND "+dateTime+"='"+dateTimevalue+"'");
    }
}
