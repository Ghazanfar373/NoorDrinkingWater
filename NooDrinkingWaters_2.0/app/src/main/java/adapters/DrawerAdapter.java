package adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.MainActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

import model.DrawerModel;

/**
 * Created by Oracle on 4/9/2018.
 */

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {

    private ArrayList<DrawerModel> drawerModelArrayList;
    private Context context;
    private int clickedPosition;
    private int selectedColor, primaryColor;
    private DrawerModel drawerModel, clickedDrawerModel = null;

    class MyViewHolder extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private ImageView ivLogo;
        private View row, vSelector;

        MyViewHolder(View view) {
            super(view);
            row = view;
            tvTitle = view.findViewById(R.id.tvTitle);
            ivLogo = view.findViewById(R.id.ivLogo);
            vSelector = view.findViewById(R.id.vSelector);
        }
    }

    public DrawerAdapter(Context context, ArrayList<DrawerModel> drawerModelArrayList) {
        this.context = context;
        selectedColor = context.getResources().getColor(R.color.colorWhite);
        primaryColor = context.getResources().getColor(R.color.colorPrimary);
        this.drawerModelArrayList = drawerModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_drawer_menu, parent, false));
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, final int position) {
        drawerModel = drawerModelArrayList.get(position);
        holder.tvTitle.setText(drawerModel.getTitle());
        Picasso.get().load(drawerModel.getLogo()).into(holder.ivLogo);
        if (drawerModel.isSelected()) {
            holder.vSelector.setBackgroundColor(selectedColor);
            holder.tvTitle.setTypeface(holder.tvTitle.getTypeface(), Typeface.BOLD);
        } else {
            holder.vSelector.setBackgroundColor(primaryColor);
            holder.tvTitle.setTypeface(holder.tvTitle.getTypeface(), Typeface.NORMAL);
        }

        holder.row.setTag(position);
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickedPosition = (int) v.getTag();
                ((MainActivity) context).performDrawerMenuClickAction(clickedPosition);
                notifyDataSetChanged();
            }
        });
    }

    @Override
    public int getItemCount() {
        return drawerModelArrayList.size();
    }
}

