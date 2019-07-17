package com.pac.ideatech.noordrinkingwater;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class CartItemsFragment extends Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;
    private SessionManager sessionManager;
    public static TextView tv_subtotal,tv_total;


   // private OnListFragmentInteractionListener mListener;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    String key="false";
    public CartItemsFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if(getArguments()!=null) {
            Bundle bundle = getArguments();
            key = bundle.getString("updateVal");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);
        tv_subtotal= (TextView) view.findViewById(R.id.tv_subtotal);
        tv_total= (TextView) view.findViewById(R.id.tv_total);
        tv_total.setText("Total: "+Order.getTotalPrice()+" AED");
        tv_subtotal.setText("Subtotal: "+Order.getTotalPrice()+" AED");
        if(key.equals("yes")){
            tv_subtotal.setVisibility(View.GONE);
            tv_total.setVisibility(View.GONE);
        }
        sessionManager=new SessionManager(getActivity());
       // if (view instanceof RecyclerView) {
            Context context = view.getContext();
            RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.listorders);
            if (mColumnCount <= 1) {

                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
            recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
        }
            recyclerView.setAdapter(new MyOrderRecyclerViewAdapter(Order.orderList,getActivity(),new CartItemsFragment()));

       //}
        return view;
    }

    private void getOrderJSON(final String user_id) {

        String tag_string_req = "req_get_orders";
        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Please Wait...");
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("######RES", "JSON Order Response: " + response);
                Log.w("######RES", response);
                // hideDialog();
              //  Toast.makeText(getActivity(), "responce: "+response, Toast.LENGTH_LONG).show();
                try {
                    JSONObject object = new JSONObject(response);
                    if(object.getString("result").equals("true")){

                        Order.orderList.clear();
                        Order.setClearEnabled(true);



                    }
                }catch (Exception e){
                    e.printStackTrace();
                }

                dialog.hide();
//

            }

        }, new Response.ErrorListener()

        {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "json Error: " + error.getMessage());
                Toast.makeText(getActivity(),
                        "json error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                //hideDialog();

                dialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String, String>();
                try {
                    params.put("module", "purchase_history");
                  //  params.put("module", "place_order");
                    params.put("from", "app");
                    params.put("user_id", user_id);

                }catch (Exception ef){
                    ef.printStackTrace();
                }
                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
