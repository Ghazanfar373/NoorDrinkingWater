package com.frandydevs.apps.noordrinkingwater;

import android.os.Bundle;
import android.view.View;
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

public class RegisterActivity extends ParentActivity implements View.OnClickListener {

    LinearLayout llMain, llFields;
    TextView tvHeader;
    ImageView ivBG, ivBottle;
    Button btnRegister, btnSignIn;
    EditText etName, etEmail, etPhone, etPassword, etPasswordRepeat, etTRN;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        ivBG = findViewById(R.id.ivBG);
        llFields = findViewById(R.id.llFields);
        tvHeader = findViewById(R.id.tvHeader);
        llMain = findViewById(R.id.llMain);
        ivBottle = findViewById(R.id.ivBottle);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnRegister = findViewById(R.id.btnRegister);
        etName = findViewById(R.id.etName);
        etEmail = findViewById(R.id.etEmail);
        etPhone = findViewById(R.id.etPhone);
        etPassword = findViewById(R.id.etPassword);
        etPasswordRepeat = findViewById(R.id.etPasswordRepeat);
        etTRN = findViewById(R.id.etTRN);

        llMain.setPadding(0, AppConfig.DEVICE_HEIGHT / 10, 0, AppConfig.DEVICE_HEIGHT / 12);
        llFields.setPadding(AppConfig.DEVICE_WIDTH / 7, 0, AppConfig.DEVICE_WIDTH / 7, 0);
        tvHeader.setPadding(AppConfig.DEVICE_WIDTH / 7, 0, 0, 0);

        llParams = (LinearLayout.LayoutParams) ivBottle.getLayoutParams();
        llParams.height = AppConfig.DEVICE_HEIGHT / 6;
        ivBottle.setLayoutParams(llParams);

        btnSignIn.setOnClickListener(this);
        btnRegister.setOnClickListener(this);

        Picasso.get().load(R.drawable.bg_product_image).into(ivBottle);
        Picasso.get().load(R.drawable.bg_blue_layers).into(ivBG);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btnRegister:
                hideKeyboard(RegisterActivity.this, false, null);
                String name = etName.getText().toString().trim();
                String phone = etPhone.getText().toString().trim();
                String email = etEmail.getText().toString().trim();
                String password = etPassword.getText().toString();
                String rePassword = etPasswordRepeat.getText().toString();
                String trn = etTRN.getText().toString().trim();
                if (name.isEmpty()) {
                    showToast("Plz enter name", 1, 17);
                    return;
                }
                if (phone.isEmpty()) {
                    showToast("Plz enter contact no", 1, 17);
                    return;
                }
                if (email.isEmpty() || !isEmailValid(email)) {
                    showToast("Plz enter valid email", 1, 17);
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
               /* if (trn.isEmpty()) {
                    showToast("Plz enter TRN", 1, 17);
                    return;
                }*/
                registerServerRequest(name, email, phone, password, trn);
                break;

            case R.id.btnSignIn:
                finish();
                break;
        }
    }

    private void registerServerRequest(String name, String email, String phone, String password, String trn) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("username", email);
            params.put("name", name);
            params.put("phone", phone);
            params.put("password", password);
            params.put("re_password", password);
            params.put("from", "app");
            params.put("module", "add_customer");
            params.put("trn", trn);
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 2, null, this, getString(R.string.registering)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                Bundle bundle = new Bundle();
                bundle.putString("email", etEmail.getText().toString().trim());
                bundle.putString("password", etPassword.getText().toString());
                showMessageDialog("Register", "Your account created successfully.", "OK",
                        "", "goToLoginWithAutoLogin", "", bundle);
            } else {
                showToast(jsonObject.getString("msg"), 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
