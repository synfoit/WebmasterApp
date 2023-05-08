package com.servilink.webmasterapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;

import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.repository.MasterRepository;


@SuppressLint("CustomSplashScreen")
public class SplashScreen extends AppCompatActivity {
    UserDatabase database;
    JSONParser jsonParser;
    ConnectionDetector detector;
    UserDatabase userDatabase;
    MasterRepository masterRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        database = new UserDatabase(SplashScreen.this);
        jsonParser = new JSONParser();
        userDatabase = new UserDatabase(SplashScreen.this);
        masterRepository = new MasterRepository();
        detector = new ConnectionDetector(SplashScreen.this);
        new Handler().postDelayed(() -> {

            if (database.getcount() != 0) {
                if (detector.isConnectingToInternet()) {
                    masterRepository.setMasterData(SplashScreen.this, ApiUrl.getPlcOfCreatedBy + "?" + "createdBy=" + userDatabase.getUserName(), "DeviceList");

                    startSpecificActivity(SplashScreen.this, HomeScreen.class);
                } else {
                    startSpecificActivity(SplashScreen.this, HomeScreen.class);
                }
            } else {
                if (detector.isConnectingToInternet()) {
                    startSpecificActivity(SplashScreen.this, LoginScreen.class);
                } else {
                    Comman.getToast("Please connect to internet", SplashScreen.this);
                }
            }

        }, 2500);
    }

    public void startSpecificActivity(Context context, Class<?> otherActivityClass) {
        Intent intent = new Intent(context, otherActivityClass);
        startActivity(intent);
        finish();
    }
}
