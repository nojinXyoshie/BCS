package com.annisa.bcs.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.annisa.bcs.Data.Data_Overtime;
import com.annisa.bcs.R;
import com.annisa.bcs.Util.Server;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class Adapter_Overtime extends RecyclerView.Adapter<Adapter_Overtime.ViewHolder> {
    private List<Data_Overtime> datas;
    private Context context;
    private List<Data_Overtime> orig;

    public Adapter_Overtime(Context context) {

        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<Data_Overtime> datas) {this.datas = datas;}

    public List<Data_Overtime> getDatas() {return datas;}

    @Override
    public Adapter_Overtime.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.overtime_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }



    @Override
    public void onBindViewHolder(Adapter_Overtime.ViewHolder holder, int position) {
        holder.projectOT.setText(datas.get(position).getProjectOt());
//        holder.customer.setText(datas.get(position).getCustomer());
        holder.claimstatus.setText(datas.get(position).getClaimstatus());
        holder.atten_date.setText(datas.get(position).getAtten_date());
        Data_Overtime data = datas.get(position);
        Picasso.with(context).load(Server.url_image_user + data.getImageBean().getIMAGE())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {

                    }
                });


//        Picasso.with(context).load(Server.IMAGE_URL + data.getFoto_ikan())
//                .memoryPolicy(MemoryPolicy.NO_CACHE)
//                .networkPolicy(NetworkPolicy.NO_CACHE)
//                .into(holder.foto_ikan, new Callback() {
//                    @Override
//                    public void onSuccess() {
////                        holder.loading_image.setVisibility(View.GONE);
//                    }
//
//                    @Override
//                    public void onError() {
////                        holder.foto_seller.setImageDrawable(context.getResources().getDrawable(R.drawable.noimage));
////                        holder.loading_image.setVisibility(View.GONE);
//                    }
//                });
        holder.projectOT.setText(data.getProjectOt());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }


    public class ViewHolder extends RecyclerView.ViewHolder {
//        @BindView(R.id.customer)
//        TextView customer;
        @BindView(R.id.projectOt)
        TextView projectOT;
        @BindView(R.id.claimstatus)
        TextView claimstatus;
        @BindView(R.id.atten_date)
        TextView atten_date;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    context.startActivity(new Intent(context, Detail_paket.class)
//                            .putExtra("dataf",datas.get(getAdapterPosition())));
                }
            });
        }
    }
}