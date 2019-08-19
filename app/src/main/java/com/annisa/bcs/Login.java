package com.annisa.bcs;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.annisa.bcs.Util.MyApplication;
import com.annisa.bcs.Util.Server;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Login extends AppCompatActivity{
    ProgressDialog pDialog;
    Button btn_login;
    EditText txt_nik, txt_password;
    Intent intent;

    int success;
    ConnectivityManager conMgr;

    private String url = Server.URL+"login.php";
    private static final String TAG = MainActivity.class.getSimpleName();
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    public final static String TAG_NIK = "nik";
    public final static String TAG_NAME = "name";
    public final static String TAG_LEVEL = "level";
    public final static String TAG_EMAIL = "email";
    public final static String TAG_IMAGE = "image";

    String tag_json_obj = "json_obj_req";

    SharedPreferences sharedPreferences;
    Boolean session = false;
    String nik, name;
    public static final String my_shared_preferences = "my_shared_preferences";
    public static final String session_status = "session_status";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        conMgr = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        {
            if (conMgr.getActiveNetworkInfo() != null
                    && conMgr.getActiveNetworkInfo() .isAvailable()
                    && conMgr.getActiveNetworkInfo() .isConnected()){
            } else {
                Toast .makeText(getApplicationContext(), "No Internet Connection", Toast .LENGTH_LONG).show();
            }
        }

        btn_login = (Button) findViewById(R.id.btnLogin);
        txt_nik = (EditText) findViewById(R.id.nik);
        txt_password = (EditText) findViewById(R.id.password);

        TextView forgotPassword = (TextView) findViewById(R.id.textViewLupaPassword);
        forgotPassword.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                // Switching to Register screen
                Intent i = new Intent(getApplicationContext(), ForgotPassword.class);
                startActivity(i);
            }
        });


        sharedPreferences = getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
        session = sharedPreferences.getBoolean(session_status, false);
        nik = sharedPreferences.getString(TAG_NIK, null);
        name = sharedPreferences.getString(TAG_NAME, null);
        String email = sharedPreferences.getString(TAG_EMAIL, null);
        String level = sharedPreferences.getString(TAG_LEVEL, null);
        String image = sharedPreferences.getString(TAG_IMAGE, null);

        if (session){
            if(level.equals("HR")) {
                Intent intent = new Intent(Login.this, HRActivity.class);
                intent.putExtra(TAG_NIK, nik);
                intent.putExtra(TAG_NAME, name);
                intent.putExtra(TAG_EMAIL, email);
                intent.putExtra(TAG_LEVEL, level);
                finish();
                startActivity(intent);
            }
            else if(level.equals("PEGAWAI")) {
                Intent intent = new Intent(Login.this, MainActivity.class);
                intent.putExtra(TAG_NIK, nik);
                intent.putExtra(TAG_NAME, name);
                intent.putExtra(TAG_EMAIL, email);
                intent.putExtra(TAG_LEVEL, level);
                intent.putExtra(TAG_IMAGE, image);
                finish();
                startActivity(intent);
            }
            else if(level.equals("TL")) {
                Intent intent = new Intent(Login.this, TLActivity.class);
                intent.putExtra(TAG_NIK, nik);
                intent.putExtra(TAG_NAME, name);
                intent.putExtra(TAG_EMAIL, email);
                intent.putExtra(TAG_LEVEL, level);
                finish();
                startActivity(intent);
            }
        }

        btn_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String nik = txt_nik.getText().toString();
                String password = txt_password.getText().toString();

                if (nik.trim().length() > 0 && password.trim().length() > 0){
                    if (conMgr.getActiveNetworkInfo() != null
                            && conMgr.getActiveNetworkInfo().isAvailable()
                            && conMgr.getActiveNetworkInfo().isConnected()){
                        checkLogin(nik,password);
                    } else {
                        Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_LONG).show();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "Kolom tidak boleh kosong", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void checkLogin(final String nik, final String password){
        pDialog = new ProgressDialog(this);
        pDialog.setCancelable(false);
        pDialog.setMessage("Logging in...");
        showDialog();

        StringRequest strReq = new StringRequest(Request.Method.POST,url, new Response.Listener<String>(){

            @Override
            public void onResponse(String response){
                Log.e(TAG, "Login Response: " + response.toString());
                hideDialog();

                try {
                    JSONObject jObj = new JSONObject(response);
                    success = jObj.getInt(TAG_SUCCESS);

                    if (success == 1){
                        String nik = jObj.getString(TAG_NIK);
                        String name = jObj.getString(TAG_NAME);
                        String level = jObj.getString(TAG_LEVEL);
                        String email = jObj.getString(TAG_EMAIL);
                        String image = jObj.getString(TAG_IMAGE);

                        Log.e("Successfully Login!", jObj.toString());

                        Toast.makeText(getApplicationContext(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.putBoolean(session_status, true);
                        editor.putString(TAG_NIK, nik);
                        editor.putString(TAG_NAME, name);
                        editor.putString(TAG_LEVEL, level);
                        editor.putString(TAG_EMAIL, email);
                        editor.putString(TAG_IMAGE, image);
                        editor.commit();

                        if(level.equals("PEGAWAI")){
                            Intent intent = new Intent(Login.this, MainActivity.class);
                            intent.putExtra(TAG_NIK, nik);
                            intent.putExtra(TAG_NAME, name);
                            intent.putExtra(TAG_LEVEL, level);
                            intent.putExtra(TAG_EMAIL, email);
                            intent.putExtra(TAG_IMAGE, image);
                            finish();
                            startActivity(intent);
                        } else if(level.equals("HR")){
                            Intent intent = new Intent(Login.this, HRActivity.class);
                            intent.putExtra(TAG_NIK, nik);
                            intent.putExtra(TAG_NAME, name);
                            intent.putExtra(TAG_LEVEL, level);
                            intent.putExtra(TAG_EMAIL, email);
                            finish();
                            startActivity(intent);
                        } else if(level.equals("TL")){
                            Intent intent = new Intent(Login.this, TLActivity.class);
                            intent.putExtra(TAG_NIK, nik);
                            intent.putExtra(TAG_NAME, name);
                            intent.putExtra(TAG_LEVEL, level);
                            intent.putExtra(TAG_EMAIL, email);
                            finish();
                            startActivity(intent);
                        }


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
                Log.e(TAG, "Login Error: "+error.getMessage());
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_LONG).show();

                hideDialog();
            }
        }) {
            @Override
            protected Map<String, String> getParams(){
                Map<String, String> params = new HashMap<String, String>();
                params.put("nik", nik);
                params.put("password", password);

                return params;
            }
        };
        MyApplication.getInstance().addToRequestQueue(strReq,tag_json_obj);
    }
    private void showDialog(){
        if (!pDialog.isShowing())
            pDialog.show();
    }
    private void hideDialog(){
        if (pDialog.isShowing())
            pDialog.dismiss();
    }


}