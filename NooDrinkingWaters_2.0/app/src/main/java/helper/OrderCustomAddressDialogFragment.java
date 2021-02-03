package helper;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.frandydevs.apps.noordrinkingwater.CartAndOrderProcessActivity;
import com.frandydevs.apps.noordrinkingwater.MainActivity;
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

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import app.AppConfig;
import app.AppController;
import app.DataSaving;
import fragments.OrderAddressFragment;

import static app.AppConfig.PREF_ADDRESS;
import static app.AppConfig.PREF_ADDRESS_LATITUDE;
import static app.AppConfig.PREF_ADDRESS_LONGITUDE;
import static app.AppConfig.PREF_BUILDING_NO;
import static app.AppConfig.PREF_CITY;
import static app.AppConfig.PREF_HOUSE_NO;
import static com.frandydevs.apps.noordrinkingwater.ParentActivity.hideKeyboard;

public class OrderCustomAddressDialogFragment extends DialogFragment implements GoogleMap.OnCameraMoveCanceledListener,
        GoogleMap.OnCameraIdleListener, GoogleMap.OnCameraMoveStartedListener, GoogleMap.OnCameraMoveListener {

    GoogleMap googleMap;
    boolean isMyLocationCameraMoveStarted;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NO_TITLE, R.style.Theme_Dialog_FullScreen);
    }

    // TODO: Rename and change types and number of parameters
    public static OrderCustomAddressDialogFragment newInstance(OrderAddressFragment orderAddressFragment) {
        addressFragment = orderAddressFragment;
        Bundle args = new Bundle();
        OrderCustomAddressDialogFragment fragment = new OrderCustomAddressDialogFragment();
        fragment.setArguments(args);
        return fragment;
    }

    static OrderAddressFragment addressFragment;
    View returnView;
    String lat = "", lng = "";
    private LocationManager locationManager;
    LatLng currentAddress;
    MarkerOptions markerOptions;
    Marker marker;
    Geocoder geocoder;
    DataSaving dataSaving = AppController.getDataSaving();
    boolean firstTime = true;

    public void onDestroyView() {
        super.onDestroyView();
        FragmentManager fm = getActivity().getSupportFragmentManager();
        Fragment fragment = (fm.findFragmentById(R.id.map));
        FragmentTransaction ft = fm.beginTransaction();
        ft.remove(fragment);
        ft.commit();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        returnView = inflater.inflate(R.layout.dialog_custom_order_address, container, false);

        // Do all the stuff to initialize your custom view

        Button btnUpdateAddress = returnView.findViewById(R.id.btnUpdateAddress);
        final EditText etHouseNo = returnView.findViewById(R.id.etHouseNo);
        final EditText etBuildingNo = returnView.findViewById(R.id.etBuildingNo);
        LinearLayout llCities = returnView.findViewById(R.id.llCities);
        final TextView tvCity = returnView.findViewById(R.id.tvCity);
        final EditText etAddress = returnView.findViewById(R.id.etAddress);
        final Spinner spCities = returnView.findViewById(R.id.spCities);

        etHouseNo.setText(dataSaving.getPrefValue(getContext(), PREF_HOUSE_NO));
        etBuildingNo.setText(dataSaving.getPrefValue(getContext(), PREF_BUILDING_NO));
        etAddress.setText(dataSaving.getPrefValue(getContext(), PREF_ADDRESS));
        tvCity.setText(dataSaving.getPrefValue(getContext(), PREF_CITY));

        geocoder = new Geocoder(getContext(), Locale.getDefault());
        locationManager = (LocationManager) getContext().getSystemService(Context.LOCATION_SERVICE);

        lat = dataSaving.getPrefValue(getContext(), PREF_ADDRESS_LATITUDE);
        lng = dataSaving.getPrefValue(getContext(), PREF_ADDRESS_LONGITUDE);

        if (lat.isEmpty() && lng.isEmpty()) {
            lat = AppConfig.DEFAULT_LOCATION_LAT;
            lng = AppConfig.DEFAULT_LOCATION_LNG;
        }

        ImageView ivClose = returnView.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

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

        llCities.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                spCities.performClick();
            }
        });
        btnUpdateAddress.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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
                addressFragment.updateUserAddressServerRequest(lat, lng, tvCity.getText().toString().trim(),
                        etHouseNo.getText().toString().trim(), etAddress.getText().toString().trim(), etBuildingNo.getText().toString().trim());
            }
        });

//        btnUpdateAddress.setText(R.string.payment_mode);
        SupportMapFragment mapFragment = (SupportMapFragment) getActivity().getSupportFragmentManager().findFragmentById(R.id.map);

        mapFragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap gMap) {
                googleMap = gMap;
                googleMap.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                    @Override
                    public boolean onMyLocationButtonClick() {
                        hideKeyboard(getActivity(), false, null);
                        if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                            Intent gpsOptionsIntent = new Intent(android.provider.Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                            startActivity(gpsOptionsIntent);
                            return true;
                        }
                        isMyLocationCameraMoveStarted = true;
                        return false;
                    }
                });

                googleMap.setOnCameraIdleListener(OrderCustomAddressDialogFragment.this);
                googleMap.setOnCameraMoveStartedListener(OrderCustomAddressDialogFragment.this);
                googleMap.setOnCameraMoveListener(OrderCustomAddressDialogFragment.this);
                googleMap.setOnCameraMoveCanceledListener(OrderCustomAddressDialogFragment.this);

                googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {
                    @Override
                    public void onMapClick(LatLng latLng) {
                        hideKeyboard(getActivity(), false, null);
                        lat = String.valueOf(latLng.latitude);
                        lng = String.valueOf(latLng.longitude);
                        currentAddress = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
                        if (lat.isEmpty() && lng.isEmpty()) {
                            marker = googleMap.addMarker(markerOptions);
                        }
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
                                            OrderAddressFragment.this); //You can also use LocationManager.GPS_PROVIDER and LocationManager.PASSIVE_PROVIDER
*/
                                    googleMap.setMyLocationEnabled(true);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
                setLocationPin();
            }
        });
        return returnView;
    }

    private void setLocationPin() {
        if (!lat.isEmpty() && !lng.isEmpty()) {
            googleMap.clear();
            currentAddress = new LatLng(Double.parseDouble(lat), Double.parseDouble(lng));
            markerOptions = new MarkerOptions().position(currentAddress).title(("Current Address")).visible(true);
            marker = googleMap.addMarker(markerOptions);
            googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(currentAddress, 12.0f));
        }
    }

    @Override
    public void onCameraMoveStarted(int reason) {

        if (reason == GoogleMap.OnCameraMoveStartedListener.REASON_GESTURE) {
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_API_ANIMATION) {
        } else if (reason == GoogleMap.OnCameraMoveStartedListener
                .REASON_DEVELOPER_ANIMATION) {
        }
    }

    @Override
    public void onCameraMove() {
    }

    @Override
    public void onCameraMoveCanceled() {
        isMyLocationCameraMoveStarted = false;
    }

    @Override
    public void onCameraIdle() {
        if (isMyLocationCameraMoveStarted) {
            lat = String.valueOf(googleMap.getCameraPosition().target.latitude);
            lng = String.valueOf(googleMap.getCameraPosition().target.longitude);
            setLocationPin();
        }
        isMyLocationCameraMoveStarted = false;
    }
}

