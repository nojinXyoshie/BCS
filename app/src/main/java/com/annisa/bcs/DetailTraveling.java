package com.annisa.bcs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by User on 7/5/2019.
 */

public class DetailTraveling extends AppCompatActivity {

    @BindView(R.id.nik) TextView _nik;
    @BindView(R.id.name) TextView _name;
    @BindView(R.id.from) TextView _from;
    @BindView(R.id.to) TextView _to;
    @BindView(R.id.totalDays) TextView _totaldays;
    @BindView(R.id.rate) TextView _rate;
    @BindView(R.id.budget) TextView _budget;
    @BindView(R.id.project) TextView _project;
    @BindView(R.id.circleImageView) ImageView _image;
    @BindView(R.id.cvCancelButton) CardView _cancel;
    @BindView(R.id.status) TextView _status;
    Data_Traveling data;
    final Context context = this;

    private static final String TAG = DetailTraveling.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    String tag_json_obj = "json_obj_req";
    int success;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.detail_traveling);
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


        String stts = _status.getText().toString();
        _cancel.setVisibility(View.VISIBLE);
        if (stts.equals("H")){
            _cancel.setVisibility(View.GONE);
        } else if (stts.equals("R")) {
            _cancel.setVisibility(View.GONE);
        } else {
            _cancel.setVisibility(View.VISIBLE);
        }


        _cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder alertDialoBuilder = new AlertDialog.Builder(context);
                alertDialoBuilder.setTitle("Alert!");
                alertDialoBuilder.setMessage("Are you sure want to cancel your request?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Cancel();
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
    }

    @Override
    public void onBackPressed() {
        finish();
    }

    private void Cancel(){
        StringRequest strReq = new StringRequest(Request.Method.POST,Server.delete_traveling, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){
                Log.d(TAG, "Response: " + response.toString());

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1){

                        Log.d("delete", jObj.toString());

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

