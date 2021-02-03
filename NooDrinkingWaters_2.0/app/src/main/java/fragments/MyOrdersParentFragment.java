package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.frandydevs.apps.noordrinkingwater.R;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link MyOrdersParentFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link MyOrdersParentFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MyOrdersParentFragment extends ParentFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public MyOrdersParentFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment MyOrdersParentFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static MyOrdersParentFragment newInstance(String param1, String param2) {
        MyOrdersParentFragment fragment = new MyOrdersParentFragment();
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

    ViewPager vpOrders;
    TabLayout tlOrders;
    MyOrdersFragmentsPagerAdapter myOrdersFragmentsPagerAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_my_orders_parent, container, false);

        tlOrders = returnView.findViewById(R.id.tlOrders);
        vpOrders = returnView.findViewById(R.id.vpOrders);

        myOrdersFragmentsPagerAdapter = new MyOrdersFragmentsPagerAdapter(getChildFragmentManager());
        vpOrders.setAdapter(myOrdersFragmentsPagerAdapter);
        tlOrders.setupWithViewPager(vpOrders);
        vpOrders.setOffscreenPageLimit(2);

        return returnView;
    }

    public class MyOrdersFragmentsPagerAdapter extends FragmentPagerAdapter {

        public MyOrdersFragmentsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public android.support.v4.app.Fragment getItem(int position) {
            /** Show a Fragment based on the position of the current screen */
            switch (position) {
                case 0:
                    return MyOrdersFragment.newInstance("", "");
                case 1:
                    return MyOrdersFragment.newInstance("", "");
                case 2:
                    return MyOrdersFragment.newInstance("", "");
            }
            return null;
        }

        @Override
        public int getCount() {
            return 3;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {
            switch (position) {
                case 0:
                    return "In Process";
                case 1:
                    return "Delivered";
                case 2:
                    return "Cancelled";
                default:
                    return "";
            }
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
