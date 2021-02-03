package com.frandydevs.apps.noordrinkingwater;

import android.Manifest;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
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

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;
import id.zelory.compressor.Compressor;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_VIDEO;
import static app.AppConfig.PREF_USER_ID;
import static helper.FilePath.getPath;

public class SuggestionComplaintActivity extends ParentActivity implements View.OnClickListener {

    TextView tvHeader, tvAttachment;
    String viewType = "Suggestion";
    ImageView ivBG, ivAttachment, ivAttachedPhoto, ivRemoveAttachment;
    Spinner spSubjects;
    Button btnSend;
    EditText etMessage;
    LinearLayout llAttachment;
    RelativeLayout rlAttachedPhoto;

    public static final String IMAGE_DIRECTORY = "NoorDrinkingWaterTempFolder";
    private int GALLERY_IMAGE_PICKER_INTENT = 101, CAMERA_IMAGE_CAPTURE_INTENT = 102, CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE = 111;

    Uri cameraPhotoAttachmentRequestURI;
    Uri pickedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_suggestion_complaint);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        extras = getIntent().getExtras();
        tvHeader = findViewById(R.id.tvHeader);
        spSubjects = findViewById(R.id.spSubjects);
        btnSend = findViewById(R.id.btnSend);
        ivBG = findViewById(R.id.ivBG);
        etMessage = findViewById(R.id.etMessage);
        llAttachment = findViewById(R.id.llAttachment);
        ivAttachment = findViewById(R.id.ivAttachment);
        tvAttachment = findViewById(R.id.tvAttachment);
        ivRemoveAttachment = findViewById(R.id.ivRemoveAttachment);
        ivAttachedPhoto = findViewById(R.id.ivAttachedPhoto);
        rlAttachedPhoto = findViewById(R.id.rlAttachedPhoto);

        viewType = extras.getString("viewType", viewType);
        if (viewType.equalsIgnoreCase("Complaint")) {
            tvHeader.setText(R.string.write_us_your_complaint);
//            llAttachment.setVisibility(View.GONE);
            etMessage.setHint(R.string.type_your_complaint);
        }
        getSupportActionBar().setTitle(viewType);
        Picasso.get().load(R.drawable.bg_suggestion_illustration).into(ivBG);
        ivBG.setPadding(0, AppConfig.DEVICE_HEIGHT / 3, 0, 0);

        btnSend.setOnClickListener(this);
        llAttachment.setOnClickListener(this);
        ivRemoveAttachment.setOnClickListener(this);
    }

    private void suggestionComplaintServerRequest(String message) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("from", "app");
            params.put("subject", viewType.toLowerCase());
            params.put("msg", message);
            if (pickedImageUri != null) {
                File file = new File(getPath(context, pickedImageUri));
                if (file != null) {
                    try {
                        File compressedImageFile = new Compressor(context).setQuality(30).compressToFile(file);
                        params.put("attachment", compressedImageFile);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            params.put("module", "feed_back");
            params.put("type", spSubjects.getSelectedItem().toString());
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 6, null, this, getString(R.string.wait)), false);
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                if (viewType.equalsIgnoreCase("Suggestion")) {
                    showMessageDialog("Thanks for Your Suggestion.", "", "Welcome", "", "finishSuggestion", "");
                } else {
                    showMessageDialog("Thanks for You Complaint.", "", "Welcome", "", "finishComplaint", "");
                }
            } else {
                showToast(jsonObject.getString("error_msg"), 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnSend:
                hideKeyboard(SuggestionComplaintActivity.this, false, null);
                String message = etMessage.getText().toString().trim();
                if (spSubjects.getSelectedItemPosition() == 0) {
                    showToast("Plz select subject.", 1, 17);
                    return;
                }
                if (message.isEmpty()) {
                    showToast("Plz enter message.", 1, 17);
                    return;
                }
                suggestionComplaintServerRequest(message);
                break;
            case R.id.llAttachment:
                hideKeyboard(SuggestionComplaintActivity.this, false, null);
                ActivityCompat.requestPermissions(SuggestionComplaintActivity.this,
                        new String[]{"android.permission.CAMERA", "android.permission.WRITE_EXTERNAL_STORAGE"},
                        CAMERA_AND_STORAGE_PERMISSION_REQUEST_CODE);
//                showImagePickerDialog();
                break;
            case R.id.ivRemoveAttachment:
                hideKeyboard(SuggestionComplaintActivity.this, false, null);
                ivAttachedPhoto.setImageResource(0);
                rlAttachedPhoto.setVisibility(View.GONE);
                pickedImageUri = null;
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
            pickedImageUri = data.getData();
            ivAttachedPhoto.setImageResource(0);
        } else if (requestCode == CAMERA_IMAGE_CAPTURE_INTENT) {
            pickedImageUri = cameraPhotoAttachmentRequestURI;
            ivAttachedPhoto.setImageResource(0);
        }
        try {
            Glide.with(context).load(getPath(context, pickedImageUri)).apply(new RequestOptions().diskCacheStrategy(DiskCacheStrategy.NONE)
                    .skipMemoryCache(true)).into(ivAttachedPhoto);
            rlAttachedPhoto.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
        }
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
                Log.d("MyCameraApp", "failed to create directory");
                return null;
            }
        }
        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        File mediaFile;
        if (type == MEDIA_TYPE_IMAGE) {
            mediaFile = new File(mediaStorageDir.getPath() + File.separator +
                    "IMG_HMI_Temp.jpg");
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

}
