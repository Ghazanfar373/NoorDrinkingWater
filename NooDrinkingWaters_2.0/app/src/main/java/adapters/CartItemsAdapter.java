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

public class CartItemsAdapter extends RecyclerView.Adapter<CartItemsAdapter.MyViewHolder> {

    private ArrayList<CartProductModel> cartProductModelArrayList;
    private Context context;
    private int clickedPosition;
    private int activeColor, processedColor, cancelColor;
    private CartProductModel cartProductModel, clickedCartProductModel = null;
    private GradientDrawable drawable;
    OrderProductsDetailAdapter orderProductsDetailAdapter;
    private SQLiteDatabaseHelper sqLiteDatabaseHelper;
    CartFragment cartFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvPrice, tvQty, tvTitle;
        private ImageView ivProduct, ivClose;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvTitle = row.findViewById(R.id.tvTitle);
            tvPrice = row.findViewById(R.id.tvPrice);
            tvQty = row.findViewById(R.id.tvQty);
            ivProduct = row.findViewById(R.id.ivProduct);
            ivClose = row.findViewById(R.id.ivClose);
        }
    }

    public CartItemsAdapter(Context context, ArrayList<CartProductModel> cartProductModelArrayList,
                            SQLiteDatabaseHelper sqLiteDatabaseHelper, CartFragment cartFragment) {
        this.context = context;
        activeColor = context.getResources().getColor(R.color.colorBGActiveOrder);
        processedColor = context.getResources().getColor(R.color.colorBGProcessedOrder);
        cancelColor = context.getResources().getColor(R.color.colorBGCancelOrder);
        this.cartProductModelArrayList = cartProductModelArrayList;
        this.sqLiteDatabaseHelper = sqLiteDatabaseHelper;
        this.cartFragment = cartFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_cart_item, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        cartProductModel = cartProductModelArrayList.get(position);
        holder.tvTitle.setText(cartProductModel.getName());
        holder.tvQty.setText(String.valueOf(cartProductModel.getQty()));
        holder.tvPrice.setText(String.valueOf(cartProductModel.getPrice()));
        String imageUrl = cartProductModel.getImage();
        if (imageUrl.equalsIgnoreCase("null") || imageUrl.equalsIgnoreCase("")) {
            Picasso.get().load(R.drawable.ic_bottle_blue).into(holder.ivProduct);
        } else {
            Picasso.get().load(imageUrl).placeholder(R.drawable.ic_bottle_blue).error(R.drawable.ic_bottle_blue).into(holder.ivProduct);
        }
        holder.ivClose.setTag(position);
        holder.ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = (int) v.getTag();
                clickedCartProductModel = cartProductModelArrayList.get(clickedPosition);
                if (sqLiteDatabaseHelper.removeItemFromCart(clickedCartProductModel.getId())) {
                    cartFragment.updateTotalAmountAfterRemovingItem(clickedCartProductModel.getTotalAmount());
                    cartProductModelArrayList.remove(clickedPosition);
                    notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return cartProductModelArrayList.size();
    }

}

