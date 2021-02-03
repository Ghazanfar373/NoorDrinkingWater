package adapters;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.frandydevs.apps.noordrinkingwater.R;

import model.WalletListData;

public class WalletListAdapter extends RecyclerView.Adapter<WalletListAdapter.ViewHolder>{
    private WalletListData[] listdata;

    // RecyclerView recyclerView;
    public WalletListAdapter(WalletListData[] listdata) {
        this.listdata = listdata;
    }
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
     LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
       View listItem= layoutInflater.inflate(R.layout.wallet_listview_layout,parent, false);
        ViewHolder viewHolder = new ViewHolder(listItem);
       return viewHolder;
     //   return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final WalletListData myListData = listdata[position];
        holder.textView.setText(listdata[position].getDescription());
        holder.imageView.setImageResource(listdata[position].getImgId());
        holder.tvTime.setText(listdata[position].getDay()+" "+listdata[position].getTime());
        holder.tvCash.setText("$"+listdata[position].getAmountCash());
        holder.relativeLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(),"click on item: "+myListData.getDescription(),Toast.LENGTH_LONG).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return listdata.length;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ImageView imageView;
        public TextView textView;
        public RelativeLayout relativeLayout;
        public TextView tvTime;
        public TextView tvCash;

        public ViewHolder(View itemView) {
            super(itemView);
            this.imageView = (ImageView) itemView.findViewById(R.id.imageView);
            this.textView = (TextView) itemView.findViewById(R.id.tvOrderDet);
            this.tvTime=    (TextView) itemView.findViewById(R.id.tvOrderTime);
            this.tvCash=    (TextView) itemView.findViewById(R.id.tvcashAmount);
            relativeLayout =(RelativeLayout)itemView.findViewById(R.id.relativeLayout);
        }
    }
}