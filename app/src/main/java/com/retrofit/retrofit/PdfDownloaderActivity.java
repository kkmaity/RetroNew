package com.retrofit.retrofit;

import android.app.ProgressDialog;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adeel.library.easyFTP;

import java.io.InputStream;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static android.view.View.GONE;

/**
 * Created by kamal on 03/07/2018.
 */

public class PdfDownloaderActivity extends AppCompatActivity {
    Button btnDownload;
    TextView path;
    private ProgressDialog pr;
    public ArrayList<Items> orderList=new ArrayList<>();
    ArrayList<String>  mStringList= new ArrayList<String>();
    private AutoCompleteTextView actv;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);

        btnDownload=(Button)findViewById(R.id.btnDownload);
        pr=new ProgressDialog(this);
        pr.setMessage("Loading...");
        path=(TextView)findViewById(R.id.path);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });
        actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        getSortedList();
       /* String[] fruits = {"Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango", "Pear","Apple", "Banana", "Cherry", "Date", "Grape", "Kiwi", "Mango"};


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, fruits);
        //Getting the instance of AutoCompleteTextView
        AutoCompleteTextView actv = (AutoCompleteTextView) findViewById(R.id.autoCompleteTextView);
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
*/



        //Creating the instance of ArrayAdapter containing list of fruit names


    }

    public void download(){
        String address="82.79.184.253",u="mircea",p="mircea823333",serverPath="2100.pdf",destination="/storage/sdcard0/Download/2100.pdf";
        downloadTask async=new downloadTask();
        async.execute(address,u,p,serverPath,destination);
    }


    class downloadTask extends AsyncTask<String, Void, String> {

        private ProgressDialog prg;

        @Override
        protected void onPreExecute(){
            super.onPreExecute();
            prg = new ProgressDialog(PdfDownloaderActivity.this);
            prg.setMessage("Uploading...");
            prg.show();
        }

        @Override
        protected String doInBackground(String... params) {
            try {
                easyFTP ftp = new easyFTP();
              //  InputStream is=getResources().openRawResource(+R.drawable.easyftptest);
                ftp.connect(params[0],params[1],params[2]);
                ftp.downloadFile(params[3],params[4]);
                return new String("Download Successful");
            }catch (Exception e){
                String t="Failure : " + e.getLocalizedMessage();
                return t;
            }
        }

        @Override
        protected void onPostExecute(String str) {
            prg.dismiss();
            Toast.makeText(PdfDownloaderActivity.this,str,Toast.LENGTH_LONG).show();

        }
    }
    private void getSortedList() {
        pr.show();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://www.floridaconstruct.eu/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        MInterface restInt=retrofit.create(MInterface.class);
        restInt.getItems().enqueue(new Callback<ArrayList<Items>>() {


            @Override
            public void onResponse(Call<ArrayList<Items>> call, Response<ArrayList<Items>> response) {
                pr.dismiss();
                if(response.isSuccessful()) {

                     orderList = response.body();
                     for (int i=0;i<orderList.size();i++){
                         if (orderList.get(i).getItem2()!=null)
                         mStringList.add(orderList.get(i).getItem2());
                     }
                     getAutoComplete();


                }else {
                    int statusCode  = response.code();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Items>> call, Throwable t) {
                pr.dismiss();
                Toast.makeText(PdfDownloaderActivity.this,"No Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void getAutoComplete() {
        Object[] mStringArray = mStringList.toArray();
       // String[] mStringArray = new String[mStringList.size()];
        //mStringArray = mStringList.toArray(mStringArray);
        String [] stockArr= mStringList.toArray(new String[mStringList.size()]);
       // String [] stockArr = (String[]) mStringList.toArray();


        ArrayAdapter<String> adapter = new ArrayAdapter<String>
                (this, android.R.layout.select_dialog_item, stockArr);

        System.out.println(stockArr.toString());
        //Getting the instance of AutoCompleteTextView
        //Getting the instance of AutoCompleteTextView
        actv.setThreshold(1);//will start working from first character
        actv.setAdapter(adapter);//setting the adapter data into the AutoCompleteTextView
        actv.setTextColor(Color.RED);
    }
}
