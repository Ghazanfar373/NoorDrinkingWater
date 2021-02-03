package adapters;

import android.content.Context;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import db.SQLiteDatabaseHelper;
import fragments.CartFragment;
import model.CartProductModel;

/**
 * Created by Oracle on 4/9/2018.
 */

public class CartItemsSummaryAdapter extends RecyclerView.Adapter<CartItemsSummaryAdapter.MyViewHolder> {

    private ArrayList<CartProductModel> cartProductModelArrayList;
    private Context context;
    private int clickedPosition;
    private int activeColor, processedColor, cancelColor;
    private CartProductModel cartProductModel, clickedCartProductModel = null;
    private GradientDrawable drawable;
    OrderProductsDetailAdapter orderProductsDetailAdapter;
    private SQLiteDatabaseHelper sqLiteDatabaseHelper;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPrice, tvQty, tvTitle;
        private ImageView ivProduct, ivClose;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvTitle = row.findViewById(R.id.tvTitle);
            tvQty = row.findViewById(R.id.tvQty);
            ivProduct = row.findViewById(R.id.ivProduct);
        }
    }

    public CartItemsSummaryAdapter(Context context, ArrayList<CartProductModel> cartProductModelArrayList) {
        this.context = context;
        activeColor = context.getResources().getColor(R.color.colorBGActiveOrder);
        processedColor = context.getResources().getColor(R.color.colorBGProcessedOrder);
        cancelColor = context.getResources().getColor(R.color.colorBGCancelOrder);
        this.cartProductModelArrayList = cartProductModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_cart_item_summary, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        cartProductModel = cartProductModelArrayList.get(position);
        holder.tvTitle.setText(cartProductModel.getName());
        holder.tvQty.setText(String.valueOf(cartProductModel.getQty()));
        String imageUrl = cartProductModel.getImage();
        if (imageUrl.equalsIgnoreCase("null") || imageUrl.equalsIgnoreCase("")) {
            Picasso.get().load(R.drawable.ic_bottle_blue).into(holder.ivProduct);
        } else {
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_bottle_blue).error(R.drawable.ic_bottle_blue).into(holder.ivProduct);
        }
    }

    @Override
    public int getItemCount() {
        return cartProductModelArrayList.size();
    }

}

