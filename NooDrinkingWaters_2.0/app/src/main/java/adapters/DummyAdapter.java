package adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frandydevs.apps.noordrinkingwater.R;

public class DummyAdapter extends RecyclerView.Adapter<DummyAdapter.MyViewHolder> {
    Context context;

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new MyViewHolder(((Activity) context).getLayoutInflater().inflate(R.layout.lv_products, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {

        MyViewHolder(View view) {
            super(view);

        }
    }
}
