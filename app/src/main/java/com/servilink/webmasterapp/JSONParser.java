
package com.servilink.webmasterapp;

import android.util.Log;

import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.NameValuePair;
import com.google.firebase.crashlytics.buildtools.reloc.org.apache.http.message.BasicNameValuePair;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class JSONParser {
    static JSONObject jObj = null;
    static String json = "";

    public JSONObject getJSONFromUrl(String url1, String params, String token) {
        Log.d("url",url1);
        Log.d("param",params);
        Log.d("token",token);
        String responeString = "";
        try {
            URL url = new URL(url1);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            conn.setUseCaches(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = params.getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responeString = response.toString();

                jObj=new JSONObject(responeString);

               System.out.println("responestring"+responeString+conn.getResponseCode());
            }

            return jObj;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return jObj;


    }


    public String uploadToUrl(String url1, JSONObject params) {

        String responeString = "";
        try {

            URL url = new URL(url1);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Content-Type", "application/json; utf-8");
            conn.setDoOutput(true);
            conn.setUseCaches(true);

            try (OutputStream os = conn.getOutputStream()) {
                byte[] input = params.toString().getBytes(StandardCharsets.UTF_8);
                os.write(input, 0, input.length);
            }
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8))) {
                StringBuilder response = new StringBuilder();
                String responseLine;
                while ((responseLine = br.readLine()) != null) {
                    response.append(responseLine.trim());
                }
                responeString = response.toString();
            }

            if (conn.getResponseCode() == 200) {
                return responeString;
            }

            return responeString;
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responeString;

    }


    public String registerDevice(String Url, String params, String token) {

        String responeString = "";
        try {
            URL url = new URL(Url);
            String urlParameters = "username=" + params;
            byte[] postData = urlParameters.getBytes(StandardCharsets.UTF_8);
            int postDataLength = postData.length;
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
            conn.setRequestProperty("charset", "utf-8");
            conn.setRequestProperty("Content-Length", Integer.toString(postDataLength));
            conn.connect();

            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(1);
            nameValuePairs.add(new BasicNameValuePair("username", URLEncoder.encode(params, "UTF-8")));
            try (DataOutputStream wr = new DataOutputStream(conn.getOutputStream())) {
                wr.write(postData);
            }

            System.out.println("Response Code:"
                    + conn.getResponseCode());
            System.out.println(
                    "Response Message:"
                            + conn.getResponseMessage());
            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));

            System.out.println(
                    "InstanceFollowRedirects:"
                            + conn.getInstanceFollowRedirects());

            // Printing the 1st header field
            System.out.println("Header field 1:"
                    + conn.getHeaderField(1));

            // Printing if usingProxy flag set or not
            System.out.println("Using proxy:"
                    + conn.usingProxy());
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            responeString = sb.toString();
            System.out.println("responstring" + responeString);
        } catch (Exception e) {
            e.printStackTrace();
        }

        return responeString;

    }

    public String getJsonParser(String Url) {

        CookieHandler.setDefault(new CookieManager(null, CookiePolicy.ACCEPT_ALL));
        try {
            URL url = new URL(Url);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Content-Type", "application/x-www-form-urlencoded;charset=UTF-8");

            json = "";
            jObj = null;

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream(), StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();

            json = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }

        return json;

    }

    public String getData(String Url, String token) {
        String response = "";

        try {

            URL url = new URL(Url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json; utf-8");

            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            response = sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;


    }

    public String getPostData(String Url, String token) {

        String response = "";

        try {
            System.out.println("url" + Url + "token" + token);
            URL url = new URL(Url);

            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization", "Bearer " + token);
            conn.setRequestProperty("Content-Type", "application/json; utf-8");

            conn.connect();

            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line + "\n");
            }
            br.close();
            response = sb.toString();
            //jObj = new JSONObject(sb.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;

    }


}
