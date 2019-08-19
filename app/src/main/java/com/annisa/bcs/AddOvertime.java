package com.annisa.bcs;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.annisa.bcs.Util.MyApplication;
import com.annisa.bcs.Util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;

import static com.annisa.bcs.Login.TAG_NAME;
import static com.annisa.bcs.Login.TAG_NIK;


public class AddOvertime extends AppCompatActivity {

    private static final String TAG = MainActivity.class.getSimpleName();
    Button bSend;
    Button bClose;
    SharedPreferences sharedPreferences;
    ConnectivityManager conMgr;
    final Context context = this;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    String nik, name, spinner;
    @BindView(R.id.overtimeDate)
    EditText dateOt;
    @BindView(R.id.projectOt)
    EditText project_Overtime;
    @BindView(R.id.activity)
    EditText activityOt;
    @BindView(R.id.total)
    EditText total_time;
    @BindView(R.id.checkIn)
    EditText check_In;
    @BindView(R.id.checkOut)
    EditText check_Out;
    @BindView(R.id.bruto)
    EditText bruto;
    @BindView(R.id.nett)
    EditText nett;
    @BindView(R.id.nominal)
    TextView nominal;
    @BindView(R.id.btn_check)
    TextView btn_check;
    Spinner day;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_overtime);

        sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        nik = sharedPreferences.getString(TAG_NIK, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }

        dateOt = findViewById(R.id.overtimeDate);
        project_Overtime = findViewById(R.id.projectOt);
        activityOt = findViewById(R.id.activity);
        check_In = findViewById(R.id.checkIn);
        check_Out = findViewById(R.id.checkOut);
        total_time = findViewById(R.id.total);
        bruto = findViewById(R.id.bruto);
        nett = findViewById(R.id.nett);
        nominal = findViewById(R.id.nominal);
        btn_check = findViewById(R.id.btn_check);
        dateOt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddOvertime.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });

        day = (Spinner) findViewById(R.id.overtimeDay);

        btn_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                spinner = day.getSelectedItem().toString();
                if (spinner.equals("Sunday") || spinner.equals("Saturday")){
                    Locale lokal = null;
                    String pola = "HH:mm";
                    String waktuSatuStr = check_In.getText().toString();
                    String waktuDuaStr = check_Out.getText().toString();
                    Date waktuSatu = SelisihDateTime.konversiStringkeDate(waktuSatuStr,
                            pola, lokal);
                    Date WaktuDua = SelisihDateTime.konversiStringkeDate(waktuDuaStr, pola,
                            lokal);
                    String hasilNett = SelisihDateTime.nettJam(waktuSatu,
                            WaktuDua);
                    //mengubah variabel str menjadi int
                    int num = Integer.parseInt(hasilNett);
                    if (num > 5){
                        nominal.setText("200.000");
                    } else {
                        nominal.setText("100.000");
                    }
                } else {
                    Locale lokal = null;
                    String pola = "HH:mm";
                    String waktuSatuStr = check_In.getText().toString();
                    String waktuDuaStr = check_Out.getText().toString();
                    Date waktuSatu = SelisihDateTime.konversiStringkeDate(waktuSatuStr,
                            pola, lokal);
                    Date WaktuDua = SelisihDateTime.konversiStringkeDate(waktuDuaStr, pola,
                            lokal);
                    String hasilNett = SelisihDateTime.nettJam(waktuSatu,
                            WaktuDua);
                    //mengubah variabel str menjadi int
                    int num = Integer.parseInt(hasilNett);
                    if (num == 1 && num < 2) {
                        nominal.setText("35.000");
                    } else if (num > 1) {
                        int hasil = ((num-1)*20000)+35000;
                        String hasill = Integer.toString(hasil);
                        nominal.setText(hasill);
                    } else {
                        nominal.setText("0");
                    }
                }

            }
        });

        total_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale lokal = null;
                String pola = "HH:mm";
                String waktuSatuStr = check_In.getText().toString();
                String waktuDuaStr = check_Out.getText().toString();
                Date waktuSatu = SelisihDateTime.konversiStringkeDate(waktuSatuStr,
                        pola, lokal);
                Date WaktuDua = SelisihDateTime.konversiStringkeDate(waktuDuaStr, pola,
                        lokal);
                String hasilSelisih = SelisihDateTime.selisihDateTime(waktuSatu,
                        WaktuDua);

                total_time.setText(hasilSelisih);
            }
        });

        bruto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale lokal = null;
                String pola = "HH:mm";
                String waktuSatuStr = check_In.getText().toString();
                String waktuDuaStr = check_Out.getText().toString();
                Date waktuSatu = SelisihDateTime.konversiStringkeDate(waktuSatuStr,
                        pola, lokal);
                Date WaktuDua = SelisihDateTime.konversiStringkeDate(waktuDuaStr, pola,
                        lokal);
                String hasilBruto = SelisihDateTime.brutoDateTime(waktuSatu,
                        WaktuDua);
                bruto.setText(hasilBruto);
            }
        });

        nett.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Locale lokal = null;
                String pola = "HH:mm";
                String waktuSatuStr = check_In.getText().toString();
                String waktuDuaStr = check_Out.getText().toString();
                Date waktuSatu = SelisihDateTime.konversiStringkeDate(waktuSatuStr,
                        pola, lokal);
                Date WaktuDua = SelisihDateTime.konversiStringkeDate(waktuDuaStr, pola,
                        lokal);
                String hasilNett = SelisihDateTime.nettDateTime(waktuSatu,
                        WaktuDua);
                nett.setText(hasilNett);
            }
        });

        check_In.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddOvertime.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        check_In.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        check_Out.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(AddOvertime.this, new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        check_Out.setText(selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        bClose = findViewById(R.id.btnclose);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        TextView toolbar = (TextView) findViewById(R.id.toolbar_text);
        toolbar.setText("Add Overtime");

        bSend = (Button) findViewById(R.id.buttonSend);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialoBuilder = new AlertDialog.Builder(context);
                alertDialoBuilder.setTitle("Alert!");
                alertDialoBuilder.setMessage("Are you sure want to send this data?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getApplicationContext(), "Nisa cantik", Toast.LENGTH_LONG).show();
//                                send();
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.cancel();
                            }
                        });
                AlertDialog alertDialog = alertDialoBuilder.create();
                alertDialog.show();
            }
        });
    }

    final Calendar myCalendar = Calendar.getInstance();

    DatePickerDialog.OnDateSetListener date = new DatePickerDialog.OnDateSetListener() {

        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear,
                              int dayOfMonth) {
            // TODO Auto-generated method stub
            myCalendar.set(Calendar.YEAR, year);
            myCalendar.set(Calendar.MONTH, monthOfYear);
            myCalendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);
            updateLabel();
        }

    };

    private void updateLabel() {
        String myFormat = "yyyy-MM-dd"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("in", "ID"));
        dateOt.setText(sdf.format(myCalendar.getTime()));
    }

}