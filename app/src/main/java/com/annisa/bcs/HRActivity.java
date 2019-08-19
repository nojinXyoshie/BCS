package com.annisa.bcs;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.content.Context;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.TextView;

import com.annisa.bcs.Fragment.Fragment_Home;
import com.annisa.bcs.Fragment.Fragment_Home_HR;
import com.annisa.bcs.Fragment.Fragment_Overtime_HR;
import com.annisa.bcs.Fragment.Fragment_Profile_HR;
import com.annisa.bcs.Fragment.Fragment_Traveling_HR;

public class HRActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String nik, name;
    private Toolbar toolbar;
    public Toolbar getToolbar(){ return toolbar; }

    public static final String TAG_NIK="nik";
    public static final String TAG_NAME="name";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hr);

        sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);

        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("Home");

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_bottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        nik = getIntent().getStringExtra(TAG_NIK);
        name = getIntent().getStringExtra(TAG_NAME);

        TextView text = findViewById(R.id.toolbar_text);
        text.setText("Home");
        loadFragment(new Fragment_Home());

    }

    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Fragment fragment;
            TextView toolbar_text = findViewById(R.id.toolbar_text);
            switch (item.getItemId()) {
                case R.id.action_home:
                    toolbar_text.setText("Home");
                    fragment = new Fragment_Home_HR();
                    loadFragment(fragment);
                    return true;
                case R.id.action_overtime:
                    toolbar_text.setText("Overtime");
                    fragment = new Fragment_Overtime_HR();
                    loadFragment(fragment);
                    return true;
                case R.id.action_travelling:
                    toolbar_text.setText("Travelling");
                    fragment = new Fragment_Traveling_HR();
                    loadFragment(fragment);
                    return true;
                case R.id.action_profile:
                    toolbar_text.setText("Profile");
                    fragment = new Fragment_Profile_HR();
                    loadFragment(fragment);
                    return true;
            }
            return false;
        }
    };

    private void loadFragment(Fragment fragment) {

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.framekary, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
