package fragments;

import android.Manifest;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.media.MediaScannerConnection;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.frandydevs.apps.noordrinkingwater.BuildConfig;
import com.frandydevs.apps.noordrinkingwater.R;
import com.frandydevs.apps.noordrinkingwater.SuggestionComplaintActivity;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.MultiplePermissionsReport;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.karumi.dexter.listener.single.PermissionListener;
import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import adapters.MultiSelectionAdapter;
import app.AppConfig;
import id.zelory.compressor.Compressor;
import model.DaysSpinnerAdapterModel;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static app.AppConfig.IMAGE_DIRECTORY;
import static app.AppConfig.IMAGE_PROFILE_NAME;
import static app.AppConfig.PREF_DELIVERY_DAYS;
import static app.AppConfig.PREF_USER_CUSTOMER_TYPE;
import static app.AppConfig.PREF_USER_EMAIL;
import static app.AppConfig.PREF_USER_ID;
import static app.AppConfig.PREF_USER_NAME;
import static app.AppConfig.PREF_USER_PHONE;
import static app.AppConfig.PREF_USER_PROFILE_IMAGE;
import static com.frandydevs.apps.noordrinkingwater.ParentActivity.hideKeyboard;
import static helper.FilePath.getPath;

/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link BasicProfileFragment.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link BasicProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class BasicProfileFragment extends ParentFragment implements View.OnClickListener {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private OnFragmentInteractionListener mListener;

    public BasicProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment BasicProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static BasicProfileFragment newInstance(String param1, String param2) {
        BasicProfileFragment fragment = new BasicProfileFragment();
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

    ImageView ivBG, ivProfile, ivCam;
    Button btnSaveChanges;
    EditText etName, etPhone, etEmail;
    Spinner spCustomer, spDays;
    TextView tvCustomerType, tvDays;
    LinearLayout llDays, llCustomerTypes;
    DaysSpinnerAdapterModel daysSpinnerAdapterModel;
    MultiSelectionAdapter multiSelectionDialogAdapter;
    ArrayList<DaysSpinnerAdapterModel> daysSpinnerAdapterModelArrayList;
    boolean firstTime = true, imageChanged;

    private int GALLERY_IMAGE_PICKER_INTENT = 101, CAMERA_IMAGE_CAPTURE_INTENT = 102, CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 111;
    Uri cameraPhotoAttachmentRequestURI;
    Uri pickedImageUri;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        returnView = inflater.inflate(R.layout.fragment_basic_profile, container, false);
        ivBG = returnView.findViewById(R.id.ivBG);
        ivCam = returnView.findViewById(R.id.ivCam);
        etName = returnView.findViewById(R.id.etName);
        etPhone = returnView.findViewById(R.id.etPhone);
        ivProfile = returnView.findViewById(R.id.ivProfile);
        etEmail = returnView.findViewById(R.id.etEmail);
        llDays = returnView.findViewById(R.id.llDays);
        btnSaveChanges = returnView.findViewById(R.id.btnSaveChanges);
        spCustomer = returnView.findViewById(R.id.spCustomer);
//        spDays = returnView.findViewById(R.id.spDays);
        llCustomerTypes = returnView.findViewById(R.id.llCustomerTypes);
        tvCustomerType = returnView.findViewById(R.id.tvCustomerType);
        tvDays = returnView.findViewById(R.id.tvDays);

        etName.setText(dataSaving.getPrefValue(context, PREF_USER_NAME));
        etPhone.setText(dataSaving.getPrefValue(context, PREF_USER_PHONE));
        etEmail.setText(dataSaving.getPrefValue(context, PREF_USER_EMAIL));
        tvCustomerType.setText(dataSaving.getPrefValue(context, PREF_USER_CUSTOMER_TYPE));
        Picasso.get().load(R.drawable.ic_add_another_picture).into(ivCam);
        Picasso.get().load(R.drawable.bg_profile_illustration).into(ivBG);
        ivBG.setPadding(0, AppConfig.DEVICE_HEIGHT / 3, 0, 0);

        try {
            String imageUrl = dataSaving.getPrefValue(context, PREF_USER_PROFILE_IMAGE);
            Glide.with(context).load(imageUrl).apply(new RequestOptions().error(R.drawable.ic_user_profile_image)
                    .placeholder(R.drawable.ic_user_profile_image)).into(ivProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
        String daysPrefStr = dataSaving.getPrefValue(context, PREF_DELIVERY_DAYS);
        List<String> daysList = Arrays.asList(getResources().getStringArray(R.array.deliveryDays));
        daysSpinnerAdapterModelArrayList = new ArrayList<>();
        for (int i = 0; i < daysList.size(); i++) {
            daysSpinnerAdapterModel = new DaysSpinnerAdapterModel();
            switch (i) {
                case 0:
                    if (daysPrefStr.toLowerCase().contains("mon")) {
                        daysSpinnerAdapterModel.setChecked(true);
                    }
                    break;

                case 1:
                    if (daysPrefStr.toLowerCase().contains("tue")) {
                        daysSpinnerAdapterModel.setChecked(true);
                    }
                    break;

                case 2:
                    if (daysPrefStr.toLowerCase().contains("wed")) {
                        daysSpinnerAdapterModel.setChecked(true);
                    }
                    break;

                case 3:
                    if (daysPrefStr.toLowerCase().contains("thu")) {
                        daysSpinnerAdapterModel.setChecked(true);
                    }
                    break;

                case 4:
                    if (daysPrefStr.toLowerCase().contains("fri")) {
                        daysSpinnerAdapterModel.setChecked(true);
                    }
                    break;

                case 5:
                    if (daysPrefStr.toLowerCase().contains("sat")) {
                        daysSpinnerAdapterModel.setChecked(true);
                    }
                    break;

                case 6:
                    if (daysPrefStr.toLowerCase().contains("sun")) {
                        daysSpinnerAdapterModel.setChecked(true);
                    }
                    break;
            }
            daysSpinnerAdapterModel.setCheckAbleItemTitle(daysList.get(i));
            daysSpinnerAdapterModelArrayList.add(daysSpinnerAdapterModel);
        }
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < daysSpinnerAdapterModelArrayList.size(); i++) {
            daysSpinnerAdapterModel = daysSpinnerAdapterModelArrayList.get(i);
            if (daysSpinnerAdapterModel.isChecked()) {
                text.append(daysSpinnerAdapterModel.getCheckAbleItemTitle()).append(", ");
            }
        }
        if (text.length() > 1) {
            tvDays.setText(text.substring(0, text.length() - 2));
        } else {
            tvDays.setText("");
        }

        llDays.setOnClickListener(this);
        llCustomerTypes.setOnClickListener(this);
        ivCam.setOnClickListener(this);
        btnSaveChanges.setOnClickListener(this);

        spCustomer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!firstTime) {
                    tvCustomerType.setText(((TextView) view).getText().toString().trim());
                } else {
                    firstTime = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        getCustomerTypes();
        return returnView;
    }

    private void updateBasicUserInfoServerRequest(String name, String phone, String customerTypeStr) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("customer_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "update_customer_profile");
            params.put("from", "app");
            params.put("customer_name", name);
            params.put("contact_no", phone);
            params.put("customer_email", etEmail.getText().toString().trim());
            params.put("delivery_required", tvDays.getText().toString().trim());
            params.put("customer_group", customerTypeStr);
            if (pickedImageUri != null) {
                File file = new File(getPath(context, pickedImageUri));
                if (file != null) {
                    try {
                        File compressedImageFile = new Compressor(context).setQuality(30).compressToFile(file);
                        params.put("profile_image", compressedImageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 8, this, null, getString(R.string.wait)), false);
        }
    }

    private void getCustomerTypes() {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("customer_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("module", "customer_type");
            params.put("from", "app");
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 16, this, null, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        switch (REQUEST_TYPE) {
            case 8:
                try {
                    if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                        dataSaving.setPrefValue(context, PREF_USER_NAME, etName.getText().toString().trim());
                        dataSaving.setPrefValue(context, PREF_USER_PHONE, etPhone.getText().toString().trim());
                        dataSaving.setPrefValue(context, PREF_USER_EMAIL, etEmail.getText().toString().trim());
                        dataSaving.setPrefValue(context, PREF_USER_CUSTOMER_TYPE, tvCustomerType.getText().toString().trim());
                        dataSaving.setPrefValue(context, PREF_DELIVERY_DAYS, tvDays.getText().toString().trim());
                        dataSaving.setPrefValue(context, PREF_USER_PROFILE_IMAGE, jsonObject.getString("profile_image"));
                        mListener.updateUserInfoInDrawer();
                        showToast("Profile updated successfully.", 1, 17);
                    } else {
                        showToast(jsonObject.getString("error_msg"), 1, 17);
                    }
                } catch (Exception e) {
                    showToast(context.getString(R.string.something_wrong), Toast.LENGTH_LONG, Gravity.CENTER);
                    e.printStackTrace();
                }
                break;
            case 16:
                try {
                    JSONArray customerTypesJsonArray = jsonObject.getJSONArray("result");
                    int len = customerTypesJsonArray.length();
                    String[] customerTypes = new String[len];
                    for (int i = 0; i < len; i++) {
                        customerTypes[i] = (customerTypesJsonArray.get(i).toString());
                    }
                    ArrayAdapter<String> adapter = new ArrayAdapter(context, android.R.layout.simple_spinner_item, customerTypes);
                    adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                    spCustomer.setAdapter(adapter);
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }

    private void showDaysSelectionDialog(
            final ArrayList<DaysSpinnerAdapterModel> daysSpinnerAdapterModelArrayList) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_multi_selection_days);
        dialog.setCancelable(false);
        RecyclerView rvDays = dialog.findViewById(R.id.rvDays);
        final Button btnDone = dialog.findViewById(R.id.btnDone);
        ImageView ivClose = dialog.findViewById(R.id.ivClose);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        /*DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(context,
                linearLayoutManager.getOrientation());
        rvDays.addItemDecoration(dividerItemDecoration);*/
        rvDays.setLayoutManager(linearLayoutManager);
        multiSelectionDialogAdapter = new MultiSelectionAdapter(context, daysSpinnerAdapterModelArrayList);
        rvDays.setAdapter(multiSelectionDialogAdapter);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnDone.performClick();
            }
        });
        btnDone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < daysSpinnerAdapterModelArrayList.size(); i++) {
                    daysSpinnerAdapterModel = daysSpinnerAdapterModelArrayList.get(i);
                    if (daysSpinnerAdapterModel.isChecked()) {
                        text.append(daysSpinnerAdapterModel.getCheckAbleItemTitle()).append(", ");
                    }
                }
                if (text.length() > 1) {
                    tvDays.setText(text.substring(0, text.length() - 2));
                } else {
                    tvDays.setText("");
                }
                dialog.dismiss();
            }
        });
        dialog.show();
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
            case R.id.llDays:
//                spDays.performClick();
                showDaysSelectionDialog(daysSpinnerAdapterModelArrayList);
                break;
            case R.id.llCustomerTypes:
                spCustomer.performClick();
                break;
            case R.id.ivCam:
                requestPermissions(new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"},
                        CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE);
                break;
            case R.id.btnSaveChanges:
                hideKeyboard(getActivity(), false, null);
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                if (name.isEmpty()) {
                    showToast("Please enter name.", 1, 17);
                    return;
                }
                if (phone.isEmpty()) {
                    showToast("Please enter contact no.", 1, 17);
                    return;
                }
                String customerTypeStr = tvCustomerType.getText().toString().trim();
                if (customerTypeStr.isEmpty()) {
                    showToast("Please select customer type.", 1, 17);
                    return;
                }
                if (imageChanged) {
//                    BitmapDrawable draw = (BitmapDrawable) ivProfile.getDrawable();
//                    Bitmap bitmap = draw.getBitmap();
//                    saveImage(bitmap);
//                    mListener.updateUserInfoInDrawer();
                }
                updateBasicUserInfoServerRequest(name, phone, customerTypeStr);
                break;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE && grantResults[0] == PackageManager.PERMISSION_GRANTED &&
                grantResults[1] == PackageManager.PERMISSION_GRANTED) {
            showImagePickerDialog();
        }
    }

    private void showImagePickerDialog() {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_photo_picker);
        final Button btnCamera = dialog.findViewById(R.id.btnCamera);
        final Button btnGallery = dialog.findViewById(R.id.btnGallery);
        btnCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                captureNewPhoto();
            }
        });
        btnGallery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                pickFromGallery();
            }
        });
        final ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    public void pickFromGallery() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, GALLERY_IMAGE_PICKER_INTENT);
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    showToast("Storage Permission Required!", 1, 17);
                }
                requestPermissions(new String[]{android.Manifest.permission.WRITE_EXTERNAL_STORAGE}, GALLERY_IMAGE_PICKER_INTENT);
            }
        } else {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, GALLERY_IMAGE_PICKER_INTENT);
        }
    }

    private void captureNewPhoto() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (ContextCompat.checkSelfPermission(context, android.Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                File file = getOutputMediaFile(1);
                Uri fileUri = null;
                if (android.os.Build.VERSION.SDK_INT >= 24) {
                    fileUri = FileProvider.getUriForFile(context, BuildConfig.APPLICATION_ID + ".provider", file);
                } else {
                    fileUri = Uri.fromFile(file); // create
                }
                cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileUri); // set the image file
                cameraPhotoAttachmentRequestURI = Uri.fromFile(file); // create
                startActivityForResult(cameraIntent, CAMERA_IMAGE_CAPTURE_INTENT);
            } else {
                if (shouldShowRequestPermissionRationale(android.Manifest.permission.CAMERA)) {
                    showToast("Camera Permission Required!", 1, 17);
                }
                requestPermissions(new String[]{android.Manifest.permission.CAMERA}, CAMERA_IMAGE_CAPTURE_INTENT);
            }
        } else {
            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            File file = getOutputMediaFile(1);
            cameraPhotoAttachmentRequestURI = Uri.fromFile(file); // create
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, cameraPhotoAttachmentRequestURI); // set the image file
            startActivityForResult(cameraIntent, CAMERA_IMAGE_CAPTURE_INTENT);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == SuggestionComplaintActivity.RESULT_CANCELED) {
            return;
        }
        if (requestCode == GALLERY_IMAGE_PICKER_INTENT) {
            try {
                pickedImageUri = data.getData();
                ivProfile.setImageResource(0);
                imageChanged = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (requestCode == CAMERA_IMAGE_CAPTURE_INTENT) {
            try {
                pickedImageUri = cameraPhotoAttachmentRequestURI;
                ivProfile.setImageResource(0);
                imageChanged = true;
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        try {
            Glide.with(context).load(getPath(context, pickedImageUri)).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)).into(ivProfile);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public String saveImage(Bitmap myBitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        myBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bytes);
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);
        // have the object build the directory structure, if needed.
        if (!mediaStorageDir.exists()) {
            mediaStorageDir.mkdirs();
        }

        try {
            File f = new File(mediaStorageDir, IMAGE_PROFILE_NAME);
            if (f.exists()) {
                f.delete();
            }
            f.createNewFile();
            FileOutputStream fo = new FileOutputStream(f);
            fo.write(bytes.toByteArray());
            MediaScannerConnection.scanFile(getActivity(),
                    new String[]{f.getPath()},
                    new String[]{"image/jpeg"}, null);
            fo.close();

            return f.getAbsolutePath();
        } catch (IOException e1) {
            e1.printStackTrace();
        }
        return "";
    }

    void saveImageToFolder(Uri uri) {
        String sourceFilename = uri.getPath();
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
            }
        }
        // Create a media file name
        File mediaFile;
        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                IMAGE_PROFILE_NAME);
        if (mediaFile.exists()) {
            mediaFile.delete();
        }
        try {
            mediaFile.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

        BufferedInputStream bis = null;
        BufferedOutputStream bos = null;

        try {
            bis = new BufferedInputStream(new FileInputStream(sourceFilename));
            bos = new BufferedOutputStream(new FileOutputStream(mediaFile, false));
            byte[] buf = new byte[1024];
            bis.read(buf);
            do {
                bos.write(buf);
            } while (bis.read(buf) != -1);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (bis != null) bis.close();
                if (bos != null) bos.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private String getStringImage(Bitmap lastBitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        lastBitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private static File getOutputMediaFile(int type) {
        // To be safe, you should check that the SDCard is mounted
        // using Environment.getExternalStorageState() before doing this.

        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(
                Environment.DIRECTORY_PICTURES), IMAGE_DIRECTORY);
        // This location works best if you want the created images to be shared
        // between applications and persist after your app has been uninstalled.

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator + IMAGE_PROFILE_NAME);
            if (mediaFile.exists()) {
                mediaFile.delete();
            }
            try {
                mediaFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (type == MEDIA_TYPE_VIDEO) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "VID_" + timeStamp + ".mp4");
        } else {
            return null;
        }

        return mediaFile;
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

        void updateUserInfoInDrawer();
    }
}
