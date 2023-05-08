package com.servilink.webmasterapp.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.gson.Gson;
import com.servilink.webmasterapp.ApiUrl;
import com.servilink.webmasterapp.Comman;
import com.servilink.webmasterapp.Database.ManualDataEntry;
import com.servilink.webmasterapp.Database.ManualFloatDatabase;
import com.servilink.webmasterapp.Database.UserDatabase;
import com.servilink.webmasterapp.HomeScreen;
import com.servilink.webmasterapp.JSONParser;
import com.servilink.webmasterapp.LoginScreen;
import com.servilink.webmasterapp.MainActivity;
import com.servilink.webmasterapp.Model.Drawer;
import com.servilink.webmasterapp.Model.ManualEntry;
import com.servilink.webmasterapp.Model.ManualFloatData;
import com.servilink.webmasterapp.R;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DrawerAdpter extends RecyclerView.Adapter<DrawerAdpter.MyViewHolder> {
    ManualFloatDatabase floatDatabase;
    ManualDataEntry dataEntry;
    List<Drawer> drawerItemList;
    Context context;
    JSONParser jsonParser;
    UserDatabase database;
    Comman comman;
    public static int selectedItem = 0;

    public DrawerAdpter(Context context, List<Drawer> drawerItemList) {
        this.context = context;
        this.drawerItemList = drawerItemList;

    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.customdrawerlayout, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {


        holder.name.setText(drawerItemList.get(position).getName());
        holder.imageView.setImageResource(drawerItemList.get(position).getImage());
        holder.name.setTextColor(context.getResources().getColor(R.color.purple_700));

       /* if (selectedItem == position) {
            holder.name.setTextColor(context.getResources().getColor(R.color.purple_500));
            holder.imageView.setImageResource(drawerItemList.get(position).getBlueimage());
             }*/


        holder.itemView.setOnClickListener(v -> {
            database = new UserDatabase(context);
            int previousItem = selectedItem;
            selectedItem = position;

            notifyItemChanged(previousItem);
            notifyItemChanged(position);

            if (drawerItemList.get(position).getName().equalsIgnoreCase("Home")) {
                Intent intent = new Intent(context, HomeScreen.class);
                context.startActivity(intent);
            } else if (drawerItemList.get(position).getName().equalsIgnoreCase("Sync")) {
                List<Integer> stringList = new ArrayList<>();

                floatDatabase = new ManualFloatDatabase(context);
                dataEntry = new ManualDataEntry(context);
                jsonParser = new JSONParser();
                comman = new Comman(context);
                List<ManualFloatData> floatEntry = floatDatabase.getFloatEntry();
                List<ManualEntry> manualEntries = dataEntry.getStringEntry();
                if (floatEntry.size() != 0 || manualEntries.size() != 0) {
                    try {
                        for (int i = 0; i < floatEntry.size(); i++) {

                            String str = new Gson().toJson(floatEntry.get(i));
                            System.out.println("floatEntry" + str);
                            Comman.intertnetStricNode();
                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualFloatdata, str, Comman.getSavedUserData(context, Comman.Key_Usertoken));
                            String message = jsonObject.getString("msg");
                            floatDatabase.updateFlag("Yes",floatEntry.get(i).getTagId(),floatEntry.get(i).getDateTime());

                            if (message.equalsIgnoreCase("Manual Float Data is already exist")) {
                                stringList.add(1);
                                //stringList.add(devalue[1].trim());
                            }

                        }
                        for (int j = 0; j < manualEntries.size(); j++) {

                            String str = new Gson().toJson(manualEntries.get(j));
                            System.out.println("StringEntry" + str);
                            Comman.intertnetStricNode();
                            JSONObject jsonObject = jsonParser.getJSONFromUrl(ApiUrl.putManualStringdata, str, Comman.getSavedUserData(context, Comman.Key_Usertoken));
                            dataEntry.updateStringFlag("Yes",manualEntries.get(j).getTagId(),manualEntries.get(j).getDateTime());
                            String message = jsonObject.getString("msg");
                            if (message.equalsIgnoreCase("Manual Float Data is already exist")) {
                                stringList.add(2);
                                //stringList.add(devalue[1].trim());
                            }

                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    if (stringList.size() == 0) {
                        Comman.getToast("Sync Data", context);
                    } else {
                        new SweetAlertDialog(context, SweetAlertDialog.WARNING_TYPE).setTitleText("Already Exits!").setContentText("Data Already Exist Want to update").show();

                    }
                } else {
                    Comman.getToast("No any Data in Local Database", context);
                }

            } else if (drawerItemList.get(position).getName().equalsIgnoreCase("Add ManualEntry")) {
                Intent intent = new Intent(context, MainActivity.class);
                context.startActivity(intent);
            } else if (drawerItemList.get(position).getName().equalsIgnoreCase("Logout")) {
                database.deleteEntry(database.getUserToken());
                Comman.deleteUserData(context);
                Intent intent = new Intent(context, LoginScreen.class);
                context.startActivity(intent);


            }


        });

    }

    @Override
    public int getItemCount() {
        return drawerItemList.size();
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name;

        public ImageView imageView;

        public MyViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.dr_text);
            imageView = view.findViewById(R.id.imageView);

        }
    }

}
