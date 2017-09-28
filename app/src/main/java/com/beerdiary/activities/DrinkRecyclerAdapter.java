package com.beerdiary.activities;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
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

import static android.support.v4.app.ActivityCompat.startActivityForResult;
import static android.support.v4.content.ContextCompat.startActivity;
import static com.beerdiary.BeerDiary.EDIT_BEVERAGE;
import static com.beerdiary.BeerDiary.PICK_BEVERAGE;

/**
 * Created by Jim on 9/4/2017.
 */
// TODO 1 make the on Click Listener for the card display a toast message with the position of the line
// TODO 2 make the on Click Listener for the card display more info about that drink (a popup with beverage details?)

// TODO make the Long Click Listener start an intent for the beverage recycler with the position of the selected line in edit mode
// TODO create an edit view, similar to listbeverage.xml, but updated with newer android support
// TODO update editBeverage class to work with new edit view
// TODO update edit view with a picker control for hop flavor, color, aroma, mouthfeel, and taste.  You should be able to multi select from a picker control into a multivalue field for each of these
// TODO taste is more complex - shoudl be able to specify front mid or end of taste

// TODO update the SelectBeverage view to support searches of multiple columns (brewer, beer name, tags)  might need a separate view for complex queries?
// TODO create a view that has some access to canned reports on data (how many drinks avg, scoring numbers, analysis on favorite styles, etc)
// TODO update the drawables, some icons aren't displaying correctly, might be a new way to store/categorize them that we aren't doing

// TODO remove classes and layouts that aren't needed anymore after updates are complete

// TODO update the themes.xml and style.xml to make this app actually look decent
// TODO stylize the controls on views to make them look better
// TODO review font choices throughout the app
// TODO review if out of box icons will work to replace the beer icons all over the place


public class DrinkRecyclerAdapter extends RecyclerView.Adapter<DrinkRecyclerAdapter.ViewHolder> {
    private  static String TAG = "DrinkRecyclerAdapter";
    private ArrayList<DBHelper.DrinkRow> mDataset;
    static Context context;

    public DrinkRecyclerAdapter(Context con, ArrayList<DBHelper.DrinkRow> myDataset) {
        mDataset=myDataset;
        context = con;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public CardView mCardView;
        public TextView toptext, bottomtext;
        public ImageButton btnEdit, btnAdd;

        public ViewHolder(CardView itemview) {
            super(itemview);
            mCardView= itemview.findViewById(R.id.select_drink_recycler_view_recyclerview);
            toptext= itemview.findViewById(R.id.drink_recycler_cardview_toptext);
            bottomtext= itemview.findViewById(R.id.drink_recycler_cardview_bottomtext);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new card view
        CardView v = (CardView) LayoutInflater.from(parent.getContext()).inflate(R.layout.drink_recycler_cardview, parent, false);
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
    }

    @Override
    public int getItemCount() { return mDataset.size(); }

    @Override
    public int getItemViewType(int position) {	return R.layout.drink_recycler_cardview;  }

}
