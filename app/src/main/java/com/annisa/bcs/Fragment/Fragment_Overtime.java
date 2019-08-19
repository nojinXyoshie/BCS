package com.annisa.bcs.Fragment;


import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.*;

import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;


import com.annisa.bcs.Adapter.Adapter_Overtime;
import com.annisa.bcs.AddOvertime;
import com.annisa.bcs.Data.Data_Overtime;
import com.annisa.bcs.R;
import com.annisa.bcs.Util.MySingleton;
import com.annisa.bcs.Util.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import static com.annisa.bcs.Login.TAG_NIK;


public class Fragment_Overtime extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FloatingActionButton btn_add_overtime;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    private static final String TAG = Fragment_Overtime.class.getSimpleName();
    SwipeRefreshLayout swipe;

    AlertDialog.Builder dialog;
    LayoutInflater inflater;
    View dialogView;

    //private ShimmerFrameLayout mShimmerViewContainer;
    private List<Data_Overtime> overtimeList = new ArrayList<Data_Overtime>();

    public Fragment_Overtime() {
        // Required empty public constructor
    }

    @BindView(R.id.rv_overtime)
    RecyclerView rv_overtime;
    Adapter_Overtime adapter_overtime;

    @BindView(R.id.search)
    EditText search;
    EditText searchPeriod;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_overtime, container, false);
        ButterKnife.bind(this, view);


        btn_add_overtime = (FloatingActionButton) view.findViewById(R.id.addDataOvertime);
        btn_add_overtime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddOvertime.class);
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
                           overtimeList.clear();
                           adapter_overtime.notifyDataSetChanged();
                           getOvertime();
                       }
                   }
        );

        getOvertime();

        return view;
    }

    private void Dialog(){
        dialog = new AlertDialog.Builder(getActivity());
        inflater = getLayoutInflater();
        dialogView = inflater.inflate(R.layout.search_overtime_dialog, null);
        dialog.setView(dialogView);
        dialog.setTitle("Search Overtime");

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
            public void onClick(DialogInterface dialogInterface, int i) {

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
        overtimeList.clear();
        adapter_overtime.notifyDataSetChanged();
        getOvertime();
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


    public void getOvertime() {
        adapter_overtime = new Adapter_Overtime(getActivity());
        rv_overtime.setLayoutManager(new GridLayoutManager(getActivity(), 1));
        rv_overtime.setAdapter(adapter_overtime);
        adapter_overtime.getDatas().clear();
        adapter_overtime.notifyDataSetChanged();
        final StringRequest request = new StringRequest(Request.Method.POST, Server.get_overtime, new Response.Listener<String>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onResponse(String response) {
                try {
                    JSONArray data = new JSONArray(response);
                    Gson gson = new Gson();
                    TypeToken<List<Data_Overtime>> token = new TypeToken<List<Data_Overtime>>() {
                    };
                    overtimeList = gson.fromJson(data.toString(), token.getType());
                    adapter_overtime.setDatas(overtimeList);
                    adapter_overtime.notifyDataSetChanged();
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

