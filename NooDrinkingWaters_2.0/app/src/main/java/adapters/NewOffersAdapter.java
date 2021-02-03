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

import model.NewOfferModel;
import model.OrderModel;

/**
 * Created by Oracle on 4/9/2018.
 */

public class NewOffersAdapter extends RecyclerView.Adapter<NewOffersAdapter.MyViewHolder> {

    private ArrayList<NewOfferModel> newOfferModelArrayList;
    private Context context;
    private int clickedPosition;
    private int activeColor, processedColor, cancelColor;
    private NewOfferModel newOfferModel, clickedNewOfferModel = null;
    private GradientDrawable drawable;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNo, tvTime, tvStatus, tvDate;
        private ImageView ivProduct, ivOffer;
        private View row, vSelector;

        MyViewHolder(View view) {
            super(view);
            row = view;
            ivProduct = row.findViewById(R.id.ivProduct);
            ivOffer = row.findViewById(R.id.ivOffer);
        }
    }

    public NewOffersAdapter(Context context, ArrayList<NewOfferModel> newOfferModelArrayList) {
        this.context = context;
        activeColor = context.getResources().getColor(R.color.colorBGActiveOrder);
        processedColor = context.getResources().getColor(R.color.colorBGProcessedOrder);
        cancelColor = context.getResources().getColor(R.color.colorBGCancelOrder);
        this.newOfferModelArrayList = newOfferModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_new_offers, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        newOfferModel = newOfferModelArrayList.get(position);
        Picasso.get().load(R.drawable.bg_product_image).into(holder.ivProduct);
        Picasso.get().load(R.drawable.bg_sp_offers).into(holder.ivOffer);
    }

    @Override
    public int getItemCount() {
        return newOfferModelArrayList.size();
    }
}

