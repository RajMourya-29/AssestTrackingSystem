package com.example.assesttrackingsystem.mappingassest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assesttrackingsystem.R;

import java.util.ArrayList;

public class MappingAdapter extends RecyclerView.Adapter<MappingAdapter.ViewHolder>{

    ArrayList<MappingModel> result;
    Context context;

    public MappingAdapter(Context c, ArrayList<MappingModel> test) {

        result = test;
        context = c;

    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View listItem= layoutInflater.inflate(R.layout.barcode_list_item, parent, false);
        return new ViewHolder(listItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tv1.setText(result.get(position).getBarcode());
        holder.tv2.setText(result.get(position).getRfid());
    }

    @Override
    public int getItemCount() {
        return result.size();
    }

    public void clear(){
        int newPosition = getItemCount();
        result.remove(newPosition);
        notifyItemRemoved(newPosition);
        notifyItemRangeChanged(newPosition, result.size());
    }


    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView tv1, tv2, tv3, tv4;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv1 = (TextView) itemView.findViewById(R.id.barcode);
            tv2 = (TextView) itemView.findViewById(R.id.rfid);
          ;
        }
    }
}

