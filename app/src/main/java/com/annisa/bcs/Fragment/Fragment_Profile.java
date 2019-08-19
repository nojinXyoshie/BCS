package com.annisa.bcs.Fragment;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.annisa.bcs.ChangePassword;
import com.annisa.bcs.Login;
import com.annisa.bcs.R;
import com.annisa.bcs.Util.MyApplication;
import com.annisa.bcs.Util.Server;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static android.app.Activity.RESULT_OK;
import static com.annisa.bcs.Login.TAG_EMAIL;
import static com.annisa.bcs.Login.TAG_IMAGE;
import static com.annisa.bcs.Login.TAG_LEVEL;
import static com.annisa.bcs.Login.TAG_NAME;
import static com.annisa.bcs.Login.TAG_NIK;



public class Fragment_Profile extends Fragment {

    ArrayList<HashMap<String, String>> list_data;
    Intent intent;
    Uri fileUri;
    String nik, image;
    TableRow tbl_row_change_password;
    ImageButton bchooseImage;
    ImageView imageView;
    Bitmap bitmap, decoded;
    public final int REQUEST_CAMERA = 0;
    public final int PICK_IMAGE_REQUEST = 1;
    int success;
    int bitmap_size = 60; // image quality 1 - 100;
    int max_resolution_image = 800;
    String tag_json_obj = "json_obj_req";

    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";
    private String KEY_NIK = "nik";
    private static final String TAG = Fragment_Home.class.getSimpleName();

    public Fragment_Profile() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_profile, container, false);
        final SharedPreferences sharedPreferences;
        sharedPreferences = getActivity().getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);
        nik = sharedPreferences.getString(TAG_NIK, null);
        image = sharedPreferences.getString(TAG_IMAGE, null);

        bchooseImage = (ImageButton) view.findViewById(R.id.chooseImage);
        imageView = (ImageView) view.findViewById(R.id.profileImage);
        Context c = getActivity().getApplicationContext();
        Picasso.with(c).load(Server.url_image_user + image)
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(imageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        imageView.setImageDrawable(getContext().getResources().getDrawable(R.drawable.profile));
                    }

                });

        TextView txtname = (TextView) view.findViewById(R.id.txtname);
        final TextView txtnik = (TextView) view.findViewById(R.id.txtnik);
        TextView txtemail = (TextView) view.findViewById(R.id.txtemail);
        String name = sharedPreferences.getString(TAG_NAME, null);
        txtname.setText(name);
        final String nik = sharedPreferences.getString(TAG_NIK, null);
        txtnik.setText(nik);
        String email = sharedPreferences.getString(TAG_EMAIL, null);
        txtemail.setText(email);


        TableRow rowLogout = (TableRow) view.findViewById(R.id.rowLogout);
        rowLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(Login.session_status, false);
                editor.putString(TAG_NIK, null);
                editor.putString(TAG_NAME, null);
                editor.putString(TAG_LEVEL, null);
                editor.putString(TAG_EMAIL, null);
                editor.commit();

                Intent intent = new Intent(getActivity().getApplication(), Login.class);
                getActivity().finish();
                startActivity(intent);
            }
        });

        TableRow changePassword = (TableRow) view.findViewById(R.id.changePassword);
        changePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ChangePassword.class);
//                intent.putExtra("nik", txtnik.getText().toString());
                startActivity(intent);
            }
        });

        //Action ketika button image di klik
        bchooseImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showFileChooser();
            }
        });


        return view;
    }

    public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void uploadImage() {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, Server.upload_profile,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e(TAG, "Response: " + response.toString());

                        try {
                            JSONObject jObj = new JSONObject(response);
                            success = jObj.getInt(TAG_SUCCESS);

                            if (success == 1) {
                                Log.e("v Add", jObj.toString());

                                Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();

                                //kosong();
                                //finish();

                            } else {
                                Toast.makeText(getActivity(), jObj.getString(TAG_MESSAGE), Toast.LENGTH_LONG).show();
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        //menampilkan toast
                        Toast.makeText(getActivity(), error.getMessage().toString(), Toast.LENGTH_LONG).show();
                        Log.e(TAG, error.getMessage().toString());
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                //membuat parameters
                Map<String, String> params = new HashMap<String, String>();

                //menambah parameter yang di kirim ke web servis
                params.put(KEY_IMAGE, getStringImage(decoded));
                params.put(KEY_NIK, nik);

                //kembali ke parameters
                Log.e(TAG, "" + params);
                return params;
            }
        };

        MyApplication.getInstance().addToRequestQueue(stringRequest, tag_json_obj);
    }

    private void showFileChooser(){
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //mengambil fambar dari Gallery
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                // 512 adalah resolusi tertinggi setelah image di resize, bisa di ganti.
                setToImageView(getResizedBitmap(bitmap, 512));
                uploadImage();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void setToImageView(Bitmap bmp) {
        //compress image
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, bitmap_size, bytes);
        decoded = BitmapFactory.decodeStream(new ByteArrayInputStream(bytes.toByteArray()));

        //menampilkan gambar yang dipilih dari camera/gallery ke ImageView
        imageView.setImageBitmap(decoded);

    }

    // fungsi resize image
    public Bitmap getResizedBitmap(Bitmap image, int maxSize) {
        int width = image.getWidth();
        int height = image.getHeight();

        float bitmapRatio = (float) width / (float) height;
        if (bitmapRatio > 1) {
            width = maxSize;
            height = (int) (width / bitmapRatio);
        } else {
            height = maxSize;
            width = (int) (height * bitmapRatio);
        }
        return Bitmap.createScaledBitmap(image, width, height, true);
    }

}
