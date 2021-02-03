package adapters;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.LoginActivity;
import com.frandydevs.apps.noordrinkingwater.MainActivity;
import com.frandydevs.apps.noordrinkingwater.R;

import java.util.ArrayList;
import java.util.List;

import app.AppConfig;
import fragments.MyOrdersFragment;
import model.DaysSpinnerAdapterModel;
import model.OrderModel;
import model.OrderProductModel;

/**
 * Created by Oracle on 4/9/2018.
 */

public class MyOrdersAdapter extends RecyclerView.Adapter<MyOrdersAdapter.MyViewHolder> {

    private ArrayList<OrderModel> orderModelArrayList;
    private Context context;
    public int clickedPosition;
    private OrderModel orderModel, clickedOrderModel = null;
    private GradientDrawable drawable;
    OrderProductsDetailAdapter orderProductsDetailAdapter;
    MyOrdersFragment myOrdersFragment;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvOrderNo, tvTime, tvStatus, tvDate;
        private ImageView ivLogo;
        private Button btnCancelOrder;
        private View row, vSelector;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvOrderNo = row.findViewById(R.id.tvOrderNo);
            tvTime = row.findViewById(R.id.tvTime);
            tvStatus = row.findViewById(R.id.tvStatus);
            tvDate = row.findViewById(R.id.tvDate);
            btnCancelOrder = row.findViewById(R.id.btnCancelOrder);
        }
    }

    public MyOrdersAdapter(Context context, ArrayList<OrderModel> orderModelArrayList, MyOrdersFragment myOrdersFragment) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
        this.myOrdersFragment = myOrdersFragment;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_my_orders, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        orderModel = orderModelArrayList.get(position);
        holder.tvOrderNo.setText(orderModel.getOrderNum());
        holder.tvDate.setText(orderModel.getOrderDate());
        holder.tvTime.setText(orderModel.getOrderTime());
        holder.tvStatus.setText(orderModel.getStatus());
        holder.btnCancelOrder.setTag(position);
        holder.btnCancelOrder.setVisibility(View.GONE);
        drawable = (GradientDrawable) holder.tvStatus.getBackground();
        drawable.mutate();
        drawable.setColor(orderModel.getStatusColor());

        if (orderModel.getStatus().equalsIgnoreCase("In Process")
                || orderModel.getStatus().equalsIgnoreCase("Re-Scheduled")) {
            holder.btnCancelOrder.setVisibility(View.VISIBLE);
            holder.btnCancelOrder.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    clickedPosition = (int) v.getTag();
                    showMessageDialog("Cancel Confirm?", "Are you sure that you want to cancel this order?",
                            "Yes", "No", "", "");
                }
            });
        }
        holder.row.setTag(position);
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = (int) v.getTag();
                showOrderDetailDialog(orderModelArrayList.get(clickedPosition));
            }
        });
    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    private void showOrderDetailDialog(OrderModel orderModel) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog_FullScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_detail);
        dialog.setCancelable(false);
        final Button btnDone = dialog.findViewById(R.id.btnDone);
        TextView tvOrderNo = dialog.findViewById(R.id.tvOrderNo);
        TextView tvOrderDate = dialog.findViewById(R.id.tvOrderDate);
        TextView tvItemDescription = dialog.findViewById(R.id.tvItemDescription);
        TextView tvAddress = dialog.findViewById(R.id.tvAddress);
        TextView tvStatus = dialog.findViewById(R.id.tvStatus);
        TextView tvReason = dialog.findViewById(R.id.tvReason);
        TextView tvTotalAmount = dialog.findViewById(R.id.tvTotalAmount);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        tvOrderNo.requestFocus();
        tvOrderNo.setText(orderModel.getOrderNum());
        tvOrderDate.setText(orderModel.getOrderDate());
        tvAddress.setText(orderModel.getAddress());
        tvStatus.setText(orderModel.getStatus());
        String reason = orderModel.getReason();
        if (reason != null && !reason.isEmpty()) {
            dialog.findViewById(R.id.llReason).setVisibility(View.VISIBLE);
            tvReason.setText(reason);
        }

        String items = "";
        List<OrderProductModel> orderProductModelArrayList = orderModel.getProducts();
        OrderProductModel orderProductModel;
        double totalAmount = 0;
        for (int i = 0; i < orderProductModelArrayList.size(); i++) {
            orderProductModel = orderProductModelArrayList.get(i);
            totalAmount = totalAmount + Double.parseDouble(orderProductModel.getAmount());
            items = items + orderProductModel.getName() + " (" + orderProductModel.getQty() + ")\n";
        }
        if (!items.isEmpty()) {
            items = items.substring(0, items.length() - 1);
        }
        tvItemDescription.setText(items);

        RecyclerView rvProducts = dialog.findViewById(R.id.rvProducts);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                linearLayoutManager.getOrientation());
        rvProducts.addItemDecoration(dividerItemDecoration);
        rvProducts.setLayoutManager(linearLayoutManager);
        orderProductsDetailAdapter = new OrderProductsDetailAdapter(context, orderProductModelArrayList);
        rvProducts.setAdapter(orderProductsDetailAdapter);
        tvTotalAmount.setText(context.getString(R.string.total_amount_semi) + " " + String.format("%.1f", totalAmount));
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    /*
    private void showOrderProductsDialog(List<OrderProductModel> orderProductModelArrayList) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog_FullScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_detail);
        dialog.setCancelable(false);
        RecyclerView rvProducts = dialog.findViewById(R.id.rvProducts);
        final Button btnDone = dialog.findViewById(R.id.btnDone);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                linearLayoutManager.getOrientation());
        rvProducts.addItemDecoration(dividerItemDecoration);
        rvProducts.setLayoutManager(linearLayoutManager);
        orderProductsDetailAdapter = new OrderProductsDetailAdapter(context, orderProductModelArrayList);
        rvProducts.setAdapter(orderProductsDetailAdapter);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }
*/

    public void showMessageDialog(String title, String message, String positiveBtnText, String negativeBtnText,
                                  final String positiveBtnAction, final String negativeBtnAction) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCancelable(false);
        final TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        final TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnNo = dialog.findViewById(R.id.btnNo);
        final ImageView ivClose = dialog.findViewById(R.id.ivClose);
        if (title.equalsIgnoreCase("")) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        if (message.equalsIgnoreCase("")) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setText(message);
        }
        if (positiveBtnText.equalsIgnoreCase("")) {
            btnYes.setVisibility(View.GONE);
        } else {
            btnYes.setText(positiveBtnText);
        }
        if (negativeBtnText.equalsIgnoreCase("")) {
            btnNo.setVisibility(View.GONE);
        } else {
            btnNo.setText(negativeBtnText);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                myOrdersFragment.cancelOrderServerRequest(orderModelArrayList.get(clickedPosition));
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

}

