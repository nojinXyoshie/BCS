package com.annisa.bcs.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.annisa.bcs.Data.Data_Travelling;
import com.annisa.bcs.DetailTravelling;
import com.annisa.bcs.Login;
import com.annisa.bcs.R;
import com.annisa.bcs.Util.Server;
import com.squareup.picasso.Callback;
import com.squareup.picasso.MemoryPolicy;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.annisa.bcs.Login.TAG_IMAGE;
import static com.annisa.bcs.Login.TAG_NIK;

public class Adapter_Travelling extends RecyclerView.Adapter<Adapter_Travelling.ViewHolder> {
    private List<Data_Travelling> datas;
    private Context context;
    private List<Data_Travelling> orig;

    SharedPreferences sharedPreferences;

    public Adapter_Travelling(Context context) {

        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<Data_Travelling> datas) {this.datas = datas;}

    public List<Data_Travelling> getDatas() {return datas;}

    @Override
    public Adapter_Travelling.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.travelling_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(Adapter_Travelling.ViewHolder holder, int position) {
        holder.from.setText(datas.get(position).getFrom());
        holder.project.setText(datas.get(position).getProject());
        holder.to.setText(datas.get(position).getTo());
        holder.status.setText(datas.get(position).getStatus());
        Data_Travelling data = datas.get(position);
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

        //date format
        String dates = datas.get(position).getDate();
        SimpleDateFormat spf = new SimpleDateFormat("yyyy-MM-dd");
        try {
            Date newDate = spf.parse(dates);
            spf = new SimpleDateFormat("dd MMMM yyyy", new Locale("in", "ID"));
            dates = spf.format(newDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.date.setText(dates);

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
        holder.from.setText(data.getFrom());
    }

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public Filter getFilter(){
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                final FilterResults oReturn = new FilterResults();
                final List<Data_Travelling> results = new ArrayList<>();
                if (orig == null) {
                    orig = datas;
                }
                if (charSequence != null){
                    if (orig != null && orig.size() > 0){
                        for (final Data_Travelling g : orig){
                            if (g.getDate().toLowerCase().contains(charSequence.toString().toLowerCase())) {
                                results.add(g);
                            } else if (g.getStatus().toLowerCase().contains(charSequence.toString().toLowerCase())){
                                results.add(g);
                            }
                        }
                        oReturn.values = results;
                    }
                }
                return oReturn;
            }

            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                datas = (ArrayList<Data_Travelling>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.from)
        TextView from;
        @BindView(R.id.to)
        TextView to;
        @BindView(R.id.project)
        TextView project;
        @BindView(R.id.status)
        TextView status;
        @BindView(R.id.date)
        TextView date;
        @BindView(R.id.thumbnail)
        ImageView thumbnail;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, DetailTravelling.class)
                            .putExtra("dataf",datas.get(getAdapterPosition())));
                }
            });
        }
    }
}