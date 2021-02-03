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
import adapters.NotificationsAdapter;
import model.NotificationModel;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_USER_ID;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link NotificationsFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link NotificationsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotificationsFragment extends ParentFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public NotificationsFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NotificationsFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NotificationsFragment newInstance(String param1, String param2) {
        NotificationsFragment fragment = new NotificationsFragment();
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

    SwipeRefreshLayout srlNotifications;
    RecyclerView rvNotifications;
    ArrayList<NotificationModel> notificationModelArrayList;
    NotificationModel notificationModel;
    NotificationsAdapter notificationsAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_notifications, container, false);
        srlNotifications = returnView.findViewById(R.id.srlNotifications);
        rvNotifications = returnView.findViewById(R.id.rvNotifications);
        notificationModelArrayList = new ArrayList<>();

        linearLayoutManager = new LinearLayoutManager(context);
        rvNotifications.setLayoutManager(linearLayoutManager);
        notificationsAdapter = new NotificationsAdapter(context, notificationModelArrayList);
        rvNotifications.setAdapter(notificationsAdapter);

        srlNotifications.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                srlNotifications.setRefreshing(false);
                getNotificationsServerRequest();
            }
        });
        getNotificationsServerRequest();
        return returnView;
    }

    private void getNotificationsServerRequest() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "get_notification");
            params.put("from", "app");
            params.put("notification_type", "for_customer");
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 10, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                notificationModelArrayList.clear();
                JSONArray notificationsJArray = jsonObject.getJSONArray("notification");
                for (int i = 0; i < notificationsJArray.length(); i++) {
                    JSONObject notificationJObject = notificationsJArray.getJSONObject(i);
                    notificationModel = new NotificationModel();
                    notificationModel.setDataId(notificationJObject.getString("data_id"));
                    notificationModel.setNotification(notificationJObject.getString("notification"));
                    notificationModel.setStatus(notificationJObject.getString("status"));
                    notificationModel.setNotificationDate(notificationJObject.getString("notification_date"));
                    notificationModelArrayList.add(notificationModel);
                }
                notificationsAdapter.notifyDataSetChanged();
            } else {
                showToast(jsonObject.getString("error_msg"), 1, 17);
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
