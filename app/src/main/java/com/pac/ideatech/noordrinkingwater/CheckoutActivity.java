package com.pac.ideatech.noordrinkingwater;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.HashMap;

import java.util.Map;
public class CheckoutActivity extends AppCompatActivity {
  private Button btn_Place_Order;
  private SessionManager sessionmanager;
  private EditText ed_Note,ed_coupon;
  boolean responceback=false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_checkout);
        sessionmanager=new SessionManager(this);

if(getSupportActionBar()!=null) {
    getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    getSupportActionBar().setDisplayShowHomeEnabled(true);
 }
        Intent intent=getIntent();
        btn_Place_Order= (Button) findViewById(R.id.btn_place_order);
        ed_Note= (EditText) findViewById(R.id.ed_note);
        ed_coupon= (EditText) findViewById(R.id.ed_coupon);

//        if(intent.getStringExtra("from").equals("history")){
//            Toast.makeText(this, intent.getStringExtra("from"), Toast.LENGTH_SHORT).show();
//            btn_Place_Order.setVisibility(View.GONE);
//            ed_Note.setVisibility(View.GONE);
//            ed_coupon.setVisibility(View.GONE);
//            tv_subtotal.setVisibility(View.GONE);
//            tv_total.setVisibility(View.GONE);
//



        CartItemsFragment fragment=new CartItemsFragment();
        final android.app.FragmentManager manager=getFragmentManager();
        android.app.FragmentTransaction transaction=manager.beginTransaction();
        transaction.replace(R.id.container_checkout,fragment);
        transaction.commit();
        btn_Place_Order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             Log.w("Items", getItems());
             if(Utils.isConnected(CheckoutActivity.this)) {

                     // Tag used to cancel the request
                   String note=" ",coupon=" ";
        if(TextUtils.isEmpty(ed_Note.getText().toString())) note="null";
                 if(TextUtils.isEmpty(ed_coupon.getText().toString())) coupon="null";

                    note=ed_Note.getText().toString();
                     String cartitems=getItems();
                     String userid=sessionmanager.getUserId();
                     Order.setNote(note);
                     Order.setPrepaidCoupon(coupon);
                     Order.setCartitemslist(cartitems);
                     startActivity(new Intent(CheckoutActivity.this,OrderConfirmationActivity.class));
                     finish();
                     //SendOrderJSON(userid,note,cartitems);

             }else Utils.showSettingsAlert("DATA",CheckoutActivity.this);
            }
        });
    }




    private String getItems()
    {
        JSONObject dataObj = new JSONObject();
        try
        {

            JSONArray cartItemsArray = new JSONArray();
            JSONObject cartItemsObjedct;
            for (int i = 0; i < Order.orderList.size(); i++)
            {
                cartItemsObjedct = new JSONObject();
                cartItemsObjedct.putOpt("ProductID", Order.orderList.get(i).getProduct_id());
                cartItemsObjedct.putOpt("ProductQuantity",Order.orderList.get(i).getQnty());
                cartItemsObjedct.putOpt("TotalAmount",Order.orderList.get(i).getTotal_amount());
                cartItemsArray.put(cartItemsObjedct);
            }

            dataObj.put("cart", cartItemsArray);

        } catch (JSONException e)
        {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return dataObj.toString();
    }

    @Override
    public void onBackPressed() {
      //  this.finish();
        //super.onBackPressed();
    }
    @Override
    public boolean onSupportNavigateUp(){
        Intent returnIntent = new Intent();
        if(Order.isResponceback()) {
            setResult(Activity.RESULT_OK, returnIntent);
        }else if(Order.orderList.size()<1){
            setResult(Activity.RESULT_OK, returnIntent);
        } else setResult(Activity.RESULT_CANCELED, returnIntent);

        finish();
        this.finish();
        return true;
    }
}
