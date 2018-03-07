package com.retrofit.retrofit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ComenziBarChartActivity extends AppCompatActivity {
    public static String url = "http://floridaconstruct.eu/";
    public static String url2 = "http://floridaconstruct.eu/comenzi/";
    private ProgressBar bar;
    BarChart chart;
    ArrayList<BarEntry> VALORI;
    ArrayList<String> BarEntryLabels;
    BarDataSet Bardataset;
    BarData VANZATORI;
    String start;
    String end;
    private MInterface service;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comenzibarchart);
        bar = (ProgressBar) findViewById(R.id.progressBar2);
        chart = (BarChart) findViewById(R.id.chart);
        VALORI = new ArrayList<>();
        BarEntryLabels = new ArrayList<String>();
        bar.setVisibility(VISIBLE);
        getBarValue();

    }

    public void AddValuesToBARENTRY(ArrayList<Order> orderList) {
        if (orderList!=null&&orderList.size()>0){
            for (int i=0;i<orderList.size();i++){

                VALORI.add(new BarEntry(Float.parseFloat(orderList.get(i).getItem3().replace("lei", "")), i));
                BarEntryLabels.add(orderList.get(i).getItem4());
            }
            Bardataset = new BarDataSet(VALORI, "Comenzi");
            VANZATORI = new BarData(BarEntryLabels, Bardataset);
            Bardataset.setColors(ColorTemplate.COLORFUL_COLORS);
            chart.setData(VANZATORI);
            chart.animateY(3000);
        }


    }

    void getBarValue(){
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MInterface restInt=retrofit.create(MInterface.class);
        restInt.getComenziGrafic().enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {

                if(response.isSuccessful()) {
                    bar.setVisibility(GONE);
                    ArrayList<Order> orderList = response.body();
                    AddValuesToBARENTRY(orderList);

                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                bar.setVisibility(GONE);
                Toast.makeText(ComenziBarChartActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==2) {
            if (data != null) {
                //  bar.setVisibility(VISIBLE);
                start = data.getStringExtra("start");
                end = data.getStringExtra("end");
                Toast.makeText(ComenziBarChartActivity.this, "Comenzi in perioada" + start + "--" + end, Toast.LENGTH_SHORT).show();
                getSortedList(start, end);
                // MInterface.getComenziGraficPerioada(start,end).enqueue(listener);
            }
        }

    }

    private void getSortedList(String start, String end) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(url2)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MInterface restInt=retrofit.create(MInterface.class);
        restInt.getComenziGraficPerioada(start,end).enqueue(new Callback<ArrayList<Order>>() {
            @Override
            public void onResponse(Call<ArrayList<Order>> call, Response<ArrayList<Order>> response) {

                if(response.isSuccessful()) {
                    VALORI.clear();
                    BarEntryLabels.clear();
                    bar.setVisibility(GONE);
                    ArrayList<Order> orderList = response.body();
                    AddValuesToBARENTRY(orderList);

                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Order>> call, Throwable t) {
                bar.setVisibility(GONE);
                Toast.makeText(ComenziBarChartActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meniubargraph, menu);

        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {


            case R.id.cauta:
                Intent i = new Intent(getApplicationContext(), FiltruActivity.class);
                // Activity is started with requestCode 2
                startActivityForResult(i, 2);
                return true;

            default:
                return super.onOptionsItemSelected(item);

            //  return true;
        }
    }


}






