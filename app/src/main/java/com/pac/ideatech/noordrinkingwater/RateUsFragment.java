package com.pac.ideatech.noordrinkingwater;


import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;


/**
 * A simple {@link Fragment} subclass.
 */
public class RateUsFragment extends Fragment {
SessionManager manager;
     RatingBar bar;
     EditText Edmsg;
    public RateUsFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        manager=new SessionManager(getActivity());
       View view=inflater.inflate(R.layout.fragment_rate_us, container, false);
        bar= view.findViewById(R.id.ratingBar);
         Edmsg=(EditText) view.findViewById(R.id.ed_feedback);
        Button btnsubmit=view.findViewById(R.id.btnSubmit);
        btnsubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(TextUtils.isEmpty(Edmsg.getText().toString())){
                    Toast.makeText(getActivity(), "Please type some message.", Toast.LENGTH_SHORT).show();
                }else {
                    if(Utils.isConnected(getActivity())){
                        sendFeedback(Edmsg.getText().toString(),String.valueOf(bar.getRating()),manager.getUserId());
                    }else Utils.showSettingsAlert("Data",getActivity());
                }
               // Toast.makeText(getActivity(), ""+bar.getRating()+"/"+msg.getText().toString(), Toast.LENGTH_SHORT).show();
            }
        });


        return view;
    }
    private void sendFeedback(final String msg, final String rating, final String id) {
        // Tag used to cancel the request

        String tag_string_req = "req_feedback";
        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("######RES", "JSON Response: " + response);
                Log.w("######RES", response);
                // hideDialog();

                dialog.hide();

                Toast.makeText(getActivity(), "Thanks For your Feedback!", Toast.LENGTH_LONG).show();
                bar.setRating(0f);
               Edmsg.getText().clear();
            }

        }, new Response.ErrorListener()

        {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "json Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "Network Problem", Toast.LENGTH_LONG).show();
                //hideDialog();

                dialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String,String>();
                params.put("rate_msg", msg);
                params.put("user_id",id);
                params.put("rating",rating);
                params.put("from", "app");
                params.put("module", "rateus");


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
