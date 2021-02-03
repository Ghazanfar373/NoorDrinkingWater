package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.frandydevs.apps.noordrinkingwater.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import helper.DisabledScrollingLayoutManager;
import model.ProductModel;

/**
 * Created by Oracle on 4/9/2018.
 */

public class ProductsAdapterOld extends RecyclerView.Adapter<ProductsAdapterOld.MyViewHolder> {

    private ArrayList<ProductModel> productModelArrayList;
    private Context context;
    private int clickedPosition;
    private int activeColor, processedColor, cancelColor;
    private ProductModel productModel, clickedProductModel = null;
    private DisabledScrollingLayoutManager linearLayoutManager;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvTime, tvStatus, tvDate;
        private ImageView ivProduct, ivOffer;
        private View row, vSelector;

        MyViewHolder(View view) {
            super(view);
            row = view;
            ivProduct = row.findViewById(R.id.ivProduct);
            tvTitle = row.findViewById(R.id.tvTitle);
        }
    }

    public ProductsAdapterOld(Context context, ArrayList<ProductModel> productModelArrayList, DisabledScrollingLayoutManager linearLayoutManager) {
        this.context = context;
        activeColor = context.getResources().getColor(R.color.colorBGActiveOrder);
        processedColor = context.getResources().getColor(R.color.colorBGProcessedOrder);
        cancelColor = context.getResources().getColor(R.color.colorBGCancelOrder);
        this.productModelArrayList = productModelArrayList;
        this.linearLayoutManager = linearLayoutManager;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_products, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        productModel = productModelArrayList.get(position);
        holder.tvTitle.setText(productModel.getName());
        String imageUrl = productModel.getImage();
        if (imageUrl.equalsIgnoreCase("null") || imageUrl.equalsIgnoreCase("")) {
            Glide.with(context).load(R.drawable.ic_products).into(holder.ivProduct);
//            Picasso.get().load(R.drawable.ic_products).into(holder.ivProduct);
        } else {
           /* RequestOptions placeholderRequest = new RequestOptions();
            placeholderRequest.placeholder(R.drawable.ic_products);
            placeholderRequest.error(R.drawable.ic_products);
            Glide.with(context).setDefaultRequestOptions(placeholderRequest).load(imageUrl).into(holder.ivProduct);*/
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_products).error(R.drawable.ic_products).into(holder.ivProduct);
        }
        holder.row.setTag(position);
/*
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = (int) v.getTag();
                linearLayoutManager.scrollToPosition(clickedPosition);
            }
        });
*/
    }

    @Override
    public int getItemCount() {
        return productModelArrayList.size();
    }
}

