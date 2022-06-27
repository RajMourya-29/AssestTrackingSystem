package com.example.assesttrackingsystem.transferassest.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.assesttrackingsystem.R;
import com.example.assesttrackingsystem.transferassest.pojoclass.IssueAssetDetail;

import java.util.List;

public class IssueAssestDetailAdapter extends RecyclerView.Adapter<IssueAssestDetailAdapter.ViewHolder> {

    List<IssueAssetDetail> issueAssetDetailList;
    Context context;

    public IssueAssestDetailAdapter(List<IssueAssetDetail> issueAssetDetailList, Context context) {
        this.issueAssetDetailList = issueAssetDetailList;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.issue_asset_details,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        IssueAssetDetail assetDetail = issueAssetDetailList.get(position);

        holder.empname.setText(assetDetail.getEmployeename());
        holder.location.setText(assetDetail.getLocationname());
        holder.sublocation.setText(assetDetail.getSubLocation());
        holder.assignon.setText(assetDetail.getDate());
        holder.active.setText(assetDetail.getIsactive());
    }

    @Override
    public int getItemCount() {
        return issueAssetDetailList.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        TextView empname,location,sublocation,assignon,active;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            empname = itemView.findViewById(R.id.empname);
            location = itemView.findViewById(R.id.location);
            sublocation = itemView.findViewById(R.id.sublocation);
            active = itemView.findViewById(R.id.active);
            assignon = itemView.findViewById(R.id.assignon);

        }
    }

}
