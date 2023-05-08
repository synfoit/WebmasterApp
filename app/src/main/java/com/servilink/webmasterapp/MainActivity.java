package com.servilink.webmasterapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;

import com.servilink.webmasterapp.Adapter.DeviceListAdapter;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.Model.Device;
import com.servilink.webmasterapp.Model.Dictionary;
import com.servilink.webmasterapp.Model.Tag;
import com.servilink.webmasterapp.Util.DeviceUtil;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    JSONParser jsonParser;
    ProgressDialog pd;
    Comman comman;
    RecyclerView recyclerView;
    DeviceListAdapter deviceListAdapter;
    UserDatabase database;
    ConnectionDetector connectionDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.Mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        jsonParser = new JSONParser();
        comman = new Comman(MainActivity.this);
        comman.sideBar(toolbar, MainActivity.this, MainActivity.this);
        pd = new ProgressDialog(MainActivity.this);
        database = new UserDatabase(MainActivity.this);
        connectionDetector = new ConnectionDetector(MainActivity.this);
        recyclerView = findViewById(R.id.recyclerView);
        new MainActivity.SendFeedbackJob().execute();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(MainActivity.this, LinearLayoutManager.VERTICAL, false);

        recyclerView.setLayoutManager(linearLayoutManager);


    }

    @SuppressLint("StaticFieldLeak")
    private class SendFeedbackJob extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            pd.setMessage("Load...");
            pd.show();
        }

        @Override
        protected String doInBackground(Void[] params) {
            // do above Server call here
            Comman.intertnetStricNode();
            if (connectionDetector.isConnectingToInternet()) {
                return jsonParser.getPostData(ApiUrl.getPlcOfCreatedBy + "?" + "createdBy=" + database.getUserName(), Comman.getSavedUserData(MainActivity.this, Comman.Key_Usertoken));
            } else {
                return "Internet is not connected";
            }
        }

        @Override
        protected void onPostExecute(String response) {
            if (pd != null)
                pd.dismiss();
            if (!response.equals("")) {
                if (response.equalsIgnoreCase("Internet is not connected")) {
                    try {
                        DeviceUtil deviceUtil = new DeviceUtil();
                        List<Device> deviceList = deviceUtil.getTemplateData(MainActivity.this);
                        deviceListAdapter = new DeviceListAdapter(MainActivity.this, deviceList);
                        recyclerView.setAdapter(deviceListAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    List<Device> deviceList = new ArrayList<>();
                    try {
                        JSONArray mainTemplateList = new JSONArray(response);

                        for (int i = 0; i < mainTemplateList.length(); i++) {
                            JSONObject jsonDevice = new JSONObject(mainTemplateList.getString(i));
                            JSONArray tagWithDropdown = jsonDevice.getJSONArray("tagWithDropdown");
                            List<Tag> tagList = new ArrayList<>();
                            for (int j = 0; j < tagWithDropdown.length(); j++) {
                                JSONObject jsonObject = tagWithDropdown.getJSONObject(j);
                                List<String> stringList = new ArrayList<>();
                                List<Dictionary> dictionaries = new ArrayList<>();
                                JSONArray objectJSONArray = jsonObject.getJSONArray("dropdownTagValues");
                                for (int k = 0; k < objectJSONArray.length(); k++) {
                                    JSONObject jsonObject1 = objectJSONArray.getJSONObject(k);
                                    Long tagId = jsonObject1.getLong("tagId");
                                    String value = jsonObject1.getString("dropDownValue");
                                    String tagName = jsonObject1.getString("tagName");
                                    Long id = jsonObject1.getLong("id");
                                    dictionaries.add(new Dictionary(id, tagId, value, tagName));
                                    stringList.add(value);
                                }
                                if (jsonObject.isNull("status")) {
                                    Tag device = new Tag(jsonObject.getLong("tagId"), jsonObject.getLong("plcId"), jsonObject.getString("datatype"), jsonObject.getString("tagName"), jsonObject.getString("tagAddress"), false, jsonObject.getString("tagType"), dictionaries, stringList);
                                    tagList.add(device);
                                } else {
                                    Tag device = new Tag(jsonObject.getLong("tagId"), jsonObject.getLong("plcId"), jsonObject.getString("datatype"), jsonObject.getString("tagName"), jsonObject.getString("tagAddress"), jsonObject.getBoolean("status"), jsonObject.getString("tagType"), dictionaries, stringList);
                                    tagList.add(device);
                                }
                            }
                            Device device = new Device(Integer.parseInt(jsonDevice.getString("id")), jsonDevice.getString("plcName"), jsonDevice.getString("plcCompanyName"), jsonDevice.getString("plcTypeName"), jsonDevice.getString("plcPyFile"), jsonDevice.getString("plcIp"), jsonDevice.getString("plcStatus"), jsonDevice.getString("plcCreatedBy"), jsonDevice.getString("dashboardType"), tagList);
                            deviceList.add(device);

                        }

                        deviceListAdapter = new DeviceListAdapter(MainActivity.this, deviceList);
                        recyclerView.setAdapter(deviceListAdapter);
                    } catch (Exception e) {
                        e.printStackTrace();
                        //jsonObject.getBoolean("status")
                    }

                }
            } else {
                database.deleteEntry(database.getUserToken());
                Comman.deleteUserData(MainActivity.this);
                Intent i = new Intent(MainActivity.this, LoginScreen.class);
                startActivity(i);
                finish();
                Comman.getToast("Please Correct Username and Password ", MainActivity.this);
            }
        }
    }
}