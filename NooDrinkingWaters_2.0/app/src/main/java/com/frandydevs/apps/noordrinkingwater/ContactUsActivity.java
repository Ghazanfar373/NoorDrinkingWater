package com.frandydevs.apps.noordrinkingwater;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_USER_EMAIL;
import static app.AppConfig.PREF_USER_ID;
import static app.AppConfig.PREF_USER_NAME;
import static app.AppConfig.PREF_USER_PHONE;

public class ContactUsActivity extends ParentActivity {

    ImageView ivBG;
    EditText etName, etPhone, etEmail, etSubject, etMessage;
    Button btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.contact_us);

        ivBG = findViewById(R.id.ivBG);
        etName = findViewById(R.id.etName);
        etPhone = findViewById(R.id.etPhone);
        etEmail = findViewById(R.id.etEmail);
        etSubject = findViewById(R.id.etSubject);
        etMessage = findViewById(R.id.etMessage);
        btnSend = findViewById(R.id.btnSend);

        etName.setText(dataSaving.getPrefValue(context, PREF_USER_NAME));
        etPhone.setText(dataSaving.getPrefValue(context, PREF_USER_PHONE));
        etEmail.setText(dataSaving.getPrefValue(context, PREF_USER_EMAIL));
        Picasso.get().load(R.drawable.bg_contact_illustration).into(ivBG);
        ivBG.setPadding(0, AppConfig.DEVICE_HEIGHT / 3, 0, 0);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(ContactUsActivity.this, false, null);
                String subject = etSubject.getText().toString().trim();
                String message = etMessage.getText().toString().trim();
                if (subject.isEmpty()) {
                    showToast("Plz enter subject.", 1, 17);
                    return;
                }
                if (message.isEmpty()) {
                    showToast("Plz enter message.", 1, 17);
                    return;
                }
                contactUsServerRequest(subject, message);
            }
        });
    }

    private void contactUsServerRequest(String subject, String message) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("from", "app");
            params.put("module", "contact_us");
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("msg", message);
            params.put("subject", subject);
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 5, null, this, getString(R.string.wait)));
        }
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                showMessageDialog("Thanks for Contacting Us.", "", "Welcome", "", "finishContactUs", "");
            } else {
                showToast(jsonObject.getString("error_msg"), 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
