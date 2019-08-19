package com.annisa.bcs;

import android.content.Intent;
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
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.annisa.bcs.Fragment.Fragment_Home;
import com.annisa.bcs.Fragment.Fragment_Overtime;
import com.annisa.bcs.Fragment.Fragment_Profile;
import com.annisa.bcs.Fragment.Fragment_Traveling;

import static com.annisa.bcs.Login.TAG_IMAGE;

public class MainActivity extends AppCompatActivity {
    SharedPreferences sharedPreferences;
    String nik, name, image;
    Button btnNotification;
    private Toolbar toolbar;
    public Toolbar getToolbar(){ return toolbar; }

    public static final String TAG_NIK="nik";
    public static final String TAG_NAME="name";

    @Override
    protected void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        sharedPreferences = getSharedPreferences(Login.my_shared_preferences, Context.MODE_PRIVATE);

        setSupportActionBar(toolbar);


        TextView toolbar_text = findViewById(R.id.toolbar_text);
        toolbar_text.setText("Home");


        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.nav_bottom);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);


        nik = getIntent().getStringExtra(TAG_NIK);
        name = getIntent().getStringExtra(TAG_NAME);
        image = getIntent().getStringExtra(TAG_IMAGE);

        //load fragment
        TextView text = findViewById(R.id.toolbar_text);
        text.setText("Home");
        loadFragment(new Fragment_Home());

        btnNotification = findViewById(R.id.notification);
        btnNotification.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, Notification.class);
                startActivity(intent);

            }
        });

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
                    fragment = new Fragment_Home();
                    loadFragment(fragment);
                    return true;
                case R.id.action_overtime:
                    toolbar_text.setText("Overtime");
                    fragment = new Fragment_Overtime();
                    loadFragment(fragment);
                    return true;
                case R.id.action_travelling:
                    toolbar_text.setText("Traveling");
                    fragment = new Fragment_Traveling();
                    loadFragment(fragment);
                    return true;
                case R.id.action_profile:
                    toolbar_text.setText("Profile");
                    fragment = new Fragment_Profile();
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
