package com.annisa.bcs.Adapter;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.ImageView;
import android.widget.TextView;

import com.annisa.bcs.Data.Data_Traveling;
import com.annisa.bcs.DetailTraveling;
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

public class Adapter_Traveling extends RecyclerView.Adapter<Adapter_Traveling.ViewHolder> {
    private List<Data_Traveling> datas;
    private Context context;
    private List<Data_Traveling> orig;

    SharedPreferences sharedPreferences;

    public Adapter_Traveling(Context context) {
        this.context = context;
        datas = new ArrayList<>();
    }

    public void setDatas(List<Data_Traveling> datas) {this.datas = datas;}

    public List<Data_Traveling> getDatas() {return datas;}

    @Override
    public Adapter_Traveling.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.traveling_list_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final Adapter_Traveling.ViewHolder holder, int position) {
        holder.from.setText(datas.get(position).getFrom());
        holder.project.setText(datas.get(position).getProject());
        holder.to.setText(datas.get(position).getTo());
        holder.status.setText(datas.get(position).getStatus());
        Data_Traveling data = datas.get(position);
        Picasso.with(context).load(Server.url_image_user + data.getImageBean().getIMAGE())
                .memoryPolicy(MemoryPolicy.NO_CACHE)
                .networkPolicy(NetworkPolicy.NO_CACHE)
                .into(holder.thumbnail, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        holder.thumbnail.setImageResource(R.drawable.profile);
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
                final List<Data_Traveling> results = new ArrayList<>();
                if (orig == null) {
                    orig = datas;
                }
                if (charSequence != null){
                    if (orig != null && orig.size() > 0){
                        for (final Data_Traveling g : orig){
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
                datas = (ArrayList<Data_Traveling>) filterResults.values;
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

        public ViewHolder(final View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, DetailTraveling.class)
                            .putExtra("dataf",datas.get(getAdapterPosition())));
                }
            });
        }
    }
}