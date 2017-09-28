package com.beerdiary.activities;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beerdiary.DBHelper;
import com.beerdiary.R;

import java.util.ArrayList;

import static android.app.Activity.RESULT_OK;
import static com.beerdiary.activities.DrinkRecyclerAdapter.context;

/**
 * Created by Jim on 9/4/2017.
 */

 class BeverageRecyclerAdapter extends RecyclerView.Adapter<BeverageRecyclerAdapter.ViewHolder> {
    private  static String TAG = "BEVERAGERecyclerAdapter";
    private ArrayList<DBHelper.BeverageRow> mDataset;
    private Activity mActivity;

    BeverageRecyclerAdapter(ArrayList<DBHelper.BeverageRow> myDataset, Activity act) { mDataset=myDataset; mActivity=act; }

    class ViewHolder extends RecyclerView.ViewHolder {
         CardView mCardView;
         TextView toptext;
         TextView bottomtext;

         ViewHolder(CardView v) {
            super(v);

            mCardView= v.findViewById(R.id.beverage_recycler_cardview);
            toptext= v.findViewById(R.id.beverage_recycler_cardview_toptext);
            bottomtext= v.findViewById(R.id.beverage_recycler_cardview_bottomtext);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new card view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beverage_recycler_cardview, parent, false);

        final ViewHolder result = new BeverageRecyclerAdapter.ViewHolder(v);
        return result;
    }

    @Override
    public void onBindViewHolder(final BeverageRecyclerAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        final int pos = holder.getAdapterPosition();
        DBHelper.BeverageRow br = mDataset.get(pos);

        holder.toptext.setText(br.getName());
        holder.bottomtext.setText(br.getMaker());
        holder.mCardView.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(final View v) {

                resultReturn(holder.getAdapterPosition());
            }
        });
        br.getName();
    }

    private void resultReturn(int pos){

        DBHelper.BeverageRow bv = mDataset.get(pos).getRow();
        Intent result = new Intent();
        result.putExtra("beverage", bv.getName());
//        result.putExtra("beverage", pos);
        Log.d(TAG, "item:" + pos + " name:" + bv.getName());
        this.mActivity.setResult(RESULT_OK, result);
        this.mActivity.finish();
    }

    @Override
    public int getItemViewType(int position) {	return R.layout.beverage_recycler_cardview;  }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }

    public String getBeverageNameAt(int pos) {
        return mDataset.get(pos).getName();
    }
}


