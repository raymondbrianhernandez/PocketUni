package raymondhernandez.pocketuniv.Activities;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import raymondhernandez.pocketuniv.Model.Assignment;
import raymondhernandez.pocketuniv.Model.Sell;
import raymondhernandez.pocketuniv.R;

/**
 * Created by Chachi on 4/19/2016.
 */
public class SellAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{

    ArrayList<Sell> items;
    Context context;

    public SellAdapter(Context mContext, ArrayList<Sell> items) {
        this.items = items;
        this.context = mContext;
    }

    public static class SellViewHolder extends RecyclerView.ViewHolder{

        public TextView item;
        public TextView type;
        public TextView price;
        public SellViewHolder(View itemView) {
            super(itemView);
            item = (TextView)itemView.findViewById(R.id.rv_item_name);
            type = (TextView)itemView.findViewById(R.id.rv_category);
            price = (TextView)itemView.findViewById(R.id.price);
        }
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.sell_item, parent, false);
        return new SellViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        SellViewHolder sellViewHolder = (SellViewHolder) holder;
        configureChatListViewHolder(sellViewHolder,position);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public void configureChatListViewHolder(final SellViewHolder holder,int pos){
        final Sell sell = items.get(pos);
        holder.item.setText(sell.getItem());
        holder.type.setText(sell.getType());
        holder.price.setText("$" + Double.toString(sell.getPrice()));
    }
}
