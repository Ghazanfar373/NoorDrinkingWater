package fragments;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.CartAndOrderProcessActivity;
import com.frandydevs.apps.noordrinkingwater.ParentActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;
import db.SQLiteDatabaseHelper;
import model.CartProductModel;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_USER_ID;
import static app.AppConfig.PREF_USER_NAME;
import static app.AppConfig.PREF_USER_PHONE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link OrderPreviewFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link OrderPreviewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class OrderPreviewFragment extends ParentFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public OrderPreviewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment OrderPreviewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static OrderPreviewFragment newInstance(String param1, String param2) {
        OrderPreviewFragment fragment = new OrderPreviewFragment();
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

    Button btnPlaceOrder;
    TextView tvOrderNo, tvDate, tvTime, tvName, tvPhoneNo, tvDeliveryTime, tvDeliveryDate, tvDeliveryMethod, tvItemDescription, tvAddress;
    String deliveryTime, deliveryDate, paymentMethod, deliveryAddress;
    ArrayList<CartProductModel> cartProductModelArrayList;
    CartProductModel cartProductModel;
    CartAndOrderProcessActivity cartAndOrderProcessActivity;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_order_preview, container, false);
        cartAndOrderProcessActivity = (CartAndOrderProcessActivity) context;

        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(context);
        tvOrderNo = returnView.findViewById(R.id.tvOrderNo);
        tvDate = returnView.findViewById(R.id.tvDate);
        tvTime = returnView.findViewById(R.id.tvTime);
        tvName = returnView.findViewById(R.id.tvName);
        tvPhoneNo = returnView.findViewById(R.id.tvPhoneNo);
        tvDeliveryTime = returnView.findViewById(R.id.tvDeliveryTime);
        tvDeliveryDate = returnView.findViewById(R.id.tvDeliveryDate);
        tvDeliveryMethod = returnView.findViewById(R.id.tvDeliveryMethod);
        tvItemDescription = returnView.findViewById(R.id.tvItemDescription);
        tvAddress = returnView.findViewById(R.id.tvAddress);
        btnPlaceOrder = returnView.findViewById(R.id.btnPlaceOrder);

        deliveryTime = mListener.getDeliveryTime();
        deliveryDate = mListener.getDeliveryDate();
        deliveryAddress = mListener.getDeliveryAddress();

        tvName.setText(dataSaving.getPrefValue(context, PREF_USER_NAME));
        tvPhoneNo.setText(dataSaving.getPrefValue(context, PREF_USER_PHONE));
        paymentMethod = mListener.getPaymentMethod();

        Calendar calendar = Calendar.getInstance();
        int hours = calendar.get(Calendar.HOUR);
        int minutes = calendar.get(Calendar.MINUTE);
        int dayCount = calendar.get(Calendar.DATE);
        int monthCount = calendar.get(Calendar.MONTH) + 1;
        int yearCount = calendar.get(Calendar.YEAR);
        int amPMDigit = calendar.get(Calendar.AM_PM);

        tvDate.setText(yearCount + "-" + DeliveryScheduleFragment.numberFormat.format(Double.valueOf(monthCount)) + "-"
                + DeliveryScheduleFragment.numberFormat.format(Double.valueOf(dayCount)));

        String amPM = "AM";
        if (amPMDigit == 1) {
            amPM = "PM";
        }
        tvTime.setText(DeliveryScheduleFragment.numberFormat.format(Double.valueOf(hours)) + ":" +
                DeliveryScheduleFragment.numberFormat.format(Double.valueOf(minutes)) + " " + amPM);

        tvDeliveryTime.setText(deliveryTime);
        tvDeliveryDate.setText(deliveryDate);
        tvDeliveryMethod.setText(paymentMethod);
        tvAddress.setText(deliveryAddress);

        cartProductModelArrayList = sqLiteDatabaseHelper.getCartProducts();
        String items = "";
        for (int i = 0; i < cartProductModelArrayList.size(); i++) {
            cartProductModel = cartProductModelArrayList.get(i);
            items = items + cartProductModel.getName() + " (" + cartProductModel.getQty() + ")\n";
        }
        if (!items.isEmpty()) {
            items = items.substring(0, items.length() - 1);
        }
        tvItemDescription.setText(items);
        btnPlaceOrder.setOnClickListener(this);
        return returnView;
    }

    public void updateDeliveryMethod() {
        paymentMethod = mListener.getPaymentMethod();
        tvDeliveryMethod.setText(paymentMethod);
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnPlaceOrder:
                placeOrderServerRequest();
                break;
        }
    }

    private void placeOrderServerRequest() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "place_order");
            params.put("from", "app");
            params.put("note", "");

            JSONObject dataObj = new JSONObject();
            JSONArray cartItemsArray = new JSONArray();
            JSONObject cartItemsObject;

            for (int i = 0; i < cartProductModelArrayList.size(); i++) {
                cartProductModel = cartProductModelArrayList.get(i);
                try {
                    cartItemsObject = new JSONObject();
                    cartItemsObject.putOpt("ProductID", cartProductModel.getId());
                    cartItemsObject.putOpt("Product_Name", cartProductModel.getName());
                    cartItemsObject.putOpt("ProductQuantity", cartProductModel.getQty());
                    cartItemsObject.putOpt("TotalAmount", cartProductModel.getTotalAmount());
                    cartItemsArray.put(cartItemsObject);
                } catch (JSONException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            try {
                dataObj.put("cart", cartItemsArray);
            } catch (JSONException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            params.put("cart", dataObj.toString());
            params.put("delivery_time", deliveryTime);
            params.put("delivery_date", deliveryDate);
            params.put("payment_method", paymentMethod);
            params.put("address", deliveryAddress);
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 12, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                //show success dialog
                sqLiteDatabaseHelper.clearCart();
                showOrderPlacedDialog();
//                showToast("Order placed successfully.", 1, 17);
            } else {
                showToast(jsonObject.getString("error_msg"), 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showOrderPlacedDialog() {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog_FullScreen);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_order_placed_message);
        dialog.setCancelable(false);
        final LinearLayout llRateUs = dialog.findViewById(R.id.llRateUs);
        final Button btnOk = dialog.findViewById(R.id.btnOk);
        final TextView tvOrderNo = dialog.findViewById(R.id.tvOrderNo);
        final TextView tvDeliveryDate = dialog.findViewById(R.id.tvDeliveryDate);
        final TextView tvDeliveryTime = dialog.findViewById(R.id.tvDeliveryTime);
        final TextView tvPaymentMethod = dialog.findViewById(R.id.tvPaymentMethod);
        final TextView tvTotalAmount = dialog.findViewById(R.id.tvTotalAmount);
        final ImageView ivSuccess = dialog.findViewById(R.id.ivSuccess);

        tvDeliveryDate.setText(deliveryDate);
        tvDeliveryTime.setText(deliveryTime);
        tvPaymentMethod.setText(paymentMethod);
        tvTotalAmount.setText(String.valueOf(mListener.getTotalAmount()));
        Picasso.get().load(R.drawable.bg_congratulation_illustration).into(ivSuccess);

        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOk.performClick();
            }
        });
        btnOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                cartAndOrderProcessActivity.setResult(Activity.RESULT_OK);
                cartAndOrderProcessActivity.finish();
            }
        });
        llRateUs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnOk.performClick();
                ((ParentActivity) context).rateApp();
                /*Intent rateUsIntent = new Intent(context, RateUsActivity.class);
                startActivity(rateUsIntent);*/
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

        String getDeliveryDate();

        String getDeliveryTime();

        String getPaymentMethod();

        String getDeliveryAddress();

        double getTotalAmount();
    }
}
