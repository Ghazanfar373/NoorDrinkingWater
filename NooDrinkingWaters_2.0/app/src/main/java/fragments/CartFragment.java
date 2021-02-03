package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.CartAndOrderProcessActivity;
import com.frandydevs.apps.noordrinkingwater.ParentActivity;
import com.frandydevs.apps.noordrinkingwater.R;

import java.util.ArrayList;

import adapters.CartItemsAdapter;
import db.SQLiteDatabaseHelper;
import model.CartProductModel;
import model.OrderModel;
import model.OrderProductModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link CartFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link CartFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class CartFragment extends ParentFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public CartFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment CartFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static CartFragment newInstance(String param1, String param2) {
        CartFragment fragment = new CartFragment();
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

    RecyclerView rvCartItems;
    ArrayList<CartProductModel> cartProductModelArrayList;
    CartProductModel cartProductModel;
    CartItemsAdapter cartItemsAdapter;
    TextView tvTotalAmount;
    LinearLayout llDeliverySchedule;
    double totalAmount;

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_cart, container, false);

        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(context);

        rvCartItems = returnView.findViewById(R.id.rvCartItems);
        tvTotalAmount = returnView.findViewById(R.id.tvTotalAmount);
        llDeliverySchedule = returnView.findViewById(R.id.llDeliverySchedule);
        cartProductModelArrayList = sqLiteDatabaseHelper.getCartProducts();

        linearLayoutManager = new LinearLayoutManager(context);
        rvCartItems.setLayoutManager(linearLayoutManager);
        cartItemsAdapter = new CartItemsAdapter(context, cartProductModelArrayList, sqLiteDatabaseHelper, this);
        rvCartItems.setAdapter(cartItemsAdapter);
        setTotalAmount();
        llDeliverySchedule.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (cartProductModelArrayList.isEmpty()) {
                    showToast("Please add some items.", 1, 17);
                    return;
                }
                mListener.setTotalAmount(totalAmount);
                mListener.nextStep();
            }
        });
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

    public void setTotalAmount() {
        totalAmount = 0;
        for (int i = 0; i < cartProductModelArrayList.size(); i++) {
            totalAmount = totalAmount + cartProductModelArrayList.get(i).getTotalAmount();
        }
        tvTotalAmount.setText("Subtotal : " + String.valueOf(totalAmount) + " AED");
    }

    public void updateTotalAmountAfterRemovingItem(double amount) {
        totalAmount = totalAmount - amount;
        tvTotalAmount.setText("Subtotal : " + String.valueOf(totalAmount) + " AED");
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

        void setTotalAmount(double totalAmount);

        void nextStep();
    }
}
