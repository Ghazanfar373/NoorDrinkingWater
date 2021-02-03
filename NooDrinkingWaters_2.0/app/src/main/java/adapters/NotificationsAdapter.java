package adapters;

import android.content.Context;
import android.os.Build;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.frandydevs.apps.noordrinkingwater.ParentActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.NotificationModel;
import model.OrderModel;


/**
 * Created by Oracle on 4/9/2018.
 */

public class NotificationsAdapter extends RecyclerView.Adapter<NotificationsAdapter.MyViewHolder> {

    private ArrayList<NotificationModel> notificationModelArrayList;
    private Context context;
    private int clickedPosition;
    private int activeColor, processedColor;
    private NotificationModel notificationModel, clickedNotificationModel = null;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvMessage, tvDate;
        private ImageView ivProfile;
        private View row, vSelector;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvMessage = row.findViewById(R.id.tvMessage);
            tvDate = row.findViewById(R.id.tvDate);
            ivProfile = row.findViewById(R.id.ivProfile);
        }
    }

    public NotificationsAdapter(Context context, ArrayList<NotificationModel> notificationModelArrayList) {
        this.context = context;
        activeColor = context.getResources().getColor(R.color.colorBGActiveOrder);
        processedColor = context.getResources().getColor(R.color.colorBGProcessedOrder);
        this.notificationModelArrayList = notificationModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_notification, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        notificationModel = notificationModelArrayList.get(position);
        holder.tvMessage.setText(notificationModel.getNotification());
        /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            holder.tvMessage.setText(Html.fromHtml(notificationModel.getNotification(), Html.FROM_HTML_MODE_COMPACT));
        } else {
            holder.tvMessage.setText(Html.fromHtml(notificationModel.getNotification()));
        }*/
        holder.tvDate.setText(notificationModel.getNotificationDate());
        Picasso.get().load(R.drawable.ic_products).into(holder.ivProfile);
    }

    @Override
    public int getItemCount() {
        return notificationModelArrayList.size();
    }
}