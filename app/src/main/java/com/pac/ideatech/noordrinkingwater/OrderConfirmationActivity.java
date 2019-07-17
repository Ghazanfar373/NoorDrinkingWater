package com.pac.ideatech.noordrinkingwater;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;

public class OrderConfirmationActivity extends AppCompatActivity {
    ShippingAddrFragment fragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_confirmation);

        fragment=new ShippingAddrFragment();
        final android.app.FragmentManager manager=getFragmentManager();
        android.app.FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.confirm_container,fragment);
        transaction.commit();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        fragment.onActivityResult(requestCode, resultCode, data);
    }
}
