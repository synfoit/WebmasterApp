package com.servilink.webmasterapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.servilink.webmasterapp.Adapter.ShowDataAdapter;
import com.servilink.webmasterapp.Database.ManualDataEntry;
import com.servilink.webmasterapp.Database.ManualFloatDatabase;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.Model.ManualEntry;
import com.servilink.webmasterapp.Model.ManualFloatData;

import java.util.ArrayList;
import java.util.List;

public class ShowData extends AppCompatActivity {
    Comman comman;
    JSONParser jsonParser;
    UserDatabase database;
    RecyclerView recyclerView;
    List<ManualFloatData> manualFloatData;
    List<ManualEntry> manualEntries;
    ManualFloatDatabase floatDatabase;
    ManualDataEntry dataEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.showdatalayout);
        Toolbar toolbar = findViewById(R.id.Mytoolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(R.string.app_name);
        jsonParser = new JSONParser();
        comman = new Comman(ShowData.this);
        comman.sideBar(toolbar, ShowData.this, ShowData.this);
        database = new UserDatabase(this);
        floatDatabase = new ManualFloatDatabase(this);
        dataEntry = new ManualDataEntry(this);
        manualFloatData = floatDatabase.getFloatEntry();
        manualEntries = dataEntry.getStringEntry();
        List<ManualEntry> manualEntriesList = new ArrayList<>();
        for (int i = 0; i < manualEntries.size(); i++) {
            manualEntriesList.add(manualEntries.get(i));
        }
        for (int j = 0; j < manualFloatData.size(); j++) {
            manualEntriesList.add(new ManualEntry(manualFloatData.get(j).getActualDateTime(), manualFloatData.get(j).getDateTime(), manualFloatData.get(j).getTagId(), manualFloatData.get(j).getValue() + ""));
        }
        ShowDataAdapter showDataAdapter = new ShowDataAdapter(this, manualEntriesList);
        recyclerView = findViewById(R.id.recyclerView);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(ShowData.this, LinearLayoutManager.VERTICAL, false);
        recyclerView.setAdapter(showDataAdapter);

        recyclerView.setLayoutManager(linearLayoutManager);
    }
}
