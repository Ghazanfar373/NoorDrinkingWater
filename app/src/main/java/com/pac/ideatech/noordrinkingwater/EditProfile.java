package com.pac.ideatech.noordrinkingwater;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class EditProfile extends AppCompatActivity {
   TextView tv_userName,tv_shot_bio,tv_address,tv_contact,tv_house_no,tv_City,tv_Cust_type,tv_pref_dlvry;
   private ImageView btn_edit;
   private SessionManager manager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
    }

    @Override
    protected void onResume() {
        super.onResume();
        manager=new SessionManager(this);
        tv_userName=(TextView) findViewById(R.id.user_profile_name);
        tv_shot_bio = (TextView) findViewById(R.id.user_profile_short_bio);
        tv_address= (TextView) findViewById(R.id.full_address);
        tv_contact= (TextView) findViewById(R.id.full_contact);
        tv_house_no= (TextView) findViewById(R.id.house_number);
        btn_edit= (ImageView) findViewById(R.id.btn_Edit);
        tv_City= (TextView) findViewById(R.id.full_city);
        tv_Cust_type= (TextView) findViewById(R.id.full_cust_type);
        tv_pref_dlvry= (TextView) findViewById(R.id.full_pref_delivery);
        btn_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent(EditProfile.this,SignupFinalActivity.class);
                intent.putExtra("from","EditProfile");
                startActivity(intent);
                finish();
            }
        });


       tv_userName.setText(manager.getUser_fName());
       tv_shot_bio.setText(manager.getUserEmail());
       tv_address.setText(manager.getUser_Address());
       tv_contact.setText("     "+manager.getUser_Mobile());
       tv_house_no.setText(" Building/House No: "+manager.getUSER_House_No());
       tv_City.setText(" City:  "+manager.getUser_City());
       tv_Cust_type.setText(" Customer Type: "+manager.getUser_Type());
       tv_pref_dlvry.setText(" Preferred Delivery: "+manager.getUSER_Pref_dlvry());


//        Log.w("name",manager.getUser_fName());
//        Log.w("id",manager.getUserId());
//        Log.w("name",manager.getUserEmail());

    }
}
