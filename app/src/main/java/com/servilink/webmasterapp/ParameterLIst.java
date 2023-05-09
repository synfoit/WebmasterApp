package com.servilink.webmasterapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.servilink.webmasterapp.Adapter.TagAdapter;
import com.servilink.webmasterapp.Database.ManualDataEntry;
import com.servilink.webmasterapp.Database.ManualFloatDatabase;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.Model.Data;
import com.servilink.webmasterapp.Model.ManualEntry;
import com.servilink.webmasterapp.Model.ManualFloatData;
import com.servilink.webmasterapp.Model.Tag;

import org.json.JSONArray;
import org.json.JSONObject;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class ParameterLIst extends AppCompatActivity {
    JSONParser jsonParser;
    ProgressDialog pd;
    Comman comman;
    RecyclerView recyclerView;
    Button cancel_button, submit;
    TextInputEditText tv_dateSection, et_time;
    String selectedDate;
    static String selectedTime;
    @SuppressLint("SimpleDateFormat")
    public TextInputLayout selection, dateSection;
    final Calendar myCalendar = Calendar.getInstance();
    ConnectionDetector connectionDetector;
    TagAdapter parameterLIst;
    int mHour, mMinute;
    ManualFloatDatabase floatDatabase;
    ManualDataEntry dataEntry;
    UserDatabase database;
    LinearLayoutManager linearLayoutManager;
    Date date1;
    static String tagstringlist="";
    static String tagfloatlist="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.parameterlist);
        Toolbar toolbar = findViewById(R.id.Mytoolbar);
        setSupportActionBar(toolbar);
        TextView tv_title = findViewById(R.id.tv_toolbar);

        jsonParser = new JSONParser();
        comman = new Comman(ParameterLIst.this);
        comman.sideBar(toolbar, ParameterLIst.this, ParameterLIst.this);
        pd = new ProgressDialog(ParameterLIst.this);
        connectionDetector = new ConnectionDetector(ParameterLIst.this);
        floatDatabase = new ManualFloatDatabase(ParameterLIst.this);
        dataEntry = new ManualDataEntry(ParameterLIst.this);
        database = new UserDatabase(ParameterLIst.this);

        List<Tag> deviceList = (List<Tag>) getIntent().getExtras().getSerializable("tagIdList");
        String deviceName = getIntent().getStringExtra("deviceName");
        tv_title.setText(deviceName);

        selection = findViewById(R.id.cpdTypeLayout);
        dateSection = findViewById(R.id.qqet_date);
        tv_dateSection = findViewById(R.id.et_date);
        et_time = findViewById(R.id.et_time1);
        cancel_button = findViewById(R.id.bt_cancel);
        recyclerView = findViewById(R.id.recyclerView);
        submit = findViewById(R.id.bt_sumit);

        selectedDate = Comman.dateFormat.format(new Date());
        tv_dateSection.setText(selectedDate);

        final Calendar c = Calendar.getInstance();
        mHour = c.get(Calendar.HOUR_OF_DAY);
        mMinute = c.get(Calendar.MINUTE);
        selectedTime = Comman.hourFormate.format(new Date()) + ":" + "00" + ":" + "00.000";
        String showtime = Comman.hourFormate.format(new Date()) + ":" + "00" + ":" + "00";
        et_time.setText(showtime);
        linearLayoutManager = new LinearLayoutManager(ParameterLIst.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(linearLayoutManager);

        try {

            for(int k=0;k<deviceList.size();k++){
                Tag tag=deviceList.get(k);
                if(tag.getDatatype().equalsIgnoreCase("Text") || tag.getDatatype().equalsIgnoreCase("Boolean") || tag.getDatatype().equalsIgnoreCase("Dropdown"))
                {
                tagstringlist=tag.getTagId()+","+tagstringlist;
                }
                else if(tag.getDatatype().equalsIgnoreCase("Numeric")){
                    tagfloatlist=tag.getTagId()+","+tagfloatlist;
                }
            }


            System.out.println(tagstringlist);
            System.out.println(tagfloatlist);
            List<ManualFloatData> manualFloatData=new ArrayList<>();
            List<ManualEntry> manualEntryList=new ArrayList<>();
            try {
                date1 = Comman.sqltimestamp.parse(tv_dateSection.getText().toString() + " " + selectedTime);

                String selectDateTime = Comman.sqltimestamp.format(date1);
                Data data=new Data(selectDateTime,"1 Hour",tagstringlist);
                String str = new Gson().toJson(data);
                JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.getManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                JSONArray message = jsonObject.getJSONArray("manualStrings");

                for (int m=0;m<message.length();m++){
                    JSONObject ob=message.getJSONObject(m);

                    ManualEntry manualEntry=new ManualEntry(ob.getString("id"),Long.parseLong(ob.getString("tagId")), ob.getString("value"));

                    manualEntryList.add(manualEntry);}


                Data data1=new Data(selectDateTime,"1 Hour",tagfloatlist);
                String str1 = new Gson().toJson(data1);
                JSONObject jsonObject1 = jsonParser.getJSONFromUrl(ApiUrl.getManualFloatdata, str1, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                JSONArray message1 = jsonObject1.getJSONArray("manualFloat");
                Log.d("manualfloat",message1.toString());
                for (int n=0;n<message1.length();n++){
                    JSONObject ob1=message1.getJSONObject(n);

                    ManualFloatData floatData=new ManualFloatData( ob1.getString("id"),Long.parseLong( ob1.getString("tagId")),Float.valueOf(ob1.getInt("value")));
                    manualFloatData.add(floatData);
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            parameterLIst = new TagAdapter(ParameterLIst.this, deviceList,manualFloatData,manualEntryList);
            recyclerView.setAdapter(parameterLIst);
        }
        catch (Exception e){
            e.printStackTrace();
        }


        TimePickerDialog timePicker = new TimePickerDialog(ParameterLIst.this, (timePicker1, hour, minute) -> {
            Calendar newAlarmTime = Calendar.getInstance();
            newAlarmTime.set(Calendar.HOUR_OF_DAY, hour);
            newAlarmTime.set(Calendar.MINUTE, minute);
            newAlarmTime.set(Calendar.SECOND, 0);
            try {
                selectedTime = Comman.timeFormat.format(Objects.requireNonNull(Comman.timeFormat.parse(hour + ":" + "00" + ":" + "00.000")));
                et_time.setText(Comman.showtimeFormat.format(Objects.requireNonNull(Comman.showtimeFormat.parse(hour + ":" + "00" + ":" + "00"))));


                List<ManualFloatData> manualFloatData=new ArrayList<>();
                List<ManualEntry> manualEntryList=new ArrayList<>();
                try {
                    date1 = Comman.sqltimestamp.parse(tv_dateSection.getText().toString() + " " + selectedTime);

                    String selectDateTime = Comman.sqltimestamp.format(date1);
                    Data data=new Data(selectDateTime,"1 Hour",tagstringlist);
                    String str = new Gson().toJson(data);
                    JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.getManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));

                    if(jsonObject!=null) {
                        JSONArray message = jsonObject.getJSONArray("manualString");

                        for (int m = 0; m < message.length(); m++) {
                            JSONObject ob = message.getJSONObject(m);

                            ManualEntry manualEntry = new ManualEntry(ob.getString("id"), Long.parseLong(ob.getString("tagId")), ob.getString("value"));

                            manualEntryList.add(manualEntry);
                        }
                    }

                    Data data1=new Data(selectDateTime,"1 Hour",tagfloatlist);
                    String str1 = new Gson().toJson(data1);
                    JSONObject jsonObject1 = jsonParser.getJSONFromUrl(ApiUrl.getManualFloatdata, str1, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                    if(jsonObject1!=null) {
                        JSONArray message1 = jsonObject1.getJSONArray("manualFloat");
                        for (int m = 0; m < message1.length(); m++) {
                            JSONObject ob = message1.getJSONObject(m);

                            ManualFloatData floatData = new ManualFloatData(ob.getString("id"), Long.parseLong(ob.getString("tagId")), Float.valueOf(ob.getString("value")));
                            manualFloatData.add(floatData);
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                linearLayoutManager = new LinearLayoutManager(ParameterLIst.this, LinearLayoutManager.VERTICAL, false);
                recyclerView.setLayoutManager(linearLayoutManager);
                parameterLIst = new TagAdapter(ParameterLIst.this, deviceList,manualFloatData,manualEntryList);
                recyclerView.setAdapter(parameterLIst);
            } catch (Exception e) {
                e.printStackTrace();
            }
            //code here
        }, mHour, mMinute, true);
        DatePickerDialog.OnDateSetListener date = (view, year, month, day) -> {
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, month);
            myCalendar.set(Calendar.DAY_OF_MONTH, day);
            updateLabel();


        };
        //new ParameterLIst.SendFeedbackJob().execute(deviceId);

        tv_dateSection.setOnClickListener(view -> new DatePickerDialog(ParameterLIst.this, date, myCalendar.get(Calendar.YEAR), myCalendar.get(Calendar.MONTH), myCalendar.get(Calendar.DAY_OF_MONTH)).show());
        et_time.setOnClickListener(view -> timePicker.show());

        submit.setOnClickListener(view -> {
            Map<String, EditText> parameterIntList = parameterLIst.getIntList();
            Map<String, EditText> parameterTextList = parameterLIst.getList();
            Map<String, String> StringMap = parameterLIst.getSelectionList();
            Map<String, String> radioMap = parameterLIst.getRadioHashMap();

            String actualDateTime = Comman.sqltimestamp.format(new Date());

            if (parameterIntList.values().size() != 0 || parameterTextList.values().size() != 0 || StringMap.size() != 0 || radioMap.size() != 0) {
                try {

                    date1 = Comman.sqltimestamp.parse(tv_dateSection.getText().toString() + " " + selectedTime);

                    String selectDateTime = Comman.sqltimestamp.format(date1);
                    new SendDataToServer(parameterIntList, parameterTextList, StringMap, radioMap, actualDateTime, selectDateTime).execute();


                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Comman.getToast("Please Enter Some Value", ParameterLIst.this);
            }


        });
        cancel_button.setOnClickListener(view -> {
            Intent i = new Intent(ParameterLIst.this, MainActivity.class);
            startActivity(i);
            finish();
        });

    }


    @SuppressLint("StaticFieldLeak")
    private class SendDataToServer extends AsyncTask<Integer, Void, Integer> {
        Map<String, EditText> parameterIntList;
        Map<String, EditText> parameterTextList;
        Map<String, String> StringMap;
        Map<String, String> radioMap;
        String actualDateTime;
        String selectDateTime;
        int count = 0;
        ArrayAdapter<String> stringList = new ArrayAdapter<>(ParameterLIst.this, android.R.layout.select_dialog_singlechoice);

        public SendDataToServer(Map<String, EditText> parameterIntList, Map<String, EditText> parameterTextList, Map<String, String> StringMap, Map<String, String> radioMap, String actualDateTime, String selectDateTime) {
            this.parameterIntList = parameterIntList;
            this.parameterTextList = parameterTextList;
            this.StringMap = StringMap;
            this.radioMap = radioMap;
            this.actualDateTime = actualDateTime;
            this.selectDateTime = selectDateTime;
        }

        @Override
        protected void onPreExecute() {
            pd.setMessage("Send...");
            pd.show();
        }

        @Override
        protected Integer doInBackground(Integer[] params) {
            Comman.intertnetStricNode();
            int data = 0;

            if (connectionDetector.isConnectingToInternet()) {

                for (Map.Entry<String, EditText> entry : parameterIntList.entrySet()) {

                    String[] devalue = entry.getKey().split(",");
                    if (!entry.getValue().getText().toString().isEmpty()) {
                        try {
                            ManualFloatData manualDataDetail = new ManualFloatData(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), Float.valueOf(entry.getValue().getText().toString().trim()));
                            String str = new Gson().toJson(manualDataDetail);
                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualFloatdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                            String message = jsonObject.getString("msg");
                            if (message.equalsIgnoreCase("Manual Float Data is already exist")) {
                                stringList.add(devalue[1].trim());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data++;
                    }
                }
                for (Map.Entry<String, EditText> entry : parameterTextList.entrySet()) {

                    String[] devalue = entry.getKey().split(",");

                    if (!entry.getValue().getText().toString().isEmpty()) {
                        try {
                            ManualEntry manualDataDetail = new ManualEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue().getText().toString());
                            String str = new Gson().toJson(manualDataDetail);
                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                            String message = jsonObject.getString("msg");
                            if (message.equalsIgnoreCase("Manual String Data is already exist")) {
                                stringList.add(devalue[1].trim());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data++;
                    }
                }
                for (Map.Entry<String, String> entry : StringMap.entrySet()) {

                    String[] devalue = entry.getKey().split(",");

                    if (!entry.getValue().isEmpty()) {
                        try {

                            ManualEntry manualDataDetail = new ManualEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue());
                            String str = new Gson().toJson(manualDataDetail);
                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                            String message = jsonObject.getString("msg");
                            if (message.equalsIgnoreCase("Manual String Data is already exist")) {
                                stringList.add(devalue[1].trim());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data++;

                    }
                }
                for (Map.Entry<String, String> entry : radioMap.entrySet()) {

                    String[] devalue = entry.getKey().split(",");

                    if (!entry.getValue().isEmpty()) {
                        try {
                            ManualEntry manualDataDetail = new ManualEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue());
                            String str = new Gson().toJson(manualDataDetail);
                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                            String message = jsonObject.getString("msg");
                            if (message.equalsIgnoreCase("Manual String Data is already exist")) {
                                stringList.add(devalue[1].trim());
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data++;
                    }
                }
                if (data == 0) {
                    count = 5;
                } else if (stringList.isEmpty()) {
                    count = 1;

                } else if (!stringList.isEmpty()) {
                    count = 2;
                }


            } else {
                for (Map.Entry<String, EditText> entry : parameterIntList.entrySet()) {

                    String[] devalue = entry.getKey().split(",");

                    if (!entry.getValue().getText().toString().isEmpty()) {
                        try {
                            if (floatDatabase.checkDataEntry(Long.parseLong(devalue[0].trim()), selectDateTime) == 0) {
                                floatDatabase.addManualFloatEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), Float.valueOf(entry.getValue().getText().toString().trim()), "No");
                            } else {
                                stringList.add(devalue[1].trim());


                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data++;
                    }
                }
                for (Map.Entry<String, EditText> entry : parameterTextList.entrySet()) {

                    String[] devalue = entry.getKey().split(",");

                    if (!entry.getValue().getText().toString().isEmpty()) {
                        try {
                            if (dataEntry.checkDataEntry(Long.parseLong(devalue[0].trim()), selectDateTime) == 0) {
                                dataEntry.addManualStringEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue().getText().toString(), "No");
                            } else {
                                stringList.add(devalue[1].trim());

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        data++;
                    }
                }
                for (Map.Entry<String, String> entry : StringMap.entrySet()) {

                    String[] devalue = entry.getKey().split(",");
                    if (!entry.getValue().isEmpty()) {
                        try {

                            if (dataEntry.checkDataEntry(Long.parseLong(devalue[0].trim()), selectDateTime) == 0) {
                                dataEntry.addManualStringEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue(), "No");
                            } else {
                                stringList.add(devalue[1].trim());

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        data++;
                    }
                }
                for (Map.Entry<String, String> entry : radioMap.entrySet()) {
                    String[] devalue = entry.getKey().split(",");
                    if (!entry.getValue().isEmpty()) {
                        try {

                            if (dataEntry.checkDataEntry(Long.parseLong(devalue[0].trim()), selectDateTime) == 0) {
                                dataEntry.addManualStringEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue(), "No");
                            } else {
                                stringList.add(devalue[1].trim());

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        data++;
                    }
                }
                if (data == 0) {
                    count = 5;
                } else if (stringList.isEmpty()) {
                    count = 3;

                } else if (!stringList.isEmpty()) {
                    count = 4;
                }


            }

            // do above Server call here

            return count;
        }

        @Override
        protected void onPostExecute(Integer response) {
            if (pd != null)
                pd.dismiss();

            if (response == 1) {
                new SweetAlertDialog(ParameterLIst.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Submit!").setContentText("Send Data Server!").show();

            } else if (response == 3) {
                new SweetAlertDialog(ParameterLIst.this, SweetAlertDialog.SUCCESS_TYPE).setTitleText("Saved!").setContentText("Data Save Local!").show();

            } else if (response == 4) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ParameterLIst.this);
                builderSingle.setTitle("Already Exited Tag Value ");
                builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());

                builderSingle.setPositiveButton("Update", (dialogInterface, i) -> {
                    for (Map.Entry<String, EditText> entry : parameterIntList.entrySet()) {

                        String[] devalue = entry.getKey().split(",");

                        if (!entry.getValue().getText().toString().isEmpty()) {
                            try {
                                floatDatabase.updateData(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), Float.valueOf(entry.getValue().getText().toString()), "No");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    for (Map.Entry<String, EditText> entry : parameterTextList.entrySet()) {

                        String[] devalue = entry.getKey().split(",");

                        if (!entry.getValue().getText().toString().isEmpty()) {
                            try {
                                dataEntry.updateData(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue().getText().toString(), "No");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                    for (Map.Entry<String, String> entry : StringMap.entrySet()) {

                        String[] devalue = entry.getKey().split(",");
                        if (!entry.getValue().isEmpty()) {
                            try {

                                dataEntry.updateData(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue(), "No");


                            } catch (Exception e) {
                                e.printStackTrace();
                            }


                        }
                    }
                    for (Map.Entry<String, String> entry : radioMap.entrySet()) {

                        String[] devalue = entry.getKey().split(",");
                        if (!entry.getValue().isEmpty()) {
                            try {
                                dataEntry.updateData(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue(), "No");

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                });
                builderSingle.setAdapter(stringList, (dialog, which) -> {
                });
                builderSingle.show();
            } else if (response == 2) {
                AlertDialog.Builder builderSingle = new AlertDialog.Builder(ParameterLIst.this);
                builderSingle.setTitle("Already Exited Tag Value ");
                builderSingle.setNegativeButton("cancel", (dialog, which) -> dialog.dismiss());
                builderSingle.setPositiveButton("Update", (dialogInterface, i) ->
                        {
                            Thread gfgThread1 = new Thread(() -> {
                                for (Map.Entry<String, EditText> entry : parameterIntList.entrySet()) {

                                    String[] devalue = entry.getKey().split(",");
                                    if (!entry.getValue().getText().toString().isEmpty()) {
                                        try {
                                            ManualFloatData manualDataDetail = new ManualFloatData(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), Float.valueOf(entry.getValue().getText().toString().trim()));
                                            String str = new Gson().toJson(manualDataDetail);
                                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.updateManualFloatdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                                            String message = jsonObject.getString("msg");

                                            /*if (message.equalsIgnoreCase("Manual Float Data is already exist")) {
                                                stringList.add(devalue[1].trim());
                                            }*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                for (Map.Entry<String, EditText> entry : parameterTextList.entrySet()) {

                                    String[] devalue = entry.getKey().split(",");
                                    if (!entry.getValue().getText().toString().isEmpty()) {
                                        try {
                                            ManualEntry manualDataDetail = new ManualEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue().getText().toString());
                                            String str = new Gson().toJson(manualDataDetail);
                                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.updateManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                                            String message = jsonObject.getString("msg");

                                           /* if (message.equalsIgnoreCase("Manual String Data is already exist")) {
                                                stringList.add(devalue[1].trim());
                                            }*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                                for (Map.Entry<String, String> entry : StringMap.entrySet()) {

                                    String[] devalue = entry.getKey().split(",");
                                    if (!entry.getValue().isEmpty()) {
                                        try {

                                            ManualEntry manualDataDetail = new ManualEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue());
                                            String str = new Gson().toJson(manualDataDetail);
                                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.updateManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                                            String message = jsonObject.getString("msg");

                                           /* if (message.equalsIgnoreCase("Manual String Data is already exist")) {
                                                stringList.add(devalue[1].trim());
                                            }*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }


                                    }
                                }
                                for (Map.Entry<String, String> entry : radioMap.entrySet()) {

                                    String[] devalue = entry.getKey().split(",");
                                    if (!entry.getValue().isEmpty()) {
                                        try {
                                            ManualEntry manualDataDetail = new ManualEntry(actualDateTime, selectDateTime, Long.parseLong(devalue[0].trim()), entry.getValue());
                                            String str = new Gson().toJson(manualDataDetail);
                                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.updateManualStringdata, str, Comman.getSavedUserData(ParameterLIst.this, Comman.Key_Usertoken));
                                            String message = jsonObject.getString("msg");

                                          /*  if (message.equalsIgnoreCase("Manual String Data is already exist")) {
                                                stringList.add(devalue[1].trim());
                                            }*/
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                            gfgThread1.start();
                            dialogInterface.dismiss();
                        }
                );
                builderSingle.setAdapter(stringList, (dialog, which) -> {
                });
                builderSingle.show();
            } else if (response == 5) {
                new SweetAlertDialog(ParameterLIst.this, SweetAlertDialog.WARNING_TYPE).setTitleText("No Value!").setContentText("Please Enter Some Value!").show();

            }
        }
    }

    private void updateLabel() {

        tv_dateSection.setText(Comman.dateFormat.format(myCalendar.getTime()));
    }


}
