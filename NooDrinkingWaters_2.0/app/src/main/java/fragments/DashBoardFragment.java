package fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.frandydevs.apps.noordrinkingwater.ContactUsActivity;
import com.frandydevs.apps.noordrinkingwater.R;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;

import java.util.List;

import adapters.MainSliderAdapter;
import app.AppConfig;
import ss.com.bannerslider.Slider;

import static com.frandydevs.apps.noordrinkingwater.ParentActivity.hideKeyboard;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link DashBoardFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link DashBoardFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DashBoardFragment extends ParentFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public DashBoardFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DashBoardFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DashBoardFragment newInstance(String param1, String param2) {
        DashBoardFragment fragment = new DashBoardFragment();
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

    View vTop;
    LinearLayout llSearch, llProducts, llNewOffers, llMyOrders, llNotifications, llMyProfile, llContactUs;
    RelativeLayout rlBanner;
    Slider sBanners;
    EditText etSearch;
    TextView tvViewNewProducts;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_dash_board, container, false);
        vTop = returnView.findViewById(R.id.vTop);
        llSearch = returnView.findViewById(R.id.llSearch);
        etSearch = returnView.findViewById(R.id.etSearch);
        tvViewNewProducts = returnView.findViewById(R.id.tvViewNewProducts);
        rlBanner = returnView.findViewById(R.id.rlBanner);
        llProducts = returnView.findViewById(R.id.llProducts);
        llNewOffers = returnView.findViewById(R.id.llNewOffers);
        llMyOrders = returnView.findViewById(R.id.llMyOrders);
        llNotifications = returnView.findViewById(R.id.llNotifications);
        llMyProfile = returnView.findViewById(R.id.llMyProfile);
        llContactUs = returnView.findViewById(R.id.llContactUs);

        rlParams = (RelativeLayout.LayoutParams) rlBanner.getLayoutParams();
        rlParams.height = AppConfig.DEVICE_HEIGHT / 4;
        rlBanner.setLayoutParams(rlParams);

        llSearch.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                llSearch.getViewTreeObserver().removeOnGlobalLayoutListener(this);

                int height = llSearch.getHeight();

                rlParams = (RelativeLayout.LayoutParams) vTop.getLayoutParams();
                rlParams.height = (int) (height / 2 + context.getResources().getDimension(R.dimen.dp10));
                vTop.setLayoutParams(rlParams);
            }
        });

        etSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_GO) {
                    hideKeyboard(getActivity(), false, null);
                    mListener.setProductsSearchQuery(etSearch.getText().toString().trim());
                    llProducts.performClick();
                    return true;
                }
                return false;
            }
        });
        tvViewNewProducts.setOnClickListener(this);
        llProducts.setOnClickListener(this);
        llNewOffers.setOnClickListener(this);
        llMyOrders.setOnClickListener(this);
        llNotifications.setOnClickListener(this);
        llMyProfile.setOnClickListener(this);
        llContactUs.setOnClickListener(this);

        sBanners = returnView.findViewById(R.id.sBanners);
        sBanners.setAdapter(new MainSliderAdapter());
        sBanners.setSelectedSlide(2);

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
            case R.id.llContactUs:
                showContactUsModeDialog();
                break;
            case R.id.tvViewNewProducts:
                llProducts.performClick();
                break;
            case R.id.llProducts:
                mListener.onDashBoardMenuClick("products");
                break;
            case R.id.llNewOffers:
                mListener.onDashBoardMenuClick("newOffers");
                break;
            case R.id.llMyOrders:
                mListener.onDashBoardMenuClick("myOrders");
                break;
            case R.id.llNotifications:
                mListener.onDashBoardMenuClick("notifications");
                break;
            case R.id.llMyProfile:
                mListener.onDashBoardMenuClick("myProfile");
                break;
        }
    }

    private void showContactUsModeDialog() {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_contact_us_mode);
        dialog.setCancelable(false);
        final LinearLayout llViaPhone = dialog.findViewById(R.id.llViaPhone);
        final LinearLayout llViaEmail = dialog.findViewById(R.id.llViaEmail);
        final ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        llViaPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Dexter.withActivity(getActivity())
                        .withPermissions(Manifest.permission.CALL_PHONE)
                        .withListener(new MultiplePermissionsListener() {
                            @Override
                            public void onPermissionsChecked(MultiplePermissionsReport report) {
                                if (report.areAllPermissionsGranted()) {
                                    dialog.dismiss();
                                    Intent intent = new Intent(Intent.ACTION_CALL);
                                    intent.setData(Uri.parse("tel:+971067481801"));
                                    startActivity(intent);
                                }
                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(List<PermissionRequest> permissions, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        llViaEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                Intent contactUsIntent = new Intent(context, ContactUsActivity.class);
                startActivity(contactUsIntent);
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

        void onDashBoardMenuClick(String menu);

        void setProductsSearchQuery(String query);
    }
}
