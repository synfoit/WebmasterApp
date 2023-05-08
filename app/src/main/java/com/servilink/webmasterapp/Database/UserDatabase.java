package com.servilink.webmasterapp.Database;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.servilink.webmasterapp.Comman;


public class UserDatabase extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "Elogbook.db";
    //table name
    private static final String TABLE_Name = "User";
    private static final String userName = "userName";
    private static final String id = "id";
    private static final String passWord = "passWord";
    private static final String userToken = "userToken";
    private static final String firstName = "firstName";
    private static final String lastName = "lastName";
    private static final String email = "email";
    private static final String phone = "phone";
    private static final String dob = "dob";
    private static final String role = "role";
    private static final String higherAuth = "higherAuth";


    // create table sql query
    private String CREATE_TABLE = "CREATE TABLE " + TABLE_Name + "("
            + userName + " TEXT," + passWord + " TEXT," + userToken + " TEXT"
            + ")";

    // drop table sql query
    private String DROP_TABLE = "DROP TABLE IF EXISTS " + TABLE_Name;

    /**
     * Constructor
     *
     * @param context
     */
    public UserDatabase(Context context) {
        super(context, Comman.DATABASENAME, null, Comman.DATABASEVERSION);
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

    public void adduserToken(String username, String password, String token) {

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();

        values.put(userName, username);
        values.put(passWord, password);
        values.put(userToken, token);


        // Inserting Row
        db.insert(TABLE_Name, null, values);
        db.close();
    }


    public void deleteEntry(String usertoken) {
        SQLiteDatabase db = this.getWritableDatabase();
        // delete user record by id
        db.delete(TABLE_Name, userToken + " = ?",
                new String[]{usertoken});
        db.close();
    }

    @SuppressLint("Range")
    public String getUserToken() {

        String usertoken = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String Select_data = " Select " + userToken + " From " + TABLE_Name;
        Cursor cursor = db.rawQuery(Select_data, null);
        while (cursor.moveToNext()) {

            usertoken = cursor.getString(cursor.getColumnIndex(userToken));

        }
        cursor.close();
        db.close();

        return usertoken;
    }

    @SuppressLint("Range")
    public String getUserName() {

        String username = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String Select_data = " Select " + userName + " From " + TABLE_Name;
        Cursor cursor = db.rawQuery(Select_data, null);
        while (cursor.moveToNext()) {

            username = cursor.getString(cursor.getColumnIndex(userName));

        }
        cursor.close();
        db.close();

        return username;
    }

    @SuppressLint("Range")
    public String getUserpassword() {

        String password = null;
        SQLiteDatabase db = this.getReadableDatabase();
        String Select_data = " Select " + passWord + " From " + TABLE_Name;
        Cursor cursor = db.rawQuery(Select_data, null);
        while (cursor.moveToNext()) {

            password = cursor.getString(cursor.getColumnIndex(passWord));

        }
        cursor.close();
        db.close();

        return password;
    }

}
