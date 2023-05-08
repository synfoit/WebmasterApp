package com.servilink.webmasterapp.Util;

import android.content.Context;

import com.servilink.webmasterapp.ApiUrl;
import com.servilink.webmasterapp.Comman;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.JSONParser;
import com.servilink.webmasterapp.Model.Item;
import com.servilink.webmasterapp.Model.dashboarLayoutd;
import com.servilink.webmasterapp.Model.dashboardLayoutTag;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class HomeUtil {
    JSONParser jsonParser;
    UserDatabase database;
    JSONObject object;
    ArrayList<Integer> arrayList = new ArrayList<>();

    public ArrayList<Item> getPlcList(Context context) {
        Comman.intertnetStricNode();
        database = new UserDatabase(context);
        jsonParser = new JSONParser();
        String response = jsonParser.getData(ApiUrl.getallplc, database.getUserToken());
        ArrayList<Item> stringList = new ArrayList<>();
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                jsonObject.getString("plcName");
                stringList.add(new Item(jsonObject.getInt("id"), jsonObject.getString("plcName")));
                arrayList.add(jsonObject.getInt("id"));

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringList;
    }

    public ArrayList<Item> getDashboardList(Context context, int position) {
        database = new UserDatabase(context);
        jsonParser = new JSONParser();
        ArrayList<Item> dashboardList = new ArrayList<>();
        String response = jsonParser.getData(ApiUrl.getdashboardName + position, database.getUserToken());
        try {
            JSONArray jsonArray = new JSONArray(response);
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                dashboardList.add(new Item(jsonObject.getInt("id"), jsonObject.getString("dashboardName")));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return dashboardList;
    }

    public ArrayList<dashboarLayoutd> getDashboardDetail(Context context, int dashboardId) {
        database = new UserDatabase(context);
        jsonParser = new JSONParser();
        Comman.intertnetStricNode();
        String response = jsonParser.getData(ApiUrl.getDashboardLayout + dashboardId, database.getUserToken());
        ArrayList<dashboarLayoutd> layoutdArrayList = new ArrayList<>();
        try {
            Comman.intertnetStricNode();
            JSONArray jsonArray = new JSONArray(response);
            for (int k = 0; k < jsonArray.length(); k++) {
                JSONObject jsonObject = jsonArray.getJSONObject(k);
                JSONArray datalist = jsonObject.getJSONArray("data");
                ArrayList<dashboardLayoutTag> tagList = new ArrayList<>();
                for (int i = 0; i < datalist.length(); i++) {
                    JSONObject datajson = datalist.getJSONObject(i);
                    tagList.add(new dashboardLayoutTag(datajson.getString("dashboardLayoutTagName"), datajson.getString("tagName"), datajson.getString("dashboardLayoutName"), datajson.getString("dashboardName")));
                }

                JSONArray jsonArray1 = jsonObject.getJSONArray("listOfValues");

                ArrayList<JSONArray> listOfValues = new ArrayList<>();
                for (int j = 0; j < jsonArray1.length(); j++) {
                    listOfValues.add(jsonArray1.getJSONArray(j));
                }
                JSONArray jsonArray2 = jsonObject.getJSONArray("valueTime");
                ArrayList<String> arrayList = new ArrayList<>();
                for (int p = 0; p < jsonArray2.length(); p++) {
                    arrayList.add(jsonArray2.getString(p));
                }
                if (jsonObject.isNull("max") || jsonObject.isNull("min")) {
                    layoutdArrayList.add(new dashboarLayoutd(jsonObject.getString("dashboarLayoutdName"), jsonObject.getString("plcName"), jsonObject.getString("chartType"), tagList, listOfValues, arrayList, jsonObject.getString("unit"), 0, 0));

                } else {
                    layoutdArrayList.add(new dashboarLayoutd(jsonObject.getString("dashboarLayoutdName"), jsonObject.getString("plcName"), jsonObject.getString("chartType"), tagList, listOfValues, arrayList, jsonObject.getString("unit"), jsonObject.getInt("min"), jsonObject.getInt("max")));
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return layoutdArrayList;
    }

}
