package com.pac.ideatech.noordrinkingwater;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class InvoiceListAdapter extends ArrayAdapter<Order> {
    private Context context;
    private List<Order> allorders;
    private static LayoutInflater inflater=null;
    public InvoiceListAdapter(@NonNull Context context, List<Order> morder) {
        super(context, R.layout.layout_listitems_invoice);
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.context = context;
        this.allorders =morder;
    }
    @Override
    public int getCount() {
        return allorders .size();
    }

    @Override
    public Order getItem(int position) {
        return allorders .get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public int getPosition(Order item) {
        return allorders .indexOf(item);
    }

    @Override
    public int getViewTypeCount() {
        return 1; //Number of types + 1 !!!!!!!!
    }

    @Override
    public int getItemViewType(int position) {
        return 1;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
       // return super.getView(position, convertView, parent);

        View vi=convertView;
        if(convertView==null)
            vi = inflater.inflate(R.layout.layout_listitems_invoice, null);

        TextView item = (TextView)vi.findViewById(R.id.lstitem); // title
        TextView qty = (TextView)vi.findViewById(R.id.lstqty); // artist name
        TextView amount = (TextView)vi.findViewById(R.id.lstamnt); // duration

        item.setText(allorders.get(position).getProdut_title());
        qty.setText(allorders.get(position).getQnty());
        amount.setText(allorders.get(position).getTotal_amount()+"");



        return vi;
    }
}
