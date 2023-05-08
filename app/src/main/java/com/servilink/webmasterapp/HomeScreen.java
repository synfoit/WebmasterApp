package com.servilink.webmasterapp;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.servilink.webmasterapp.Adapter.CustomDashboardAdapter;
import com.servilink.webmasterapp.Database.ManualDataEntry;
import com.servilink.webmasterapp.Database.ManualFloatDatabase;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.Model.Item;
import com.servilink.webmasterapp.Model.ManualEntry;
import com.servilink.webmasterapp.Model.ManualFloatData;
import com.servilink.webmasterapp.Model.dashboarLayoutd;
import com.servilink.webmasterapp.Util.HomeUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class HomeScreen extends AppCompatActivity {
    JSONParser jsonParser;
    BottomNavigationView bottomNavigationView;
    FloatingActionButton fab;
    Comman comman;
    ManualFloatDatabase floatDatabase;
    ManualDataEntry dataEntry;

    UserDatabase database;
    public TextInputLayout til_plc, til_dashboard;
    AutoCompleteTextView at_plc, at_dashboard;

    HomeUtil homeUtil;
    RecyclerView recyclerView;
    CustomDashboardAdapter customDashboardAdapter;
    ArrayList<dashboarLayoutd> dashboardLayout;
    ArrayList<Item> dashboardListData;
    ArrayAdapter<Item> adapterDashboard;
    ConnectionDetector connectionDetector;
    static int plcId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.homescreen);
        Toolbar toolbar = findViewById(R.id.Mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        jsonParser = new JSONParser();
        comman = new Comman(HomeScreen.this);
        comman.sideBar(toolbar, HomeScreen.this, HomeScreen.this);
        database = new UserDatabase(this);
        bottomNavigationView = findViewById(R.id.bottomNavigationView);
        fab = findViewById(R.id.fab);
        til_plc = findViewById(R.id.tvl_plclist);
        til_dashboard = findViewById(R.id.tvl_dashboardList);
        at_plc = findViewById(R.id.act_plclist);
        at_dashboard = findViewById(R.id.act_dashboardList);
        recyclerView = findViewById(R.id.rv_dashboard);
        homeUtil = new HomeUtil();

        connectionDetector=new ConnectionDetector(HomeScreen.this);
        ArrayList<Item> plcList = homeUtil.getPlcList(HomeScreen.this);

        ArrayAdapter<Item> adapter =
                new ArrayAdapter<>(HomeScreen.this, R.layout.layout, plcList);


        at_plc.setAdapter(adapter);

        at_plc.setOnItemClickListener((adapterView, view, i, l) -> {
            Item item = plcList.get(i);

            dashboardListData = homeUtil.getDashboardList(HomeScreen.this, item.getId());
            adapterDashboard = new ArrayAdapter<>(
                    HomeScreen.this,
                    R.layout.layout,
                    dashboardListData
            );
            at_dashboard.setAdapter(adapterDashboard);
        });
        at_dashboard.setOnItemClickListener((adapterView, view, i, l) -> {
            Item item = dashboardListData.get(i);
            plcId=item.getId();
            dashboardLayout = homeUtil.getDashboardDetail(HomeScreen.this, item.getId());
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeScreen.this, LinearLayoutManager.VERTICAL, false);
            recyclerView.setLayoutManager(linearLayoutManager);
            customDashboardAdapter = new CustomDashboardAdapter(HomeScreen.this, dashboardLayout);
            recyclerView.setAdapter(customDashboardAdapter);
        });
        bottomNavigationView.setBackground(null);
        fab.setOnClickListener(view -> {
            Intent intent = new Intent(HomeScreen.this, MainActivity.class);
            startActivity(intent);
        });

      /*  final Handler handler = new Handler();

        //handler.post(r);

        final Runnable r = new Runnable() {
            public void run() {
                //Do something after 15000ms or 15sec
                //call you getData() method here
                //getData();
                dashboardLayout = homeUtil.getDashboardDetail(HomeScreen.this, plcId);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(HomeScreen.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                customDashboardAdapter = new CustomDashboardAdapter(HomeScreen.this, dashboardLayout);
                recyclerView.setAdapter(customDashboardAdapter);
                //now call the runnable again after 15sec
                //you can also add some condition to stop this

                handler.postDelayed(this, 15000); // 15000 = 15 sec
            }
        };*/
        bottomNavigationView.setOnNavigationItemSelectedListener(item -> {

            if (item.getItemId() == R.id.it_sync) {
                floatDatabase = new ManualFloatDatabase(HomeScreen.this);
                dataEntry = new ManualDataEntry(HomeScreen.this);
                jsonParser = new JSONParser();
                comman = new Comman(HomeScreen.this);
                List<ManualFloatData> floatEntry = floatDatabase.getFloatEntry();
                List<ManualEntry> manualEntries = dataEntry.getStringEntry();
                if(connectionDetector.isConnectingToInternet()) {
                    if (floatEntry.size() != 0 || manualEntries.size() != 0) {

                        List<Integer> stringList = new ArrayList<>();
                        for (int i = 0; i < floatEntry.size(); i++) {
                            try {
                                String str = new Gson().toJson(floatEntry.get(i));
                                Comman.intertnetStricNode();

                                System.out.println(" sync data" + str);
                                JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualFloatdata, str, Comman.getSavedUserData(HomeScreen.this, Comman.Key_Usertoken));
                                String message = jsonObject.getString("msg");
                                if (message.equalsIgnoreCase("Manual Float Data is already exist")) {
                                    stringList.add(1);

                                }
                                floatDatabase.updateFlag("Yes", floatEntry.get(i).getTagId(), floatEntry.get(i).getDateTime());

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        for (int j = 0; j < manualEntries.size(); j++) {
                            try {
                                String str = new Gson().toJson(manualEntries.get(j));
                                Comman.intertnetStricNode();
                                System.out.println("sync string data" + str);
                                JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualStringdata, str, Comman.getSavedUserData(HomeScreen.this, Comman.Key_Usertoken));
                                String message = jsonObject.getString("msg");
                                if (message.equalsIgnoreCase("Manual Float Data is already exist")) {
                                    stringList.add(1);

                                }
                                dataEntry.updateStringFlag("Yes", manualEntries.get(j).getTagId(), manualEntries.get(j).getDateTime());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                        if (stringList.size() == 0) {
                            new SweetAlertDialog(HomeScreen.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Sync Data!").setContentText("Send Data Server!").show();


                        } else {
                            new SweetAlertDialog(HomeScreen.this, SweetAlertDialog.WARNING_TYPE).setTitleText("Already Exits!").setContentText("Data Already Exist Want to update").show();

                        }

                    } else {
                        Comman.getToast("No any Data in Local Database", HomeScreen.this);
                    }
                }else {
                    new SweetAlertDialog(HomeScreen.this, SweetAlertDialog.ERROR_TYPE).setTitleText("No Internet").setContentText("Please connect to internet!").show();

                }

            } else if (item.getItemId() == R.id.it_showdata) {
                Intent intent = new Intent(HomeScreen.this, ShowData.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.logout) {
                //new SendData().execute();
                database.deleteEntry(database.getUserToken());
                Comman.deleteUserData(HomeScreen.this);
                Intent intent = new Intent(HomeScreen.this, LoginScreen.class);
                startActivity(intent);
            } else if (item.getItemId() == R.id.dashboard) {
                Intent intent = new Intent(HomeScreen.this, HomeScreen.class);
                startActivity(intent);

            }

            return true;
        });

    }
}
