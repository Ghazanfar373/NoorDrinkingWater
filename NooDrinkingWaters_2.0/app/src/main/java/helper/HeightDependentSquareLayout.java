package helper;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

/**
 * Created by Lincoln on 05/04/16.
 */
public class HeightDependentSquareLayout extends RelativeLayout {

    public HeightDependentSquareLayout(Context context) {
        super(context);
    }

    public HeightDependentSquareLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public HeightDependentSquareLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeightDependentSquareLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // Set a square layout.
        super.onMeasure(heightMeasureSpec, heightMeasureSpec);
    }
}