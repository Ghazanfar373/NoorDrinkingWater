package com.pac.ideatech.noordrinkingwater;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
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

import ss.com.bannerslider.Slider;

/**
 * A fragment representing a list of Items.
 * <p/>
 * Activities containing this fragment MUST implement the {@link OnListFragmentInteractionListener}
 * interface.
 */
public class ProductFragment extends android.app.Fragment {

    // TODO: Customize parameters
    private int mColumnCount = 1;


   // private OnListFragmentInteractionListener mListener;
    private TextView product_Title;
    private Button btn_Price,btn_OrderNow;
    final List<Product> serverList=new ArrayList<>();
    RecyclerView recyclerView;
    private Slider slider;
    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public ProductFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            getJSON();
        }catch (Exception ex){
            ex.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();


       // Log.w("LIST SIZE******",Product.ITEMS.size()+""+Product.ITEMS.get(0).getProduct_id());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_product_list, container, false);
        product_Title= (TextView) view.findViewById(R.id.idtitle);
        btn_Price= (Button) view.findViewById(R.id.btn_price);
        btn_OrderNow= (Button) view.findViewById(R.id.btn_ordernow);



        //Set Slider
        slider = view.findViewById(R.id.banner_slider1);
        slider.setAdapter(new MainSliderAdapter());
        slider.setSelectedSlide(2);
        // Set the adapter

//        if (view instanceof RecyclerView) {
           Context context = view.getContext();
             recyclerView = (RecyclerView) view.findViewById(R.id.list);
//            if (mColumnCount <= 1) {
//                recyclerView.setLayoutManager(new LinearLayoutManager(context));
//            } else {
                recyclerView.setLayoutManager(new GridLayoutManager(context, mColumnCount));
            //}
            recyclerView.setAdapter(new MyProductRecyclerViewAdapter(Product.ITEMS,getActivity(),getActivity().getFragmentManager()));


      //  }
        return view;
    }


//    @Override
//    public void onAttach(Context context) {
//        super.onAttach(context);
//        if (context instanceof OnListFragmentInteractionListener) {
//            mListener = (OnListFragmentInteractionListener) context;
//        } else {
//            throw new RuntimeException(context.toString()
//                    + " must implement OnListFragmentInteractionListener");
//        }
//    }

//    @Override
//    public void onDetach() {
//        super.onDetach();
//        mListener = null;
//    }


    private void getJSON() {
        // Tag used to cancel the request

        String tag_string_req = "req_json";
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

                dialog.hide();
                try {
                    Product.ITEMS.clear();
                    JSONObject object=new JSONObject(response);
                    JSONArray jsonArray=object.getJSONArray("result");
                    for (int i=0;i<jsonArray.length();i++){
                        JSONObject product_object=(JSONObject)jsonArray.get(i);
                        Product product=new Product();
                        product.setTitle(product_object.getString("name"));
                        product.setProduct_id(product_object.getString("id"));
                        product.setImgURL(product_object.getString("image"));
                        product.setPrice(product_object.getString("price"));
                        Product.ITEMS.add(product);
                    }
                    recyclerView.setAdapter(new MyProductRecyclerViewAdapter(Product.ITEMS,getActivity(),getActivity().getFragmentManager()));
                 Log.w("LIST SIZE******",Product.ITEMS.size()+"");
                } catch (Exception e) {
                    e.printStackTrace();
                }

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
                params.put("module", "get_products");
                params.put("from", "app");

                return params;
            }

        };
        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(strReq, tag_string_req);

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
        void onListFragmentInteraction(Product item);
    }
}
