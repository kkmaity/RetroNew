package com.retrofit.retrofit;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;
import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.retrofit.retrofit.recycler.ComenziGrupatActivity;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

//import retrofit2.RetrofitError;


public class MainActivity extends AppCompatActivity {

    public static String url = "http://floridaconstruct.eu/";

    private PieChart mChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mChart = (PieChart) findViewById(R.id.mainLayout);

        // configure pie chart;
        mChart.setUsePercentValues(true);
        mChart.setDescription("Smart Market Share");

        //enable hole and configure
        mChart.setDrawHoleEnabled(true);
        mChart.setHoleRadius(7);
        mChart.setTransparentCircleRadius(10);
        // enable rotation of the chart by touch
        mChart.setRotationAngle(0);
        mChart.setRotationEnabled(true);
        mChart.setOnChartValueSelectedListener(new OnChartValueSelectedListener() {
        @Override
        public void onValueSelected(Entry e, int i, Highlight highlight) {
            // display msg when value selected
            if (e == null)
                return;
            }

            @Override
            public void onNothingSelected() {

            }
        });

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MInterface restInt=retrofit.create(MInterface.class);
        restInt.getUsers().enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {

                if(response.isSuccessful()) {
                    ArrayList<Order> orderList = response.body();
                    addData(orderList);
                    Legend l = mChart.getLegend();
                    l.setPosition(Legend.LegendPosition.RIGHT_OF_CHART);
                    l.setXEntrySpace(7);
                    l.setYEntrySpace(5);
                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                Toast.makeText(MainActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public ArrayList<GraphItem> groupBySeller(ArrayList<Order> orderList) {
        Map<String, Float> orderMap= new HashMap<String, Float>();
        ArrayList<GraphItem> itemList = new ArrayList<GraphItem>();

        for (Order order: orderList) {
            String seller = order.getItem4();
            String orderValStr = order.getItem3();
            orderValStr = orderValStr.substring(0, orderValStr.length() - 3);
            float orderVal = Float.valueOf(orderValStr);

            if (orderMap.get(seller) == null) {
                orderMap.put(seller, orderVal);
            } else {
                orderMap.put(seller, orderMap.get(seller) + orderVal);
            }
        }

        Iterator<String> iter = orderMap.keySet().iterator();
        while (iter.hasNext()) {
            String seller = iter.next();
            float value = orderMap.get(seller);
            itemList.add(new GraphItem(seller, value));
        }

        return itemList;
    }

    class GraphItem {
        public String seller;
        public float value;

        public GraphItem(String seller, float value) {
            this.seller = seller;
            this.value = value;
        }
    }

    private void addData(ArrayList<Order> orderList) {
        ArrayList<GraphItem> itemList=groupBySeller(orderList);

        ArrayList<Entry> yVals1 = new ArrayList<Entry>();


        for (int i = 0; i < itemList.size(); i++) {
            yVals1.add(new Entry(itemList.get(i).value, i));
        }


        ArrayList<String> xVals = new ArrayList<String>();

        for (int i = 0; i < itemList.size(); i++)
            xVals.add(itemList.get(i).seller);

        // create pie data set
        PieDataSet dataSet = new PieDataSet(yVals1, "Florida");
        dataSet.setSliceSpace(3);
        dataSet.setSelectionShift(5);

        // add many colors
        ArrayList<Integer> colors = new ArrayList<Integer>();

        for (int c : ColorTemplate.VORDIPLOM_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.JOYFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.COLORFUL_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.LIBERTY_COLORS)
            colors.add(c);

        for (int c : ColorTemplate.PASTEL_COLORS)
            colors.add(c);

        colors.add(ColorTemplate.getHoloBlue());
        dataSet.setColors(colors);

        // instantiate pie data object now
        PieData data = new PieData(xVals, dataSet);
        data.setValueFormatter(new PercentFormatter());
        data.setValueTextSize(11f);
        data.setValueTextColor(Color.GRAY);

        mChart.setData(data);

        // undo all highlights
        mChart.highlightValue(null);

        // update pie chart
        mChart.invalidate();

        // now it's time for drawing pie graph !!!

    }
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meniu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.barchart:
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, ComenziBarChartActivity.class);
                startActivityForResult(intent, 0);


                return true;
            case R.id.sign:
                Intent intent1 = new Intent();
                intent1.setClass(MainActivity.this, SignInActivity.class);
                startActivityForResult(intent1, 0);


                return true;
            case R.id.settings:
                Intent intent2 = new Intent();
                intent2.setClass(MainActivity.this, SetPreferenceActivity.class);
                startActivityForResult(intent2, 0);
                return true;
            case R.id.comenzi:
                Intent intent3 = new Intent();
                intent3.setClass(MainActivity.this, ComenziGrupatActivity.class);
                startActivityForResult(intent3, 0);
                return true;
            case R.id.download:
                Intent intent4 = new Intent();
                intent4.setClass(MainActivity.this, PdfDownloaderActivity.class);
                startActivityForResult(intent4, 0);
                return true;


            default:
                return super.onOptionsItemSelected(item);

            //	Intent intent = new Intent();
            //   intent.setClass(Meniu.this, SetPreferenceActivity.class);
            //   startActivityForResult(intent, 0);

            //   return true;
        }
    }
}
