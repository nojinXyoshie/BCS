package com.annisa.bcs;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.InputType;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.annisa.bcs.Data.Data_User;
import com.annisa.bcs.Fragment.Fragment_Profile;
import com.annisa.bcs.Util.MyApplication;
import com.annisa.bcs.Util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import cn.pedant.SweetAlert.SweetAlertDialog;
import cyd.awesome.material.AwesomeText;
import cyd.awesome.material.FontCharacterMaps;

import static com.annisa.bcs.Login.TAG_NIK;


public class ChangePassword extends AppCompatActivity {

    @BindView(R.id.buttonSave) Button bSave;
    @BindView(R.id.btnclose) Button bClose;
    @BindView(R.id.currentPassword) EditText currentPassword;
    @BindView(R.id.newPassword) EditText newPassword;
    @BindView(R.id.confirmPassword) EditText confirmPassword;
    @BindView(R.id.showCurrentPassword) AwesomeText showCurrentPassword;
    @BindView(R.id.showNewPassword) AwesomeText showNewPassword;
    @BindView(R.id.showConfirmPassword) AwesomeText showConfirmPassword;
    private String nik, Current_Password, New_Password, Confirm_Password;
    private static final String TAG = ChangePassword.class.getSimpleName();
    private SweetAlertDialog pDialog;
    private int success;
    private String Url = Server.change_password;

    public String TAG_SUCCESS = "response";
    public String TAG_MESSAGE = "message";

    String tag_json_obj = "json_obj_req";
    ConnectivityManager conMgr;
    boolean pwd_status = true;
    SharedPreferences sharedPreferences;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo().isAvailable()
                    && conMgr.getActiveNetworkInfo().isConnected()) {
            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection",
                        Toast.LENGTH_LONG).show();
            }
        }

        currentPassword = (EditText) findViewById(R.id.currentPassword);
        newPassword = (EditText) findViewById(R.id.newPassword);
        confirmPassword = (EditText) findViewById(R.id.confirmPassword);

        bSave = findViewById(R.id.buttonSave);
        bSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Current_Password = currentPassword.getText().toString();
                New_Password = newPassword.getText().toString();
                Confirm_Password = confirmPassword.getText().toString();

                conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
                {
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()) {
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection",
                                Toast.LENGTH_LONG).show();
                    }
                }

                nik = getIntent().getStringExtra("nik");
                sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

                doEdit(nik,Current_Password,New_Password,Confirm_Password);

            }
        });

        showCurrentPassword = findViewById(R.id.showCurrentPassword);
        showCurrentPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    currentPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    showCurrentPassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    currentPassword.setSelection(currentPassword.length());
                } else {
                    currentPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    showCurrentPassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    currentPassword.setSelection(currentPassword.length());
                }
            }
        });

        showNewPassword = findViewById(R.id.showNewPassword);
        showNewPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    showNewPassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    newPassword.setSelection(newPassword.length());
                } else {
                    newPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    showNewPassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    newPassword.setSelection(newPassword.length());
                }
            }
        });

        showConfirmPassword = findViewById(R.id.showConfirmPassword);
        showConfirmPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (pwd_status) {
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_VISIBLE_PASSWORD);
                    pwd_status = false;
                    showConfirmPassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY);
                    confirmPassword.setSelection(confirmPassword.length());
                } else {
                    confirmPassword.setInputType(InputType.TYPE_TEXT_VARIATION_PASSWORD | InputType.TYPE_CLASS_TEXT);
                    pwd_status = true;
                    showConfirmPassword.setMaterialDesignIcon(FontCharacterMaps.MaterialDesign.MD_VISIBILITY_OFF);
                    confirmPassword.setSelection(confirmPassword.length());
                }
            }
        });


        bClose = findViewById(R.id.btnclose);
        bClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void doEdit(final String nik, final String current_password, final String new_password, final String confirm_password){
        pDialog = new SweetAlertDialog(ChangePassword.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setCancelable(false);
        pDialog.setTitleText("Please Wait ...");
        showDialog();
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Url+"?nik="+nik, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.e(TAG, "Edit Password Response: " + response.toString());
                hideDialog();
                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    // Check for error node in json
                    if (success == 1) {

                        Log.e("Successfully Edit!", jObj.toString());

                        showDialog(jObj.getString(TAG_MESSAGE));


                    } else {
                        Toast.makeText(getApplicationContext(),jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    // JSON error
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {

            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Edit Password Error: " + error.getMessage());
                hideDialog();
                Toast.makeText(getApplicationContext(),
                        error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                params.put("current_password", current_password);
                params.put("new_password", new_password);
                params.put("confirm_password", confirm_password);

                return params;
            }

        };

        MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);

    }

    private void showDialog(final String message){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage(message)
                .setNeutralButton("Oke", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Intent ni = new Intent(getApplicationContext(), Fragment_Profile.class);
                        ni.putExtra("menu","3");
                        startActivity(ni);
                        finish();
                    }
                });
        AlertDialog alertDialog = alertDialogBuilder.create();

        alertDialog.show();

        Button pButton = alertDialog.getButton(DialogInterface.BUTTON_POSITIVE);
        pButton.setTextColor(getResources().getColor(R.color.white));
    }

    @Override
    public void onBackPressed() {
        Intent ni = new Intent(getApplicationContext(), Fragment_Profile.class);
        ni.putExtra("menu","3");
        startActivity(ni);
        finish();
    }

    private void showDialog() {
        if (!pDialog.isShowing())
            pDialog.show();
    }

    private void hideDialog() {
        if (pDialog.isShowing())
            pDialog.dismiss();
    }

}
