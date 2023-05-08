package com.servilink.webmasterapp;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.google.android.material.textfield.TextInputEditText;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.repository.MasterRepository;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Objects;

public class LoginScreen extends AppCompatActivity {
    Button bt_login;
    TextInputEditText et_username, et_password;
    ProgressDialog pd;
    String username, password;
    ConnectionDetector connectionDetector;
    JSONParser jsonParser;
    Comman comman;
    UserDatabase userDatabase;
    MasterRepository masterRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_screen);

        bt_login = findViewById(R.id.bt_login);
        et_username = findViewById(R.id.et_userid);
        et_password = findViewById(R.id.et_password);
        pd = new ProgressDialog(this);
        comman = new Comman(LoginScreen.this);
        userDatabase = new UserDatabase(LoginScreen.this);
        masterRepository = new MasterRepository();
        connectionDetector = new ConnectionDetector(LoginScreen.this);
        jsonParser = new JSONParser();

        bt_login.setOnClickListener(view -> {

            if (connectionDetector.isConnectingToInternet()) {
                try {
                    username = Objects.requireNonNull(et_username.getText()).toString();
                    password = Objects.requireNonNull(et_password.getText()).toString();

                    if (username.isEmpty()) {
                        Comman.getToast("Please Enter UserName", LoginScreen.this);
                    } else if (password.isEmpty()) {
                        Comman.getToast("Please Enter Password", LoginScreen.this);
                    } else {
                        JSONObject object = new JSONObject();
                        object.put("username", username);
                        object.put("password", password);


                        new SendfeedbackJob().execute(object);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Comman.getToast("Please Connect Internet", LoginScreen.this);
            }
        });

    }


    @SuppressLint("StaticFieldLeak")
    private class SendfeedbackJob extends AsyncTask<JSONObject, Void, String> {

        @Override
        protected void onPreExecute() {
            pd.setMessage("Login...");
            pd.show();
        }

        @Override
        protected String doInBackground(JSONObject[] params) {
            // do above Server call here
            comman.intertnetStricNode();
            return jsonParser.uploadToUrl(ApiUrl.generateToken, params[0]);
        }

        @Override
        protected void onPostExecute(String response) {
            if (pd != null)
                pd.dismiss();
            if (!response.equals("")) {
                String message = "";

                try {
                    JSONObject object = new JSONObject(response);
                    String token = object.getString("token");

                    userDatabase.adduserToken(username, password, token);
                    Comman.saveUserData(LoginScreen.this, Comman.Key_Usertoken, token);

                } catch (Exception e) {
                    e.printStackTrace();
                }


                if (userDatabase.getcount() != 0) {
                    masterRepository.setMasterData(LoginScreen.this, ApiUrl.getPlcOfCreatedBy + "?" + "createdBy=" + userDatabase.getUserName(), "DeviceList");

                    Thread gfgThread = new Thread(() -> {
                        try {
                            String responsevalue = jsonParser.getData(ApiUrl.getuserdetail + username, Comman.getSavedUserData(LoginScreen.this, Comman.Key_Usertoken));
                            JSONObject userobject = new JSONObject(responsevalue);

                            if (userobject.getString("role").equalsIgnoreCase("ADMIN")) {
                                Intent i = new Intent(LoginScreen.this, HomeScreen.class);
                                startActivity(i);
                                finish();
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }


                    });

                    gfgThread.start();
                }
            } else {
                Comman.getToast("Please Correct Username and Password ", LoginScreen.this);

            }

        }
    }

    @Override
    public void onBackPressed() {
        Intent i = getIntent();
        startActivity(i);

    }
}
