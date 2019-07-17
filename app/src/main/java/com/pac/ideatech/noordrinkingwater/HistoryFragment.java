package com.pac.ideatech.noordrinkingwater;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;


import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class HistoryFragment extends Fragment {
    List<Order> order_historyList=new ArrayList<>();
    // TODO: Customize parameters
    private int mColumnCount = 1;
   private  RecyclerView recyclerView;
    private OnListFragmentInteractionListener mListener;
    private SessionManager manager;
    Order order;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public HistoryFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        manager=new SessionManager(getActivity());
        try {
            getJSON();
        }catch (Exception ex){
            ex.printStackTrace();

        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_history_list, container, false);

        // Set the adapter


        if (view instanceof RecyclerView) {
            Context context = view.getContext();
            recyclerView = (RecyclerView) view;
            if (mColumnCount <= 1) {
                recyclerView.setLayoutManager(new LinearLayoutManager(context));
            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            }
            recyclerView.setAdapter(new MyHIstoryRecyclerViewAdapter(Order.order_historyList,getActivity(),getFragmentManager()));
        }

        return view;
    }



    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     * <p/>
     * See the Android Training lesson <a href=
     * "http://developer.android.com/training/basics/fragments/communicating.html"
     * >Communicating with Other Fragments</a> for more information.
     */
    public interface OnListFragmentInteractionListener {
        // TODO: Update argument type and name
        void onListFragmentInteraction(Order item);
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    private void getJSON() {
        // Tag used to cancel the request

        String tag_string_req = "req_history";
        final ProgressDialog dialog=new ProgressDialog(getActivity());
        dialog.setMessage("Loading Data...");
        dialog.show();
        StringRequest strReq = new StringRequest(Request.Method.POST,
                WebServerLinks.signup, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Log.d("######RES", "JSON Response: " + response);
                Log.w("######RES", response);
                // hideDialog();


                try {
                    JSONObject object=new JSONObject(response);
                    if(object.getString("result").equals("true")) {
                        Order.order_historyList.clear();
                        JSONArray jsonArray = object.getJSONArray("listing");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject order_object = (JSONObject) jsonArray.get(i);
                            order = new Order();
                            order.setProduct_id("xacfwqnfbcqevbfebqveev");
                            order.setProdut_title(order_object.getString("product_name"));
                            order.setUnit_price(order_object.getString("price"));
                            order.setQnty(order_object.getString("qty"));
                            order.setTotal_amount(Double.parseDouble(order_object.getString("total")));
                            order.setSaleDate(order_object.getString("sale_date"));
                            Order.order_historyList.add(order);
                        }
                    }else
                        Toast.makeText(getActivity(), "There is no record in your History !", Toast.LENGTH_SHORT).show();
//                    order.setProduct_id(Product.ITEMS.get(postion).getProduct_id());
//                    order.setProdut_title(Product.ITEMS.get(postion).getTitle());
//                    order.setUnit_price(Product.ITEMS.get(postion).getPrice());
//                    order.setQnty(ed_qntyLabel.getText().toString());
//                    order.setTotal_amount(total_amount);
                    recyclerView.setAdapter(new MyHIstoryRecyclerViewAdapter(Order.order_historyList,getActivity(),getFragmentManager()));
//                    Log.w("LIST SIZE******",Order.order_historyList.size()+"");
//                    Log.w("LIST SIZE******",Order.order_historyList.get(0).getProdut_title()+"");
//                    Log.w("LIST SIZE******",Order.order_historyList.get(0).getQnty()+"");
//                    Log.w("LIST SIZE******",Order.order_historyList.get(0).getSaleDate()+"");
//                    Log.w("LIST SIZE******",Order.order_historyList.get(0).getUnit_price()+"");
//                    Log.w("LIST SIZE******",Order.order_historyList.get(0).getTotal_amount()+"");
                    dialog.hide();
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }

        }, new Response.ErrorListener()

        {


            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("There was Error", "json Error: " + error.getMessage());
//                Toast.makeText(getActivity(),
//                        "Network Problem", Toast.LENGTH_LONG).show();
                //hideDialog();

                dialog.hide();
            }
        }) {

            @Override
            protected Map<String, String> getParams() {
                // Posting parameters to login url
                Map<String, String> params = new HashMap<String,String>();
                params.put("module", "purchase_history");
                params.put("from", "app");
                params.put("user_id", manager.getUserId());

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

    }
}
