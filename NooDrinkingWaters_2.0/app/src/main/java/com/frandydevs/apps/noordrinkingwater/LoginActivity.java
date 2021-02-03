package com.frandydevs.apps.noordrinkingwater;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.Iterator;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;

import static app.AppConfig.FIRE_BASE_TOKEN;

public class LoginActivity extends ParentActivity implements View.OnClickListener {

    Button btnRegister, btnSignIn;
    LinearLayout llMain, llFields;
    ImageView ivBG, ivBottle;
    TextView tvHeader, tvForgotPassword;
    EditText etEmail, etPassword;
    CheckBox cbRemember;
    public int FORGOT_PASSWORD_INTENT_REQUEST_CODE = 201;
    public int SIGN_UP_INTENT_REQUEST_CODE = 202;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        ivBG = findViewById(R.id.ivBG);
        llFields = findViewById(R.id.llFields);
        tvHeader = findViewById(R.id.tvHeader);
        llMain = findViewById(R.id.llMain);
        ivBottle = findViewById(R.id.ivBottle);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        etEmail = findViewById(R.id.etEmail);
        etPassword = findViewById(R.id.etPassword);
        cbRemember = findViewById(R.id.cbRemember);
        tvForgotPassword = findViewById(R.id.tvForgotPassword);

        llMain.setPadding(0, AppConfig.DEVICE_HEIGHT / 10, 0, AppConfig.DEVICE_HEIGHT / 12);
        llFields.setPadding(AppConfig.DEVICE_WIDTH / 7, 0, AppConfig.DEVICE_WIDTH / 7, 0);
        tvHeader.setPadding(AppConfig.DEVICE_WIDTH / 7, 0, 0, 0);

        llParams = (LinearLayout.LayoutParams) ivBottle.getLayoutParams();
        llParams.height = AppConfig.DEVICE_HEIGHT / 6;
        ivBottle.setLayoutParams(llParams);

        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);
        tvForgotPassword.setOnClickListener(this);

        Picasso.get().load(R.drawable.bg_product_image).into(ivBottle);
        Picasso.get().load(R.drawable.bg_blue_layers).into(ivBG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                Intent registerIntent = new Intent(context, RegisterActivity.class);
                startActivityForResult(registerIntent, SIGN_UP_INTENT_REQUEST_CODE);
                break;

            case R.id.tvForgotPassword:
                Intent resetPassIntent = new Intent(context, ForgotPasswordActivity.class);
                startActivityForResult(resetPassIntent, FORGOT_PASSWORD_INTENT_REQUEST_CODE);
                break;

            case R.id.btnSignIn:
                hideKeyboard(LoginActivity.this, false, null);
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();
                if (email.isEmpty() || !isEmailValid(email)) {
                    showToast("Plz enter valid email", 1, 17);
                    return;
                }
                if (password.isEmpty()) {
                    showToast("Plz enter password", 1, 17);
                    return;
                }
                loginServerRequest(email, password);
                break;
        }
    }

    private void loginServerRequest(String email, String password) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("username", email);
            params.put("password", password);
            params.put("for", "customer");
            params.put("from", "app");
            params.put("token", dataSaving.getPrefValue(context, FIRE_BASE_TOKEN));
            WebRequestHandlerInstance.post("login/login_exec", params, new LoopJRequestHandler(context, 1, null, this, getString(R.string.signing_in)));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == SIGN_UP_INTENT_REQUEST_CODE || requestCode == FORGOT_PASSWORD_INTENT_REQUEST_CODE) {
                try {
                    Bundle bundle = data.getBundleExtra("loginCredentials");
                    String email = bundle.getString("email");
                    String password = bundle.getString("password");
                    etEmail.setText(email);
                    etPassword.setText(password);
                    loginServerRequest(email, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                try {
                    JSONObject userDetailJObject = jsonObject.getJSONObject("user_details");
                    Iterator<String> userDetailJObjectKeysIterator = userDetailJObject.keys();
                    while (userDetailJObjectKeysIterator.hasNext()) {
                        try {
                            String key = userDetailJObjectKeysIterator.next();
                            String value = userDetailJObject.getString(key);
                            dataSaving.setPrefValue(context, key, value);
                        } catch (JSONException e) {
                            // Something went wrong!
                        }
                    }
                    try {
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                dataSaving.setPrefValue(context, AppConfig.PREF_IS_LOGIN, "true");
                if (cbRemember.isChecked()) {
                    //important notes
                    //set PREF_IS_REMEMBER_LOGIN to false on logout
                    //set IS_Login to false on logout and exit(if PREF_IS_REMEMBER_LOGIN is false)
                    //check PREF_IS_REMEMBER_LOGIN value on exit(no logout), if true then don't set IS_Login to false
                    dataSaving.setPrefValue(context, AppConfig.PREF_IS_REMEMBER_LOGIN, "true");
                }
                Intent dashBoardIntent = new Intent(context, MainActivity.class);
                startActivity(dashBoardIntent);
                finish();
//                showToast("Goto dashboard.", 1, 17);
            } else {
                showToast("Wrong email or password.", 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
            try {
                showToast(jsonObject.getString("msg"), 1, 17);
            } catch (JSONException e1) {
                e1.printStackTrace();
            }
        }
    }
}
