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

import org.json.JSONObject;

import java.util.ArrayList;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import adapters.NewOffersAdapter;
import model.NewOfferModel;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NewOffersFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NewOffersFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NewOffersFragment extends ParentFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NewOffersFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NewOffersFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NewOffersFragment newInstance(String param1, String param2) {
        NewOffersFragment fragment = new NewOffersFragment();
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

    SwipeRefreshLayout srlNewOffers;
    RecyclerView rvNewOffers;
    ArrayList<NewOfferModel> newOfferModelArrayList;
    NewOfferModel newOfferModel;
    NewOffersAdapter newOffersAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_new_offers, container, false);

        srlNewOffers = returnView.findViewById(R.id.srlNewOffers);
        rvNewOffers = returnView.findViewById(R.id.rvNewOffers);
        /*newOfferModelArrayList = new ArrayList<>();

        for (int i = 0; i < 10; i++) {
            newOfferModel = new NewOfferModel();
            newOfferModelArrayList.add(newOfferModel);
        }

        linearLayoutManager = new LinearLayoutManager(context);
        rvNewOffers.setLayoutManager(linearLayoutManager);
        newOffersAdapter = new NewOffersAdapter(context, newOfferModelArrayList);
        rvNewOffers.setAdapter(newOffersAdapter);

        srlNewOffers.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlNewOffers.setRefreshing(false);
                getNewOffersServerRequest();
            }
        });
        getNewOffersServerRequest();*/

        return returnView;
    }

    private void getNewOffersServerRequest() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
//            params.put("module", "get_customer_orders");
            params.put("from", "app");
//            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 12, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {

        } catch (Exception e) {
            e.printStackTrace();
            showToast(getString(R.string.something_wrong), 1, 17);
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
