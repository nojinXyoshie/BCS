package com.annisa.bcs.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
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

import android.widget.Toast;
import butterknife.BindView;
import butterknife.ButterKnife;
import com.android.volley.*;
import com.android.volley.toolbox.StringRequest;


import com.annisa.bcs.Adapter.Adapter_Travelling;
import com.annisa.bcs.AddTraveling;
import com.annisa.bcs.Data.Data_Travelling;
import com.annisa.bcs.R;
import com.annisa.bcs.Util.MySingleton;
import com.annisa.bcs.Util.Server;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.annisa.bcs.Login.TAG_NIK;


public class Fragment_Travelling_HR extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    FloatingActionButton btn_add_travelling;
    SharedPreferences sharedPreferences;
    public static final String my_shared_preferences = "my_shared_preferences";
    private static final String TAG = Fragment_Travelling.class.getSimpleName();
    SwipeRefreshLayout swipe;

    //private ShimmerFrameLayout mShimmerViewContainer;
    private List<Data_Travelling> travellingList = new ArrayList<Data_Travelling>();

    public Fragment_Travelling_HR() {
        // Required empty public constructor
    }

    @BindView(R.id.rv_travelling)
    RecyclerView rv_travelling;
    Adapter_Travelling adapter_travelling;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_travelling_hr, container, false);
        ButterKnife.bind(this, view);


        btn_add_travelling = (FloatingActionButton) view.findViewById(R.id.addDataTravelling);
        btn_add_travelling.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AddTraveling.class);
                startActivity(intent);
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
        final StringRequest request = new StringRequest(Request.Method.POST, Server.get_traveling, new Response.Listener<String>() {
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

