package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.R;
import com.squareup.picasso.Picasso;

import app.AppConfig;
import helper.WidthDependentSquareLayout;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ModeOfPaymentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ModeOfPaymentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ModeOfPaymentFragment extends ParentFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ModeOfPaymentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ModeOfPaymentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ModeOfPaymentFragment newInstance(String param1, String param2) {
        ModeOfPaymentFragment fragment = new ModeOfPaymentFragment();
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

    ImageView ivCOD, ivCC, ivCP, ivPC, ivBG;
    WidthDependentSquareLayout slCOD, slCC, slCP, slPC, previousSelected, currentSelected;
    TextView btnViewInvoice, tvHeader;
    String paymentMethod = "";

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_mode_of_payment, container, false);

        btnViewInvoice = returnView.findViewById(R.id.btnViewInvoice);
        ivBG = returnView.findViewById(R.id.ivBG);
        tvHeader = returnView.findViewById(R.id.tvHeader);
        slCOD = returnView.findViewById(R.id.slCOD);
        slCC = returnView.findViewById(R.id.slCC);
        slCP = returnView.findViewById(R.id.slCP);
        slPC = returnView.findViewById(R.id.slPC);
        ivCOD = returnView.findViewById(R.id.ivCOD);
        ivCC = returnView.findViewById(R.id.ivCC);
        ivCP = returnView.findViewById(R.id.ivCP);
        ivPC = returnView.findViewById(R.id.ivPC);

        Picasso.get().load(R.drawable.bg_payment_illustration).into(ivBG);
        Picasso.get().load(R.drawable.ic_cash_on_delivery).into(ivCOD);
        Picasso.get().load(R.drawable.ic_credit_card).into(ivCC);
        Picasso.get().load(R.drawable.ic_credit_purchase).into(ivCP);
        Picasso.get().load(R.drawable.ic_preparid_coupon).into(ivPC);
        ivBG.setPadding(0, AppConfig.DEVICE_HEIGHT / 3, 0, 0);
        tvHeader.setText(getString(R.string.authorize_noor_water_with) + " " + mListener.getTotalAmount() + " AED");

        slCOD.setOnClickListener(this);
        slCC.setOnClickListener(this);
        slCP.setOnClickListener(this);
        slPC.setOnClickListener(this);
        btnViewInvoice.setOnClickListener(this);

        paymentMethod = getString(R.string.cash_on_delivery);
        currentSelected = previousSelected = slCOD;

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

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.slCOD:
                paymentMethod = getString(R.string.cash_on_delivery);
                setPaymentModeSelected(slCOD);
                break;
            case R.id.slPC:
                showToast(getString(R.string.coming_soon), 1, 17);
//                setPaymentModeSelected(slPC);
                break;
            case R.id.slCC:
                showToast(getString(R.string.coming_soon), 1, 17);
//                setPaymentModeSelected(slCC);
                break;
            case R.id.slCP:
                paymentMethod = getString(R.string.credit_purchase);
                setPaymentModeSelected(slCP);
                break;
            case R.id.btnViewInvoice:
                mListener.setPaymentMethod(paymentMethod);
                break;
        }
    }

    private void setPaymentModeSelected(WidthDependentSquareLayout widthDependentSquareLayout) {
        currentSelected.setBackground(getResources().getDrawable(R.drawable.draw_bg_et));
        widthDependentSquareLayout.setBackground(getResources().getDrawable(R.drawable.draw_bg_selected_mode_of_payment));
        currentSelected = widthDependentSquareLayout;
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

        void setPaymentMethod(String paymentMethod);

        double getTotalAmount();
    }
}
