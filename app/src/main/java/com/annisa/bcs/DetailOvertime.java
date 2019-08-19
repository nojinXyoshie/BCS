package com.annisa.bcs;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.annisa.bcs.Data.Data_Overtime;

import butterknife.BindView;

/**
 * Created by User on 8/2/2019.
 */

public class DetailOvertime extends AppCompatActivity {

    @BindView(R.id.nik) TextView _nik;
    @BindView(R.id.name) TextView _name;
    @BindView(R.id.from) TextView _from;
    @BindView(R.id.to) TextView _to;
    @BindView(R.id.totalDays) TextView _totaldays;
    @BindView(R.id.rate) TextView _rate;
    @BindView(R.id.budget) TextView _budget;
    @BindView(R.id.project) TextView _project;
    @BindView(R.id.circleImageView) ImageView _image;
    @BindView(R.id.cancel) Button _cancel;
    @BindView(R.id.status) TextView _status;
    DetailOvertime data;
    final Context context = this;
}
