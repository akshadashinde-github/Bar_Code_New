package com.maitriinfosoft.bar_code_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class Scandata_Adapter extends RecyclerView.Adapter<Scandata_Adapter.ViewHolder> {

    Context context;
    List<ScanData> scanData;
    String itemCode, itemName,quantity,batchNo,scanQty,draftNumber;

    public Scandata_Adapter(Context context, List<ScanData> scanData) {
        this.context = context;
        this.scanData = scanData;
    }

    @NonNull
    @Override
    public Scandata_Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view= LayoutInflater.from(context).inflate(R.layout.list_design,parent,false);
        return new ViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull Scandata_Adapter.ViewHolder holder, int position) {


        holder.tv_draftNumber.setText(scanData.get(position).getDraftNumber());
        holder.tv_itemCode.setText(scanData.get(position).getItemCode());
        holder.tv_itemName.setText(scanData.get(position).getDescription());
        holder.tv_quality.setText(scanData.get(position).getQuality());
        holder.tv_batchNo.setText(scanData.get(position).getBatchNo());
        holder.tv_batch_qty.setText(scanData.get(position).getBatchQty());
        holder.tv_scanQty.setText(scanData.get(position).getScan_qlt());
       // holder.tv_draftNumber.setText(scanData.get(position).getDraftNumber());



         draftNumber = scanData.get(position).getDraftNumber();
        itemCode = scanData.get(position).getItemCode();
        itemName = scanData.get(position).getDescription();
        quantity = scanData.get(position).getQuality();
        batchNo = scanData.get(position).getBatchNo();
        scanQty = scanData.get(position).getScan_qlt();


    }

    public void updateList (List<ScanData> items) {
        if (items != null && items.size() > 0) {
            scanData.clear();
            scanData.addAll(items);
            notifyDataSetChanged();
        }
    }


    @Override
    public int getItemCount() {
        return scanData.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView tv_itemCode, tv_itemName, tv_quality,tv_scanQty,tv_batchNo,tv_draftNumber,tv_batch_qty;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            tv_itemCode=itemView.findViewById(R.id.tv_ItemCode);
            tv_itemName=itemView.findViewById(R.id.tv_Description);
            tv_quality=itemView.findViewById(R.id.tv_quantity);
            tv_scanQty=itemView.findViewById(R.id.tv_scan_qty);
            tv_batchNo=itemView.findViewById(R.id.tv_batchNo);
            tv_draftNumber=itemView.findViewById(R.id.tv_draftNumber);
            tv_batch_qty=itemView.findViewById(R.id.tv_batch_qty);



        }
    }
}
