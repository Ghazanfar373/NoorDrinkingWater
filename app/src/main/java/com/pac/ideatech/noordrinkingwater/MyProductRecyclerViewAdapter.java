package com.pac.ideatech.noordrinkingwater;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import java.util.List;


public class MyProductRecyclerViewAdapter extends RecyclerView.Adapter<MyProductRecyclerViewAdapter.ViewHolder> {

    private final List<Product> mValues;
   // private final OnListFragmentInteractionListener mListener;
    private Context context;
    private android.app.FragmentManager fragmentManager;

    public MyProductRecyclerViewAdapter(List<Product> items,  Context ctx, android.app.FragmentManager manager) {
        mValues = items;
       // mListener = listener;
        context=ctx;

        fragmentManager=manager;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.fragment_product, parent, false);


        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {
        Product product = mValues.get(position);
        holder.mTitle.setText(product.getTitle());
        holder.button_price.setText(product.getPrice() + " AED");
        Picasso.with(context).load(product.getImgURL()).networkPolicy(NetworkPolicy.NO_CACHE).into(holder.imageView);
        holder.button_ordernow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Bundle data = new Bundle();//create bundle instance
                data.putInt("key", position);
                OnlineOrderFragment fragment = new OnlineOrderFragment();
                fragment.setArguments(data);
                android.app.FragmentManager manager = fragmentManager;
                android.app.FragmentTransaction transaction = manager.beginTransaction();
                transaction.replace(R.id.frag_container, fragment);
                transaction.commit();
                Log.w("Product id$$$$", position + "==" + holder.getPosition());
            }
        });

    }


    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public final View mView;
        public final TextView mTitle;
        public final Button button_price,button_ordernow;
        ImageView imageView;
        public Product mItem;

        public ViewHolder(View view) {
            super(view);
            mView = view;
            mTitle = (TextView) view.findViewById(R.id.idtitle);
            button_price = (Button) view.findViewById(R.id.btn_price);
            imageView = (ImageView) view.findViewById(R.id.imageView);
            button_ordernow = (Button) view.findViewById(R.id.btn_ordernow);
        }
        @Override
        public String toString() {
            return super.toString();
        }
    }
}
