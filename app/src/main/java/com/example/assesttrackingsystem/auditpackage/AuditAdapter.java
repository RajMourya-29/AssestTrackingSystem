package com.example.assesttrackingsystem.auditpackage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assesttrackingsystem.R;

import java.util.ArrayList;

public class AuditAdapter extends RecyclerView.Adapter<AuditAdapter.ViewHolder>{

    ArrayList<AuditModel> result;
    Context context;
    private Databasehelper helper;

    public AuditAdapter(Context c, ArrayList<AuditModel> test) {

        result = test;
        context = c;
        helper = new Databasehelper(context);

    }

    @NonNull
    @Override
    public AuditAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.scanbatches, parent, false);
        return new ViewHolder(listItem);

    }

    @Override
    public void onBindViewHolder(@NonNull AuditAdapter.ViewHolder holder, int position)
    {
        holder.tv1.setText(result.get(position).getBarcode());
        holder.tv2.setText(result.get(position).getRfid());
        holder.tv3.setText(result.get(position).getLoc());
        holder.tv4.setText(result.get(position).getSubloc());
        holder.tv5.setText(result.get(position).getDept());

    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2, tv3, tv4,tv5;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tv1 = (TextView) itemView.findViewById(R.id.barcode);
            tv2 = (TextView) itemView.findViewById(R.id.rfid);
            tv3 = (TextView) itemView.findViewById(R.id.loc);
            tv4 = (TextView) itemView.findViewById(R.id.subloc);
            tv5 = (TextView) itemView.findViewById(R.id.dpt);
        }
    }
}
