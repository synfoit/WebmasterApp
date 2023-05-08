package com.servilink.webmasterapp.Adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.github.anastr.speedviewlib.SpeedView;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;
import com.servilink.webmasterapp.Comman;
import com.servilink.webmasterapp.Model.dashboarLayoutd;
import com.servilink.webmasterapp.Model.dashboardLayoutTag;
import com.servilink.webmasterapp.R;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

public class CustomDashboardAdapter extends RecyclerView.Adapter<CustomDashboardAdapter.MyViewHolder> {


    Activity context;
    ArrayList<dashboarLayoutd> dashboardLayout;


    public CustomDashboardAdapter(Activity context, ArrayList<dashboarLayoutd> dashboardLayout) {
        this.context = context;
        this.dashboardLayout = dashboardLayout;


    }

    @NonNull
    @Override
    public CustomDashboardAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.custom_dashboard, parent, false);
        return new CustomDashboardAdapter.MyViewHolder(itemView);
    }


    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(final CustomDashboardAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") final int position) {
        dashboarLayoutd layout = dashboardLayout.get(position);
        holder.layoutName.setText(layout.getDashboarLayoutdName());
        holder.unit.setText(layout.getUnit());
        int[] colorarray=new int[]{R.color.teal_200,R.color.teal_700,R.color.purple_200,R.color.seagreen};
        if (layout.getChartType().equalsIgnoreCase("line")) {
            holder.lineChart.setVisibility(View.VISIBLE);
            holder.lineChart.setTouchEnabled(true);
            holder.lineChart.setPinchZoom(true);
            ArrayList<LineDataSet> lines = new ArrayList<>();
            try {
                ArrayList<dashboardLayoutTag> layoutTags = layout.getDashboardLayoutTags();
                ArrayList<String> xAxisValues = new ArrayList<>();
                for (int t = 0; t < layout.getValueTime().size(); t++) {

                    Date date = Comman.sqltimestamp.parse(layout.getValueTime().get(t));
                    assert date != null;
                    String time = Comman.timeFormate.format(date);
                    xAxisValues.add(time);
                }
                XAxis xAxis = holder.lineChart.getXAxis();
                XAxis.XAxisPosition position1 = XAxis.XAxisPosition.BOTTOM;
                xAxis.setPosition(position1);
                //xAxis.enableGridDashedLine(2f, 7f, 0f);
                //xAxis.setAxisMaximum(5f);
                //xAxis.setAxisMinimum(0f);
                //xAxis.setLabelCount(6, true);
                xAxis.setGranularityEnabled(true);
                xAxis.setGranularity(1f);
                xAxis.setLabelRotationAngle(315f);



                //xAxis.setCenterAxisLabels(true);


                xAxis.setDrawLimitLinesBehindData(true);
                holder.lineChart.getXAxis().setValueFormatter(new IndexAxisValueFormatter(xAxisValues));
                ArrayList<JSONArray> jsonArray = layout.getListOfValues();

                ArrayList<ILineDataSet> dataSets = new ArrayList<>();
                for (int r = 0; r < jsonArray.size(); r++) {
                    ArrayList<Entry> entries = new ArrayList<>();
                    JSONArray listOfValue = jsonArray.get(r);
                    for (int a = 0; a < listOfValue.length(); a++) {
                        entries.add(new Entry(a, Float.parseFloat(listOfValue.getString(a))));
                    }

                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));

                    LineDataSet set1 = new LineDataSet(entries, layoutTags.get(r).getDashboardLayoutTagName());
                    set1.setDrawFilled(true);
                    set1.setFillColor(Color.WHITE);
                    set1.setColor(ContextCompat.getColor(context, colorarray[r]));
                    set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
                    set1.setCircleColor(Color.DKGRAY);
                    lines.add(set1);
                    dataSets.add(set1);
                }

                holder.lineChart.setData(new LineData(dataSets));

                holder.lineChart.getDescription().setText("");

                holder.lineChart.getDescription().setTextColor(Color.RED);


                holder.lineChart.animateY(1400, Easing.EaseInOutBounce);
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (layout.getChartType().equalsIgnoreCase("progress")) {
            holder.progressBar.setVisibility(View.VISIBLE);
            holder.progress_text.setVisibility(View.VISIBLE);

            ArrayList<JSONArray> jsonArray = layout.getListOfValues();
            try {
                ArrayList<Float> entries = new ArrayList<>();
                for (int r = 0; r < jsonArray.size(); r++) {

                    JSONArray listOfValue = jsonArray.get(r);
                    for (int a = 0; a < listOfValue.length(); a++) {
                        entries.add(Float.valueOf(listOfValue.getString(a)));
                    }
                }
                if (entries.size() != 0) {
                    holder.progress_text.setText(" " + Math.round(entries.get(entries.size() - 1)));
                    holder.progressBar.setProgress(Math.round(entries.get(entries.size() - 1)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (layout.getChartType().equalsIgnoreCase("bar")) {
            holder.barChart.setVisibility(View.VISIBLE);
            holder.lineChart.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.speedView.setVisibility(View.GONE);

            ArrayList<dashboardLayoutTag> layoutTags = layout.getDashboardLayoutTags();
            ArrayList<String> xAxisValues = new ArrayList<>();
            try {
                if (layout.getValueTime().size() != 0) {
                    for (int t = 0; t < layout.getValueTime().size(); t++) {
                        Date date = Comman.sqltimestamp.parse(layout.getValueTime().get(t));
                        assert date != null;
                        String time = Comman.timeFormate.format(date);
                        xAxisValues.add(time);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            holder.barChart.setDrawBarShadow(false);
            holder.barChart.getDescription().setEnabled(false);
            holder.barChart.setPinchZoom(false);
            holder.barChart.setDrawGridBackground(true);
            holder.barChart.getLegend().setEnabled(true);
            Legend l = holder.barChart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            // empty labels so that the names are spread evenly



            ArrayList<IBarDataSet> dataSets = new ArrayList<>();

            ArrayList<JSONArray> jsonArray = layout.getListOfValues();

            if (jsonArray.size() > 1) {
                XAxis xAxis = holder.barChart.getXAxis();
                xAxis.setCenterAxisLabels(true);
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                xAxis.setDrawGridLines(true);
                xAxis.setGranularity(1f); // only intervals of 1 day
                xAxis.setTextColor(Color.BLACK);
                xAxis.setTextSize(12);
                xAxis.setAxisLineColor(Color.WHITE);
                xAxis.setAxisMinimum(1f);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

                YAxis leftAxis = holder.barChart.getAxisLeft();
                leftAxis.setTextColor(Color.BLACK);
                leftAxis.setTextSize(12);
                leftAxis.setAxisLineColor(Color.WHITE);
                leftAxis.setDrawGridLines(true);
                leftAxis.setGranularity(2);
                leftAxis.setLabelCount(8, true);
                leftAxis.setPosition(YAxis.YAxisLabelPosition.OUTSIDE_CHART);

                holder.barChart.getAxisRight().setEnabled(false);
                for (int r = 0; r < jsonArray.size(); r++) {
                    ArrayList<BarEntry> entries = new ArrayList<>();


                    JSONArray listOfValue = jsonArray.get(r);
                    for (int a = 0; a < listOfValue.length(); a++) {
                        try {
                            entries.add(new BarEntry(a, Float.parseFloat(listOfValue.getString(a))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    BarDataSet barDataSet2 = new BarDataSet(entries, layoutTags.get(r).getDashboardLayoutTagName());
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    barDataSet2.setColor(ContextCompat.getColor(context, colorarray[r]));

                    dataSets.add(barDataSet2);


                }

                BarData data = new BarData(dataSets);
                float groupSpace = 0.4f;
                float barSpace = 0f;
                float barWidth = 0.3f;
                // (barSpace + barWidth) * 2 + groupSpace = 1
                data.setBarWidth(barWidth);
                // so that the entire chart is shown when scrolled from right to left
                xAxis.setAxisMaximum(xAxisValues.size() - 1.1f);
                holder.barChart.setData(data);
                holder.barChart.setScaleEnabled(false);
                holder.barChart.setVisibleXRangeMaximum(6f);
                holder.barChart.groupBars(1f, groupSpace, barSpace);
                holder.barChart.invalidate();
            } else {
                holder.barChart.setDrawGridBackground(false);
                //remove the bar shadow, default false if not set
                holder.barChart.setDrawBarShadow(false);
                //remove border of the chart, default false if not set
                holder.barChart.setDrawBorders(false);

                //remove the description label text located at the lower right corner
                Description description = new Description();
                description.setEnabled(false);
                holder.barChart.setDescription(description);

                //setting animation for y-axis, the bar will pop up from 0 to its value within the time we set
                holder.barChart.animateY(1000);
                //setting animation for x-axis, the bar will pop up separately within the time we set
                holder.barChart.animateX(1000);

                XAxis xAxis = holder.barChart.getXAxis();
                //change the position of x-axis to the bottom
                xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
                //set the horizontal distance of the grid line
                xAxis.setGranularity(1f);
                //hiding the x-axis line, default true if not set
                xAxis.setDrawAxisLine(false);
                //hiding the vertical grid lines, default true if not set
                xAxis.setDrawGridLines(false);
                xAxis.setValueFormatter(new IndexAxisValueFormatter(xAxisValues));

                YAxis leftAxis = holder.barChart.getAxisLeft();
                //hiding the left y-axis line, default true if not set
                leftAxis.setDrawAxisLine(false);

                YAxis rightAxis = holder.barChart.getAxisRight();
                //hiding the right y-axis line, default true if not set
                rightAxis.setDrawAxisLine(false);
                BarDataSet barDataSet1 = null;
                for (int r = 0; r < jsonArray.size(); r++) {
                    ArrayList<BarEntry> entries = new ArrayList<>();


                    JSONArray listOfValue = jsonArray.get(r);
                    for (int a = 0; a < listOfValue.length(); a++) {
                        try {
                            entries.add(new BarEntry(a, Float.parseFloat(listOfValue.getString(a))));
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    barDataSet1 = new BarDataSet(entries, layoutTags.get(r).getDashboardLayoutTagName());
                    Random rnd = new Random();
                    int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
                    barDataSet1.setColor(ContextCompat.getColor(context, colorarray[r]));

                    // dataSets.add(barDataSet1);


                }
                BarData data = new BarData(barDataSet1);

                float barWidth = 0.3f;
                // (barSpace + barWidth) * 2 + groupSpace = 1
                data.setBarWidth(barWidth);
                // so that the entire chart is shown when scrolled from right to left
               // xAxis.setAxisMaximum(xAxisValues.size() - 1.0f);
                holder.barChart.setData(data);
                holder.barChart.setScaleEnabled(false);
                //holder.barChart.setVisibleXRangeMaximum(6f);
               // holder.barChart.groupBars(1f, groupSpace, barSpace);
                holder.barChart.invalidate();
            }


        }
        else if (layout.getChartType().equalsIgnoreCase("gauge")) {
            holder.speedView.setVisibility(View.VISIBLE);

            holder.progress_text.setVisibility(View.GONE);
            holder.progressBar.setVisibility(View.GONE);
            holder.lineChart.setVisibility(View.GONE);
            holder.barChart.setVisibility(View.GONE);

            holder.speedView.setUnit(" ");
            holder.speedView.getSections().get(0).setColor(Color.parseColor("#7EDFB4"));
            holder.speedView.getSections().get(1).setColor(Color.parseColor("#F7993F"));
            holder.speedView.getSections().get(2).setColor(Color.parseColor("#DF5353"));


            ArrayList<JSONArray> jsonArray = layout.getListOfValues();
            try {
                ArrayList<Float> entries = new ArrayList<>();
                for (int r = 0; r < jsonArray.size(); r++) {

                    JSONArray listOfValue = jsonArray.get(r);
                    for (int a = 0; a < listOfValue.length(); a++) {
                        entries.add(Float.valueOf(listOfValue.getString(a)));
                    }
                }
                if (entries.size() != 0) {
                    holder.speedView.setMinMaxSpeed(layout.getMin(), layout.getMax());
                    holder.speedView.speedPercentTo(Math.round(entries.get(entries.size() - 1)));
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
        else if (layout.getChartType().equalsIgnoreCase("label")) {
            holder.progress_text.setVisibility(View.VISIBLE);

            ArrayList<JSONArray> jsonArray = layout.getListOfValues();
            try {
                ArrayList<String> entries = new ArrayList<>();
                for (int r = 0; r < jsonArray.size(); r++) {

                    JSONArray listOfValue = jsonArray.get(r);
                    for (int a = 0; a < listOfValue.length(); a++) {
                        entries.add(listOfValue.getString(a));
                    }
                }
                if (entries.size() != 0) {
                    holder.progress_text.setPadding(5,10,5,20);

                    holder.progress_text.setText(" " + entries.get(entries.size() - 1));

                }
            } catch (Exception e) {
                e.printStackTrace();
            }

        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dashboardLayout.size();
    }


    public static class MyViewHolder extends RecyclerView.ViewHolder {
        LineChart lineChart;
        ProgressBar progressBar;
        BarChart barChart;
        TextView layoutName, unit, progress_text;
        SpeedView speedView;

        public MyViewHolder(View view) {
            super(view);
            lineChart = view.findViewById(R.id.reportingChart);
            progressBar = view.findViewById(R.id.horizontal_progress_bar);
            barChart = view.findViewById(R.id.idBarChart);
            layoutName = view.findViewById(R.id.tv_layoutname);
            unit = view.findViewById(R.id.tv_unit);
            progress_text = view.findViewById(R.id.tv_progresstext);
            speedView = view.findViewById(R.id.speedView);
        }
    }
}
