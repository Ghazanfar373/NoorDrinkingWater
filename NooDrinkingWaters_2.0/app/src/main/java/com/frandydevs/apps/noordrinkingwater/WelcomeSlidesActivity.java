package com.frandydevs.apps.noordrinkingwater;

import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.Timer;
import java.util.TimerTask;

import app.AppConfig;
import fragments.WelcomeSlideFragment;

import static app.AppConfig.PREF_IS_FIRST_RUN;

public class WelcomeSlidesActivity extends ParentActivity implements WelcomeSlideFragment.OnFragmentInteractionListener {

    ViewPager vpSlides;
    LinearLayout llSlides, llDots;
    ImageView ivBG, ivDotOne, ivDotTwo, ivDotThree;
    int dim20, dim40;
    TextView tvHeader;
    Button btnGetStarted;
    Resources resource;
    Timer timer = new Timer();
    Handler handler = new Handler();
    Runnable update = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome_slides);

        resource = context.getResources();
        dim20 = (int) resource.getDimension(R.dimen.dp20);
        dim40 = (int) resource.getDimension(R.dimen.dp40);

        ivBG = findViewById(R.id.ivBG);
        btnGetStarted = findViewById(R.id.btnGetStarted);
        llSlides = findViewById(R.id.llSlides);
        vpSlides = findViewById(R.id.vpSlides);
        tvHeader = findViewById(R.id.tvHeader);
        llDots = findViewById(R.id.llDots);
        ivDotOne = findViewById(R.id.ivDotOne);
        ivDotTwo = findViewById(R.id.ivDotTwo);
        ivDotThree = findViewById(R.id.ivDotThree);

        vpSlides.setAdapter(new SamplePagerAdapter(
                getSupportFragmentManager()));
        llSlides.setPadding(0, AppConfig.DEVICE_HEIGHT / 4, 0, AppConfig.DEVICE_HEIGHT / 10);

        Picasso.get().load(R.drawable.bg_red_layers).into(ivBG);
        Picasso.get().load(R.drawable.bg_get_started).into(ivDotOne);
        Picasso.get().load(R.drawable.bg_get_started).into(ivDotTwo);
        Picasso.get().load(R.drawable.bg_get_started).into(ivDotThree);

        btnGetStarted.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timer.cancel();
                dataSaving.setPrefValue(context, PREF_IS_FIRST_RUN, "false");
                Intent registerIntent = new Intent(context, LoginActivity.class);
                startActivity(registerIntent);
                finish();
            }
        });
        vpSlides.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        tvHeader.setText(R.string.select_from_multiple_products);

                        llParams = (LinearLayout.LayoutParams) ivDotOne.getLayoutParams();
                        llParams.width = dim40;
                        ivDotOne.setLayoutParams(llParams);
                        llParams = (LinearLayout.LayoutParams) ivDotTwo.getLayoutParams();
                        llParams.width = dim20;
                        ivDotTwo.setLayoutParams(llParams);
                        llParams = (LinearLayout.LayoutParams) ivDotThree.getLayoutParams();
                        llParams.width = dim20;
                        ivDotThree.setLayoutParams(llParams);
                        break;
                    case 1:
                        tvHeader.setText(R.string.order_online);

                        llParams = (LinearLayout.LayoutParams) ivDotOne.getLayoutParams();
                        llParams.width = dim20;
                        ivDotOne.setLayoutParams(llParams);
                        llParams = (LinearLayout.LayoutParams) ivDotTwo.getLayoutParams();
                        llParams.width = dim40;
                        ivDotTwo.setLayoutParams(llParams);
                        llParams = (LinearLayout.LayoutParams) ivDotThree.getLayoutParams();
                        llParams.width = dim20;
                        ivDotThree.setLayoutParams(llParams);
                        break;
                    case 2:
                        tvHeader.setText(R.string.get_your_home_delivery);

                        llParams = (LinearLayout.LayoutParams) ivDotOne.getLayoutParams();
                        llParams.width = dim20;
                        ivDotOne.setLayoutParams(llParams);
                        llParams = (LinearLayout.LayoutParams) ivDotTwo.getLayoutParams();
                        llParams.width = dim20;
                        ivDotTwo.setLayoutParams(llParams);
                        llParams = (LinearLayout.LayoutParams) ivDotThree.getLayoutParams();
                        llParams.width = dim40;
                        ivDotThree.setLayoutParams(llParams);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
        vpSlides.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                timer.cancel();
                return false;
            }
        });

        update = new Runnable() {
            public void run() {
                vpSlides.setCurrentItem(vpSlides.getCurrentItem() + 1, true);
            }
        };

        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (vpSlides.getCurrentItem() < 2) {
                    handler.post(update);
                } else {
                    timer.cancel();
                }
            }
        }, 3000, 3000);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    public class SamplePagerAdapter extends FragmentPagerAdapter {

        public SamplePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            switch (position) {
                case 0:
                    return WelcomeSlideFragment.newInstance("1", "");
                case 1:
                    return WelcomeSlideFragment.newInstance("2", "");
                case 2:
                    return WelcomeSlideFragment.newInstance("3", "");
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        timer.cancel();
    }
}
