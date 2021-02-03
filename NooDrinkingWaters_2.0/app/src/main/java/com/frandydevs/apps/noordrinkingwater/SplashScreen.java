package com.frandydevs.apps.noordrinkingwater;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.crashlytics.android.Crashlytics;
import com.squareup.picasso.Picasso;

import app.AppConfig;
import app.AppController;
import io.fabric.sdk.android.Fabric;

import static app.AppConfig.PREF_IS_FIRST_RUN;
import static app.AppConfig.PREF_IS_LOGIN;

public class SplashScreen extends ParentActivity {

    ImageView ivBottle;
    ImageView ivBG;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Fabric.with(this, new Crashlytics());
        setContentView(R.layout.activity_splash_screen);

        WindowManager wm = (WindowManager) getSystemService(Context.WINDOW_SERVICE);
        Display display = wm.getDefaultDisplay();
        final Point size = new Point();
        display.getSize(size);

        AppConfig.DEVICE_HEIGHT = size.y;
        AppConfig.DEVICE_WIDTH = size.x;

        sqLiteDatabaseHelper.clearCart();

        ivBottle = findViewById(R.id.ivBottle);
        ivBG = findViewById(R.id.ivBG);

        llParams = (LinearLayout.LayoutParams) ivBottle.getLayoutParams();
        llParams.height = AppConfig.DEVICE_HEIGHT / 4;
        ivBottle.setLayoutParams(llParams);

        Picasso.get().load(R.drawable.bg_product_image).into(ivBottle);
        Picasso.get().load(R.drawable.bg_blue_layers).into(ivBG);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                if (dataSaving.getPrefValue(context, PREF_IS_LOGIN).equalsIgnoreCase("true")) {
                    //goto main activity
                    Intent mainActivityIntent = new Intent(context, MainActivity.class);
                    startActivity(mainActivityIntent);
                    finish();
                } else if (!dataSaving.getPrefValue(context, PREF_IS_FIRST_RUN).equalsIgnoreCase("false")) {
                    //goto welcomeslides activity
                    Intent welcomeIntent = new Intent(context, WelcomeSlidesActivity.class);
                    startActivity(welcomeIntent);
                    finish();
                } else {
                    //goto login activity
                    Intent loginIntent = new Intent(context, LoginActivity.class);
                    startActivity(loginIntent);
                    finish();
                }
            }
        }, 3000);
    }
}
