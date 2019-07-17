package com.pac.ideatech.noordrinkingwater;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.app.Fragment;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


public class BlankFragment extends Fragment {
    EditText ed_msg;
    Button btn_send;
    SessionManager manager;
    Spinner spinner;


    public BlankFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_blank, container, false);
        spinner=view.findViewById(R.id.spinner_feedback);
        manager=new SessionManager(getActivity());
        ed_msg= (EditText) view.findViewById(R.id.ed_msg);
        btn_send= (Button) view.findViewById(R.id.btn_send);
        btn_send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String msgtxt=" ";
                if(TextUtils.isEmpty(ed_msg.getText().toString())){
                    Toast.makeText(getActivity(), "Please type some message.", Toast.LENGTH_SHORT).show();
                }else {
                    if(Utils.isConnected(getActivity())){
                        sendFeedback(ed_msg.getText().toString());
                    }else Utils.showSettingsAlert("Data",getActivity());
                }

            }
        });
        return view;
    }
    private void sendFeedback(final String msg) {
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
                Toast.makeText(getActivity(), "Thanks for your feedback!", Toast.LENGTH_SHORT).show();
                dialog.hide();



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
                params.put("msg", msg);
                params.put("user_id",manager.getUserId() );
                params.put("from", "app");
                params.put("subject", "complaint");
                params.put("module","feed_back");
                params.put("type",spinner.getSelectedItem().toString());


                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
