package com.retrofit.retrofit.recycler;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.retrofit.retrofit.FiltruActivity;
import com.retrofit.retrofit.MainActivity;
import com.retrofit.retrofit.R;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

//import retrofit2.R;

/**
 * Created by mircea on 12.06.2017.
 */

public class ComenziGrupatActivity extends AppCompatActivity {
    private RecyclerView list;
    private ProgressBar bar;
    private ListAdapterGrupat2 adapter;
    private ArrayList<CustomObject> arrayList;
    private API service;
    String start;
    String end;
    String user;
    String montator;



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler);
        this.setTitle("Comenzi");
        list = (RecyclerView) findViewById(R.id.list);
        list.setLayoutManager(new LinearLayoutManager(this));
        bar = (ProgressBar) findViewById(R.id.progressBar2);

        SharedPreferences mySharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        montator = mySharedPreferences.getString("montator", "");
        user = mySharedPreferences.getString("edittext_preference", "");



        arrayList = new ArrayList<>();
        // aici trec ce activity vreau sa deschid la tap pe articol. Trebuie inlocuit ResultActivity cu numele dorit
        adapter = new ListAdapterGrupat2(this, arrayList, MainActivity.class);
        list.setAdapter(adapter);

        final Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://floridaconstruct.eu/comenzi/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(API.class);
      //  service.getComenziGrupat().enqueue(listener);
        bar.setVisibility(VISIBLE);
      //  list.setVisibility(GONE);
      //  service.getComenzileMele(user).enqueue(listener);

        if (montator.equals("1")) {
            service.getComenzileMele(user).enqueue(listener);

        } else {
            service.getComenziGrupat().enqueue(listener);
        }

    }


    Callback<List<CustomObject>> listener = new Callback<List<CustomObject>>() {

        @Override
        public void onResponse(Call<List<CustomObject>> call, Response<List<CustomObject>> response) {
            bar.setVisibility(GONE);
         //  list.setVisibility(VISIBLE);
            arrayList.clear();
            arrayList.addAll(response.body());
            adapter.notifyDataSetChanged();
        }

        @Override
        public void onFailure(Call<List<CustomObject>> call, Throwable t) {
            bar.setVisibility(GONE);
         //   list.setVisibility(VISIBLE);
            Toast.makeText(ComenziGrupatActivity.this, "Eroare", Toast.LENGTH_SHORT).show();

            Log.d("MyLogs", "" + t.getStackTrace());
            Log.d("MyLogs", "" + t.getLocalizedMessage());
        }
    };
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == 100) {
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
        if(requestCode==2)
        {
            bar.setVisibility(VISIBLE);
            start=data.getStringExtra("start");
            end=data.getStringExtra("end");
            Toast.makeText(ComenziGrupatActivity.this, "Comenzi in perioada"+start+"--"+end, Toast.LENGTH_SHORT).show();
            service.searchcomenziperioada(start,end).enqueue(listener);
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.meniucomenzigrupat, menu);
        SearchView searchView = (SearchView) menu.findItem(R.id.search).getActionView();

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.length() > 3) {
                    String text = newText;
                    service.searchcomenzi(text).enqueue(listener);
//
                    return true;
                } else
                    return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()) {

            case R.id.addclient:
                Intent intent = new Intent();
                intent.setClass(ComenziGrupatActivity.this, MainActivity.class);
                startActivityForResult(intent, 0);
                return true;

            case R.id.cauta:
                Intent i = new Intent(getApplicationContext(), FiltruActivity.class);
                // Activity is started with requestCode 2
                startActivityForResult(i, 2);
                return true;

            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
             //   overridePendingTransition(0,R.anim.right_out);
                return true;
            default:
                return super.onOptionsItemSelected(item);

            //  return true;
        }
    }

    private void getOverflowMenu() {

        try {

            ViewConfiguration config = ViewConfiguration.get(this);
            java.lang.reflect.Field menuKeyField = ViewConfiguration.class.getDeclaredField("sHasPermanentMenuKey");
            if(menuKeyField != null) {
                menuKeyField.setAccessible(true);
                menuKeyField.setBoolean(config, false);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

    }



}



