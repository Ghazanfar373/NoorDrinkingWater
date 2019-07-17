package com.pac.ideatech.noordrinkingwater;

import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;




import java.util.List;

public class MyHIstoryRecyclerViewAdapter extends RecyclerView.Adapter<MyHIstoryRecyclerViewAdapter.ViewHolder> {

    private final List<Order> mValues;
    private Context context;
    private android.app.FragmentManager fragmentManager;

    public MyHIstoryRecyclerViewAdapter(List<Order> items, Context ctx, android.app.FragmentManager manager) {
        mValues = items;
        context=ctx;
        fragmentManager=manager;

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order2, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        Order order = mValues.get(position);
        holder.index.setText(Integer.toString(position + 1) + ".");
        holder.mtitleView.setText(order.getProdut_title());
        holder.unitprice_view.setText("Unit Price: " + order.getUnit_price());
        holder.qty_view.setText("Quantity: " + order.getQnty());
        holder.total_price_view.setText(order.getTotal_amount()+" AED");
        holder.sale_date.setText(order.getSaleDate());
        holder.sale_date.setVisibility(View.VISIBLE);
        holder.btn_clear.setImageResource(R.drawable.ic_tick_black_24dp);
        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;

        public final TextView index;
        public final TextView mtitleView;
        public final TextView unitprice_view;
        public final TextView qty_view;
        public final TextView total_price_view;
        public TextView sale_date;
        //   public final TextView mContentView;
        public  ImageButton btn_clear;
        CardView cardView;
        public Order mItem;
        public ViewHolder(View view) {
            super(view);
            mView = view;
            index= (TextView) view.findViewById(R.id.index);
            mtitleView = (TextView) view.findViewById(R.id.item_title);
            unitprice_view= (TextView) view.findViewById(R.id.unit_price);
            qty_view= (TextView) view.findViewById(R.id.qntity);
            total_price_view= (TextView) view.findViewById(R.id.tv_price_sum);
            btn_clear= (ImageButton) view.findViewById(R.id.btn_clear);
            cardView= (CardView) view.findViewById(R.id.card);
            sale_date= (TextView) view.findViewById(R.id.sale_date);
        }

        @Override
        public String toString() {
            return super.toString() + " '" + "'";
        }
    }
}
