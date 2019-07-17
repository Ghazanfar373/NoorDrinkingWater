package com.pac.ideatech.noordrinkingwater;


import android.app.Fragment;
import android.content.Context;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyOrderRecyclerViewAdapter extends RecyclerView.Adapter<MyOrderRecyclerViewAdapter.ViewHolder> {

    private final List<Order> mValues;
    public static View view;
    Context context;
    Fragment fragment;

    //public static ImageButton btn_clear;
   //private final OnListFragmentInteractionListener mListener;

    public MyOrderRecyclerViewAdapter(List<Order> items, Context ctx, Fragment frag) {
        mValues = items;
        context=ctx;
        fragment=frag;


    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
         view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        holder.mItem = mValues.get(position);
        holder.index.setText(Integer.toString(position + 1) + ".");
        holder.mtitleView.setText(holder.mItem.getProdut_title());
        holder.unitprice_view.setText("Unit Price: " + holder.mItem.getUnit_price());
        holder.qty_view.setText("Qty: " + holder.mItem.getQnty());
        holder.total_price_view.setText(holder.mItem.getTotal_amount()+" AED");
        Picasso.with(context).load(holder.mItem.getImgURI()).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.tv_img_view);
        //Order.setTotalPrice(holder.mItem.getTotal_amount());
        //    holder.mContentView.setText(mValues.get(position).content);
        if(Order.isClearEnabled()) {
            holder.btn_clear.setImageResource(R.drawable.ic_tick_black_24dp);
            holder.btn_clear.setEnabled(false);
        }
        holder.btn_clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Order.adjustTotalPrice(mValues.get(position).getTotal_amount());
                mValues.remove(position);
                notifyItemRemoved(position);
                CartItemsFragment.tv_subtotal.setText("Total: "+Order.getTotalPrice()+" AED");
                CartItemsFragment.tv_total.setText("Subtotal: "+Order.getTotalPrice()+" AED");
                Order.adjustNum_orders(mValues.size());

            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView index,mtitleView,unitprice_view,qty_view,total_price_view;
        private ImageView tv_img_view;
        public  ImageButton btn_clear;
        CardView cardView;
        public Order mItem;


        public ViewHolder(View view) {
            super(view);
            mView = view;
            tv_img_view=view.findViewById(R.id.thumbnail);
            index= (TextView) view.findViewById(R.id.index);
            mtitleView = (TextView) view.findViewById(R.id.item_title);
            unitprice_view= (TextView) view.findViewById(R.id.unit_price);
            qty_view= (TextView) view.findViewById(R.id.qntity);
            total_price_view= (TextView) view.findViewById(R.id.tv_price_sum);
            btn_clear= (ImageButton) view.findViewById(R.id.btn_clear);
            cardView= (CardView) view.findViewById(R.id.card);
        }


    }
}
