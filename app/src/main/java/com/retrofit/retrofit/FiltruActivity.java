package com.retrofit.retrofit;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import java.util.Calendar;

public class FiltruActivity extends AppCompatActivity {
    EditText txtStart;
    EditText txtEnd;
    TextView lblEnd;
    int nr;
    static final int DATE_DIALOG_ID = 0;
    public  int year,month,day;
    private int mYear, mMonth, mDay;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_filtru);
        txtStart = (EditText) findViewById(R.id.editText);
        txtEnd = (EditText) findViewById(R.id.editText2);

        final Button btnOk = (Button) findViewById(R.id.btnOk);
        final Button btnCancel = (Button) findViewById(R.id.btnCancel);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);
        java.text.SimpleDateFormat sdf = new java.text.SimpleDateFormat("yyyy-MM-dd_HH:mm:ss");
        txtStart.setOnClickListener(new View.OnClickListener() {

            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                // Show the DatePickerDialog
                nr=0;
                showDialog(DATE_DIALOG_ID);
            }
        });
        txtEnd.setOnClickListener(new View.OnClickListener() {

            @Override
            @SuppressWarnings("deprecation")
            public void onClick(View v) {
                // Show the DatePickerDialog
                nr=1;
                showDialog(DATE_DIALOG_ID);
            }
        });
        btnCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // i.putExtra("cod", "4");
              //  Intent in =new Intent();
             //   setResult(1,in);
                finish();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            String start =txtStart.getText().toString();;
            String end =txtEnd.getText().toString();
            Intent in =new Intent();
            in.putExtra("start",start);
            in.putExtra("end",end);
            setResult(1,in);
            finish();
        }
        });
    }
// Register  DatePickerDialog listener
        private DatePickerDialog.OnDateSetListener mDateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    // the callback received when the user "sets" the Date in the DatePickerDialog
                    @Override
                    public void onDateSet(DatePicker view, int yearSelected,
                                          int monthOfYear, int dayOfMonth) {
                        year = yearSelected;
                        month = monthOfYear + 1;
                        day = dayOfMonth;
                        // Set the Selected Date in Select date Button
                        if(nr==1){txtEnd.setText(year + "-" + month + "-" + day);
                        }else{txtStart.setText(year + "-" + month + "-" + day);}
                    }
                };
        // Method automatically gets Called when you call showDialog()  method
        @Override
        protected Dialog onCreateDialog ( int id){
            switch (id) {
                case DATE_DIALOG_ID:
                    // create a new DatePickerDialog with values you want to show
                    return new DatePickerDialog(this,
                            mDateSetListener,
                            mYear, mMonth, mDay);

            }
            return null;

        }
    }

