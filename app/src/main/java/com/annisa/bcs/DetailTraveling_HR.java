package com.annisa.bcs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.annisa.bcs.Data.Data_Traveling;
import com.annisa.bcs.Util.MyApplication;
import com.annisa.bcs.Util.Server;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 7/24/2019.
 */

public class DetailTraveling_HR extends AppCompatActivity {
    @BindView(R.id.nik)
    TextView _nik;
    @BindView(R.id.name) TextView _name;
    @BindView(R.id.from) TextView _from;
    @BindView(R.id.to) TextView _to;
    @BindView(R.id.totalDays) TextView _totaldays;
    @BindView(R.id.rate) TextView _rate;
    @BindView(R.id.budget)
    EditText _budget;
    @BindView(R.id.note)
    EditText _note;
    @BindView(R.id.project) TextView _project;
    @BindView(R.id.circleImageView) ImageView _image;
    @BindView(R.id.approve) Button _approve;
    @BindView(R.id.reject) Button _reject;
//    @BindView(R.id.approved) Button _approved;
//    @BindView(R.id.rejected) Button _rejected;
//    @BindView(R.id.paid) Button _paid;
//    @BindView(R.id.paiddone) Button done_paid;
    @BindView(R.id.status) TextView _status;
    Data_Traveling data;
    Context context = this;

    private static final String TAG = DetailTraveling.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";
    int success;
    String edit_budget, note;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_traveling_hr);
        ButterKnife.bind(this);
        data = (Data_Traveling) getIntent().getSerializableExtra("dataf");

        //date format
//        String dates = data.getDate();
//        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
//        try {
//            Date newDate = spf.parse(dates);
//            spf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));
//            dates = spf.format(newDate);
//        } catch (ParseException e) {
//            e.printStackTrace();
//        }
        _name.setText(data.getName());
        _nik.setText(data.getNik());
        _from.setText(data.getFrom());
        _to.setText(data.getTo());
        _totaldays.setText(data.getDays());
        _rate.setText(data.getRate());
        _budget.setText(data.getBudget());
        _project.setText(data.getProject());
        _status.setText(data.getStatus());
        Picasso.with(getApplicationContext()).load(Server.url_image_user + data.getImageBean().getIMAGE())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(_image, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }

                });


//        String stts = _status.getText().toString();
//        _cancel.setVisibility(View.VISIBLE);
//        if (stts.equals("H")){
//            _cancel.setVisibility(View.GONE);
//        } else {
//            _cancel.setVisibility(View.VISIBLE);
//        }


        _approve.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialoBuilder = new AlertDialog.Builder(context);
                alertDialoBuilder.setTitle("Alert!");
                alertDialoBuilder.setMessage("Are you sure want to approve this request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Approve();
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

        _reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialoBuilder = new AlertDialog.Builder(context);
                alertDialoBuilder.setTitle("Alert!");
                alertDialoBuilder.setMessage("Are you sure want to reject this request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Reject();
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
        });
//
//        _paid.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                AlertDialog.Builder alertDialoBuilder = new AlertDialog.Builder(context);
//                alertDialoBuilder.setTitle("Alert!");
//                alertDialoBuilder.setMessage("Are you sure want to pay this request?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                Paid();
//                                finish();
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            @Override
//                            public void onClick(DialogInterface dialog, int which) {
//                                dialog.cancel();
//                            }
//                        });
//                AlertDialog alertDialog = alertDialoBuilder.create();
//                alertDialog.show();
//            }
//        });
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    private void Approve() {
        StringRequest strReq = new StringRequest(Request.Method.POST, Server.approve_traveling_hr, new Response.Listener<String>() {

            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1) {

                        Log.e("approve", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        finish();

                    } else {
                        //Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                        _note.setError("Note needed!");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                String id_traveling = data.getId_traveling();
                String nik = data.getNik();
                String date = data.getDate();
                String name = data.getName();
                String budget = data.getBudget();
                edit_budget = _budget.getText().toString();
                note = _note.getText().toString();
                params.put("id", id_traveling);
                params.put("date", date);
                params.put("note", note);
                params.put("budget", budget);
                params.put("edit_budget", edit_budget);
                params.put("name", name);
                params.put("nik", nik);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq,tag_json_obj);

    }
    private void Reject(){
        StringRequest strReq = new StringRequest(Request.Method.POST,Server.reject_traveling, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1){

                        Log.d("reject", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                String id_traveling = data.getId_traveling();
                params.put("id_traveling", id_traveling);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq,tag_json_obj);

    }

    private void Paid(){
        StringRequest strReq = new StringRequest(Request.Method.POST,Server.paid_traveling, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1){

                        Log.d("paid", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                    } else {
                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e){
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener(){

            @Override
            public void onErrorResponse(VolleyError error){
                Log.e(TAG, "Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                String id_traveling = data.getId_traveling();
                params.put("id_traveling", id_traveling);
                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq,tag_json_obj);

    }

}

