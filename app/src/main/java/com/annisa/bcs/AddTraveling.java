package com.annisa.bcs;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.annisa.bcs.Util.MyApplication;
import com.annisa.bcs.Util.Server;

import net.gotev.uploadservice.MultipartUploadRequest;
import net.gotev.uploadservice.UploadNotificationConfig;

import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.UUID;

import butterknife.BindView;

import static com.annisa.bcs.Login.TAG_NAME;
import static com.annisa.bcs.Login.TAG_NIK;

/**
 * Created by User on 6/17/2019.
 */

public class AddTraveling extends AppCompatActivity {

    // Pdf upload request code.
    public int PDF_REQ_CODE = 1;
    String PdfNameHolder, PdfPathHolder, PdfID;
    // Creating URI .
    Uri uri;
    private static final String TAG = MainActivity.class.getSimpleName();
    private String PDF_UPLOAD_HTTP_URL = "http://1603061.domainon.top/annisa.com/AndroidPdfUpload/upload.php";
    Button bUpload;
    Button bSend;
    Button bClose;
    ProgressDialog dialog;
    SharedPreferences sharedPreferences;
    ConnectivityManager conMgr;
    int success;
    final Context context = this;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";

    String nik, name;
    @BindView(R.id.edittext_name_pdf) EditText PdfNameEditText;
    @BindView(R.id.insertDate) EditText _date;
    @BindView(R.id.from) EditText _from;
    @BindView(R.id.to) EditText _to;
    @BindView(R.id.total_day) EditText _total_days;
    @BindView(R.id.rate) EditText _rate;
    @BindView(R.id.budget) EditText _budget;
    @BindView(R.id.project) EditText _project;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_traveling);

        sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        nik = sharedPreferences.getString(TAG_NIK, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if(conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()){
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
            }
        }

        _date = findViewById(R.id.insertDate);
        _from = findViewById(R.id.from);
        _to = findViewById(R.id.to);
        _total_days = findViewById(R.id.total_day);
        _rate = findViewById(R.id.rate);
        _budget = findViewById(R.id.budget);
        _project = findViewById(R.id.project);
        PdfNameEditText = findViewById(R.id.edittext_name_pdf);

        _date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(AddTraveling.this, date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
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
        toolbar.setText("Add Travelling");


        bSend = (Button) findViewById(R.id.buttonSend);
        bSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (_date.getText().toString().length() == 0) {
                    _date.setError("Date needed!");
                } else if (_from.getText().toString().length() == 0) {
                    _from.setError("From needed!");
                } else if (_to.getText().toString().length() == 0) {
                    _to.setError("To needed!");
                } else if (_rate.getText().toString().length() == 0) {
                    _rate.setError("Rate needed!");
                } else if (_budget.getText().toString().length() == 0) {
                    _budget.setError("Budget needed!");
                } else if (_project.getText().toString().length() == 0) {
                    _project.setError("Project needed!");
                } else if (PdfNameEditText.getText().toString().length() == 0){
                    PdfNameEditText.setError("File needed!");
                }
                else {
                    AlertDialog.Builder alertDialoBuilder = new AlertDialog.Builder(context);
                    alertDialoBuilder.setTitle("Alert!");
                    alertDialoBuilder.setMessage("Are you sure want to send this data?")
                            .setCancelable(false)
                            .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {

                                    PdfUploadFunction();
                                    send();
                                    finish();

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
            }
        });


        bUpload = (Button) findViewById(R.id.buttonUpload);
        bUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // PDF selection code start from here .
                // Creating intent object.
                Intent intent = new Intent();

                // Setting up default file pickup time as PDF.
                intent.setType("application/pdf");

                intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(intent, "Select Pdf"), PDF_REQ_CODE);
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

        _date = findViewById(R.id.insertDate);
        _date.setText(sdf.format(myCalendar.getTime()));
    }

    public void send() {
         Log.d(TAG, "Sending...");

         String date_travelling = (String) _date.getText().toString();
         String from_travelling = (String) _from.getText().toString();
         String to_travelling = (String) _to.getText().toString();
         String rate_travelling = (String) _rate.getText().toString();
         String budget_travelling = (String) _budget.getText().toString();
         String total_days_travelling = (String) _total_days.getText().toString();
         String project_travelling = (String) _project.getText().toString();

        if (conMgr.getActiveNetworkInfo() != null
                && conMgr.getActiveNetworkInfo().isAvailable()
                && conMgr.getActiveNetworkInfo().isConnected()) {
            checkSend(date_travelling, from_travelling, to_travelling, rate_travelling, budget_travelling, total_days_travelling, project_travelling);
            onSendSuccess();
        } else {
            Toast.makeText(getApplicationContext(), "No inernet connection", Toast.LENGTH_SHORT).show();
        }

        new android.os.Handler().postDelayed(
                new Runnable() {
                    @Override
                    public void run() {
                        onSendSuccess();
                    }
                }, 3000);
        }

    public void onSendSuccess() {
        setResult(RESULT_OK, null);
        finish();
    }

    public void onSendFailed(){
        finish();
    }

    private void checkSend(final String date, final String from, final String to, final String rate, final String budget, final String total_days, final String project) {

        StringRequest strReq = new StringRequest(Request.Method.POST, Server.url_send_traveling, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Add Travelling Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Send Travelling Sukses!", jObj.toString());

                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        _date.setText("");
                        _from.setText("");
                        _to.setText("");
                        _total_days.setText("");
                        _rate.setText("");
                        _budget.setText("");
                        _project.setText("");

                        finish();

                    } else {
                        Toast.makeText(getApplicationContext(),
                                jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("date", date);
                params.put("name", name);
                params.put("total_days", total_days);
                params.put("from", from);
                params.put("to", to);
                params.put("rate", rate);
                params.put("budget", budget);
                params.put("project", project);

                return params;
            }

        };


        // Adding request to request queue
        MyApplication.getInstance().addToRequestQueue(strReq, tag_json_obj);
    }

    //kebawah ini fungsi yang berkaitan dengan upload pdf
    // PDF upload function starts from here.
    public void PdfUploadFunction() {

        // Getting pdf name from EditText.
        PdfNameHolder = PdfNameEditText.getText().toString().trim();

        try {
            // Getting file path using Filepath class.
            PdfPathHolder = FilePath.getPath(this, uri);
        } catch (Exception e) {
            e.printStackTrace();
        }
        // If file path object is null then showing toast message to move file into internal storage.
        if (PdfPathHolder == null) {

            Toast.makeText(this, "Please move your PDF file to internal storage & try again.", Toast.LENGTH_LONG).show();
            PdfNameEditText.setError("File needed!");

        }
        // If file path is not null then PDF uploading file process will starts.
        else {

            try {

                PdfID = UUID.randomUUID().toString();

                new MultipartUploadRequest(this, PdfID, PDF_UPLOAD_HTTP_URL)
                        .addFileToUpload(PdfPathHolder, "pdf")
                        .addParameter("name", PdfNameHolder)
                        .addParameter("nik", nik)
                        .setNotificationConfig(new UploadNotificationConfig())
                        .setMaxRetries(5)
                        .startUpload();

            } catch (Exception exception) {

                Toast.makeText(this, exception.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PDF_REQ_CODE && resultCode == RESULT_OK && data != null && data.getData() != null) {

            uri = data.getData();

            // After selecting the PDF set PDF is Selected text inside Button.
            bUpload.setText("PDF is Selected");
        }
    }

    // Requesting run time permission method starts from here.
    public void RequestRunTimePermission(){

        if (ActivityCompat.shouldShowRequestPermissionRationale(AddTraveling.this, Manifest.permission.READ_EXTERNAL_STORAGE))
        {

            Toast.makeText(AddTraveling.this,"READ_EXTERNAL_STORAGE permission Access Dialog", Toast.LENGTH_LONG).show();

        } else {

            ActivityCompat.requestPermissions(AddTraveling.this,new String[]{ Manifest.permission.READ_EXTERNAL_STORAGE}, 1);

        }
    }

    @Override
    public void onRequestPermissionsResult(int RC, String per[], int[] Result) {

        switch (RC) {

            case 1:

                if (Result.length > 0 && Result[0] == PackageManager.PERMISSION_GRANTED) {

                    Toast.makeText(AddTraveling.this,"Permission Granted", Toast.LENGTH_LONG).show();

                } else {

                    Toast.makeText(AddTraveling.this,"Permission Canceled", Toast.LENGTH_LONG).show();

                }
                break;
        }
    }


}
