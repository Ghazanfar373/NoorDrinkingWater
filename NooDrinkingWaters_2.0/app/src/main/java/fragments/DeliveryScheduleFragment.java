package fragments;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.DatePicker;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Calendar;

import adapters.CartItemsSummaryAdapter;
import db.SQLiteDatabaseHelper;
import model.CartProductModel;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DeliveryScheduleFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DeliveryScheduleFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DeliveryScheduleFragment extends ParentFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DeliveryScheduleFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DeliveryScheduleFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DeliveryScheduleFragment newInstance(String param1, String param2) {
        DeliveryScheduleFragment fragment = new DeliveryScheduleFragment();
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
    CartItemsSummaryAdapter cartItemsSummaryAdapter;
    TextView btnConfirmAddress;
    LinearLayout llDeliveryDate;

    RadioGroup rgDeliveryTime;

    static TextView tvDeliveryDate;
    public static NumberFormat numberFormat = new DecimalFormat("00");
    static int dayCount, monthCount, yearCount;
    static int dateType; //0 for tvDeliveryDate

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_delivery_schedule, container, false);

        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(context);

        tvDeliveryDate = returnView.findViewById(R.id.tvDeliveryDate);
        rvCartItems = returnView.findViewById(R.id.rvCartItems);
        btnConfirmAddress = returnView.findViewById(R.id.btnConfirmAddress);
        llDeliveryDate = returnView.findViewById(R.id.llDeliveryDate);
        rgDeliveryTime = returnView.findViewById(R.id.rgDeliveryTime);

        cartProductModelArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(context);
        rvCartItems.setLayoutManager(linearLayoutManager);
        cartItemsSummaryAdapter = new CartItemsSummaryAdapter(context, cartProductModelArrayList);
        rvCartItems.setAdapter(cartItemsSummaryAdapter);

        llDeliveryDate.setOnClickListener(this);
        btnConfirmAddress.setOnClickListener(this);

        return returnView;
    }

    public void refreshViews() {
        //need to refresh on selection of this view pager item so that effect of cart can be effect on this page too.
        //because of viewpager is loading both at same time
        cartProductModelArrayList.clear();
        cartProductModelArrayList.addAll(sqLiteDatabaseHelper.getCartProducts());
        cartItemsSummaryAdapter.notifyDataSetChanged();

        Calendar calendar = Calendar.getInstance();
        dayCount = calendar.get(Calendar.DATE);
        monthCount = calendar.get(Calendar.MONTH) + 1;
        yearCount = calendar.get(Calendar.YEAR);
        dateType = 0;
        updateDateView(yearCount, monthCount, dayCount);
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
            case R.id.llDeliveryDate:
                showDatePicker();
                break;
            case R.id.btnConfirmAddress:
                String time = ((RadioButton) returnView.findViewById(rgDeliveryTime.getCheckedRadioButtonId())).getText().toString().trim();
                mListener.setDeliveryDateAndTime(tvDeliveryDate.getText().toString().trim(), time);
                break;
        }
    }

    public void showDatePicker() {
        DialogFragment datePickerFragment = new DatePickerFragment();
        Bundle bundle = new Bundle();
        switch (dateType) {
            case 0:
                bundle.putInt("day", dayCount);
                bundle.putInt("month", monthCount);
                bundle.putInt("year", yearCount);
                break;
        }

        datePickerFragment.setArguments(bundle);
        datePickerFragment.show(getActivity().getSupportFragmentManager(), "datePickerFragment");
    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            int year = getArguments().getInt("year");
            int month = getArguments().getInt("month") - 1;
            int day = getArguments().getInt("day");
            Calendar calendar = Calendar.getInstance();

            DatePickerDialog datePickerDialog = new DatePickerDialog(getActivity(), dateSetListener, year, month, day);
            datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            return datePickerDialog;
        }

        private DatePickerDialog.OnDateSetListener dateSetListener =
                new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker view, int year, int month, int day) {
                        updateDateView(year, month + 1, day);
                    }
                };
    }

    public static void updateDateView(int year, int month, int day) {
        switch (dateType) {
            case 0:
                dayCount = day;
                monthCount = month;
                yearCount = year;
                tvDeliveryDate.setText(yearCount + "-" + numberFormat.format(Double.valueOf(month)) + "-"
                        + numberFormat.format(Double.valueOf(day)));
                break;
        }
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

        void setDeliveryDateAndTime(String date, String time);
    }
}
