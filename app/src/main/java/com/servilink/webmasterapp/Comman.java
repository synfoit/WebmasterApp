package com.servilink.webmasterapp;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import android.os.StrictMode;
import android.provider.Settings;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.servilink.webmasterapp.Adapter.DrawerAdpter;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.Model.Drawer;

import org.json.JSONObject;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class Comman {


    DrawerLayout dLayout;
    Context context;
    RecyclerView dList;
    List<Drawer> list;
    @SuppressLint("SimpleDateFormat")
    public static SimpleDateFormat timeFormate = new SimpleDateFormat("HH:mm");
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss.SSS");
    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat hourFormate=new SimpleDateFormat("HH");

    @SuppressLint("SimpleDateFormat")
    static SimpleDateFormat showtimeFormat = new SimpleDateFormat("HH:mm:ss");
   @SuppressLint("SimpleDateFormat")
   public static SimpleDateFormat sqltimestamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
    public static final String DATABASENAME = "WEBMASTER";
    public static final int DATABASEVERSION = 1;
    public static final String SHARED_PREF = "userData";
    public static final String SHARED_PREF_DATE = "DataDeleteDate";
    public static final String SHARED_PREF_UUID = "AndroidUUID";
    public static String Key_UNIQUE_ID = "Android_DeviceUniqueID";
    public static String Key_Usertoken = "UserToken";
    UserDatabase database;
    DrawerAdpter drawerAdpter;
    JSONParser jsonParser;
    public Comman(Context context) {
        this.context = context;
    }

    void sideBar(Toolbar toolbar, final Context context, final Activity activity) {
        database = new UserDatabase(context);
        ActionBarDrawerToggle mDrawerToggle;
        TextView userImage,  userTest;
        CircleImageView imageView;
        jsonParser = new JSONParser();
        int[] icon = {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_insert_drive_file_24, R.drawable.ic_baseline_loop_24, R.drawable.ic_baseline_logout_24};
        int[] blue_icon = {R.drawable.ic_baseline_home_24, R.drawable.ic_baseline_insert_drive_file_24, R.drawable.ic_baseline_loop_24, R.drawable.ic_baseline_logout_24};

        String[] Sidebar_Title = {"Home", "Add ManualEntry", "Sync", "Logout"};


        list = new ArrayList<>();
        for (int i1 = 0; i1 < Sidebar_Title.length; i1++) {
            Drawer drawerBean = new Drawer(Sidebar_Title[i1], icon[i1], blue_icon[i1]);
            list.add(drawerBean);

        }
        userImage = activity.findViewById(R.id.tvemail);
        userTest = activity.findViewById(R.id.tvUsername);
        imageView = activity.findViewById(R.id.imageView);
        userTest.setText(database.getUserName());
        //imageView.setText(database.getUserName().substring(0, 2));
        try {
            Comman.intertnetStricNode();
            String responseValue = jsonParser.getData(ApiUrl.getuserdetail + database.getUserName(), Comman.getSavedUserData(context, Comman.Key_Usertoken));
            JSONObject userObject = new JSONObject(responseValue);


            String image = jsonParser.registerDevice(ApiUrl.getImageDetail, database.getUserName(), database.getUserToken());
            Bitmap bitmap = StringToBitMap(image);

            imageView.setImageBitmap(bitmap);

        } catch (Exception e) {

            e.printStackTrace();
        }




        imageView.setOnClickListener(view -> {
            Intent i = new Intent(context, UserProfile.class);
            context.startActivity(i);

        });
        ///set list
        dLayout = activity.findViewById(R.id.drawer_layout);
        dList = activity.findViewById(R.id.left_drawer);
        drawerAdpter = new DrawerAdpter(context, list);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);

        dList.setLayoutManager(linearLayoutManager);

        dList.setAdapter(drawerAdpter);

        mDrawerToggle = new ActionBarDrawerToggle(activity, dLayout,
                toolbar,
                R.string.Open, R.string.Close) {
            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);

            }

            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);

            }
        };
        mDrawerToggle.getDrawerArrowDrawable().setColor(context.getResources().getColor(R.color.purple_500));
        dLayout.setDrawerListener(mDrawerToggle);
        mDrawerToggle.syncState();

    }

    public static void getToast(String message, Context context) {

        new Handler(Looper.getMainLooper()).post(() -> {
            Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
            toast.show();
        });

    }

    public static Timestamp date2Timestamp(String str) throws Exception {
        if (str == null || str.length() == 0)
            return null;
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss.SSS");
        Date date = sdf.parse(str);//  ww w .  ja va2  s  . c  o  m
        return new Timestamp(date.getTime());
    }

    public static void saveUserData(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);

        editor.apply();
    }

    public static String getSavedUserData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        return pref.getString(key, "");

    }

    public static void saveUpdateDate(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);

        editor.apply();
    }

    public static String getUpdateDate(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF, 0);
        return pref.getString(key, "");

    }

    public static void deleteUpdateData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Comman.SHARED_PREF_DATE, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void deleteUserData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Comman.SHARED_PREF, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveDateofDeleteData(Context context, String key, String value) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_DATE, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getDateofDeleteData(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_DATE, 0);
        return pref.getString(key, "");

    }

    public static void deleteDateData(Context context) {
        SharedPreferences preferences = context.getSharedPreferences(Comman.SHARED_PREF_DATE, 0);
        SharedPreferences.Editor editor = preferences.edit();
        editor.clear();
        editor.apply();
    }

    public static void saveUUID(Context context, String key, String value) {
        Log.d("key//", key);
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_UUID, 0);
        SharedPreferences.Editor editor = pref.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public static String getUUID(Context context, String key) {
        SharedPreferences pref = context.getSharedPreferences(SHARED_PREF_UUID, 0);
        return pref.getString(key, "");

    }

    public static void intertnetStricNode() {
        StrictMode.ThreadPolicy gfgPolicy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(gfgPolicy);
    }


    public static long dateDifference(Date startDate, Date endDate) {
        //milliseconds
        long different = endDate.getTime() - startDate.getTime();


        long secondsInMilli = 1000;
        long minutesInMilli = secondsInMilli * 60;
        long hoursInMilli = minutesInMilli * 60;
        long daysInMilli = hoursInMilli * 24;

        long elapsedDays = different / daysInMilli;
        different = different % daysInMilli;

        long elapsedHours = different / hoursInMilli;
        different = different % hoursInMilli;

        long elapsedMinutes = different / minutesInMilli;
        different = different % minutesInMilli;

        long elapsedSeconds = different / secondsInMilli;

        System.out.printf(
                "%d days, %d hours, %d minutes, %d seconds%n",
                elapsedDays, elapsedHours, elapsedMinutes, elapsedSeconds);

        return elapsedDays;
    }

    @RequiresApi(api = Build.VERSION_CODES.O)
    @SuppressLint("HardwareIds")
    public static String androidUniqeId(Context context) throws SecurityException, NullPointerException {
        return Settings.Secure.getString(context.getContentResolver(),
                Settings.Secure.ANDROID_ID);
    }
    public static String addHour(String myTime,int number,SimpleDateFormat df,int min)
    {
        try
        {
            //SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Date d = df.parse(myTime);
            Calendar cal = Calendar.getInstance();
            cal.setTime(d);
            cal.add(Calendar.HOUR, number);
            cal.add(Calendar.MINUTE,min);
            String newTime = df.format(cal.getTime());
            return newTime;
        }
        catch(ParseException e)
        {
            System.out.println(" Parsing Exception");
        }
        return null;

    }


    public Bitmap StringToBitMap(String encodedString) {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);

            return BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);

}

}
