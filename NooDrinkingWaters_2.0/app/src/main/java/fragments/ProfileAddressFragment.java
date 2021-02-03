package fragments;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.frandydevs.apps.noordrinkingwater.ParentActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.google.android.gms.maps.CameraUpdate;
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

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_ADDRESS;
import static app.AppConfig.PREF_ADDRESS_LATITUDE;
import static app.AppConfig.PREF_ADDRESS_LONGITUDE;
import static app.AppConfig.PREF_ARIA_ID;
import static app.AppConfig.PREF_BUILDING_NO;
import static app.AppConfig.PREF_CITY;
import static app.AppConfig.PREF_DELIVERY_DAYS;
import static app.AppConfig.PREF_HOUSE_NO;
import static app.AppConfig.PREF_USER_CUSTOMER_TYPE;
import static app.AppConfig.PREF_USER_EMAIL;
import static app.AppConfig.PREF_USER_ID;
import static app.AppConfig.PREF_USER_NAME;
import static app.AppConfig.PREF_USER_PHONE;
import static com.frandydevs.apps.noordrinkingwater.ParentActivity.hideKeyboard;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ProfileAddressFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ProfileAddressFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileAddressFragment extends ParentFragment implements OnMapReadyCallback, View.OnClickListener, LocationListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ProfileAddressFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileAddressFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileAddressFragment newInstance(String param1, String param2) {
        ProfileAddressFragment fragment = new ProfileAddressFragment();
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
    EditText etHouseNo, etBuildingNo, etAddress;
    ImageView ivBG;
    Spinner spCities;
    TextView tvCity;
    LinearLayout llCities;
    Button btnUpdateAddress;
    boolean firstTime = true;

    String lat = "", lng = "";
    private LocationManager locationManager;
    LatLng currentAddress;
    MarkerOptions markerOptions;
    Marker marker;
    private static final long MIN_TIME = 400;
    private static final float MIN_DISTANCE = 1000;
    Geocoder geocoder;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_profile_address, container, false);

        geocoder = new Geocoder(context, Locale.getDefault());
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);

        ivBG = returnView.findViewById(R.id.ivBG);
        etHouseNo = returnView.findViewById(R.id.etHouseNo);
        etBuildingNo = returnView.findViewById(R.id.etBuildingNo);
        etAddress = returnView.findViewById(R.id.etAddress);
        spCities = returnView.findViewById(R.id.spCities);
        tvCity = returnView.findViewById(R.id.tvCity);
        llCities = returnView.findViewById(R.id.llCities);
        btnUpdateAddress = returnView.findViewById(R.id.btnUpdateAddress);

        Picasso.get().load(R.drawable.bg_profile_illustration).into(ivBG);
        ivBG.setPadding(0, AppConfig.DEVICE_HEIGHT / 3, 0, 0);

        etHouseNo.setText(dataSaving.getPrefValue(context, PREF_HOUSE_NO));
        etBuildingNo.setText(dataSaving.getPrefValue(context, PREF_BUILDING_NO));
        etAddress.setText(dataSaving.getPrefValue(context, PREF_ADDRESS));
        tvCity.setText(dataSaving.getPrefValue(context, PREF_CITY));

        lat = dataSaving.getPrefValue(context, PREF_ADDRESS_LATITUDE);
        lng = dataSaving.getPrefValue(context, PREF_ADDRESS_LONGITUDE);

        if (lat.isEmpty() && lng.isEmpty()) {
            lat = AppConfig.DEFAULT_LOCATION_LAT;
            lng = AppConfig.DEFAULT_LOCATION_LNG;
        }

        llCities.setOnClickListener(this);
        btnUpdateAddress.setOnClickListener(this);

        spCities.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!firstTime || tvCity.getText().toString().trim().equalsIgnoreCase("null")
                        || tvCity.getText().toString().trim().equalsIgnoreCase("")) {
                    firstTime = false;
                    tvCity.setText(((TextView) view).getText().toString().trim());
                } else {
                    firstTime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
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
        mMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                hideKeyboard(getActivity(), false, null);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                    startActivity(gpsOptionsIntent);
                    return true;
                }
                return false;
            }
        });
        mMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
            @Override
            public void onMapClick(LatLng latLng) {
                hideKeyboard(getActivity(), false, null);
                lat = String.valueOf(latLng.latitude);
                lng = String.valueOf(latLng.longitude);
                currentAddress = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                marker.setPosition(currentAddress);

                List<Address> addresses = new ArrayList<>();
                try {
                    addresses = geocoder.getFromLocation(Double.parseDouble(lat), Double.parseDouble(lng), 1);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                if (!addresses.isEmpty()) {
                    android.location.Address address = addresses.get(0);

                    if (address != null) {
                        StringBuilder sb = new StringBuilder();
                   /* for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                        sb.append(address.getAddressLine(i) + "\n");
                    }*/
                        if (address.getMaxAddressLineIndex() > 0) {
                            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                                sb.append(address.getAddressLine(i) + "\n");
                            }
                        } else {
                            try {
                                sb.append(address.getAddressLine(0) + "\n");
                            } catch (Exception ignored) {
                            }
                        }
                        etAddress.setText(sb);
                    }
                }
            }
        });

        Dexter.withActivity(getActivity())
                .withPermissions(Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION)
                .withListener(new MultiplePermissionsListener() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onPermissionsChecked(MultiplePermissionsReport report) {
                        if (report.areAllPermissionsGranted()) {
/*
                            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, MIN_TIME, MIN_DISTANCE,
                                    ProfileAddressFragment.this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
*/
                            mMap.setMyLocationEnabled(true);
                        }
                    }

                    @Override
                    public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

        // Add a marker in Sydney, Australia, and move the camera.
        if (!lat.isEmpty() && !lng.isEmpty()) {
            currentAddress = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            markerOptions = new MarkerOptions().position(currentAddress).title(("Current Address")).visible(true);
            marker = mMap.addMarker(markerOptions);
            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentAddress, 12.0f));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.llCities:
                spCities.performClick();
                break;
            case R.id.btnUpdateAddress:
                hideKeyboard(getActivity(), false, null);
                if (etHouseNo.getText().toString().trim().isEmpty()) {
                    ((ParentActivity) getContext()).showToast("Plz enter house no", 1, 17);
                    return;
                }
                if (etBuildingNo.getText().toString().trim().isEmpty()) {
                    ((ParentActivity) getContext()).showToast("Plz enter building no", 1, 17);
                    return;
                }
                if (etAddress.getText().toString().trim().isEmpty()) {
                    ((ParentActivity) getContext()).showToast("Plz enter address", 1, 17);
                    return;
                }
                updateUserAddressServerRequest();
                break;
        }
    }

    private void updateUserAddressServerRequest() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("customer_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "update_customer_address");
            params.put("from", "app");
            params.put("customer_address_lat", lat);
            params.put("customer_address_lng", lng);
            params.put("customer_city", tvCity.getText().toString().trim());
            params.put("house_no", etHouseNo.getText().toString().trim());
            params.put("customer_address", etAddress.getText().toString().trim());
            params.put("building_no", etBuildingNo.getText().toString().trim());
            params.put("area_id", dataSaving.getPrefValue(context, PREF_ARIA_ID));
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 13, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                dataSaving.setPrefValue(context, PREF_HOUSE_NO, etHouseNo.getText().toString().trim());
                dataSaving.setPrefValue(context, PREF_BUILDING_NO, etBuildingNo.getText().toString().trim());
                dataSaving.setPrefValue(context, PREF_CITY, tvCity.getText().toString().trim());
                dataSaving.setPrefValue(context, PREF_ADDRESS, etAddress.getText().toString().trim());
                dataSaving.setPrefValue(context, PREF_ADDRESS_LATITUDE, lat);
                dataSaving.setPrefValue(context, PREF_ADDRESS_LONGITUDE, lng);
                showToast("Address updated successfully.", 1, 17);
            } else {
                showToast(jsonObject.getString("error_msg"), 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onLocationChanged(Location location) {
       /* LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, 10);
        mMap.animateCamera(cameraUpdate);
        locationManager.removeUpdates(this);*/
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
    }

    @Override
    public void onProviderEnabled(String provider) {
    }

    @Override
    public void onProviderDisabled(String provider) {
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
    }
}
