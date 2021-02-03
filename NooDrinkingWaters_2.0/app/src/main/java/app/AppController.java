package app;

import android.app.Application;
import android.content.Context;
import android.graphics.Point;
import android.view.Display;
import android.view.WindowManager;

import helper.PicassoImageLoadingService;
import ss.com.bannerslider.Slider;

public class AppController extends Application {
    public static final String TAG = AppController.class.getSimpleName();
    private static AppController mInstance;
    private static DataSaving dataSaving = new DataSaving();

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;

        Slider.init(new PicassoImageLoadingService(this));
        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        AppConfig.DEVICE_HEIGHT = size.y;
        AppConfig.DEVICE_WIDTH = size.x;
    }

    public static synchronized AppController getInstance() {
        return mInstance;
    }

    public static DataSaving getDataSaving() {
        return dataSaving;
    }
}
