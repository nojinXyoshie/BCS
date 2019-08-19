package com.annisa.bcs.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.*;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;


import com.annisa.bcs.Adapter.Adapter_Travelling;
import com.annisa.bcs.AddTraveling;
import com.annisa.bcs.Data.Data_Travelling;
import com.annisa.bcs.R;
import com.annisa.bcs.Util.MyApplication;
import com.annisa.bcs.Util.MySingleton;
import com.annisa.bcs.Util.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.annisa.bcs.Login.TAG_NIK;


public class Fragment_Travelling extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FloatingActionButton btn_add_travelling;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    private static final String TAG = Fragment_Travelling.class.getSimpleName();
    SwipeRefreshLayout swipe;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    int success;
    private static final String TAG_SUCCESS = "success";
    private static final String TAG_MESSAGE = "message";
    private String KEY_IMAGE = "image";
    private String KEY_NIK = "nik";
    String tag_json_obj = "json_obj_req";

    //private ShimmerFrameLayout mShimmerViewContainer;
    private List<Data_Travelling> travellingList = new ArrayList<Data_Travelling>();

    public Fragment_Travelling() {
        // Required empty public constructor
    }

    @BindView(R.id.rv_travelling)
    RecyclerView rv_travelling;
    @BindView(R.id.search)
    EditText search;
    EditText searchPeriod;
    Adapter_Travelling adapter_travelling;
    Spinner status;
    String period;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travelling, container, false);
        ButterKnife.bind(this, view);

        status = view.findViewById(R.id.searchStatusTravelling);

        btn_add_travelling = (FloatingActionButton) view.findViewById(R.id.addDataTravelling);
        btn_add_travelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTraveling.class);
                startActivity(intent);
            }

        });

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Dialog();
            }
        });

        swipe   = (SwipeRefreshLayout) view.findViewById(R.id.swipe_refresh_layout);
       // mShimmerViewContainer = (ShimmerFrameLayout) view.findViewById(R.id.shimmer_view_container);

        // menampilkan widget refresh
        swipe.setOnRefreshListener(this);
        swipe.post(new Runnable() {
                       @Override
                       public void run() {
                           swipe.setRefreshing(true);
                           travellingList.clear();
                           adapter_travelling.notifyDataSetChanged();
                           getTravelling();
                       }
                   }
        );

        getTravelling();

        return view;
    }

    private void Dialog(){
        dialog = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.search_travelling_dialog, null);
        dialog.setView(dialogView);
        dialog.setTitle("Search Travelling");

        searchPeriod = dialogView.findViewById(R.id.searchPeriod);
        searchPeriod.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub
                new DatePickerDialog(getActivity(), date, myCalendar
                        .get(Calendar.YEAR), myCalendar.get(Calendar.MONTH),
                        myCalendar.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        dialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                period = searchPeriod.getText().toString();
                adapter_travelling.getFilter().filter(period);
            }
        });

        dialog.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        dialog.show();
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
        String myFormat = "MMMM yyyy"; //In which you need put here
        SimpleDateFormat sdf = new SimpleDateFormat(myFormat, new Locale("in", "ID"));

        searchPeriod.setText(sdf.format(myCalendar.getTime()));
    }

    @Override
    public void onRefresh() {
        travellingList.clear();
        adapter_travelling.notifyDataSetChanged();
        getTravelling();
    }

    @Override
    public void onResume() {
        super.onResume();
        //mShimmerViewContainer.startShimmerAnimation();
    }

    @Override
    public void onPause() {
        //mShimmerViewContainer.stopShimmerAnimation();
        super.onPause();
    }
    

    public void getTravelling() {
        adapter_travelling = new Adapter_Travelling(getActivity());
        rv_travelling.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rv_travelling.setAdapter(adapter_travelling);
        adapter_travelling.getDatas().clear();
        adapter_travelling.notifyDataSetChanged();
        final StringRequest request = new StringRequest(Request.Method.POST, Server.get_traveling_pegawai, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray data = new JSONArray(response);
                    Gson gson = new Gson();
                    TypeToken<List<Data_Travelling>> token = new TypeToken<List<Data_Travelling>>() {
                    };
                    travellingList = gson.fromJson(data.toString(), token.getType());
                    adapter_travelling.setDatas(travellingList);
                    adapter_travelling.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "Error: " + error.getMessage());
                Toast.makeText(getContext(), "Error: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                // Posting parameters ke post url
                Map<String, String> params = new HashMap<String, String>();

                sharedPreferences = getActivity().getSharedPreferences(my_shared_preferences, Context.MODE_PRIVATE);
                String nik = sharedPreferences.getString(TAG_NIK, null);
                params.put("nik", nik);

                return params;
            }
        };
        swipe.setRefreshing(false);
        request.setRetryPolicy(new DefaultRetryPolicy(
                Server.TIMEOUT_ACCESS,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        MySingleton.getInstance(getActivity()).addToRequestQueue(request);
    }

}

