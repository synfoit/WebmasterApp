package com.servilink.webmasterapp.repository;

import android.content.Context;
import android.util.Log;


import com.servilink.webmasterapp.Comman;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.JSONParser;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;

public class MasterRepository {
    JSONParser jsonParser = new JSONParser();
    Comman comman;
    UserDatabase database;
    public void setMasterData(Context context,String url,String fileName) {
        comman=new Comman(context);
        Thread gfgThread = new Thread(() -> {
            try {
                comman.intertnetStricNode();
                database =new UserDatabase(context);

                String jsonData = jsonParser.getPostData(url,database.getUserToken());
                File file = new File(context.getFilesDir(), fileName);
                if (file.exists()) {

                    file.delete();

                }
                FileWriter fileWriter = new FileWriter(file,true);
                BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                bufferedWriter.write(jsonData);
                bufferedWriter.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
        });

        gfgThread.start();

    }
}
