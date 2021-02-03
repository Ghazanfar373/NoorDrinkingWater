package com.frandydevs.apps.noordrinkingwater;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;

import com.loopj.android.http.RequestParams;
import com.squareup.picasso.Picasso;

import org.json.JSONObject;

import Web.LoopJRequestHandler;
import Web.WebRequestHandlerInstance;
import app.AppConfig;

import static Web.WebRequestHandlerInstance.PROCESS_URL;
import static app.AppConfig.PREF_USER_ID;

public class RateUsActivity extends ParentActivity {

    ImageView ivBottle, ivBG;
    RatingBar arbRating;
    Button btnSend;
    EditText etMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rate_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(R.string.rate_us);

        ivBG = findViewById(R.id.ivBG);
        ivBottle = findViewById(R.id.ivBottle);
        arbRating = findViewById(R.id.arbRating);
        btnSend = findViewById(R.id.btnSend);
        etMessage = findViewById(R.id.etMessage);

        llParams = (LinearLayout.LayoutParams) ivBottle.getLayoutParams();
        llParams.height = AppConfig.DEVICE_HEIGHT / 6;
        ivBottle.setLayoutParams(llParams);
        Picasso.get().load(R.drawable.bg_suggestion_illustration).into(ivBG);
        ivBG.setPadding(0, AppConfig.DEVICE_HEIGHT / 3, 0, 0);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                hideKeyboard(RateUsActivity.this, false, null);
                rateUsServerRequest(String.valueOf(arbRating.getRating()), etMessage.getText().toString().trim());
            }
        });

        Picasso.get().load(R.drawable.bg_product_image).into(ivBottle);
    }

    private void rateUsServerRequest(String rating, String message) {
        if (!isNetworkAvailable()) {
            showToast("No Internet Available!", 1, 17);
            return;
        }
        if (!IS_REQUEST_IN_PROCESS) {
            RequestParams params = new RequestParams();
            params.put("from", "app");
            params.put("module", "rateus");
            params.put("user_id", dataSaving.getPrefValue(context, PREF_USER_ID));
            params.put("rate_msg", message);
            params.put("rating", rating);
            params.put("sales_team", extras.getString("salesTeam"));
            params.put("order_id", extras.getString("orderID"));
            WebRequestHandlerInstance.post(PROCESS_URL, params, new LoopJRequestHandler(context, 4, null, this, getString(R.string.wait)));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        extras = intent.getExtras();
        super.onNewIntent(intent);
    }

    @Override
    public void handleServerResponse(int REQUEST_TYPE, JSONObject jsonObject) {
        try {
            if (jsonObject.getString("result").equalsIgnoreCase("true")) {
                showMessageDialog("Thanks for Rating Us.", "", "Welcome", "", "finishRateUs", "");
            } else {
                showToast(jsonObject.getString("error_msg"), 1, 17);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
