package adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.Filter;
import android.widget.Filterable;


import com.frandydevs.apps.noordrinkingwater.R;

import java.util.ArrayList;
import java.util.List;

import model.DaysSpinnerAdapterModel;

/**
 * Created by Oracle on 4/9/2018.
 */

public class MultiSelectionAdapter extends RecyclerView.Adapter<MultiSelectionAdapter.MyViewHolder> implements Filterable {

    private ArrayList<DaysSpinnerAdapterModel> daysSpinnerAdapterModelArrayList, filteredDaysSpinnerAdapterModelArrayList;
    private Context context;
    public int clickedPosition;
    public DaysSpinnerAdapterModel clickedItemModel = null, itemModel = null;
    private int totalItemsCount = 0;
    private ItemFilter mFilter = new ItemFilter();

    class MyViewHolder extends RecyclerView.ViewHolder {
        private CheckBox cbDay;
        private View row;

        MyViewHolder(View view) {
            super(view);
            row = view;
            cbDay = row.findViewById(R.id.cbDay);
        }
    }

    public MultiSelectionAdapter(Context context, ArrayList<DaysSpinnerAdapterModel> daysSpinnerAdapterModelArrayList) {
        this.context = context;
        this.daysSpinnerAdapterModelArrayList = daysSpinnerAdapterModelArrayList;
        this.filteredDaysSpinnerAdapterModelArrayList = daysSpinnerAdapterModelArrayList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new MyViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.lv_drop_down_days_selection, parent, false));
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        //item
        itemModel = filteredDaysSpinnerAdapterModelArrayList.get(position);
        holder.cbDay.setText(itemModel.getCheckAbleItemTitle());
        holder.cbDay.setChecked(itemModel.isChecked());

        holder.row.setTag(position);
        holder.row.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                clickedPosition = (int) view.getTag();
                clickedItemModel = filteredDaysSpinnerAdapterModelArrayList.get(clickedPosition);
                clickedItemModel.setChecked(!clickedItemModel.isChecked());
                notifyDataSetChanged();
            }
        });
    }

    public Filter getFilter() {
        return mFilter;
    }

    @Override
    public int getItemCount() {
        return filteredDaysSpinnerAdapterModelArrayList.size();
    }

    public int getTotalItemsCount() {
        return totalItemsCount;
    }

    private class ItemFilter extends Filter {
        @Override
        protected FilterResults performFiltering(CharSequence constraint) {

            String filterString = constraint.toString().toLowerCase();

            FilterResults results = new FilterResults();

            final List<DaysSpinnerAdapterModel> list = daysSpinnerAdapterModelArrayList;

            int count = list.size();
            final ArrayList<DaysSpinnerAdapterModel> nlist = new ArrayList<>(count);

            for (int i = 0; i < count; i++) {
                DaysSpinnerAdapterModel daysSpinnerAdapterModel = daysSpinnerAdapterModelArrayList.get(i);
                if (daysSpinnerAdapterModel.getCheckAbleItemTitle().toLowerCase().contains(filterString)) {
                    nlist.add(daysSpinnerAdapterModel);
                }
            }

            results.values = nlist;
            results.count = nlist.size();

            return results;
        }

        @SuppressWarnings("unchecked")
        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            filteredDaysSpinnerAdapterModelArrayList = (ArrayList<DaysSpinnerAdapterModel>) results.values;
            notifyDataSetChanged();
        }
    }
}

