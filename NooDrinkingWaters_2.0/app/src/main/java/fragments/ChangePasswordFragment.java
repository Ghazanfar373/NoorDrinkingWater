package fragments;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.frandydevs.apps.noordrinkingwater.R;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_DELIVERY_DAYS;
import static app.AppConfig.PREF_USER_CUSTOMER_TYPE;
import static app.AppConfig.PREF_USER_EMAIL;
import static app.AppConfig.PREF_USER_ID;
import static app.AppConfig.PREF_USER_NAME;
import static app.AppConfig.PREF_USER_PHONE;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link ChangePasswordFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link ChangePasswordFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChangePasswordFragment extends ParentFragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public ChangePasswordFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ChangePasswordFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ChangePasswordFragment newInstance(String param1, String param2) {
        ChangePasswordFragment fragment = new ChangePasswordFragment();
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

    Button btnUpdatePassword;
    ImageView ivBG;
    EditText etOldPassword, etNewPassword, etReNewPassword;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_change_passwrod, container, false);

        ivBG = returnView.findViewById(R.id.ivBG);
        etOldPassword = returnView.findViewById(R.id.etOldPassword);
        etNewPassword = returnView.findViewById(R.id.etNewPassword);
        etReNewPassword = returnView.findViewById(R.id.etReNewPassword);
        btnUpdatePassword = returnView.findViewById(R.id.btnUpdatePassword);

        Picasso.get().load(R.drawable.bg_profile_illustration).into(ivBG);
        ivBG.setPadding(0, AppConfig.DEVICE_HEIGHT / 3, 0, 0);

        btnUpdatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String oldPass = etOldPassword.getText().toString().trim();
                String newPass = etNewPassword.getText().toString().trim();
                String confirmPass = etReNewPassword.getText().toString().trim();
                if (oldPass.isEmpty()) {
                    showToast("Please enter old password.", 1, 17);
                    return;
                }
                if (newPass.isEmpty()) {
                    showToast("Please enter new password.", 1, 17);
                    return;
                }
                if (!newPass.equalsIgnoreCase(confirmPass)) {
                    showToast("New password not matched.", 1, 17);
                    return;
                }
                changePasswordServerRequest(oldPass, newPass);
            }
        });
        return returnView;
    }

    private void changePasswordServerRequest(String oldPass, String newPass) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("customer_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "update_customer_password");
            params.put("from", "app");
            params.put("current_password", oldPass);
            params.put("new_password", newPass);
            params.put("confirm_new_password", newPass);
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 9, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                etOldPassword.setText("");
                etNewPassword.setText("");
                etReNewPassword.setText("");
                showToast("Password changed successfully.", 1, 17);
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
