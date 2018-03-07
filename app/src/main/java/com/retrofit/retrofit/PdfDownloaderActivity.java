package com.retrofit.retrofit;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.adeel.library.easyFTP;

import java.io.InputStream;

/**
 * Created by kamal on 03/07/2018.
 */

public class PdfDownloaderActivity extends AppCompatActivity {
    Button btnDownload;
    TextView path;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pdf_download);
        btnDownload=(Button)findViewById(R.id.btnDownload);
        path=(TextView)findViewById(R.id.path);
        btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                download();
            }
        });

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
}
