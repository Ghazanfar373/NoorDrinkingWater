package fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.ParentActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;
import helper.OrderCustomAddressDialogFragment;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_ADDRESS;
import static app.AppConfig.PREF_ADDRESS_LATITUDE;
import static app.AppConfig.PREF_ADDRESS_LONGITUDE;
import static app.AppConfig.PREF_ARIA_ID;
import static app.AppConfig.PREF_BUILDING_NO;
import static app.AppConfig.PREF_CITY;
import static app.AppConfig.PREF_HOUSE_NO;
import static app.AppConfig.PREF_USER_ID;
import static com.frandydevs.apps.noordrinkingwater.ParentActivity.hideKeyboard;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderAddressFragment extends ParentFragment implements OnMapReadyCallback, View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrderAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderAddressFragment newInstance(String param1, String param2) {
        OrderAddressFragment fragment = new OrderAddressFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getActivity();
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    private GoogleMap mMap;
    String lat = "", lng = "";
    private LocationManager locationManager;
    LatLng currentAddress;
    MarkerOptions markerOptions;
    Marker marker;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    Geocoder geocoder;
    RadioButton rbAddress;
    LinearLayout llAddNewAddress;
    Button btnPaymentMode;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_order_address, container, false);

        geocoder = new Geocoder(context, Locale.getDefault());
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        llAddNewAddress = returnView.findViewById(R.id.llAddNewAddress);
        rbAddress = returnView.findViewById(R.id.rbAddress);
        btnPaymentMode = returnView.findViewById(R.id.btnPaymentMode);

        rbAddress.setText("House No: " + dataSaving.getPrefValue(context, PREF_HOUSE_NO) +
                " Building: " + dataSaving.getPrefValue(context, PREF_BUILDING_NO) +
//                " Area: " + dataSaving.getPrefValue(context, PREF_ARIA_ID) +
                " City: " + dataSaving.getPrefValue(context, PREF_CITY) +
                " Address: " + dataSaving.getPrefValue(context, PREF_ADDRESS));

        lat = dataSaving.getPrefValue(context, PREF_ADDRESS_LATITUDE);
        lng = dataSaving.getPrefValue(context, PREF_ADDRESS_LONGITUDE);

        if (lat.isEmpty() && lng.isEmpty()) {
            lat = AppConfig.DEFAULT_LOCATION_LAT;
            lng = AppConfig.DEFAULT_LOCATION_LNG;
        }
        llAddNewAddress.setOnClickListener(this);
        btnPaymentMode.setOnClickListener(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.mainMap);
        mapFragment.getMapAsync(this);

        return returnView;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        // Add a marker in Sydney, Australia, and move the camera.
        if (!lat.isEmpty() && !lng.isEmpty()) {
            currentAddress = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            markerOptions = new MarkerOptions().position(currentAddress).title(("Current Address")).visible(true);
            marker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentAddress, 12.0f));
        }
    }

    OrderCustomAddressDialogFragment orderCustomAddressDialogFragment;

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llAddNewAddress:
                //show address dialog
                orderCustomAddressDialogFragment = OrderCustomAddressDialogFragment.newInstance(OrderAddressFragment.this);
                orderCustomAddressDialogFragment.show(getChildFragmentManager(), null);
                break;
            case R.id.btnPaymentMode:
                if (dataSaving.getPrefValue(context, PREF_HOUSE_NO).isEmpty() ||
                        dataSaving.getPrefValue(context, PREF_BUILDING_NO).isEmpty() ||
                        dataSaving.getPrefValue(context, PREF_CITY).isEmpty() ||
                        dataSaving.getPrefValue(context, PREF_ADDRESS).isEmpty()) {
                    ((ParentActivity) context).showMessageDialog("Address Missing", "Plz set complete address in your profile",
                            "ok", "", "dismiss", "");
                    return;
                }
                mListener.setDeliveryAddress(rbAddress.getText().toString().trim());
                break;
        }
    }

    public void updateUserAddressServerRequest(String lat, String lng, String city, String houseNo, String address, String buildingNo) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            this.lat = lat;
            this.lng = lng;
            this.city = city;
            this.houseNo = houseNo;
            this.address = address;
            this.buildingNo = buildingNo;
            RequestParams params = new RequestParams();
            params.put("customer_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "update_customer_address");
            params.put("from", "app");
            params.put("customer_address_lat", lat);
            params.put("customer_address_lng", lng);
            params.put("customer_city", city);
            params.put("house_no", houseNo);
            params.put("customer_address", address);
            params.put("building_no", buildingNo);
            params.put("area_id", dataSaving.getPrefValue(context, PREF_ARIA_ID));
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 17, this, null, getString(R.string.wait)));
        }
    }

    String houseNo = "", city = "", buildingNo = "", address = "";

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                dataSaving.setPrefValue(context, PREF_HOUSE_NO, houseNo);
                dataSaving.setPrefValue(context, PREF_BUILDING_NO, buildingNo);
                dataSaving.setPrefValue(context, PREF_CITY, city);
                dataSaving.setPrefValue(context, PREF_ADDRESS, address);
                dataSaving.setPrefValue(context, PREF_ADDRESS_LATITUDE, lat);
                dataSaving.setPrefValue(context, PREF_ADDRESS_LONGITUDE, lng);
                orderCustomAddressDialogFragment.dismiss();
                rbAddress.setText("House No: " + houseNo +
                        " Building: " + buildingNo +
//                        " Area: " + dataSaving.getPrefValue(getContext(), PREF_ARIA_ID) +
                        " City: " + city +
                        " Address: " + address);

                currentAddress = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                if (marker != null && mMap != null) {
                    marker.setPosition(currentAddress);
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentAddress, 12.0f));
                }
                showToast("Address updated successfully.", 1, 17);
            } else {
                showToast(jsonObject.getString("error_msg"), 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showCustomAddressDialog() {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog_FullScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.fragment_profile_address);
        dialog.setCancelable(false);
        final Button btnUpdateAddress = dialog.findViewById(R.id.btnUpdateAddress);
        final EditText etHouseNo = dialog.findViewById(R.id.etHouseNo);
        final EditText etBuildingNo = dialog.findViewById(R.id.etBuildingNo);
        final LinearLayout llCities = dialog.findViewById(R.id.llCities);
        final TextView tvCity = dialog.findViewById(R.id.tvCity);
        final EditText etAddress = dialog.findViewById(R.id.etAddress);
        final Spinner spCities = dialog.findViewById(R.id.spCities);

        btnUpdateAddress.setText(R.string.payment_mode);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvCity.setText(((TextView) view).getText().toString().trim());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        llCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spCities.performClick();
            }
        });
        btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                mListener.setDeliveryAddress("House No: " + etHouseNo.getText().toString().trim() +
                        " Building: " + etBuildingNo.getText().toString().trim() +
//                        " Area: " + dataSaving.getPrefValue(context, PREF_ARIA_ID) +
                        " City: " + tvCity.getText().toString().trim() +
                        " Address: " + etAddress.getText().toString().trim());
            }
        });
        dialog.show();
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);

        void setDeliveryAddress(String deliveryAddress);
    }
}
