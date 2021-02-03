package com.frandydevs.apps.noordrinkingwater;

import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.MenuItem;

import fragments.CartFragment;
import fragments.DeliveryScheduleFragment;
import fragments.ModeOfPaymentFragment;
import fragments.OrderAddressFragment;
import fragments.OrderPreviewFragment;
import helper.CustomViewPager;

public class CartAndOrderProcessActivity extends ParentActivity implements CartFragment.OnFragmentInteractionListener,
        DeliveryScheduleFragment.OnFragmentInteractionListener, OrderAddressFragment.OnFragmentInteractionListener,
        ModeOfPaymentFragment.OnFragmentInteractionListener, OrderPreviewFragment.OnFragmentInteractionListener {

    CustomViewPager vpSlides;
    PlaceOrderFragmentsPagerAdapter placeOrderFragmentsPagerAdapter;
    String deliveryTime = "Any Time", deliveryDate = "", paymentMethod = "Cash On Delivery", deliveryAddress = "";
    double totalAmount;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart_order_process);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        setTitle(getString(R.string.shopping_cart));

        fragmentManager = getSupportFragmentManager();
        vpSlides = findViewById(R.id.vpSlides);

        placeOrderFragmentsPagerAdapter = new PlaceOrderFragmentsPagerAdapter(fragmentManager);
        vpSlides.setAdapter(placeOrderFragmentsPagerAdapter);

        vpSlides.setPagingEnabled(false);
        vpSlides.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {

            }

            @Override
            public void onPageSelected(int i) {
                switch (i) {
                    case 0:
                        setTitle(R.string.shopping_cart);
                        break;
                    case 1:
                        setTitle(R.string.delivery_schedule);
                        try {
                            DeliveryScheduleFragment deliveryScheduleFragment = (DeliveryScheduleFragment) getSupportFragmentManager().findFragmentByTag(
                                    "android:switcher:" + R.id.vpSlides + ":" + vpSlides.getCurrentItem());
                            deliveryScheduleFragment.refreshViews();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                    case 2:
                        setTitle(R.string.delivery_address);
                        break;
                    case 3:
                        setTitle(R.string.mode_of_payment);
                        break;
                    case 4:
                        setTitle(R.string.order_preview);
                        break;
                }
            }

            @Override
            public void onPageScrollStateChanged(int i) {

            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                handleBackPress();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void nextStep() {
        vpSlides.setCurrentItem(vpSlides.getCurrentItem() + 1);
    }

    public void previousStep() {
        vpSlides.setCurrentItem(vpSlides.getCurrentItem() - 1);
    }

    @Override
    public void onFragmentInteraction(Uri uri) {

    }

    @Override
    public void setDeliveryDateAndTime(String date, String time) {
        setDeliveryTime(time);
        setDeliveryDate(date);
        nextStep();
    }

    @Override
    public void onBackPressed() {
        handleBackPress();
    }

    public void handleBackPress() {
        if (vpSlides.getCurrentItem() == 0) {
            super.onBackPressed();
        } else {
            previousStep();
        }
    }

    public double getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(double totalAmount) {
        this.totalAmount = totalAmount;
    }

    public String getDeliveryTime() {
        return deliveryTime;
    }

    public void setDeliveryTime(String deliveryTime) {
        this.deliveryTime = deliveryTime;
    }

    public String getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(String deliveryDate) {
        this.deliveryDate = deliveryDate;
    }

    public String getPaymentMethod() {
        return paymentMethod;
    }

    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod = paymentMethod;
        //this line will force to call getitemposition() method in adapter which will call update payment method in order preview fragment
        placeOrderFragmentsPagerAdapter.notifyDataSetChanged();
        nextStep();
    }

    public String getDeliveryAddress() {
        return deliveryAddress;
    }

    public void setDeliveryAddress(String deliveryAddress) {
        this.deliveryAddress = deliveryAddress;
        nextStep();
    }

    public class PlaceOrderFragmentsPagerAdapter extends FragmentPagerAdapter {

        public PlaceOrderFragmentsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            switch (position) {
                case 0:
                    return CartFragment.newInstance("", "");
                case 1:
                    return DeliveryScheduleFragment.newInstance("", "");
                case 2:
                    return OrderAddressFragment.newInstance("", "");
                case 3:
                    return ModeOfPaymentFragment.newInstance("", "");
                case 4:
                    return OrderPreviewFragment.newInstance("", "");
            }
            return null;
        }

        @Override
        public int getCount() {
            return 5;
        }

        @Override
        public int getItemPosition(Object object) {
            Fragment f = (Fragment) object;
            //to refresh OrderPreviewFragment
            if (f instanceof OrderPreviewFragment) {
                ((OrderPreviewFragment) f).updateDeliveryMethod();
            }
            return super.getItemPosition(object);
        }
    }
}
