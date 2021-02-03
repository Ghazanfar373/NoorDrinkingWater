package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frandydevs.apps.noordrinkingwater.R;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import adapters.MyOrdersAdapter;
import model.OrderModel;
import model.OrderProductModel;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyOrdersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyOrdersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersFragment extends ParentFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyOrdersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersFragment newInstance(String param1, String param2) {
        MyOrdersFragment fragment = new MyOrdersFragment();
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

    SwipeRefreshLayout srlOrders;
    RecyclerView rvOrders;
    ArrayList<OrderModel> orderModelArrayList;
    ArrayList<OrderProductModel> orderProductModelArrayList;
    OrderModel orderModel;
    OrderProductModel orderProductModel;
    MyOrdersAdapter myOrdersAdapter;
    private int activeColor, deliveredColor, cancelColor;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_my_orders, container, false);
        srlOrders = returnView.findViewById(R.id.srlOrders);
        rvOrders = returnView.findViewById(R.id.rvOrders);

        activeColor = context.getResources().getColor(R.color.colorBGProcessedOrder);
        deliveredColor = context.getResources().getColor(R.color.colorBGActiveOrder);
        cancelColor = context.getResources().getColor(R.color.colorBGCancelOrder);

        orderModelArrayList = new ArrayList<>();
        linearLayoutManager = new LinearLayoutManager(context);
        rvOrders.setLayoutManager(linearLayoutManager);
        myOrdersAdapter = new MyOrdersAdapter(context, orderModelArrayList, this);
        rvOrders.setAdapter(myOrdersAdapter);

        srlOrders.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlOrders.setRefreshing(false);
                getUserOrdersServerRequest();
            }
        });
        getUserOrdersServerRequest();

        return returnView;
    }

    private void getUserOrdersServerRequest() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "get_customer_orders");
            params.put("from", "app");
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 11, this, null, getString(R.string.wait)));
        }
    }

    public void cancelOrderServerRequest(OrderModel orderModel) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("module", "status_update_by_customer");
            params.put("from", "app");
            params.put("customer_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("order_id", orderModel.getOrderNum());
            params.put("status", "5");
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 15, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        switch (REQUEST_TYPE) {
            case 11:
                try {
                    orderModelArrayList.clear();
                    JSONArray ordersJArray = jsonObject.getJSONArray("in_process_orders_list");
                    populateOrdersData(ordersJArray);
                    ordersJArray = jsonObject.getJSONArray("delivered_orders_list");
                    populateOrdersData(ordersJArray);
                    ordersJArray = jsonObject.getJSONArray("cancelled_orders_list");
                    populateOrdersData(ordersJArray);
                    myOrdersAdapter.notifyDataSetChanged();
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getString(R.string.something_wrong), 1, 17);
                }
                break;
            case 15:
                try {
                    if (jsonObject.getString("result").equalsIgnoreCase("")) {
                        showToast("Order cancelled successfully.", 1, 17);
                        orderModelArrayList.get(myOrdersAdapter.clickedPosition).setStatus("Cancelled");
                        myOrdersAdapter.notifyDataSetChanged();
                    } else {
                        showToast(jsonObject.getString("error_msg"), 1, 17);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    showToast(getString(R.string.something_wrong), 1, 17);
                }
                break;
        }
    }

    public void populateOrdersData(JSONArray ordersJArray) {
        try {
            for (int i = 0; i < ordersJArray.length(); i++) {
                JSONObject ordersJObject = ordersJArray.getJSONObject(i);
                orderModel = new OrderModel();
                orderModel.setOrderNum(ordersJObject.getString("order_num"));
                orderModel.setOrderDateTime(ordersJObject.getString("order_date_time"));
                if (orderModel.getOrderDateTime().length() == 19) {//valid time date
                    orderModel.setOrderDate(orderModel.getOrderDateTime().substring(0, 10));
                    orderModel.setOrderTime(orderModel.getOrderDateTime().substring(11, 19));
                }
                orderModel.setCustomerName(ordersJObject.getString("customer_name"));
                orderModel.setCustomerId(ordersJObject.getString("customer_id"));
                orderModel.setAddress(ordersJObject.getString("address"));
                orderModel.setContact(ordersJObject.getString("contact"));
                orderModel.setReason(ordersJObject.getString("reason"));
                orderModel.setDelivery(ordersJObject.getString("delivery"));
                String status = ordersJObject.getString("status");
                orderModel.setStatus(status);
                switch (status) {
                    case "Deliverd":
                        orderModel.setStatusColor(deliveredColor);
                        break;
                    case "Cancel":
                        orderModel.setStatus("Cancelled");
                        orderModel.setStatusColor(cancelColor);
                        break;
                    case "Reject":
                        orderModel.setStatus("Rejected");
                        orderModel.setStatusColor(cancelColor);
                        break;
                    case "In Process":
                    case "Re-Scheduled":
                        orderModel.setStatusColor(activeColor);
                        break;
                }
                if (status.equalsIgnoreCase("Cancel")) {
                } else if (status.equalsIgnoreCase("Reject")) {
                }
                orderModel.setLat(ordersJObject.getString("lat"));
                orderModel.setLng(ordersJObject.getString("lng"));
                JSONArray productsJArray = ordersJObject.getJSONArray("products");
                orderProductModelArrayList = new ArrayList<>();
                for (int j = 0; j < productsJArray.length(); j++) {
                    JSONObject productJObject = productsJArray.getJSONObject(j);
                    orderProductModel = new OrderProductModel();
                    orderProductModel.setName(productJObject.getString("name"));
                    orderProductModel.setQty(productJObject.getString("qty"));
                    orderProductModel.setPrice(productJObject.getString("price"));
                    try {
                        orderProductModel.setAmount(String.format("%.1f", Double.parseDouble(orderProductModel.getQty()) *
                                Double.parseDouble(orderProductModel.getPrice())));
                    } catch (Exception e) {
                        e.printStackTrace();
                        orderProductModel.setAmount("0");
                    }
                    orderProductModelArrayList.add(orderProductModel);
                }
                orderModel.setProducts(orderProductModelArrayList);
                orderModelArrayList.add(orderModel);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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
