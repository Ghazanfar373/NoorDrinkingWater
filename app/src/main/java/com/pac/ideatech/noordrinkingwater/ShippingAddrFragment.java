package com.pac.ideatech.noordrinkingwater;


import android.content.Intent;
import android.os.Bundle;
import android.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import static android.app.Activity.RESULT_OK;


/**
 * A simple {@link Fragment} subclass.
 */
public class ShippingAddrFragment extends Fragment {

    private static final int PLACE_PICKER_REQUEST =4433 ;
    private Button btnAddress,btnViewInvoice;
    private String latitude,longitude;
    private RadioGroup radioGroup;
    private SessionManager manager;
    public ShippingAddrFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manager=new SessionManager(getActivity());
        final View view= inflater.inflate(R.layout.fragment_shipping_addr, container, false);
        btnAddress=(Button) view.findViewById(R.id.btnaddloc);
        radioGroup=view.findViewById(R.id.radiogrp);
        RadioButton btn = view.findViewById(R.id.btn1);
        btn.setText(manager.getUser_Address());
        btn.setVisibility(View.VISIBLE);
        radioGroup.check(R.id.btn1);
        btnViewInvoice=view.findViewById(R.id.btninvoice);
        btnViewInvoice.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int selectedId = radioGroup.getCheckedRadioButtonId();

                // find the radiobutton by returned id
            RadioButton   selectedbtn = (RadioButton)view.findViewById(selectedId);
                Order.setUser_address(selectedbtn.getText().toString());
               // Toast.makeText(getActivity(), ""+selectedbtn.getText(), Toast.LENGTH_SHORT).show();
                InvoiceFragment fragment=new InvoiceFragment();
                final android.app.FragmentManager manager=getFragmentManager();
                android.app.FragmentTransaction transaction=manager.beginTransaction();
                transaction.replace(R.id.confirm_container,fragment);
                transaction.commit();
            }
        });
        btnAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                LatLng latLng1 = new LatLng(25.360364, 55.476593);
                LatLng latLng2 = new LatLng(25.468334, 55.500704);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(new LatLngBounds(latLng1, latLng2));

                try {
                    startActivityForResult(builder.build(getActivity()), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });


        return view;
        }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, getActivity());
                String toastMsg = String.format("Location: %s", place.getLatLng());
                // Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                Log.w("Place: %s", (String) place.getName() + place.getAddress());
                Toast.makeText(getActivity(), ""+place.getAddress(), Toast.LENGTH_SHORT).show();
                if(place.getAddress()!=null) {
                    RadioButton btn = new RadioButton(getActivity());
                    btn.setText(place.getAddress());
                    radioGroup.addView(btn);
                }

                LatLng user_latLng = place.getLatLng();
                latitude = Double.toString(user_latLng.latitude);
                longitude = Double.toString(user_latLng.longitude);
            }
        }
    }
}


