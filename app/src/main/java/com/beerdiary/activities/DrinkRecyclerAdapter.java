package com.beerdiary.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import android.content.Intent;

import com.beerdiary.DBHelper;
import com.beerdiary.R;

import java.util.ArrayList;

import static com.beerdiary.BeerDiary.EDIT_BEVERAGE;
import static com.beerdiary.BeerDiary.PICK_BEVERAGE;

/**
 * Created by Jim on 9/4/2017.
 */
public class DrinkRecyclerAdapter extends RecyclerView.Adapter<DrinkRecyclerAdapter.ViewHolder> {
    private ArrayList<DBHelper.DrinkRow> mDataset;
    static Context context;

    public DrinkRecyclerAdapter(Context con, ArrayList<DBHelper.DrinkRow> myDataset) {
        mDataset=myDataset;
        context = con;
    }

    @Override
    public int getItemViewType(int position) {	return position;  }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView toptext, bottomtext;
        public ImageButton btnEdit, btnAdd;

        public ViewHolder(CardView v) {
            super(v);
            mCardView= v.findViewById(R.id.select_drink_recycler_view_recyclerview);
            toptext= v.findViewById(R.id.drink_recycler_cardview_toptext);
            bottomtext= v.findViewById(R.id.drink_recycler_cardview_bottomtext);

            v.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Toast.makeText(v.getContext(), "Pos is " + getAdapterPosition(), Toast.LENGTH_SHORT).show();
                    DBHelper.BeverageRow br = mDataset.get(getAdapterPosition()).getBeverage();

//					Intent intent = new Intent(Intent.ACTION_PICK, SelectBeverageRecycler.class);
//                    intent.putExtra("EDITING_MODE", true); // EDITING
//					intent.putExtra("BID", br.get_id());
//					startActivityForResult(intent, PICK_BEVERAGE);
                }
            });
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new card view
        CardView v = (CardView) LayoutInflater.from(parent.getContext())
                .inflate(R.layout.drink_recycler_cardview, parent, false);
        // set the view's size, margins, paddings and layout parameters

        // return a viewholder tied to the adapter
        DrinkRecyclerAdapter.ViewHolder vh = new DrinkRecyclerAdapter.ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(final DrinkRecyclerAdapter.ViewHolder holder, final int position) {
        // - get element from your dataset at this position
        // - replace the contents of the particular card in the list with that element
        holder.toptext.setText(mDataset.get(position).getName());
        holder.bottomtext.setText(mDataset.get(position).getFormattedDate());
        // holder.mCardView.setOnClickListener();

//        holder.btnAdd.setOnClickListener(new View.OnClickListener(){
//            @Override
//            public void onClick(View view)  {
//                DBHelper.BeverageRow br = (DBHelper.BeverageRow) mDataset.get(position).getBeverage();
//                DBHelper.DrinkRow dr = new DBHelper.DrinkRow(br);
//
//                mDataset.add(dr);
                //Add to the database, then requery to get the right drinkId in the adapter
//					allDrinks = db.getAllDrinks();
//					TopDrinkAdapter=db.new DrinkAdapter(this, R.layout.listdrink, (ArrayList<DrinkRow>) allDrinks);
//					setListAdapter(TopDrinkAdapter);
//            }
//        });
//        holder.btnEdit.setOnClickListener(new View.OnClickListener(){

//            @Override
//            public void onClick(View view) {
//                DBHelper.BeverageRow br = (DBHelper.BeverageRow) mDataset.get(position).getBeverage();

//					Intent intent = new Intent(this, SelectBeverageRecycler.class);
//					intent.putExtra("EDITING_MODE", true); // EDITING
//					intent.putExtra("BID", br.get_id());
//					startActivityForResult(intent, EDIT_BEVERAGE);
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mDataset.size();
    }
}