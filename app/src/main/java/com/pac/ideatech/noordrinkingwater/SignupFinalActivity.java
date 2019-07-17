package com.pac.ideatech.noordrinkingwater;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.text.TextUtils;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * A login screen that offers login via email/password.
 */
public class SignupFinalActivity extends AppCompatActivity  {

    int PLACE_PICKER_REQUEST = 1;
    String from;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */
    private static final String[] DUMMY_CREDENTIALS = new String[]{
            "foo@example.com:hello", "bar@example.com:world"
    };
    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private AutoCompleteTextView mEmailView;

    private View mProgressView;
    private View mLoginFormView;
    private Spinner spinner_cust_type;
    List<String> cust_list=new ArrayList<>();
    private EditText Ed_fullname,Ed_Contact,Ed_Password,Ed_Password_again;
    private EditText Ed_addresss,Ed_How_To_Know,Ed_House_no;
    private ImageButton btn_placePiker;
    private Spinner spinner_country,spinner_area,spinner_pref_delivery;
    private String fullname,address,email,password,re_password,contact,house_no,how_to_know;
    private String latitude,longitude;
    TextView location_msg;
    boolean isEditProfile=false;
    SessionManager manager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup_final);
        // Set up the login form.
        manager=new SessionManager(this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
       Intent intent=getIntent();



        Ed_fullname= (EditText) findViewById(R.id.full_name);
        mEmailView = (AutoCompleteTextView) findViewById(R.id.email);
        Ed_Contact= (EditText) findViewById(R.id.contact_number);
        spinner_cust_type= (Spinner) findViewById(R.id.spinner_cust_type);


        if(Utils.isConnected(this)){
            cust_list=new ArrayList<>();
            cust_list.add("Customer Type");
            cust_list=getCustomerTypeList();

            ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, cust_list);
            spinnerAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner_cust_type.setAdapter(spinnerAdapter);
            spinner_cust_type.setSelection(1);
            spinnerAdapter.notifyDataSetChanged();

        }else{
            Utils.showSettingsAlert("DATA",this);}


        Ed_addresss= (EditText) findViewById(R.id.address);
        Ed_House_no= (EditText) findViewById(R.id.house_no);
        Ed_How_To_Know= (EditText) findViewById(R.id.msg);
      //  spinner_country= (Spinner) findViewById(R.id.spinner_country);
        spinner_area= (Spinner) findViewById(R.id.spinner_area);
        spinner_pref_delivery= (Spinner) findViewById(R.id.spinner_delivery);
        Ed_Password = (EditText) findViewById(R.id.password);
        Ed_Password_again= (EditText) findViewById(R.id.password_again);
        Ed_Password_again.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int id, KeyEvent keyEvent) {
                if (id == R.id.login || id == EditorInfo.IME_NULL) {
                    attemptLogin();
                    return true;
                }
                return false;
            }
        });
        Button mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);

        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mLoginFormView = findViewById(R.id.login_form);
        mProgressView = findViewById(R.id.login_progress);

        btn_placePiker = (ImageButton) findViewById(R.id.button_placepicker);
        btn_placePiker.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {


                LatLng latLng1 = new LatLng(25.360364, 55.476593);
                LatLng latLng2 = new LatLng(25.468334, 55.500704);
                PlacePicker.IntentBuilder builder = new PlacePicker.IntentBuilder();
                builder.setLatLngBounds(new LatLngBounds(latLng1, latLng2));

                try {
                    startActivityForResult(builder.build(SignupFinalActivity.this), PLACE_PICKER_REQUEST);
                } catch (GooglePlayServicesRepairableException e) {
                    e.printStackTrace();
                } catch (GooglePlayServicesNotAvailableException e) {
                    e.printStackTrace();
                }
            }
        });

        from=intent.getStringExtra("from");
        if(from.equals("EditProfile")){
            getSupportActionBar().setTitle("Edit Profile");
            isEditProfile=true;
            Ed_fullname.setText(manager.getUser_fName());
            Ed_Contact.setText(manager.getUser_Mobile());
            mEmailView.setText(manager.getUserEmail());
            Ed_addresss.setText(manager.getUser_Address());
            Ed_House_no.setText(manager.getUSER_House_No());
            spinner_area.setSelection(1);
            spinner_cust_type.setSelection(3);
            spinner_pref_delivery.setSelection(4);

            Ed_Password_again.setVisibility(View.GONE);


        }
    }
    public List<String> getCustomerTypeList() {

        final ProgressDialog dialog=new ProgressDialog(this);
        dialog.setMessage("Connecting to Server...");
        dialog.show();
        // Tag used to cancel the request

        String tag_string_req = "req_cust_type";

        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("######RES", "signup Response: " + response);
                Log.w("######RES", response);
                dialog.hide();

                showProgress(false);
                try {
                    JSONObject object=new JSONObject(response);
                    JSONArray jArray =  object.getJSONArray("result");
                    if (jArray != null) {
                        for (int i = 0; i < jArray.length(); i++) {
                            Log.w("@@@@@@List Items===",jArray.getString(i));
                            cust_list.add(jArray.getString(i));
                        }
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()

        {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "Login Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Network Problem", Toast.LENGTH_LONG).show();
                dialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String,String>();

                params.put("module","customer_type");

                params.put("from","app");




                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);


        return cust_list;
    }


    @Override
        public void onActivityResult(int requestCode, int resultCode, Intent data) {
            // super.onActivityResult(requestCode, resultCode, data);
            if (requestCode == PLACE_PICKER_REQUEST) {
                if (resultCode == RESULT_OK) {
                    Place place = PlacePicker.getPlace(data, SignupFinalActivity.this);
                    String toastMsg = String.format("Location: %s", place.getLatLng());
                    // Toast.makeText(getActivity(), toastMsg, Toast.LENGTH_LONG).show();
                    Log.w("Place: %s", (String) place.getName() + place.getLatLng());
                   // Toast.makeText(this, ""+place.getLatLng(), Toast.LENGTH_SHORT).show();
                    btn_placePiker.setImageResource(R.drawable.ic_assignment_turned_in_black_24dp);
                    LatLng user_latLng = place.getLatLng();
                    latitude=Double.toString(user_latLng.latitude);
                    longitude=Double.toString(user_latLng.longitude);
                    location_msg= (TextView) findViewById(R.id.loc_msg);
                    location_msg.setText("Location successfully attached!");
                    location_msg.setTextColor(Color.DKGRAY);


                }
            }
        }

    private void attemptLogin() {
        fullname=Ed_fullname.getText().toString();
        email = mEmailView.getText().toString();
        contact=Ed_Contact.getText().toString();
        password = Ed_Password.getText().toString();
        re_password=Ed_Password_again.getText().toString();


        address = Ed_addresss.getText().toString();
        house_no=Ed_House_no.getText().toString();
        how_to_know = Ed_How_To_Know.getText().toString();
        contact=Ed_Contact.getText().toString();


        boolean cancel = false;
        View focusView = null;

        // Check for a valid password, if the user entered one.
        if(TextUtils.isEmpty(fullname)){
            Ed_fullname.setError(getString(R.string.error_invalid_input));
            Ed_fullname.requestFocus();
        }
        else if (TextUtils.isEmpty(address) ) {
            Ed_addresss.setError(getString(R.string.error_invalid_input));
            Ed_addresss.requestFocus();
        }
        else if (TextUtils.isEmpty(contact) ) {
            Ed_Contact.setError(getString(R.string.error_invalid_input));
            Ed_Contact.requestFocus();
        }
        else if (TextUtils.isEmpty(email) ) {
            mEmailView.setError(getString(R.string.error_invalid_input));
            mEmailView.requestFocus();
        }
       else if (TextUtils.isEmpty(house_no) ) {

            Ed_House_no.setError(getString(R.string.error_invalid_input));
            Ed_House_no.requestFocus();
        }
        // Check for a valid email address.
        else if (TextUtils.isEmpty(how_to_know)) {
            Ed_How_To_Know.setError(getString(R.string.error_field_required));
            Ed_How_To_Know.requestFocus();

     }
        else if(spinner_area.getSelectedItem().toString().equals("Area")){
            spinner_area.requestFocus();
            Toast.makeText(this, "Please Select Area!", Toast.LENGTH_SHORT).show();
        }
        else if(spinner_pref_delivery.getSelectedItem().toString().equals("Consumptions per week")){
            spinner_area.requestFocus();
            Toast.makeText(this, "Please Select your Consumption!", Toast.LENGTH_SHORT).show();
        }
        else if (spinner_pref_delivery.getSelectedItem().toString().equals("Preffered Delivery")){
           spinner_pref_delivery.requestFocus();
            Toast.makeText(this, "Please Select Preffred Delivery!", Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(latitude) || TextUtils.isEmpty(longitude)){
            Toast.makeText(this, "Please attach your deliery address location!", Toast.LENGTH_SHORT).show();
         location_msg= (TextView) findViewById(R.id.loc_msg);
            location_msg.setTextColor(Color.RED);
        }else if (TextUtils.isEmpty(password) ) {
            Ed_Password.setError(getString(R.string.error_invalid_input));
            Ed_Password.requestFocus();
        }else if (TextUtils.isEmpty(re_password) ) {
            if(from.equals("EditProfile")) {

            }else {
                Ed_Password_again.setError(getString(R.string.error_invalid_input));
                Ed_Password_again.requestFocus();
            }
        }
            else {
            // Show a progress spinner, and kick off a background task to
            // perform the user login attempt.
            showProgress(true);
                        if(Utils.isConnected(this)){
                checkLogin();
            }else{
                Utils.showSettingsAlert("DATA",this);}

        }
    }

    private void checkLogin() {
        // Tag used to cancel the request
        String tag_string_req = "req_signup";
        showProgress(true);
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("######RES", "signup Response: " + response);
                Log.w("######RES", response);
                // hideDialog();
                String userAddress=" ";
                showProgress(false);
                try {
                    JSONObject object=new JSONObject(response);
                    if(object.getString("result").equals("true")) {

                        methodAlert();
                    }else
                        Toast.makeText(SignupFinalActivity.this, "Something wrong with Server!", Toast.LENGTH_SHORT).show();

//
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }, new Response.ErrorListener()

        {



            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "SignUp Error: " + error.getMessage());
                Toast.makeText(getApplicationContext(),
                        "Network Problem", Toast.LENGTH_LONG).show();
                //hideDialog();
                showProgress(false);
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String,String>();
                params.put("customer_name", fullname);
                params.put("contact_no", contact);
                params.put("username",email);
                params.put("customer_email",email);
                params.put("password",password);
                params.put("re_password",re_password);
                params.put("customer_city",spinner_area.getSelectedItem().toString());
                params.put("house_no",house_no);
                params.put("customer_address",address);
                params.put("customer_group",spinner_cust_type.getSelectedItem().toString());
                params.put("describe",how_to_know);

                params.put("delivery_required",spinner_pref_delivery.getSelectedItem().toString());
                params.put("customer_address_lat",latitude);
                params.put("customer_address_lng",longitude);
                params.put("from","app");
                params.put("how_to_know",how_to_know);
                if(isEditProfile){
                    params.put("module","update_customer");
                    params.put("customer_id",manager.getUserId());

                }else{
                    params.put("module","add_customer");
                }




                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);
    }
    @Override
    public boolean onSupportNavigateUp(){
       finish();
        return true;
    }
    public void methodAlert() {
        String title="Registered Successfully!";
        if(isEditProfile){
            title="Profile Successfully updated";
        }
        AlertDialog alertDialog = new AlertDialog.Builder(SignupFinalActivity.this)
                //set icon
                .setIcon(R.drawable.ic_tick_black_24dp)
                //set title

                .setTitle(title)
                //set message
                .setMessage("Please login with your new Credentials!")
                //set positive button
                .setPositiveButton("Okay", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        //set what would happen when positive button is clicked
                        manager.logout();
                        Intent intent = new Intent(SignupFinalActivity.this, LoginActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY);
                        startActivity(intent);
                        finish();

                    }
                })
                //set negative button
//            .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                @Override
//                public void onClick(DialogInterface dialogInterface, int i) {
//                    //set what should happen when negative button is clicked
//                    Toast.makeText(getApplicationContext(),"Nothing Happened",Toast.LENGTH_LONG).show();
//                }
//            })
                .show();
    }
    /**
     * Shows the progress UI and hides the login form.
     */
    @TargetApi(Build.VERSION_CODES.HONEYCOMB_MR2)
    private void showProgress(final boolean show) {
        // On Honeycomb MR2 we have the ViewPropertyAnimator APIs, which allow
        // for very easy animations. If available, use these APIs to fade-in
        // the progress spinner.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB_MR2) {
            int shortAnimTime = getResources().getInteger(android.R.integer.config_shortAnimTime);

            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
            mLoginFormView.animate().setDuration(shortAnimTime).alpha(
                    show ? 0 : 1).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
                }
            });

            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mProgressView.animate().setDuration(shortAnimTime).alpha(
                    show ? 1 : 0).setListener(new AnimatorListenerAdapter() {
                @Override
                public void onAnimationEnd(Animator animation) {
                    mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
                }
            });
        } else {
            // The ViewPropertyAnimator APIs are not available, so simply show
            // and hide the relevant UI components.
            mProgressView.setVisibility(show ? View.VISIBLE : View.GONE);
            mLoginFormView.setVisibility(show ? View.GONE : View.VISIBLE);
        }
    }


}

