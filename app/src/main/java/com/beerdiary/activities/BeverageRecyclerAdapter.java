package com.beerdiary.activities;

import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.beerdiary.DBHelper;
import com.beerdiary.R;

import java.util.ArrayList;

/**
 * Created by Jim on 9/4/2017.
 */

public class BeverageRecyclerAdapter extends RecyclerView.Adapter<BeverageRecyclerAdapter.ViewHolder> {
    private ArrayList<DBHelper.BeverageRow> mDataset;

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView toptext;
        public TextView bottomtext;

        public ViewHolder(CardView v) {
            super(v);
            mCardView= v.findViewById(R.id.select_beverage_recycler_view_recyclerview);
            toptext= v.findViewById(R.id.beverage_recycler_cardview_toptext);
            bottomtext= v.findViewById(R.id.beverage_recycler_cardview_bottomtext);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    DBHelper.BeverageRow bv;
                    Toast.makeText(v.getContext(), "Pos is " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    bv = mDataset.get(getAdapterPosition());
                }
            });
        }
    }
    @Override
    public int getItemViewType(int position) {	return position;  }
    public BeverageRecyclerAdapter(ArrayList<DBHelper.BeverageRow> myDataset) {
        mDataset=myDataset;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new card view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.beverage_recycler_cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        // return a viewholder tied to the adapter
        BeverageRecyclerAdapter.ViewHolder vh = new BeverageRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final BeverageRecyclerAdapter.ViewHolder holder, int position) {
        // - get element from your dataset at this position
        // - replace the contents of the view with that element
        holder.toptext.setText(mDataset.get(position).getName());
        holder.bottomtext.setText(mDataset.get(position).getMaker());
//        holder.mCardView.setOnClickListener();
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}


