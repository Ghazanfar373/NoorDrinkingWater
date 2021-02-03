package helper;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;

public class DisabledScrollingLayoutManager extends LinearLayoutManager {

    public DisabledScrollingLayoutManager(Context context, int orientation, boolean reverseLayout) {
        super(context, orientation, reverseLayout);
    }

    @Override
    public boolean canScrollHorizontally() {
        return false;
    }

}
