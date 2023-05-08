package com.servilink.webmasterapp.Util;

import android.content.Context;

import com.servilink.webmasterapp.Model.Device;
import com.servilink.webmasterapp.Model.Dictionary;
import com.servilink.webmasterapp.Model.Tag;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class DeviceUtil {
    public List<Device> getTemplateData(Context context) {
        List<Device> deviceList = new ArrayList<>();
        File file = new File(context.getFilesDir(), "DeviceList");
        FileReader fileReader;
        try {
            fileReader = new FileReader(file);
            BufferedReader bufferedReader = new BufferedReader(fileReader);
            StringBuilder stringBuilder = new StringBuilder();
            String line = bufferedReader.readLine();
            while (line != null) {
                stringBuilder.append(line).append("\n");
                line = bufferedReader.readLine();
            }
            bufferedReader.close();
            String response = stringBuilder.toString();
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
        } catch (Exception e) {
            e.printStackTrace();
        }
        return deviceList;
    }
}
