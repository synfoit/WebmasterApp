package com.servilink.webmasterapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.textfield.TextInputLayout;
import com.servilink.webmasterapp.Model.ManualEntry;
import com.servilink.webmasterapp.Model.ManualFloatData;
import com.servilink.webmasterapp.Model.Tag;
import com.servilink.webmasterapp.R;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TagAdapter extends RecyclerView.Adapter<TagAdapter.MyViewHolder> {
    List<Tag> deviceItemList;

    Activity context;
    Map<String, EditText> editTextHashMap = new HashMap<>();
    Map<String, EditText> editIntHashMap = new HashMap<>();
    Map<String, String> dropdownHashMap = new HashMap<>();
    Map<String, String> radioHashMap = new HashMap<>();
    List<ManualFloatData> manualFloatData;
    List<ManualEntry> manualEntryList;

    public TagAdapter(Activity context, List<Tag> deviceItemList, List<ManualFloatData> manualFloatData,List<ManualEntry> manualEntryList) {
        this.context = context;
        this.deviceItemList = deviceItemList;
        this.manualFloatData=manualFloatData;
        this.manualEntryList=manualEntryList;

    }

    @NonNull
    @Override
    public TagAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custome_parameterlist, parent, false);
        return new TagAdapter.MyViewHolder(itemView);
    }


    @Override
    public void onBindViewHolder(final TagAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {

        try {


            holder.device_name.setText(deviceItemList.get(position).getTagName());
            int index = position + 1;
            holder.tv_index.setText(String.valueOf(index));

            if (deviceItemList.get(position).getDatatype().equalsIgnoreCase("Text")) {
                holder.et_paravalue.setVisibility(View.VISIBLE);
                holder.et_intparavalue.setVisibility(View.GONE);
                holder.selection.setVisibility(View.GONE);
                holder.droptext.setVisibility(View.GONE);
                holder.radioGroup.setVisibility(View.GONE);
                for (int i=0;i<manualEntryList.size();i++){
                    ManualEntry manfloat=manualEntryList.get(i);
                    if(manfloat.getTagId().equals(deviceItemList.get(position).getTagId())){
                        holder.et_paravalue.setText(manfloat.getValue());
                    }
                }
                editTextHashMap.put(deviceItemList.get(position).getTagId() + "," + deviceItemList.get(position).getTagName(), holder.et_paravalue);

            }
            else if (deviceItemList.get(position).getDatatype().equalsIgnoreCase("Numeric")) {
                holder.et_paravalue.setVisibility(View.GONE);
                holder.et_intparavalue.setVisibility(View.VISIBLE);
                holder.selection.setVisibility(View.GONE);
                holder.droptext.setVisibility(View.GONE);
                holder.radioGroup.setVisibility(View.GONE);
                for (int i=0;i<manualFloatData.size();i++){
                    ManualFloatData manfloat=manualFloatData.get(i);
                    if(manfloat.getTagId().equals(deviceItemList.get(position).getTagId())){
                        Log.d("value",manfloat.getValue().toString());
                        holder.et_intparavalue.setText(manfloat.getValue().toString());
                    }
                }
                editIntHashMap.put(deviceItemList.get(position).getTagId() + "," + deviceItemList.get(position).getTagName(), holder.et_intparavalue);

            }
            else if (deviceItemList.get(position).getDatatype().equalsIgnoreCase("Boolean")) {
                holder.et_paravalue.setVisibility(View.GONE);
                holder.et_intparavalue.setVisibility(View.GONE);
                holder.selection.setVisibility(View.GONE);
                holder.droptext.setVisibility(View.GONE);
                /*holder.et_paravalue.setFocusable(false);
                holder.et_intparavalue.setFocusable(false);
                holder.selection.setFocusable(false);
                holder.droptext.setFocusable(false);
                holder.radioGroup.setFocusable(false)*/;
                holder.radioGroup.setFocusable(true);
                holder.radioGroup.setFocusableInTouchMode(true);
                holder.radioGroup.setVisibility(View.VISIBLE);
                for (int i=0;i<manualEntryList.size();i++){
                    ManualEntry manfloat=manualEntryList.get(i);
                    if(manfloat.getTagId().equals(deviceItemList.get(position).getTagId())){
                       if (manfloat.getValue().equalsIgnoreCase("True"))
                       {
                           holder.radioGroup.check(R.id.radia_true);

                       }
                       else if(manfloat.getValue().equalsIgnoreCase("False")){
                           holder.radioGroup.check(R.id.radia_false);
                       }
                    }
                }
                holder.radioGroup.setOnCheckedChangeListener((radioGroup, i) -> {
                    if (holder.radia_true.isChecked()) {
                        radioHashMap.put(deviceItemList.get(position).getTagId() + "," + deviceItemList.get(position).getTagName(), "True");
                    } else {
                        radioHashMap.put(deviceItemList.get(position).getTagId() + "," + deviceItemList.get(position).getTagName(), "False");
                    }

                });

            } else if (deviceItemList.get(position).getDatatype().equalsIgnoreCase("Dropdown")) {
                holder.et_paravalue.setVisibility(View.GONE);
                holder.et_intparavalue.setVisibility(View.GONE);
                holder.selection.setVisibility(View.VISIBLE);
                holder.droptext.setVisibility(View.VISIBLE);
                holder.radioGroup.setVisibility(View.GONE);

                ArrayAdapter<String> adapterType = new ArrayAdapter<>(
                        context,
                        R.layout.layout,
                        deviceItemList.get(position).getStringList()
                );

                for (int i=0;i<manualEntryList.size();i++){
                    ManualEntry manfloat=manualEntryList.get(i);
                    if(manfloat.getTagId().equals(deviceItemList.get(position).getTagId())) {
                        holder.droptext.setSelection( deviceItemList.get(position).getStringList().indexOf(manfloat.getValue()) + 1);
                        holder.droptext.setText(manfloat.getValue());
                    }
                    }
                holder.droptext.setAdapter(adapterType);
                holder.droptext.setOnItemClickListener((adapterView, view11, i1, l) -> {
                    String parameterValue = (String) adapterView.getItemAtPosition(i1);
                    dropdownHashMap.put(deviceItemList.get(position).getTagId() + "," + deviceItemList.get(position).getTagName(), parameterValue);

                });

            }
        }catch (Exception e){
            e.printStackTrace();
        }

    }


    @Override
    public int getItemCount() {
        return deviceItemList.size();
    }
    @Override
    public int getItemViewType(int position)
    {
        return position;
    }

    public static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView device_name, tv_index;
        EditText et_paravalue, et_intparavalue;
        TextInputLayout selection;
        AutoCompleteTextView droptext;
        RadioGroup radioGroup;
        RadioButton radia_true, radia_false;

        public MyViewHolder(View view) {
            super(view);
            tv_index = view.findViewById(R.id.tv_index);
            device_name = view.findViewById(R.id.tv_parameterName);
            et_paravalue = view.findViewById(R.id.et_paravalue);
            selection = view.findViewById(R.id.til_dropdown);
            droptext = view.findViewById(R.id.act_dropdown);
            radioGroup = view.findViewById(R.id.groupradio);
            et_intparavalue = view.findViewById(R.id.et_intparavalue);
            radia_true = view.findViewById(R.id.radia_true);
            radia_false = view.findViewById(R.id.radia_false);


        }
    }

    public Map<String, EditText> getList() {
        return editTextHashMap;
    }

    public Map<String, String> getSelectionList() {
        return dropdownHashMap;
    }

    public Map<String, EditText> getIntList() {
        return editIntHashMap;
    }

    public Map<String, String> getRadioHashMap() {
        return radioHashMap;
    }

}
