package com.frandydevs.apps.noordrinkingwater;

import android.app.Activity;
import android.app.Dialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import app.AppConfig;
import app.AppController;
import app.DataSaving;
import db.SQLiteDatabaseHelper;

public class ParentActivity extends AppCompatActivity {

    Context context;
    Bundle extras;
    Toast toast;
    public DataSaving dataSaving;
    FragmentManager fragmentManager;
    Gson gson;
    LinearLayout.LayoutParams llParams;
    RelativeLayout.LayoutParams rlParams;
    public boolean IS_REQUEST_IN_PROCESS = false;
    SQLiteDatabaseHelper sqLiteDatabaseHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = this;
        extras = getIntent().getExtras();
        dataSaving = AppController.getDataSaving();
        sqLiteDatabaseHelper = new SQLiteDatabaseHelper(context);
    }

    public void rateApp() {
        try {
            Intent rateIntent = rateIntentForUrl("market://details");
            startActivity(rateIntent);
        } catch (ActivityNotFoundException e) {
            Intent rateIntent = rateIntentForUrl("https://play.google.com/store/apps/details");
            startActivity(rateIntent);
        }
    }

    private Intent rateIntentForUrl(String url) {
        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(String.format("%s?id=%s", url, getPackageName())));
        int flags = Intent.FLAG_ACTIVITY_NO_HISTORY | Intent.FLAG_ACTIVITY_MULTIPLE_TASK;
        if (Build.VERSION.SDK_INT >= 21) {
            flags |= Intent.FLAG_ACTIVITY_NEW_DOCUMENT;
        } else {
            //noinspection deprecation
            flags |= Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET;
        }
        intent.addFlags(flags);
        return intent;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    public boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void showToast(String message, int length, int gravity) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, message, length);
        toast.setGravity(gravity, 0, 0);
        toast.show();
    }

    public static void hideKeyboard(Activity activity, boolean isDialog, Dialog dialog) {
        InputMethodManager imm = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (isDialog) {
            View f = dialog.getCurrentFocus();
            if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
                imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
            else
                dialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        } else {
            View f = activity.getCurrentFocus();
            if (null != f && null != f.getWindowToken() && EditText.class.isAssignableFrom(f.getClass()))
                imm.hideSoftInputFromWindow(f.getWindowToken(), 0);
            else
                activity.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        }
    }

    boolean isEmailValid(CharSequence email) {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public void handleServerResponse(int REQUEST_TYPE, JSONArray jsonArray) {
        //this method will be called in child fragments
    }

    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        //this method will be called in child fragments
    }

    public void handleServerResponse(int REQUEST_TYPE, String string) {
        //this method will be called in child fragments
    }

    public void showMessageDialog(String title, String message, String positiveBtnText, String negativeBtnText,
                                  final String positiveBtnAction, final String negativeBtnAction) {
        showMessageDialog(title, message, positiveBtnText, negativeBtnText, positiveBtnAction, negativeBtnAction, null);
    }

    public void showMessageDialog(String title, String message, String positiveBtnText, String negativeBtnText,
                                  final String positiveBtnAction, final String negativeBtnAction, final Bundle extras) {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_message);
        dialog.setCancelable(false);
        final TextView tvTitle = dialog.findViewById(R.id.tvTitle);
        final TextView tvMessage = dialog.findViewById(R.id.tvMessage);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnNo = dialog.findViewById(R.id.btnNo);
        final ImageView ivClose = dialog.findViewById(R.id.ivClose);
        if (title.equalsIgnoreCase("")) {
            tvTitle.setVisibility(View.GONE);
        } else {
            tvTitle.setText(title);
        }
        if (message.equalsIgnoreCase("")) {
            tvMessage.setVisibility(View.GONE);
        } else {
            tvMessage.setText(message);
        }
        if (positiveBtnText.equalsIgnoreCase("")) {
            btnYes.setVisibility(View.GONE);
        } else {
            btnYes.setText(positiveBtnText);
        }
        if (negativeBtnText.equalsIgnoreCase("")) {
            btnNo.setVisibility(View.GONE);
        } else {
            btnNo.setText(negativeBtnText);
        }
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (positiveBtnAction) {
                    case "goToLoginWithAutoLogin":
                        dialog.dismiss();
                        Intent intent = new Intent();
                        intent.putExtra("loginCredentials", extras);
                        setResult(RESULT_OK, intent);
                        finish();
                        break;
                    case "logout":
                        dataSaving.setPrefValue(context, AppConfig.PREF_IS_REMEMBER_LOGIN, "false");
                        dataSaving.setPrefValue(context, AppConfig.PREF_IS_LOGIN, "false");
                        dialog.dismiss();
                        sqLiteDatabaseHelper.clearCart();
                        finish();
                        Intent loginIntent = new Intent(context, LoginActivity.class);
                        startActivity(loginIntent);
                        finish();
                        break;
                    case "finishContactUs":
                    case "finishRateUs":
                    case "finishSuggestion":
                    case "finishComplaint":
                        dialog.dismiss();
                        finish();
                        break;
                    case "dismiss":
                        dialog.dismiss();
                        break;
                }
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (negativeBtnAction) {
                    case "dismiss":
                        dialog.dismiss();
                        break;
                }
            }
        });
        dialog.show();
    }
}
