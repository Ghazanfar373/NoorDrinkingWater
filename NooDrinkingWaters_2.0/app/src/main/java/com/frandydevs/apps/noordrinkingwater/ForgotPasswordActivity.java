package com.frandydevs.apps.noordrinkingwater;

import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;

import static Web.WebRequestHandlerInstance.PROCESS_URL;

public class ForgotPasswordActivity extends ParentActivity implements View.OnClickListener {

    LinearLayout llMain, llFields;
    TextView tvHeader, tvEnterCode;
    ImageView ivBottle, ivBG;
    Button btnSendCode;
    EditText etEmail;
    EditText etCode, etPassword, etPasswordRepeat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        ivBG = findViewById(R.id.ivBG);
        llFields = findViewById(R.id.llFields);
        tvHeader = findViewById(R.id.tvHeader);
        llMain = findViewById(R.id.llMain);
        ivBottle = findViewById(R.id.ivBottle);
        btnSendCode = findViewById(R.id.btnSendCode);
        etEmail = findViewById(R.id.etEmail);
        tvEnterCode = findViewById(R.id.tvEnterCode);

        llMain.setPadding(0, AppConfig.DEVICE_HEIGHT / 10, 0, AppConfig.DEVICE_HEIGHT / 12);
        llFields.setPadding(AppConfig.DEVICE_WIDTH / 7, 0, AppConfig.DEVICE_WIDTH / 7, 0);
        tvHeader.setPadding(AppConfig.DEVICE_WIDTH / 7, 0, 0, 0);

        llParams = (LinearLayout.LayoutParams) ivBottle.getLayoutParams();
        llParams.height = AppConfig.DEVICE_HEIGHT / 6;
        ivBottle.setLayoutParams(llParams);

        btnSendCode.setOnClickListener(this);
        tvEnterCode.setOnClickListener(this);

        Picasso.get().load(R.drawable.bg_product_image).into(ivBottle);
        Picasso.get().load(R.drawable.bg_blue_layers).into(ivBG);
    }

    @Override
    public void onClick(View v) {
        String email;
        switch (v.getId()) {
            case R.id.btnSendCode:
                hideKeyboard(ForgotPasswordActivity.this, false, null);
                email = etEmail.getText().toString().trim();
                if (email.isEmpty() || !isEmailValid(email)) {
                    showToast("Plz enter valid email", 1, 17);
                    return;
                }
                getPasswordResetPinServerRequest(email);
                break;
            case R.id.tvEnterCode:
                hideKeyboard(ForgotPasswordActivity.this, false, null);
                email = etEmail.getText().toString().trim();
                if (email.isEmpty() || !isEmailValid(email)) {
                    showToast("Plz enter valid email", 1, 17);
                    return;
                }
                showEnterPinDialog();
                break;
        }
    }

    private void getPasswordResetPinServerRequest(String email) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("email", email);
            params.put("from", "app");
            params.put("module", "forget_password");
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 3, null, this, getString(R.string.wait)));
        }
    }

    private void resetPasswordServerRequest(String password, String code) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("email", etEmail.getText().toString().trim());
            params.put("from", "app");
            params.put("module", "password_update");
            params.put("password", password);
            params.put("re_password", password);
            params.put("code", code);
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 7, null, this, getString(R.string.wait)));
        }
    }

    private void showEnterPinDialog() {
        final Dialog dialog = new Dialog(context, R.style.Theme_Dialog);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.dialog_enter_forgot_pass_pin);
        dialog.setCancelable(false);
        final Button btnYes = dialog.findViewById(R.id.btnYes);
        final Button btnNo = dialog.findViewById(R.id.btnNo);
        etCode = dialog.findViewById(R.id.etCode);
        etPassword = dialog.findViewById(R.id.etPassword);
        etPasswordRepeat = dialog.findViewById(R.id.etPasswordRepeat);
        final ImageView ivClose = dialog.findViewById(R.id.ivClose);
        ivClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        btnYes.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String code = etCode.getText().toString().trim();
                String password = etPassword.getText().toString().trim();
                String rePassword = etPasswordRepeat.getText().toString().trim();
                if (code.isEmpty()) {
                    showToast("Plz enter code", 1, 17);
                    return;
                }
                if (password.isEmpty()) {
                    showToast("Plz enter password", 1, 17);
                    return;
                }
                if (!rePassword.equalsIgnoreCase(password)) {
                    showToast("Password not matched", 1, 17);
                    return;
                }
                resetPasswordServerRequest(password, code);
            }
        });
        btnNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        switch (REQUEST_TYPE) {
            case 3:
                try {
                    if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                        showEnterPinDialog();
                    } else {
                        showToast(jsonObject.getString("error_msg"), 1, 17);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
            case 7:
                try {
                    if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                        Bundle bundle = new Bundle();
                        bundle.putString("email", etEmail.getText().toString().trim());
                        bundle.putString("password", etPassword.getText().toString());
                        showMessageDialog(getString(R.string.password_reset), getString(R.string.password_reset_message), getString(R.string.ok),
                                "", "goToLoginWithAutoLogin", "", bundle);
                    } else {
                        showToast(jsonObject.getString("msg"), 1, 17);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                break;
        }
    }
}
