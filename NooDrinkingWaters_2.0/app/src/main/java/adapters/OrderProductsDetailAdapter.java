package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.R;

import java.util.List;

import model.OrderProductModel;

/**
 * Created by Oracle on 4/9/2018.
 */

public class OrderProductsDetailAdapter extends RecyclerView.Adapter<OrderProductsDetailAdapter.MyViewHolder> {

    private List<OrderProductModel> orderProductModelArrayList;
    private Context context;
    private int clickedPosition;
    private int activeColor, processedColor, cancelColor;
    private OrderProductModel orderProductModel, clickedOrderProductModel = null;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle, tvQty, tvPrice, tvAmount;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvTitle = row.findViewById(R.id.tvTitle);
            tvQty = row.findViewById(R.id.tvQty);
            tvPrice = row.findViewById(R.id.tvPrice);
            tvAmount = row.findViewById(R.id.tvAmount);
        }
    }

    public OrderProductsDetailAdapter(Context context, List<OrderProductModel> orderProductModelArrayList) {
        this.context = context;
        this.orderProductModelArrayList = orderProductModelArrayList;
        activeColor = context.getResources().getColor(R.color.colorBGActiveOrder);
        processedColor = context.getResources().getColor(R.color.colorBGProcessedOrder);
        cancelColor = context.getResources().getColor(R.color.colorBGCancelOrder);
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        switch (viewType) {
            case 0:
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_order_product_detail_header, parent, false));
            default:
                return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_order_product_detail_item, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        if (position > 0) {//0 for header
            orderProductModel = orderProductModelArrayList.get(position - 1);//decrease by one due to header
            holder.tvTitle.setText(orderProductModel.getName());
            holder.tvQty.setText(orderProductModel.getQty());
            holder.tvPrice.setText(orderProductModel.getPrice());
            holder.tvAmount.setText(orderProductModel.getAmount());
        }
    }

    @Override
    public int getItemCount() {
        if (orderProductModelArrayList.isEmpty())
            return 0;
        return orderProductModelArrayList.size() + 1;//1 for header
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
}

